package Interface;

import Business.ESSLda;
import Business.UsernameInvalidoException;
import Business.Utilizador;
import jdk.internal.util.xml.impl.Input;

import java.io.*;
import java.util.*;


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
//TODO ALTERAR O RESTO
        menuinicial = new Menu(inicial);
        menuprincipal = new Menu(principal);
    }

    private static void imprimeMenuInical() {
        do{
            menuinicial.executa();
            switch(menuinicial.getOpcao()){
                case 1: sessao();
                    break;
                case 2: regiUti();
            }
        } while (menuinicial.getOpcao()!= 0);
    }

    private static void imprimeMenuPrincipal(){
        do{
            menuprincipal.executa();
            switch (menuprincipal.getOpcao()){
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
            }
        }while (menuprincipal.getOpcao()!=0);
    }

    private static void sessao(){
        Scanner input = new Scanner(System.in);
        String email,password;


        System.out.println("Nome: ");
        email = input.nextLine();

        System.out.println("Password: ");
        password = input.nextLine();

        try{
            ess.iniciarSessao(email,password);
        }
        catch (UsernameInvalidoException e){
            System.out.println(e.getMessage());
        }
        finally{
            input.close();
        }
        imprimeMenuPrincipal();
    }

    private static void regiUti(){
        String nome, password, email;
        Scanner input = new Scanner(System.in);
        float saldo;

        System.out.println("Insira o nome: ");
        nome = input.nextLine();

        System.out.println("Insira a password: ");
        password = input.nextLine();

        System.out.println("Insira o email: ");
        email = input.nextLine();

        saldo = lerFloat("Insira o saldo inicial: ");

        try{
            ess.registar(nome,password,saldo,email);
        }catch (UsernameInvalidoException e){
            System.out.println(e.getMessage());
        }finally {
            input.close();
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


    private static float lerFloat(String msg){
        Scanner input = new Scanner(System.in);
        float r = 0;
        System.out.println(msg);
        try{
            r = input.nextFloat();
        }catch (InputMismatchException e){
            System.out.println("Formato errado!");
            r = lerFloat(msg);
        }
        finally {
            input.close();
        }
        return r;
    }

    private static double lerDouble(String msg){
        Scanner input = new Scanner(System.in);
        double r = 0;
        System.out.println(msg);
        try{
            r= input.nextDouble();
        }
        catch(InputMismatchException e){
            System.out.println("Formato errado!");
            r = lerDouble(msg);
        }
        finally {
            input.close();
        }
        return r;
    }

    private static int lerInt(String msg){
        Scanner input = new Scanner(System.in);
        int r = 0;

        System.out.println(msg);
        try{
            r = input.nextInt();
        }
        catch (InputMismatchException e){
            System.out.println("Formato errado!");
            r = lerInt(msg);
        }
        finally{
            input.close();
        }
        return r;
    }

    private static boolean lerBoolean(String msg){
        Scanner input = new Scanner(System.in);
        String s;
        boolean r = true;

        System.out.println(msg);
        s = input.nextLine();
        if(s.charAt(0) == 'n')
            r = false;
        input.close();
        return r;
    }
}
