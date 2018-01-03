package Servidor;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;

import java.io.IOException;
import java.util.Set;

public class Updater extends Thread {

    private ESSLda ess;


    public Updater(ESSLda ess) {
        this.ess = ess;
    }


    public float getPrice(String entidade) {
        float res = 0;

        try {
            Stock s = YahooFinance.get(entidade);
            res = s.getQuote().getPrice().floatValue();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }

    public float getValorCompra(String entidade) {
        float res = 0;

        try {
            Stock s = YahooFinance.get(entidade);
            res = s.getQuote().getAsk().floatValue();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }

    public float getValorVenda(String entidade) {
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

        ess.criarAtivo("FB");
        ess.criarAtivo("TWTR");
        ess.criarAtivo("ORCL");
        ess.criarAtivo("BAC");
        ess.criarAtivo("VALE");
        ess.criarAtivo("MSFT");
        ess.criarAtivo("GC=F");
        ess.criarAtivo("HG=F");
        ess.criarAtivo("PL=F");
    }

    public void run() {

        povoacao();

        while (true) {
            Set<Ativo> ativos = ess.listarAtivos();

            for (Ativo a : ativos) {

                float pc = getValorCompra(a.getDescricao());
                float pv = getValorVenda(a.getDescricao());
                float p = getPrice(a.getDescricao());

                if (a.getPrecoCompra() != pc) {
                    a.setPrecoCompra(pc);
                    ess.getAtivos().put(a.getId(), a);
                    a.notifyObserversCompra();
                }

                if (a.getPrecoVenda() != pv) {
                    a.setPrecoVenda(pv);
                    ess.getAtivos().put(a.getId(), a);
                    a.notifyObserversVenda();
                }

                if (a.getPrice() != p) {
                    a.setPrice(p);
                    ess.getAtivos().put(a.getId(), a);
                    a.notifySeguidores();
                }
            }
            try {
                sleep(30000);
            } catch (InterruptedException e) {
            }
        }
    }
}



