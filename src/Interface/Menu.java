package Interface;

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
        String menu = String.join("\n", options);
        System.out.println(menu + "\n");

        while(option <= 0 || option > options.length) {
            option = readInt("Escolha uma das opções: ");
            if (option <= 0 || option > options.length)
                System.out.println("\n> Opção inválida\n");
        }

        return option;
    }

    public void printResponse(String response) {
        if (response.length() > 0)
            response += "\n";

        System.out.print("\n" + response);
    }

    public String readString(String msg) {
        System.out.print(msg);
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

    public float readFloat(String msg) {
        float num;

        try {
            System.out.print(msg);
            num = Float.parseFloat(in.next());
        } catch (NumberFormatException e) {
            System.out.println("\n> O valor introduzido não é válido\n");
            num = readFloat(msg);
        }

        return num;
    }
}