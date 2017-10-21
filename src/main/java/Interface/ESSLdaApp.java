package Interface;

import Business.ESSLda;
import Business.SaldoInsuficienteException;
import Business.UsernameInvalidoException;
import Business.UtilizadorInvalidoException;


public class ESSLdaApp {
    private ESSLdaApp () {}
    private static ESSLda ess;
    private static Menu menuinicial, menuprincipal;

    public static void main(String [] args){
        ess = new ESSLda();
        carregarMenus();
        imprimeMenuInical();
    }

    private static void carregarMenus(){
        String [] inicial= {
                "Iniciar Sessão",
                "Registar Utilizador"
        };
        String [] principal = {
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
        menuinicial = new Menu(inicial);
        menuprincipal = new Menu(principal);
    }

    private static void imprimeMenuInical() {
        int op;
        do{
            op = menuinicial.showMenu();
            switch (op){
                case 1: sessao();
                break;
                case 2: regiUti();
                break;
            }
        } while (op != 0);
    }

    private static void imprimeMenuPrincipal(){
        int op;
        do{
            op = menuprincipal.showMenu();
            switch (op){
                case 1: listA();
                break;
                case 2: saldo();
                break;
                case 3: listVendas();
                break;
                case 4: comprarA();
                break;
                case 5: venderA();
                break;
                case 6: port();
                break;
                case 7: fecharC();
                break;
                case 8: ess.terminarSessao();
                break;
            }
        }while (op != 0);
    }


    private static void sessao(){
        String email,password;

        email = menuinicial.readString("Username: ");

        password = menuinicial.readString("Password: ");

        try{
            ess.iniciarSessao(email,password);
        }
        catch (UtilizadorInvalidoException e){
            System.out.println(e.getMessage());
        }

        imprimeMenuPrincipal();
    }

    private static void regiUti(){
        String nome, password;
        float saldo;

        nome = menuinicial.readString("Insira o nome: ");

        password = menuinicial.readString("Insira a password: ");

        saldo = menuinicial.readFloat("Insira o saldo:");

        try{
            ess.registar(nome,password,saldo);
        }catch (UtilizadorInvalidoException e){
            System.out.println(e.getMessage());
        }
    }

    private static void fecharC() {
        int id;

        id = menuprincipal.readInt("Insira o id do Contrato: ");

        try {
            ess.fecharContrato(id);
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

        quant = menuinicial.readInt("Insira a quantidade:");

        ess.criarContratoCompra(idA, sl, tp, quant);
    }

    private static void venderA(){
        int idA, quant;
        float sl, tp;

        idA = menuprincipal.readInt("Insira o id do Ativo: ");

        sl = menuprincipal.readFloat("Indique o Stop Loss: ");

        tp = menuprincipal.readFloat("Indique o Take Profit: ");

        quant = menuinicial.readInt("Insira a quantidade:");

        ess.criarContratoVenda(idA, sl, tp, quant);
    }

}
