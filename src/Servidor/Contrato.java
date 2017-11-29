package Servidor;


public class Contrato implements Observer{

    private ESSLda ess;
    private int idContrato;
    private int idAtivo;
    private int idUtil;
    private int quantidade;
    private float preco;
    // 1 se for uma venda , 0 se for compra
    private int venda;
    private float stoploss;
    private float takeprofit;
    // 1 se tiver concluido, 0 caso contrário
    private int concluido;

    public Contrato () {

    }

    public Contrato(ESSLda e) {
        this.ess = e;
    }

    public Contrato(Contrato p) {
        this.ess = p.getEss();
        this.idContrato = p.getIdContrato();
        this.idAtivo = p.getIdAtivo();
        this.quantidade = p.getQuantidade();
        this.idUtil = p.getIdUtil();
        this.venda = p.getVenda();
        this.stoploss = p.getStoploss();
        this.takeprofit = p.getTakeprofit();
        this.concluido = p.getConcluido();
        this.preco = p.getPreco();
    }

    public Contrato(int idContrato, int idAtivo, int quantidade,float preco, int idUtil, int venda, float stoploss, float takeprofit, int concluido) {
        this.idContrato = idContrato;
        this.idAtivo = idAtivo;
        this.quantidade = quantidade;
        this.preco = preco;
        this.idUtil = idUtil;
        this.venda = venda;
        this.stoploss = stoploss;
        this.takeprofit = takeprofit;
        this.concluido = concluido;
    }

    public int getIdContrato() {
        return idContrato;
    }

    public void setIdContrato(int idContrato) {
        this.idContrato = idContrato;
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

    public int getIdUtil() {
        return idUtil;
    }

    public void setIdUtil(int idUtil) {
        this.idUtil = idUtil;
    }

    public int getVenda() {
        return venda;
    }

    public void setVenda(int venda) {
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

    public int getConcluido() {
        return concluido;
    }

    public void setConcluido(int concluido) {
        this.concluido = concluido;
    }

    public float getPreco() { return preco; }

    public void setPreco(float preco) { this.preco = preco; }

    public void setEss(ESSLda ess) { this.ess = ess; }


    public ESSLda getEss() { return ess; }

    public Contrato clone () {
        return new Contrato(this);
    }

    public synchronized String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("IdContrato: ").append(idContrato).append("\n");
        sb.append("IdAtivo: ").append(idAtivo).append("\n");
        sb.append("Price: ").append(preco).append("\n");
        sb.append("Stop Loss: ").append(stoploss).append("\n");
        sb.append("Take Profit: ").append(takeprofit).append("\n");
        sb.append("-----------------");
        return sb.toString();
    }


    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Contrato contrato = (Contrato) o;

        if (getIdContrato() != contrato.getIdContrato()) return false;
        if (getIdAtivo() != contrato.getIdAtivo()) return false;
        if (getIdUtil() != contrato.getIdUtil()) return false;
        if (getQuantidade() != contrato.getQuantidade()) return false;
        if (Float.compare(contrato.getPreco(), getPreco()) != 0) return false;
        if (getVenda() != contrato.getVenda()) return false;
        if (Float.compare(contrato.getStoploss(), getStoploss()) != 0) return false;
        if (Float.compare(contrato.getTakeprofit(), getTakeprofit()) != 0) return false;
        return getConcluido() == contrato.getConcluido();
    }



    public void update (Ativo a) {
        if(this.getVenda() == 0) {
            try {
                ess.comprar(this);
            } catch (SaldoInsuficienteException e) {
                e.printStackTrace();
            }
        }
        else if (this.getVenda() ==1) {
            ess.vender(this);
        }
    }
}
