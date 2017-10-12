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

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ESSLda essLda = (ESSLda) o;

        if (!getUtilizadores().equals(essLda.getUtilizadores())) return false;
        if (!getAtivos().equals(essLda.getAtivos())) return false;
        return getutilizador().equals(essLda.getutilizador());
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
     * @param nome Nome do novo registo.
     * @param email Contacto do novo registo.
     */
    public void registar (String username, String password, String nome, float saldo, String email) throws UsernameInvalidoException{
        Utilizador u = new Utilizador(username,password,nome,0,email);

        if(utilizadores.get(username) == null){
            utilizadores.add(username, u);
        }
        else new UsernameInvalidoException(e.getMessage());
    }

    public List<Ativo> consultaPort {
       return utilizador.getAtivos().values();
    }

    public float consultarSaldo() {
       return utilizador.getSaldo();
    }

    public void comprar () {

    }

    public void vender() {

    }

}
