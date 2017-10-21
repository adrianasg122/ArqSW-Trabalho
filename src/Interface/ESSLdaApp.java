package Interface;

import Business.ESSLda;
import Business.SaldoInsuficienteException;
import Business.UsernameInvalidoException;
import Business.UtilizadorInvalidoException;


public class ESSLdaApp {
    private static ESSLda ess;
    private static Menu menuprincipal;

    private static void carregarMenus(){

        String [] principal = {
                "Iniciar Sessão",
                "Registar Utilizador",
                "Listar ativos",
                "Consultar portfólio",
                "Consultar saldo",
                "Listar ativos a vender",
                "Comprar ativo",
                "Vender ativo",
                "Listar contratos",
                "Fechar contrato",
                "Terminar Sessão"
        };
        menuprincipal = new Menu(principal);
    }


    private static void imprimeMenuInicial(){
        int op;
        do{
            op = menuprincipal.showMenu();
            switch (op){
                case 1: sessao();
                break;
                case 2: regiUti();
                break;
                case 3: listA();
                break;
                case 4: saldo();
                break;
                case 5: listVendas();
                break;
                case 6: comprarA();
                break;
                case 7: venderA();
                break;
                case 8: port();
                break;
                case 9: fecharC();
                break;
                case 10: ess.terminarSessao();
                break;
            }
        }while (op != 0);
    }


    private static void sessao(){
        String email,password;

        email = menuprincipal.readString("Username: ");

        password = menuprincipal.readString("Password: ");

        try{
            ess.iniciarSessao(email,password);
            System.out.println("\n!! BEM-VINDO !!");
        }
        catch (UtilizadorInvalidoException e){
            System.out.println(e.getMessage());
        }

        imprimeMenuInicial();
    }

    private static void regiUti(){
        String nome, password;
        float saldo;

        nome = menuprincipal.readString("Insira o nome: ");

        password = menuprincipal.readString("Insira a password: ");

        saldo = menuprincipal.readFloat("Insira o saldo:");

        try{
            ess.registar(nome,password,saldo);
            System.out.println("\nUtilizador registado com sucesso!");
        }catch (UtilizadorInvalidoException e){
            System.out.println(e.getMessage());
        }
    }

    private static void fecharC() {
        int id;

        id = menuprincipal.readInt("Insira o id do Contrato: ");

        try {
            ess.fecharContrato(id);
            System.out.println("\nContrato fechado com sucesso!");
        } catch (SaldoInsuficienteException e) {
            e.printStackTrace();
        }
    }

    private static void listA() { ess.listarAtivos(); }

    private static void port(){ ess.consultaPortCFD(); }

    private static void saldo(){ ess.getSaldoUtilizador(); }

    private static void listVendas(){ ess.getAtivosVenda(); }

    private static void comprarA() {
        int idA, quant;
        float sl, tp;

        idA = menuprincipal.readInt("Insira o id do Ativo: ");

        sl = menuprincipal.readFloat("Indique o Stop Loss: ");

        tp = menuprincipal.readFloat("Indique o Take Profit: ");

        quant = menuprincipal.readInt("Insira a quantidade:");

        ess.criarContratoCompra(idA, sl, tp, quant);
    }

    private static void venderA(){
        int idA, quant;
        float sl, tp;

        idA = menuprincipal.readInt("Insira o id do Ativo: ");

        sl = menuprincipal.readFloat("Indique o Stop Loss: ");

        tp = menuprincipal.readFloat("Indique o Take Profit: ");

        quant = menuprincipal.readInt("Insira a quantidade:");

        ess.criarContratoVenda(idA, sl, tp, quant);
    }

}
