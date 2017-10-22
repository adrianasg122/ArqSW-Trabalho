package Business;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;

import java.io.IOException;
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

        try {
            Stock s = YahooFinance.get(entidade);
            res = s.getQuote().getAsk().floatValue();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }

    public static float getValorVenda(String entidade) {
        float res = 0;

        try {
            Stock s = YahooFinance.get(entidade);
            res = s.getQuote().getBid().floatValue();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }

    public void povoacao() {
        ess.criarAtivo("NVDA");
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
                            ess.comprar(c);
                        } catch (SaldoInsuficienteException e) {
                        }

                    if (a.getPrecoVenda() != pv) {
                        for (Contrato c : contVenda)
                            ess.vender(c);

                    }
                    a.setPrecoCompra(pc);
                    a.setPrecoVenda(pv);
                    ess.getAtivos().put(a.getId(), a);
                }
            }
            try {
                sleep(300000);
            } catch (InterruptedException e) {
            }
        }
    }


}