package Servidor;

public class Registo {
    private int idAtivo;
    private int idUtil;
    private int quantidade;



    public Registo (){ }

    public Registo(Registo r) {
        this.idAtivo = r.getIdAtivo();
        this.idUtil = r.getIdUtil();
        this.quantidade = r.getQuantidade();

    }

    public Registo(int idAtivo, int idUtil, int quantidade) {
        this.quantidade = quantidade;
        this.idAtivo = idAtivo;
        this.idUtil = idUtil;
    }

    public int getIdAtivo() {
        return idAtivo;
    }

    public void setIdAtivo(int id) {
        this.idAtivo = id;
    }

    public int getIdUtil() {
        return idUtil;
    }

    public void setIdUtil(int id) {
        this.idUtil = id;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public Registo clone () {
        return new Registo(this);
    }

}
