package Interface;

import Business.ESSLda;
import Business.UsernameInvalidoException;



public class ESSLdaApp {
    private ESSLdaApp () {}
    private static ESSLda ess;
    private static Menu menuinicial, menuprincipal;

    public static void main(String [] args){
        ess = new ESSLda(null);
        carregarMenus();
        imprimeMenuInical();
    }

    private static void carregarMenus(){
        String [] inicial= {
                "Iniciar Sessão",
                "Registar Utilizador"
        };
        String [] principal = {
                "Consultar portfólio",
                "Consultar saldo",
                "Listar ativos para venda",
                "Comprar ativo",
                "Vender ativo",
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
                case 1: port();
                break;
                case 2: saldo();
                break;
                case 3: listVendas();
                break;
                case 4: comprarA();
                break;
                case 5: venderA();
                break;
                case 6: ess.terminarSessao();
                break;
            }
        }while (op != 0);
    }

    private static void sessao(){
        String email,password;

        email = menuinicial.readString("Nome: ");

        password = menuinicial.readString("Password: ");

        try{
            ess.iniciarSessao(email,password);
        }
        catch (UsernameInvalidoException e){
            System.out.println(e.getMessage());
        }

        imprimeMenuPrincipal();
    }

    private static void regiUti(){
        String nome, password, email;
        float saldo;

        nome = menuinicial.readString("Insira o nome: ");

        password = menuinicial.readString("Insira a password: ");

        email = menuinicial.readString("Insira o email: ");

        saldo = menuinicial.readFloat("Insira o saldo:");

        try{
            ess.registar(nome,password,saldo,email);
        }catch (UsernameInvalidoException e){
            System.out.println(e.getMessage());
        }
    }

    private static void port(){
        System.out.println("Função ainda não implementada!");
    }

    private static void saldo(){
        System.out.println("Função ainda não implementada!");
    }

    private static void listVendas(){
        System.out.println("Função ainda não implementada!");
    }

    private static void comprarA(){
        System.out.println("Função ainda não implementada!");
    }

    private static void venderA(){
        System.out.println("Função ainda não implementada!");
    }

}
