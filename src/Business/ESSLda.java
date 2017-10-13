package Business;


import DAOS.AtivosDAO;
import DAOS.UtilizadorDAO;
import java.util.*;


public class ESSLda {

    private UtilizadorDAO utilizadores;
    private AtivosDAO ativos;
    private Utilizador utilizador;


    public ESSLda(Utilizador utilizador) {
        this.utilizadores = new UtilizadorDAO();
        this.ativos = new AtivosDAO();
        this.utilizador = utilizador;
    }


    public Utilizador getUtilizador() {
        return utilizador;
    }

    public void setUtilizador(Utilizador utilizador) {
        this.utilizador = utilizador;
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

        if (utilizadores.get(username) == null){
            utilizadores.put(username, u);
            this.utilizador = u;
        }
        else throw new UsernameInvalidoException("Username inválido");
    }


    /**
     * Consultar a lista de acções adquiridas por um utilizador
     */
    public List<Ativo> consultaPort() {
        List<Ativo> aux = new ArrayList<Ativo>(utilizador.getAtivos().values());
        return aux;
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
    public void comprar () {

    }

    /**
     * Colocar ações à venda
     */
    public void vender() {

    }

}
