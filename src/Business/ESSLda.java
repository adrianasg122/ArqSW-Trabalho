package Business;


import DAOS.AtivosDAO;
import DAOS.ContratoDAO;
import DAOS.RegistoDAO;
import DAOS.UtilizadorDAO;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


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
    public void iniciarSessao(String username, String password) throws UsernameInvalidoException {
        synchronized (utilizador) {
            try {
                this.utilizador = this.validaUtilizador(username, password);
            } catch (Exception e) {
                throw new UsernameInvalidoException(e.getMessage());
            }
        }
    }

    /**
     * Verifica se os dados do utilizador estão corretos
     *
     * @param username Username do utilizador
     * @param password password do utilizador
     */
    public Utilizador validaUtilizador(String username, String password) throws UsernameInvalidoException, PasswordInvalidaException {
        Utilizador u;
        userLock.lock();
        try {
            if (this.utilizadores.containsKey(username))
                u = this.utilizadores.get(username);
            else throw new UsernameInvalidoException("Este username não existe!");

        }
        finally {
            userLock.unlock();}

        userLock.lock();

            try {
                if (u.getPassword().equals(password)) return u;
                else throw new PasswordInvalidaException("A password está incorreta!");
            }
            finally {
                userLock.unlock();
            }
        }

    /**
     * Terminar sessão do utilizador na plataforma.
     */
    public void terminarSessao() {
        this.utilizador = null;
    }

    /**
     * Regitar novo utilizador na plataforma.
     *
     * @param username Username do novo registo.
     * @param password Password do novo registo.
     * @param saldo    Saldo inicial da conta.
     */
    public synchronized void registar(String username, String password, float saldo) throws UsernameInvalidoException {
        int id;
        Utilizador u;
            id = utilizadores.size() + 1;
            u = new Utilizador(id, username, password, saldo);
            userLock.unlock();
            if (utilizadores.get(id).getUsername() == null) {
            utilizadores.put(id, u);
            } else throw new UsernameInvalidoException("Username já existe");
    }


    /**
     * Consultar a lista de acções adquiridas por um utilizador
     */
    public List<Contrato> consultaPortCFD() {
        List<Contrato> res = new ArrayList<>();
        synchronized (contratos) {
                for (Contrato c : contratos.values()) {
                    if (c.getIdUtil() == utilizador.getId() && c.getConcluido() == 0)
                        res.add(c);
                }
        }
        return res;
    }

    /**
     * Consultar o valor dos ativos adquiridos
     */
    public Map<Integer, Float> consultaValorVendaAtivos() {
        Map<Integer, Float> res = new TreeMap<>();
        synchronized (contratos) {
            synchronized (ativos) {
                for (Contrato c : contratos.values()) {
                    if (c.getIdUtil() == utilizador.getId() && c.getConcluido() == 0 && c.getVenda() == 1) {
                        float preco = ativos.get(c.getIdAtivo()).getPrecoVenda();
                        res.put(c.getIdAtivo(), preco);
                    }
                }
            }
        }
        return res;
    }

    /**
     * Consultar o saldo de um utilizador
     */
    public float consultarSaldo() {
        return utilizador.getSaldo();
    }


    public synchronized void criarContratoCompra(int idAtivo, float sl, float tp, int quant) {
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
        contratos.put(id,c);
    }

    /**
     * Comprar ações
     * @param c Contrato
     */
    // // TODO verificar se o utilizador tem saldo suficiente
    public void comprar(Contrato c) throws SaldoInsuficienteException{
        float preco = c.getPreco() * c.getQuantidade();
        if (utilizador.getSaldo() < preco) throw new SaldoInsuficienteException("Não possui saldo suficiente");
        float sl = c.getStoploss();
        float tp = c.getTakeprofit();
        int idAtivo = c.getIdAtivo();
        synchronized (ativos) {
            synchronized (registos) {
                if (ativos.get(idAtivo).getPrecoCompra() >= tp || ativos.get(idAtivo).getPrecoCompra() <= sl) {
                    Registo r = new Registo(null);
                    r.setId(registos.size() + 1);
                    r.setIdAtivo(idAtivo);
                    r.setIdUtil(utilizador.getId());
                    r.setPreco(ativos.get(idAtivo).getPrecoCompra());
                    r.setQuantidade(c.getQuantidade());
                    r.setVenda(0);
                    registos.put(registos.size() + 1, r);
                    c.setConcluido(1);
                    utilizador.setSaldo(utilizador.getSaldo() - preco);
                }
            }
        }
    }

    /**
     * Colocar ativos à venda
     * @param c Contrato
     */
    public void vender (Contrato c){
        float preco = c.getPreco() * c.getQuantidade();
        float sl = c.getStoploss();
        float tp = c.getTakeprofit();
        int idAtivo = c.getIdAtivo();
        synchronized (ativos) {
            synchronized (registos) {
                if (ativos.get(idAtivo).getPrecoVenda() >= tp || ativos.get(idAtivo).getPrecoVenda() <= sl) {
                    Registo r = new Registo(null);
                    r.setId(registos.size() + 1);
                    r.setIdAtivo(idAtivo);
                    r.setIdUtil(utilizador.getId());
                    r.setPreco(ativos.get(idAtivo).getPrecoVenda());
                    r.setQuantidade(c.getQuantidade());
                    r.setVenda(1);
                    registos.put(registos.size() + 1, r);
                    c.setConcluido(1);
                    utilizador.setSaldo(utilizador.getSaldo() + preco);
                }
            }
        }
    }
}


