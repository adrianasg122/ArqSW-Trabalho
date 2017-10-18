package Business;

import DAOS.RegistoDAO;

import java.util.HashMap;
import java.util.Map;

public class Utilizador {

    private int id;
    private String username;
    private String password;
    private float saldo;


    public Utilizador () {
        this.id = 0;
        this.username = null;
        this.password = null;
        this.saldo = 0;
    }


    public Utilizador(int id, String username, String password, float saldo, String email) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.saldo = saldo;
    }

    public Utilizador (Utilizador u) {
        this.id = u.getId();
        this.username = u.getUsername();
        this.password = u.getPassword();
        this.saldo = u.getSaldo();
    }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

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

    public Utilizador clone () {
        return new Utilizador(this);
    }
}

