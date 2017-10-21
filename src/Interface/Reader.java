/*package Interface;


        import java.lang.Thread;
        import java.io.*;
        import java.net.*;

public class Reader extends Thread{
    private BufferedReader read_socket;
    private UserInfo client;
    private Socket socket;

    public Reader(Socket socket,UserInfo client) throws IOException{
        this.socket = socket;
        this.client = client;
        read_socket = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    public void run(){

        String line, header, conteudo = null;

        while((line = readLine()) != null){
            header = line;


            switch (client.getComando()){
                case 3 : conteudo = lerListarAcoes();
                break;
                case 5 : conteudo = lerListarVendas();
                break;
                case 8 : conteudo = lerConsultarPortfolio();
                break;
                case 9 : conteudo = lerFecharContrato();
                break;
                default:break;
            }

            giveMessege(header,conteudo);
        }

        System.out.println("\nLigação ao servidor terminada!");
        System.exit(1);
    }


    private void giveMessege(String header, String content){
        if(header.equals("EXECEPTION"))
            client.setResposta(false,">" + content);
        else if(header.equals("OK"))
            client.setResposta(false,content);
    }

    private String readContent() {
        StringBuilder sb = new StringBuilder();
        String line;

        while ((line = readLine()) != null){
            if(line.isEmpty())
                break;
            sb.append(line).append("\n");
        }
        return sb.toString();
    }

    private String readLine() {
        String line = null;

        try{
            line = read_socket.readLine();
        }catch (IOException e){
            System.out.println("Não foi possivel ler novas mensagens!");
        }
        return line;
    }


}*/
