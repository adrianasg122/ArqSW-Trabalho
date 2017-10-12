package Business;

public class Ativo {

    private int id;
    private float preco;
    private String tipo;

    public Ativo(int id, float preco, String tipo) {
        this.id = id;
        this.preco = preco;
        this.tipo = tipo;
    }

    public Ativo(Ativo a) {
        this.id = a.getId();
        this.preco = a.getPreco();
        this.tipo = a.getTipo();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getPreco() {
        return preco;
    }

    public void setPreco(float preco) {
        this.preco = preco;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Ativo ativo = (Ativo) o;

        if (getId() != ativo.getId()) return false;
        if (Float.compare(ativo.getPreco(), getPreco()) != 0) return false;
        return getTipo().equals(ativo.getTipo());
    }

    public int hashCode() {
        int result = getId();
        result = 31 * result + (getPreco() != +0.0f ? Float.floatToIntBits(getPreco()) : 0);
        result = 31 * result + getTipo().hashCode();
        return result;
    }

    public Ativo clone () {
        return new Ativo(this);
    }
}
