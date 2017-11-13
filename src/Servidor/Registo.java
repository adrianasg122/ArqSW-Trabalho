package Business;

public class Registo {
    private int id;
    private int idAtivo;
    private int idUtil;
    private float preco;
    private int quantidade;
    private int venda;


    public Registo(Registo r) {
        this.id = r.getId();
        this.idAtivo = r.getIdAtivo();
        this.idUtil = r.getIdUtil();
        this.preco = r.getPreco();
        this.quantidade = r.getQuantidade();
        this.venda = r.getVenda();

    }

    public Registo(int id, float preco, int quantidade, int idAtivo, int util, int venda) {
        this.id = id;
        this.preco = preco;
        this.quantidade = quantidade;
        this.idAtivo = idAtivo;
        this.idUtil = util;
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

    public void setIdAtivo(int idAtivo) { this.idAtivo = idAtivo; }

    public int getIdUtil() { return idUtil; }

    public void setIdUtil(int idUtil) { this.idUtil = idUtil;}

    public int getVenda() { return venda; }

    public void setVenda(int venda) { this.venda = venda; }

    public Registo clone () {
        return new Registo(this);
    }

}
