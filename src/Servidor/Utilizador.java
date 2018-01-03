package Servidor;


import DAOS.RegistoDAO;

import java.util.*;

public class Utilizador implements Comparable<Utilizador>, Observer{

    private int id;
    private String username;
    private String password;
    private float saldo;
    private RegistoDAO quant;
    private NotificationBuffer notificacoes;
    private Map<Integer,Float> aSeguir;

    public Utilizador () {
        this.id = 0;
        this.username = null;
        this.password = null;
        this.saldo = 0;
        this.quant = new RegistoDAO();
        this.notificacoes = new NotificationBuffer();
        this.aSeguir = new HashMap<>();
    }


    public Utilizador(int id, String username, String password, float saldo) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.saldo = saldo;
        this.quant = new RegistoDAO();
        this.notificacoes = new NotificationBuffer();
        this.aSeguir = new HashMap<>();
    }

    public Utilizador (Utilizador u) {
        this.id = u.getId();
        this.username = u.getUsername();
        this.password = u.getPassword();
        this.saldo = u.getSaldo();
        this.quant = u.getQuant();
        this.notificacoes = new NotificationBuffer();
        this.aSeguir = new HashMap<>();
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

    public RegistoDAO getQuant() { return quant; }

    public void setQuant(RegistoDAO quant) { this.quant = quant; }

    public NotificationBuffer getNot () { return notificacoes; }

    public void setNot(NotificationBuffer not) {
        this.notificacoes = not;
    }

    public Map<Integer, Float> getaSeguir() {
        return aSeguir;
    }

    public void setaSeguir(Map<Integer, Float> aSeguir) {
        this.aSeguir = aSeguir;
    }

    public Utilizador clone () {
        return new Utilizador(this);
    }

    public String toString() {
        return username;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Utilizador that = (Utilizador) o;

        if (getId() != that.getId()) return false;
        if (Float.compare(that.getSaldo(), getSaldo()) != 0) return false;
        if (!getUsername().equals(that.getUsername())) return false;
        if (!getPassword().equals(that.getPassword())) return false;
        return getQuant().equals(that.getQuant());
    }

    public int compareTo(Utilizador c) {
        return this.id - c.getId();
    }

    public void update (Ativo a) {
       Float f = aSeguir.get(a.getId());
       if (f != null && f.equals(a.getPrice())) {
           aSeguir.remove(a.getId());
           a.removeSeguidor(this);
           notificacoes.add("O ativo " + a.getDescricao() + " atingiu o valor desejado: " + a.getPrice());
       }
    }
}


