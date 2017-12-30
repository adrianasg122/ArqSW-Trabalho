package Servidor;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Set;

public class Updater extends Thread{

    private ESSLda ess;


    public Updater(ESSLda ess) {
        this.ess = ess;
    }


    public static String getNomeEmpresa (String entidade) {
        String res = null;
        Stock a;
        try {
            a = YahooFinance.get(entidade);
            res = a.getName();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return res;
    }

    public float getValorCompra(String entidade){
        float res = 0;
        BigDecimal value;

        try {
            Stock s = YahooFinance.get(entidade);
            value = s.getQuote().getAsk();
            res = (value == null) ? 0 : value.floatValue();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }

    public static float getValorVenda(String entidade) {
        float res = 0;

        try {
            Stock s = YahooFinance.get(entidade);
            BigDecimal value = s.getQuote().getBid();
            res = (value == null) ? 0 : value.floatValue();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }

    public void povoacao() {

        ess.criarAtivo("NVDA");
        ess.criarAtivo("APPL");
        ess.criarAtivo("GOOG");
        ess.criarAtivo("INTC");
    }

    public void run() {

        povoacao();

        while (true) {
            Set<Ativo> ativos = ess.listarAtivos();

            for (Ativo a : ativos) {

                float pc = getValorCompra(a.getDescricao());
                float pv = getValorVenda(a.getDescricao());
                Set<Contrato> contVenda = ess.listarContratosVendaAtivo(a.getId());
                Set<Contrato> contCompra = ess.listarContratosCompraAtivo(a.getId());

                if (a.getPrecoCompra() != pc) {
                    for (Contrato c : contCompra)
                        try {
                            ess.verificarContrato(c);
                            a.setPrecoCompra(pc);
                        } catch (SaldoInsuficienteException e) {
                        }
                }

                if (a.getPrecoVenda() != pv) {
                    for (Contrato c : contVenda)
                        try {
                            ess.verificarContrato(c);
                            a.setPrecoVenda(pv);
                        } catch (SaldoInsuficienteException e) {
                        }
                }
                    ess.getAtivos().put(a.getId(), a);
                }
            try {
                sleep(300000);
            } catch (InterruptedException e) {
            }
        }
    }


}
