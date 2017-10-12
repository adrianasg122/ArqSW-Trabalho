package Business;

public class Utilizador {

    private String username;
    private String password;
    private float saldo;
    private String email;


    public Utilizador () {
        this.username = null;
        this.password = null;
        this.saldo = 0.0;
        this.email = null;
    }

    public Utilizador(String username, String password, float saldo, String email) {
        this.username = username;
        this.password = password;
        this.saldo = saldo;
        this.email = email;
    }

    public Utilizador (Utilizador u) {
        this.username = u.getUtilizador();
        this.password = u.getPassword();
        this.saldo = u.getSaldo();
        this.email = u.getEmail();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

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

    public Utilizador clone () {
        return new Utilizador(this);
    }


    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Utilizador that = (Utilizador) o;

        if (Float.compare(that.saldo, saldo) != 0) return false;
        if (!username.equals(that.username)) return false;
        if (!password.equals(that.password)) return false;
        return email.equals(that.email);
    }

}
