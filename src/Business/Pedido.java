package Business;

public class Pedido {

    private int idPedido;
    private int idAtivo;
    private int quantidade;
    private String idUtil;
    private boolean venda;
    private float stoploss;
    private float takeprofit;
    private boolean concluido;


    public Pedido(Pedido p) {
        this.idPedido = p.getIdPedido();
        this.idAtivo = p.getIdAtivo();
        this.quantidade = p.getQuantidade();
        this.idUtil = p.getIdUtil();
        this.venda = p.getVenda();
        this.stoploss = p.getStoploss();
        this.takeprofit = p.getTakeprofit();
        this.concluido = p.getConcluido();
    }

    public Pedido(int idPedido, int idAtivo, int quantidade, String idUtil, boolean venda, float stoploss, float takeprofit, boolean concluido) {
        this.idPedido = idPedido;
        this.idAtivo = idAtivo;
        this.quantidade = quantidade;
        this.idUtil = idUtil;
        this.venda = venda;
        this.stoploss = stoploss;
        this.takeprofit = takeprofit;
        this.concluido = concluido;
    }

    public int getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

    public int getIdAtivo() {
        return idAtivo;
    }

    public void setIdAtivo(int idAtivo) {
        this.idAtivo = idAtivo;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public String getIdUtil() {
        return idUtil;
    }

    public void setIdUtil(String idUtil) {
        this.idUtil = idUtil;
    }

    public boolean getVenda() {
        return venda;
    }

    public void setVenda(boolean venda) {
        this.venda = venda;
    }

    public float getStoploss() {
        return stoploss;
    }

    public void setStoploss(float stoploss) {
        this.stoploss = stoploss;
    }

    public float getTakeprofit() {
        return takeprofit;
    }

    public void setTakeprofit(float takeprofit) {
        this.takeprofit = takeprofit;
    }

    public boolean getConcluido() {
        return concluido;
    }

    public void setConcluido(boolean concluido) {
        this.concluido = concluido;
    }


    public Pedido clone () {
        return new Pedido(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Pedido pedido = (Pedido) o;

        if (getIdPedido() != pedido.getIdPedido()) return false;
        if (getIdAtivo() != pedido.getIdAtivo()) return false;
        if (getQuantidade() != pedido.getQuantidade()) return false;
        if (getVenda() != pedido.getVenda()) return false;
        if (Float.compare(pedido.getStoploss(), getStoploss()) != 0) return false;
        if (Float.compare(pedido.getTakeprofit(), getTakeprofit()) != 0) return false;
        if (getConcluido() != pedido.getConcluido()) return false;
        return getIdUtil().equals(pedido.getIdUtil());
    }


    public String toString() {
        return "Pedido{" +
                "idPedido=" + idPedido +
                ", idAtivo=" + idAtivo +
                ", quantidade=" + quantidade +
                ", idUtil='" + idUtil + '\'' +
                ", venda=" + venda +
                ", stoploss=" + stoploss +
                ", takeprofit=" + takeprofit +
                ", concluido=" + concluido +
                '}';
    }
}
