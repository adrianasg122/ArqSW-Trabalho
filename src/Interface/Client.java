package Interface;

import Business.ESSLda;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
    private static int port  = 5000;

    public Client () {}

    public static void main (String [] args) throws IOException, UnknownHostException{
        if(args.length == 0)
            return;

        Socket s = new Socket(args[0],port);
        UserInfo user = new UserInfo();
        Reader r = new Reader(s,user);
        ESSLda app = new ESSLda(s,user);

        r.start();
        app.start();
    }
}
