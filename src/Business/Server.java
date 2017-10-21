package Business;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private static final int port = 5002;

    public static void main(String[] args) throws IOException {
        ServerSocket srv = new ServerSocket(port);
        ESSLda ess = new ESSLda();
        Updater u = new Updater(ess);
        u.start();

        while(true) {
            Socket cliSocket = srv.accept();
            Skeleton cli = new Skeleton(ess, cliSocket);
            cli.start();
        }
    }
}
