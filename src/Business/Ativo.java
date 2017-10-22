package Business;


public class Ativo implements Comparable<Ativo>{

    private int id;
    // ask
    private float precoCompra;
    // bid
    private float precoVenda;
    //qual a entidade responsável
    private String descricao;


    public Ativo() {//
    }

    public Ativo(int id, float precoCompra, float precoVenda, String descricao) {
        this.id = id;
        this.precoCompra = precoCompra;
        this.precoVenda = precoVenda;
        this.descricao = descricao;

    }

    public Ativo(Ativo a) {
        this.id = a.getId();
        this.precoCompra = a.getPrecoCompra();
        this.precoVenda = a.getPrecoVenda();
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


    public String getDescricao() { return descricao; }

    public void setDescricao(String descricao) { this.descricao = descricao; }

    public Ativo clone () {
        return new Ativo(this);
    }

    public synchronized String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("ID: ").append(id).append("\n");
        sb.append("Preço Compra: ").append(precoCompra).append("\n");
        sb.append("Preço Venda: ").append(precoVenda).append("\n");
        sb.append("Entidade: ").append(descricao);

        return sb.toString();
    }


    @Override
    public int compareTo(Ativo o) {
        return this.id - o.id;
    }
}



