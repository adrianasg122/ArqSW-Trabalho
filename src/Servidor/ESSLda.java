package Servidor;


import DAOS.AtivosDAO;
import DAOS.ContratoDAO;
import DAOS.UtilizadorDAO;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;


public class ESSLda extends Object{

    private Lock userLock;
    private Lock ativoLock;
    private Lock contratoLock;
    private UtilizadorDAO utilizadores;
    private AtivosDAO ativos;
    private ContratoDAO contratos;
    private Utilizador utilizador;

    public ESSLda()  {
        this.userLock = new ReentrantLock();
        this.ativoLock = new ReentrantLock();
        this.contratoLock = new ReentrantLock();
        this.utilizadores = new UtilizadorDAO();
        this.ativos = new AtivosDAO();
        this.contratos = new ContratoDAO();
    }

    public AtivosDAO getAtivos() {
        return ativos;
    }

    public String toString() {
        return "ESSLda{" +
                "utilizadores=" + utilizadores +
                ", ativos=" + ativos +
                ", utilizador=" + utilizador +
                '}';
    }

    public synchronized Utilizador iniciarSessao(String username, String password) throws UtilizadorInvalidoException {
        try {
                this.utilizador = this.validaUtilizador(username, password);
        } catch (Exception e) {
            throw new UtilizadorInvalidoException(e.getMessage());
        }
        return utilizador;
    }


    public Utilizador validaUtilizador(String username, String password) throws UtilizadorInvalidoException {
        Utilizador u = null;

        userLock.lock();
        try {
            for (Utilizador aux : utilizadores.values()) {
                if (aux.getUsername().equals(username) && aux.getPassword().equals(password))
                    u = this.utilizadores.get(aux.getId());
            }
        }finally{
            userLock.unlock();
            if (u == null) throw new UtilizadorInvalidoException("O utilizador não existe");
        }
        return u;
    }


    public void terminarSessao() {
        this.utilizador = null;
    }

    public int getUserId(String username) {

        for (Utilizador u : utilizadores.values()) {
            if (u.getUsername().equals(username))
                return u.getId();
        }
        return -1;
    }


    public synchronized void registar(String username, String password, float saldo) throws UtilizadorInvalidoException {
        int id = utilizadores.size() + 1;
        Utilizador u = u = new Utilizador(id, username, password, saldo);

        if (getUserId(username) == -1 ){
            utilizadores.put(id,u);
            System.out.println("Estou registadissimo ");}
        else throw new UtilizadorInvalidoException("Username já existe");
    }



    public Set<Contrato> consultaPortCFD() {
        Set<Contrato> res = new HashSet<>();
        synchronized (contratos) {
            for (Contrato c : contratos.values()) {
                if (c.getIdUtil() == utilizador.getId() && c.getConcluido() == 0)
                    res.add(c);
            }
        }
        return res;
    }


    public Set<Ativo> getAtivosVenda() {
        Set<Ativo> res = new HashSet<>() ;

        contratoLock.lock();
        Set<Contrato> contratos = this.contratos.values().stream().filter(c -> c.getIdUtil() == utilizador.getId() && c.getVenda() == 1 && c.getConcluido()==0).collect(Collectors.toSet());
        contratoLock.unlock ();

        ativoLock.lock();
        try{
            for (Contrato c : contratos)
                res.add(ativos.get(c.getIdAtivo()));

        }finally {
            ativoLock.unlock();
        }
        return res;
    }


    public float getSaldoUtilizador() {
        return utilizadores.get(utilizador.getId()).getSaldo();
    }


    public synchronized int criarContratoCompra(int idAtivo, float sl, float tp, int quant) {
        Contrato c = new Contrato(this);
        int id = contratos.size() + 1;
        c.setIdContrato(id);
        c.setIdAtivo(idAtivo);
        c.setIdUtil(utilizador.getId());
        c.setStoploss(sl);
        c.setTakeprofit(tp);
        c.setPreco(0);
        c.setQuantidade(quant);
        c.setVenda(0);
        c.setConcluido(0);

        contratos.put(id,c);
        ativos.get(idAtivo, this).registerObserverCompra(c);
        try {
            transacao(c);
        } catch (SaldoInsuficienteException e) {
            e.getMessage();
        }
        return id;
    }

    public synchronized int criarContratoVenda(int idAtivo, float sl, float tp, int quant) throws SaldoInsuficienteException {
        Contrato c = new Contrato(this);
        int id = contratos.size() + 1;

        if (utilizador.getQuant().get(idAtivo).getQuantidade() < quant) {
            System.out.println("Não tem quantidade suficiente para vender");
            return -1;
        }
        c.setIdContrato(id);
        c.setIdAtivo(idAtivo);
        c.setIdUtil(utilizador.getId());
        c.setStoploss(sl);
        c.setTakeprofit(tp);
        c.setQuantidade(quant);
        c.setVenda(1);
        c.setConcluido(0);
        contratos.put(id, c);
        ativos.get(idAtivo, this).registerObserverVenda(c);

        transacao(c);
        return id;
    }

