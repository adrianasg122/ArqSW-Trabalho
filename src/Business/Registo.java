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

}
