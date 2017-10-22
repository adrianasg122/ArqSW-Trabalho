package Interface;


import java.io.IOException;
import java.net.Socket;

public class Main {

    public static void main(String[] args) throws IOException {

        final int port = 5000;

        Socket cli = new Socket("127.0.0.1", port);
        Leitor leitor = new Leitor(cli);
        Escritor escritor = new Escritor(cli);
        ESSLdaApp ess = new ESSLdaApp(leitor, escritor);

        ess.executa();
    }
}
