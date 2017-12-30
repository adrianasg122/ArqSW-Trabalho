package Cliente;

import Servidor.PedidoFalhadoException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Leitor {

    private BufferedReader in;


    public Leitor(Socket socket) {
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public String ler(int comando) throws PedidoFalhadoException {
        String cabecalho, conteudo = null;
        try {
            cabecalho = in.readLine();
            System.out.println(cabecalho);
            if (cabecalho.equals("EXCEPTION")) {
                conteudo = in.readLine();
                throw new PedidoFalhadoException(conteudo);
            }  else {
                   switch (comando){
                       case 3 :
                       case 4 :
                       case 5 :
                       case 6 :
                       case 7 :
                       case 8 : conteudo = lerListar();
                       default :
                           break;
                   }
            }
        } catch (IOException e) {
            e.getMessage();
        }
        return conteudo;
    }


    private String lerListar() {
        StringBuilder sb = new StringBuilder();
        String linha;

        while ((linha = lerLinha()) != null){
            if(linha.isEmpty())
                break;
            sb.append(linha).append("\n");
        }

        return sb.toString();
    }

    private String lerLinha() {
        String linha = null;

        try{
            linha = in.readLine();
        }catch (IOException e){
            System.out.println("Não foi possivel ler novas mensagens!");
        }
        return linha;
    }
}
