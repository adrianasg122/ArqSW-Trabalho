package Servidor;


import DAOS.AtivosDAO;
import DAOS.ContratoDAO;
import DAOS.RegistoDAO;
import DAOS.UtilizadorDAO;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;


public class ESSLda {

    private Lock userLock;
    private Lock ativoLock;
    private Lock contratoLock;
    private Lock registoLock;
    private UtilizadorDAO utilizadores;
    private AtivosDAO ativos;
    private ContratoDAO contratos;
    private RegistoDAO registos;
    private Utilizador utilizador;

    public ESSLda() {
        this.userLock = new ReentrantLock();
        this.ativoLock = new ReentrantLock();
        this.contratoLock = new ReentrantLock();
        this.registoLock = new ReentrantLock();
        this.utilizadores = new UtilizadorDAO();
        this.ativos = new AtivosDAO();
        this.contratos = new ContratoDAO();
        this.registos = new RegistoDAO();
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

    /**
     * Login do utilizador na plataforma.
     *
     * @param username Username do novo registo.
     * @param password Password do novo registo.
     */
    public synchronized Utilizador iniciarSessao(String username, String password) throws UtilizadorInvalidoException {
        try {
                this.utilizador = this.validaUtilizador(username, password);
        } catch (Exception e) {
            throw new UtilizadorInvalidoException(e.getMessage());
        }
        return utilizador;
    }

    /**
     * Verifica se os dados do utilizador estão corretos
     *
     * @param username Username do utilizador
     * @param password password do utilizador
     */
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
        }
           return u;
    }

    /**
     * Verifica que o utilizador existe
     * @param utilizador
     */
    public boolean existeUtilizador(Utilizador utilizador) {
        for (Utilizador u : utilizadores.values()) {
            if (u.equals(utilizador))
                return true;
        }

        return false;
    }

    /**
     * Regitar novo utilizador na plataforma.
     *
     * @param username Username do novo registo.
     * @param password Password do novo registo.
     * @param saldo    Saldo inicial da conta.
     */
    public synchronized void registar(String username, String password, float saldo) throws UtilizadorInvalidoException {
        int id = utilizadores.size() + 1;
        Utilizador u = new Utilizador(id, username, password, saldo);

        if (!existeUtilizador(u))
            utilizadores.put(id,u);
        else throw new UtilizadorInvalidoException("Username já existe");
    }


    public synchronized Set<Contrato> getContratos (int id){
        Set<Contrato> res = new HashSet<>();
            for (Contrato c : contratos.values())
                if (c.getIdUtil() == id && c.getConcluido() == 0)
                    res.add(c);
        return res;
    }

    /**
     * Consultar a lista de acções adquiridas por um utilizador
     */
    public Set<Contrato> consultaPortCFD() {
        Set<Contrato> res = this.getContratos(utilizador.getId());
        return res;
    }

    /**
     * Consultar o valor dos ativos adquiridos
     */
    public Set<Ativo> getAtivosVenda() {
        Set<Ativo> res = new HashSet<>() ;

        contratoLock.lock();
        Set<Contrato> contratos = this.contratos.values().stream().filter(c -> c.getIdUtil() == utilizador.getId() && c.getVenda() == 1).collect(Collectors.toSet());
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

    /**
     * Consultar o saldo de um utilizador
     */
    public float getSaldoUtilizador() {
        return utilizador.getSaldo();
    }


    public synchronized int criarContratoCompra(int idAtivo, float sl, float tp, int quant) {
        Contrato c = new Contrato(null);
        int id = contratos.size() + 1;
        c.setIdContrato(id);
        c.setIdAtivo(idAtivo);
        c.setIdUtil(utilizador.getId());
        c.setStoploss(sl);
        c.setTakeprofit(tp);
        c.setQuantidade(quant);
        c.setVenda(0);
        c.setConcluido(0);
        contratos.put(id, c);
        return id;
    }

    public synchronized int criarContratoVenda(int idAtivo, float sl, float tp, int quant) {
        Contrato c = new Contrato(null);
        int id = contratos.size() + 1;
        c.setIdContrato(id);
        c.setIdAtivo(idAtivo);
        c.setIdUtil(utilizador.getId());
        c.setStoploss(sl);
        c.setTakeprofit(tp);
        c.setQuantidade(quant);
        c.setVenda(1);
        c.setConcluido(0);
        contratos.put(id, c);
        return id;
    }

    public synchronized void fecharContrato(int id) throws SaldoInsuficienteException {
        Contrato aux = contratos.get(id);
        aux.setConcluido(1);
        if (aux.getVenda() == 1) verificarContrato(aux);
        else verificarContrato(aux);
    }

    public void criarRegisto(Contrato c) {
        Registo r = new Registo(null);
        int regid;
        registoLock.lock();
        regid = registos.size() + 1;
        registoLock.unlock();
        r.setId(regid);
        r.setIdAtivo(c.getIdAtivo());
        r.setIdUtil(c.getIdUtil());
        ativoLock.lock();
        r.setPreco(ativos.get(c.getIdAtivo()).getPrecoVenda());
        ativoLock.unlock();
        r.setQuantidade(c.getQuantidade());
        r.setVenda(c.getVenda());
        registos.put(regid, r);
    }


    public void verificarContrato(Contrato c) throws SaldoInsuficienteException {
        float sl = c.getStoploss();
        float tp = c.getTakeprofit();
        int idAtivo = c.getIdAtivo();
        Ativo a = ativos.get(idAtivo);
        float preco = c.getPreco() * c.getQuantidade();
        if (c.getVenda() == 0) {
            if (utilizador.getSaldo() < preco)
                throw new SaldoInsuficienteException("Não possui saldo suficiente");
            if ((sl == 0 && tp == 0) ||
                    a.getPrecoCompra() >= tp || a.getPrecoCompra() <= sl) {
                transacao(c,preco);
            }
        }
        else if ((sl == 0 && tp == 0 ) ||
                a.getPrecoVenda() >= tp || a.getPrecoVenda() <= sl)
            transacao(c,preco);
    }

    public void transacao(Contrato c , float preco) {
        criarRegisto(c);
        c.setConcluido(1);
        if (c.getVenda() == 1)
            utilizador.setSaldo(utilizador.getSaldo() + preco);
        else
            utilizador.setSaldo(utilizador.getSaldo() - preco);
    }

    public Set<Ativo> listarAtivos() {
        Set<Ativo> res = new HashSet<>();
        ativoLock.lock();
        try {
            ativos.forEach((i,a) -> res.add(a.clone()));
        } finally {
            ativoLock.unlock();
        }
        return res;
    }

    public Set<Contrato> listarContratosVendaAtivo(int id) {
        Set<Contrato> res = new HashSet<>();
        contratoLock.lock();
        try {
            for (Contrato c : contratos.values())
                if (c.getIdAtivo() == id && c.getVenda() == 1) res.add(c);
        }
        finally {
            contratoLock.unlock();
        }
        return res;
    }

    public Set<Contrato> listarContratosCompraAtivo(int id) {
        Set<Contrato> res = new HashSet<>();
        contratoLock.lock();
        try {
            for (Contrato c : contratos.values())
                if (c.getIdAtivo() == id && c.getVenda() == 0) res.add(c);
        }
        finally {
            contratoLock.unlock();
        }
        return res;
    }

    public int existe (String nome)  {
        int existe = 0;
        ativoLock.lock();
        try {
            for (Ativo a : ativos.values())
                if (a.getDescricao().equals(nome)) existe = 0;
        }
        finally {
            ativoLock.unlock();
        }
        return existe;
    }

    public synchronized void criarAtivo(String entidade) {
        int i = this.ativos.size() + 1;
        BigDecimal value;
        float fvalue;
        Ativo aux = new Ativo();
        Stock s;
        try {
            s = YahooFinance.get(entidade);

            if (this.existe(entidade) == 0 && s!= null) {
                aux.setId(i);
                aux.setDescricao(entidade);
                value = s.getQuote().getBid();
                fvalue = (value == null) ? 0 : value.floatValue();
                aux.setPrecoVenda(fvalue);
                value = s.getQuote().getAsk();
                fvalue = (value == null) ? 0 : value.floatValue();
                aux.setPrecoCompra(fvalue);
                this.ativos.put(i, aux);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    }



