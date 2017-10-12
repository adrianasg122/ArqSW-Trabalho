package Business;

public class Ativo {

    private int id;
    private float preco;
    private String tipo;
    private boolean venda;

    public Ativo(int id, float preco, String tipo, boolean Venda) {
        this.id = id;
        this.preco = preco;
        this.tipo = tipo;
        this.venda = false;

    }

    public Ativo(Ativo a) {
        this.id = a.getId();
        this.preco = a.getPreco();
        this.tipo = a.getTipo();
        this.venda = a.getVenda();
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

    public boolean getVenda(){
        return venda;
    }

    public void setVenda(boolean venda){
        this.venda = venda;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Ativo ativo = (Ativo) o;

        if (this.getId() != ativo.getId()) return false;
        if (Float.compare(ativo.getPreco(), this.getPreco()) != 0) return false;
        if (this.getVenda() != ativo.getVenda()) return false;
        return getTipo().equals(ativo.getTipo());
    }

    public Ativo clone () {
        return new Ativo(this);
    }
}
