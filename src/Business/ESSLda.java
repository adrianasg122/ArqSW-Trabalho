package Business;


import DAOS.AtivosDAO;
import DAOS.ContratoDAO;
import DAOS.RegistoDAO;
import DAOS.UtilizadorDAO;
import java.util.*;



public class ESSLda {

    private UtilizadorDAO utilizadores;
    private AtivosDAO ativos;
    private ContratoDAO contratos;
    private RegistoDAO registos;
    private Utilizador utilizador;

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
        try {
            this.utilizador = this.validaUtilizador(username, password);
        } catch (Exception e) {
            throw new UsernameInvalidoException(e.getMessage());
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
        if (this.utilizadores.containsKey(username)) {
            u = this.utilizadores.get(username);
            if (u.getPassword().equals(password)) return u;
            else throw new PasswordInvalidaException("A password está incorreta!");
        } else throw new UsernameInvalidoException("Este username não existe!");
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
    public void registar(String username, String password, float saldo) throws UsernameInvalidoException {
        int id = utilizadores.size() + 1;
        Utilizador u = new Utilizador(id, username, password, saldo);

        if (utilizadores.get(id).getUsername() == null) {
            utilizadores.put(id, u);
        } else throw new UsernameInvalidoException("Username já existe");
    }


    /**
     * Consultar a lista de acções adquiridas por um utilizador
     */
    public List<Contrato> consultaPortCFD() {
        List<Contrato> res = new ArrayList<>();
        for (Contrato c : contratos.values()) {
            if (c.getIdUtil() == utilizador.getId())
                if (c.getConcluido() == 0)
                    res.add(c);
        }
        return res;
    }

    /**
     * Consultar o valor dos ativos adquiridos
     */
    public Map<Integer, Float> consultaValorVendaAtivos() {
        Map<Integer, Float> res = new TreeMap<>();
        for (Contrato c : contratos.values()) {
            if (c.getIdUtil() == utilizador.getId()) {
                if (c.getConcluido() == 0) {
                    if (c.getVenda() == 1) {
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


    public void criarContratoCompra(int idAtivo, float sl, float tp, int quant) {
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
    }

    /**
     * Comprar ações
     * @param c Contrato
     */
    public void comprar(Contrato c) {
        float sl = c.getStoploss();
        float tp = c.getTakeprofit();
        int idAtivo = c.getIdAtivo();
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
        }
    }

    /**
     * Colocar ativos à venda
     *
     * @param c Contrato
     */
    public void vender (Contrato c){
        float sl = c.getStoploss();
        float tp = c.getTakeprofit();
        int idAtivo = c.getIdAtivo();
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
        }
    }
}


