package Cliente;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class Escritor {

    private PrintWriter out;

    public Escritor(Socket socket) {
        try {
            out = new PrintWriter(socket.getOutputStream(),true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void escrever (String input) {
        out.println(input);
        out.flush();
    }
}
