package Cliente;

import Servidor.*;

import java.util.NoSuchElementException;


public class ESSLdaApp {
    //1 logado, zero não
    private boolean cliente;
    private Leitor leitor;
    private Escritor escritor;
    private static Menu menuinicial;
    private static Menu menuprincipal;

    public ESSLdaApp(Leitor leitor, Escritor escritor) {
        this.cliente = false;
        this.leitor = leitor;
        this.escritor = escritor;
        this.carregarMenus();
    }

    public void executa() {
        String resposta;
        int op;
        while((op = showMenu()) != -1) {
            resposta = correComando(op);
            System.out.println(resposta);
        }

        System.out.println("\nLigação terminada!");
        System.exit(0);
    }

    private int showMenu() {
        int op = -1;

        try {
            if (!cliente)
                op = menuinicial.showMenu();
            else {
                op = menuprincipal.showMenu() + 2;
            }
        } catch (NoSuchElementException e) {
            return op;
        }

        return op;
    }



    private static void carregarMenus(){
        String [] inicial = {
                "Iniciar Sessão",
                "Registar Utilizador"
        };
        menuinicial = new Menu(inicial);

        String [] principal = {
                "Listar ativos",
                "Consultar saldo",
                "Listar ativos a vender",
                "Comprar ativo",
                "Vender ativo",
                "Consultar portfólio",
                "Fechar contrato",
                "Terminar Sessão"
        };
        menuprincipal = new Menu(principal);
    }


    private String correComando(int op){
        String query = null, resposta = null;
            switch (op) {
                case 1:
                    query = sessao();
                    break;
                case 2:
                    query = regiUti();
                    break;
                case 3:
                    query = listA();
                    break;
                case 4:
                    query = saldo();
                    break;
                case 5:
                    query = listVendas();
                    break;
                case 6:
                    query = comprarA();
                    break;
                case 7:
                    query = venderA();
                    break;
                case 8:
                    query = port();
                    break;
                case 9:
                    query = fecharC();
                    break;
                case 10:
                    query = terminarSessao();
                    break;
            }
        escritor.escrever(query);
        try {
            resposta = leitor.ler(op);
            if (op == 1) this.cliente = true;
            if (op == 10) this.cliente = false;
        } catch (PedidoFalhadoException e) {
            e.getMessage();}

        return resposta;

    }


    private String sessao() {
        String email,password,query;

        email = menuprincipal.readString("Username:");

        password = menuprincipal.readString("Password:");

        query = String.join(" ","LOGIN",email,password);

        return query;
    }

    private String regiUti(){
        String nome, password, query, saldo;

        nome = menuprincipal.readString("Insira o nome:");

        password = menuprincipal.readString("Insira a password:");

        saldo = menuprincipal.readString("Insira o saldo:");

        query = String.join(" ","REGISTAR",nome,password,saldo);

        return query;
    }

    private String fecharC() {
        String id, query;

        id = menuprincipal.readString("Insira o id do Contrato: ");

        query = String.join(" ","ENCERRAR",id);

        return query;
    }

    private String listA() {
        return"LISTARATIVOS";
    }


    private String port(){
        return "LISTARCONTRATOS";
    }

    private String saldo(){
        return "SALDO";
    }

    private String listVendas(){
        return "LISTARVALORESVENDA";
    }

    private String comprarA() {
        String idA, quant, sl, tp, query;

        idA = menuprincipal.readString("Insira o id do Ativo: ");

        sl = menuprincipal.readString("Indique o Stop Loss: ");

        tp = menuprincipal.readString("Indique o Take Profit: ");

        quant = menuprincipal.readString("Insira a quantidade:");

        query = String.join(" ","COMPRA",idA,sl,tp, quant);

        return query;
    }

    private String venderA(){
        String idA, quant, sl, tp, query;

        idA = menuprincipal.readString("Insira o id do Ativo: ");

        sl = menuprincipal.readString("Indique o Stop Loss: ");

        tp = menuprincipal.readString("Indique o Take Profit: ");

        quant = menuprincipal.readString("Insira a quantidade:");

        query = String.join(" ","VENDA", idA, sl, tp, quant);

        return query;
    }

    private String terminarSessao() {
        return "TERMINAR";
    }
}
