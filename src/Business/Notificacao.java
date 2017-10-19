package Business;

import java.io.PrintWriter;

public class Notificacao extends Thread {
    private final PrintWriter out;
    private final Utilizador utilizador;

    public Notificacao(Utilizador utilizador, PrintWriter out) {
        this.utilizador = utilizador;
        this.out = out;
    }

    public void run() {
        while(true) {
            try {
                String message = utilizador.lerNotificacao();
                out.println("NOTIFICAÇÃO\n" + message + "\n");
            } catch (InterruptedException e) {
                break;
            }
        }
    }
}
