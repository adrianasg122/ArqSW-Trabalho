package Business;

public class Registo {

    private int idRegisto;
    private int idAtivo;
    private float precoCompra;
    private int quantidade;


    public Registo (int id, int idAtivo,float precoCompra, int quantidade) {
        this.idRegisto = id;
        this.idAtivo = idAtivo;
        this.precoCompra = precoCompra;
        this.quantidade = quantidade;
    }

    public int getIdRegisto() {
        return idRegisto;
    }

    public void setIdRegisto(int idRegisto) {
        this.idRegisto = idRegisto;
    }

    public int getIdAtivo() {
        return idAtivo;
    }

    public void setIdAtivo(int idAtivo) {
        this.idAtivo = idAtivo;
    }

    public float getPrecoCompra() {
        return precoCompra;
    }

    public void setPrecoCompra(float precoCompra) {
        this.precoCompra = precoCompra;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }
}
