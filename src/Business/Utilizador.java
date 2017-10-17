package Business;

import DAOS.RegistoDAO;

import java.util.HashMap;
import java.util.Map;

public class Utilizador {

    private String username;
    private String password;
    private float saldo;
    private String email;
    private RegistoDAO registos;




    public Utilizador () {
        this.username = null;
        this.password = null;
        this.saldo = 0;
        this.email = null;
        this.registos = new RegistoDAO();
    }


    public Utilizador(String username, String password, float saldo, String email) {
        this.username = username;
        this.password = password;
        this.saldo = saldo;
        this.email = email;
    }

    public Utilizador (Utilizador u) {
        this.username = u.getUsername();
        this.password = u.getPassword();
        this.saldo = u.getSaldo();
        this.email = u.getEmail();

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) { this.username = username; }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public float getSaldo() {
        return saldo;
    }

    public void setSaldo(float saldo) {
        this.saldo = saldo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public RegistoDAO getRegistos() { return registos; }

    public void setRegistos(RegistoDAO registos) { this.registos = registos; }

    public Utilizador clone () {
        return new Utilizador(this);
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Utilizador that = (Utilizador) o;

        if (Float.compare(that.getSaldo(), getSaldo()) != 0) return false;
        if (!getUsername().equals(that.getUsername())) return false;
        if (!getPassword().equals(that.getPassword())) return false;
        if (!getEmail().equals(that.getEmail())) return false;
        return getRegistos().equals(that.getRegistos());
    }

    public String toString() {
        return "Utilizador{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", saldo=" + saldo +
                ", email='" + email + '\'' +
                ", registos=" + registos +
                '}';
    }
}
