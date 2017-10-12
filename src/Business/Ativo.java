package Business;

public abstract class Ativo {

    private int id;
    private float preco;

    public Ativo(int id, float preco) {
        this.id = id;
        this.preco = preco;
    }

    public Ativo(Ativo a) {
        this.id = a.getId();
        this.preco = a.getPreco();
    }

    public int getId() {
        return id;
    }

    public float getPreco() {
        return preco;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPreco(float preco) {
        this.preco = preco;
    }

    public abstract Ativo clone ();

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Ativo ativo = (Ativo) o;

        if (id != ativo.id) return false;
        return Float.compare(ativo.preco, preco) == 0;
    }

    public String toString() {
        return "Ativo{" +
                "id=" + id +
                ", preco=" + preco +
                '}';
    }
}
