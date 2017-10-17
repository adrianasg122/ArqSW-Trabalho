package Business;

public class Registo {
    private int id;
    private float preco;
    private int quantidade;
    private int idAtivo;
    private int venda;

    public Registo (Registo r) {
        this.id = r.getId();
        this.preco = r.getPreco();
        this.quantidade = r.getQuantidade();
        this.idAtivo = r.getIdAtivo();
        this.venda = r.getVenda();
    }

    public Registo(int id, float preco, int quantidade, int idAtivo, int venda) {
        this.id = id;
        this.preco = preco;
        this.quantidade = quantidade;
        this.idAtivo = idAtivo;
        this.venda = venda;
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

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public int getIdAtivo() {
        return idAtivo;
    }

    public void setIdAtivo(int idAtivo) {
        this.idAtivo = idAtivo;
    }

    public int getVenda() {
        return venda;
    }

    public void setVenda(int venda) {
        this.venda = venda;
    }

    public Registo clone () {
        return new Registo(this);
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Registo registo = (Registo) o;

        if (getId() != registo.getId()) return false;
        if (Float.compare(registo.getPreco(), getPreco()) != 0) return false;
        if (getQuantidade() != registo.getQuantidade()) return false;
        if (getIdAtivo() != registo.getIdAtivo()) return false;
        return getVenda() == registo.getVenda();
    }

    public String toString() {
        return "Registo{" +
                "id=" + id +
                ", preco=" + preco +
                ", quantidade=" + quantidade +
                ", idAtivo=" + idAtivo +
                ", venda=" + venda +
                '}';
    }
}
