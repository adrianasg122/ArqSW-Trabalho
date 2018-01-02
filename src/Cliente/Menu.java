package Cliente;

import java.util.Scanner;

public class Menu {
    private Scanner in;
    private String[] options;

    Menu(String[] entries) {
        in = new Scanner(System.in);
        in.useDelimiter("[\r\n]");
        this.options = entries;
    }




    public int showMenu() {
        int option = 0;
        System.out.println("\n ***  Menu  *** ");
        for(int i = 0 ; i < options.length ; i++){
            System.out.print(i+1);
            System.out.print(" - ");
            System.out.println(options[i]);
        }

        while(option <= 0 || option > options.length) {
            option = readInt("Escolha uma das opções: ");
            if (option == 0)
                break;
            if (option < 0 || option > options.length)
                System.out.println("\n> Opção inválida\n");

        }

        return option;
    }


    public String readString (String msg){
        System.out.println(msg);
        return in.next();
    }

    public int readInt(String msg) {
        int num;

        try {
            System.out.print(msg);
            num = Integer.parseInt(in.next());
        } catch (NumberFormatException e) {
            System.out.println("\n> O valor introduzido não é válido\n");
            num = readInt(msg);
        }

        return num;
    }
}