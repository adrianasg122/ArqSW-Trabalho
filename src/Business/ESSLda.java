package Business;


import DAOS.AtivosDAO;
import DAOS.UtilizadorDAO;
import java.util.*;



public class ESSLda {

    private UtilizadorDAO utilizadores;
    private AtivosDAO ativos;
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
     * @param username Username do novo registo.
     * @param password Password do novo registo.
     */
    public void iniciarSessao(String username, String password) throws UsernameInvalidoException{
        try{
            this.utilizador = this.validaUtilizador(username,password);
        }
        catch(Exception e){
            throw new UsernameInvalidoException(e.getMessage());
        }
    }

    /**
     * Verifica se os dados do utilizador estão corretos
     * @param username Username do utilizador
     * @param password password do utilizador
     */
    public Utilizador validaUtilizador(String username, String password) throws UsernameInvalidoException, PasswordInvalidaException {
        Utilizador u;
        if (this.utilizadores.containsKey(username)) {
            u = this.utilizadores.get(username);
            if (u.getPassword().equals(password)) return u;
            else throw new PasswordInvalidaException("A password está incorreta!");
        }
        else throw new UsernameInvalidoException("Este username não existe!");
    }

    /**
     * Terminar sessão do utilizador na plataforma.
     */
    public void terminarSessao(){
        this.utilizador = null;
    }

    /**
     * Regitar novo utilizador na plataforma.
     * @param username Username do novo registo.
     * @param password Password do novo registo.
     * @param email Contacto do novo registo.
     */
    public void registar (String username, String password, float saldo, String email) throws UsernameInvalidoException{
        Utilizador u = new Utilizador(username,password,saldo,email);

        if (utilizadores.get(username).getUsername() == null){
            utilizadores.put(username, u);
        }
        else throw new UsernameInvalidoException("Username já existe");
    }


    /**
     * Consultar a lista de acções adquiridas por um utilizador
     */
    public List<Ativo> consultaPort() {
        List<Ativo> res = new ArrayList<Ativo>(utilizador.getAtivos().values());
        return res;
    }

    /**
     * Consultar o saldo de um utilizador
     */
    public float consultarSaldo() {
       return utilizador.getSaldo();
    }

    /**
     * Comprar ações
     */
    // TODO não está bem
    public void comprar (int id) throws SaldoInsuficienteException, IdInvalidoException{
        if (ativos.get(id) == null) throw new IdInvalidoException("Ativo não existe");
        if (utilizador.getSaldo() >= ativos.get(id).getPreco()) {
            ativos.get(id).setVenda(false);
            utilizador.getAtivos().put(id,ativos.get(id));
            String exdono = ativos.get(id).getDono();
            float s = utilizadores.get(exdono).getSaldo() + ativos.get(id).getPreco();
            utilizadores.get(exdono).setSaldo(s);
            ativos.get(id).setDono(utilizador.getUsername());
        }

        else
            throw new SaldoInsuficienteException("Saldo insuficiente");
        
    }


    public List<Ativo> listarAtivosVenda() {

        List<Ativo> res = new ArrayList<Ativo>();

        for(Ativo a : ativos.values())
            if (a.getVenda()) res.add(a);

        return res;
    }

    /**
     * Colocar ações à venda
     * @param a Ativo
     */
    // TODO não está bem
    public void vender(Ativo a) {
        a.setVenda(true);

    }

    /**
     * Adicionar um novo ativo à plataforma
     * @param preco Preço do ativo
     * @param tipo Tipo do ativo
     */
    public void criarAtivo (float preco, String tipo) {
        int idNumero = ativos.size();
        Ativo novo = new Ativo(utilizador.getUsername(), idNumero, preco, tipo, false);
        ativos.put(idNumero, novo);
        utilizador.getAtivos().put(idNumero,novo);
    }

}
