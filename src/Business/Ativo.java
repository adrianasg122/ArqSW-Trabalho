package Business;

public class Ativo {

    private int id;
    private String tipo;
    private float precoCompra;
    private float precoVenda;
    //qual a entidade responsável
    private String descricao;


    public Ativo() {//
    }

    public Ativo(int id, float precoCompra, float precoVenda, String tipo, String descricao) {
        this.id = id;
        this.precoCompra = precoCompra;
        this.precoVenda = precoVenda;
        this.tipo = tipo;
        this.descricao = descricao;

    }

    public Ativo(Ativo a) {
        this.id = a.getId();
        this.precoCompra = a.getPrecoCompra();
        this.precoVenda = a.getPrecoVenda();
        this.tipo = a.getTipo();
        this.descricao = a.getDescricao();
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getPrecoCompra() {
        return precoCompra;
    }

    public void setPrecoCompra(float preco) {
        this.precoCompra = preco;
    }

    public float getPrecoVenda() { return precoVenda; }

    public void setPrecoVenda(float precoVenda) { this.precoVenda = precoVenda; }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDescricao() { return descricao; }

    public void setDescricao(String descricao) { this.descricao = descricao; }

    public Ativo clone () {
        return new Ativo(this);
    }


}