    public synchronized void fecharContrato(int id) throws SaldoInsuficienteException {
        Contrato aux = contratos.get(id).clone();
        aux.setTakeprofit(0);
        aux.setStoploss(0);
        if (aux.getVenda() == 1) transacao(aux);
        else transacao(aux);
    }

    public void criarRegisto(Contrato c) {
        Registo r = new Registo();
        r.setIdAtivo(c.getIdAtivo());
        r.setIdUtil(c.getIdUtil());
        r.setQuantidade(c.getQuantidade());
        utilizador.getQuant().put(c.getIdAtivo(),r);
    }


    /**
     * Método que verifica se é possível realizar a transação de ativos ou não.
     * Para isso utiliza os valores guardados no contrato a fim de ver se é possível, dentro dos requisitos,
     * realizar a ação
     * @param c Contrato
     */
    public void transacao(Contrato c) throws SaldoInsuficienteException {
        float sl = c.getStoploss();
        float tp = c.getTakeprofit();
        int idAtivo = c.getIdAtivo();
        Ativo ativo = ativos.get(idAtivo, this);
        float preco = ativo.getPrecoCompra() * c.getQuantidade();
        Utilizador u = utilizadores.get(c.getIdUtil());

        if (u.getSaldo() < preco) throw new SaldoInsuficienteException("Não possui saldo suficiente");

        if (c.getVenda() == 0 && ((sl == 0 && tp == 0) || ativo.getPrecoCompra() >= tp || ativo.getPrecoCompra() <= sl)) {
                if (!u.getQuant().containsKey(idAtivo))
                    criarRegisto(c);
                else {
                    atualizarRegisto(c);
                }
                atualizarContrato(c,preco);
            }
        else if (c.getVenda() == 1) {
            if (!u.getQuant().containsKey(idAtivo) || c.getQuantidade() > u.getQuant().get(idAtivo).getQuantidade())
                return;
            if (ativo.getPrecoVenda() >= tp || ativo.getPrecoVenda() <= sl) {
                atualizarRegisto(c);
                atualizarContrato(c,preco);

            }
        }
    }

    public void atualizarRegisto(Contrato c) {
        int idAtivo = c.getIdAtivo();
        Utilizador u = utilizadores.get(c.getIdUtil());
        Registo r = u.getQuant().get(idAtivo);
        int q = r.getQuantidade();

        if (c.getVenda() == 0)
            r.setQuantidade(q + c.getQuantidade());
        else
            r.setQuantidade(q - c.getQuantidade());

        u.getQuant().put(idAtivo, r);
    }

    public void atualizarContrato(Contrato c, float preco) {
        int idAtivo = c.getIdAtivo();
        Utilizador u = utilizadores.get(c.getIdUtil());
        Ativo a = ativos.get(idAtivo, this);

        c.setPreco(a.getPrecoCompra());
        c.setConcluido(1);
        contratos.put(c.getIdContrato(), c);
        u.setSaldo(utilizador.getSaldo() - preco);
        utilizadores.put(u.getId(), u);
        a.removeObserver(c);

    }

    public Set<Ativo> listarAtivos() {
        Set<Ativo> res = new HashSet<>();
        ativoLock.lock();
        try {
            for (Ativo a : ativos.values(this)){
                res.add(a);}
        } finally {
            ativoLock.unlock();
        }
        return res;
    }

    public int existe (String nome)  {
        ativoLock.lock();
        try {
            for (Ativo a : ativos.values())
                if (a.getDescricao().equals(nome)) return 0;
        }
        finally {
            ativoLock.unlock();
        }
        return 1;
    }

    public synchronized void criarAtivo(String entidade) {
        int i = this.ativos.size() + 1;
        Ativo aux = new Ativo();
        Stock s = null;
        try {
            s = YahooFinance.get(entidade);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (this.existe(entidade) == 1 && s!= null) {
            aux.setId(i);
            aux.setPrice(s.getQuote().getPrice().floatValue());
            aux.setDescricao(entidade);
            aux.setPrecoVenda(s.getQuote().getBid().floatValue());
            aux.setPrecoCompra(s.getQuote().getAsk().floatValue());
            this.ativos.put(i, aux);
            }
    }


    public String porNome(String nome) throws PedidoFalhadoException {
       Ativo res = null;
       for (Ativo a : ativos.values())
           if (a.getDescricao().equals(nome)) {
                res = a;
                return res.toString();
           }
       if (res == null) throw new PedidoFalhadoException("O ativo não está disponível para consulta");
       return null;

    }

    public float seguir(int idAtivo, float price) {
        utilizador.getaSeguir().put(idAtivo,price);
        utilizadores.put(utilizador.getId(),utilizador);
        Ativo aux = ativos.get(idAtivo);
        aux.registarSeguidor(utilizador);
        ativos.put(idAtivo,aux);
        aux.toString();
        return price;
    }

    public String notificar() {
        return utilizador.getNot().ler();
    }

}





