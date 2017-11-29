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
            System.out.println("-------------------\n>> " + cabecalho + " <<");
            if (cabecalho.equals("EXCEPTION")) {
                conteudo = in.readLine();
                throw new PedidoFalhadoException(conteudo);
            }  else {
                   switch (comando - 2){
                       case 1 : conteudo = lerListar();
                            break;
                       case 2 : conteudo = lerLinha();
                            break;
                       case 3 : conteudo = lerListar();
                            break;
                       case 4 : conteudo = lerListar();
                           break;
                       case 5 : conteudo = lerLinha();
                            break;
                       case 6 :conteudo = lerLinha();
                            break;
                       case 7 : conteudo = lerListar();
                            break;
                       case 8 : conteudo = lerLinha();
                           break;
                       case 9 : conteudo = lerLinha();
                           break;
                       case 10 : conteudo = lerLinha();
                           break;
                       case 11 : conteudo = lerListar();
                            break;
                       default :
                           break;
                   }
            }
        } catch (IOException e) {
            e.getMessage();
        }
        if (conteudo == null) return " ";
        return conteudo;
    }


    private String lerListar() {
        StringBuilder sb = new StringBuilder();
        String linha;

        while ((linha = lerLinha()) != null){
            if(linha.isEmpty() || linha.equals("§")){
                break;}
            sb.append(linha).append("\n");
        }

        return sb.toString();
    }

    private String lerLinha() {
        try{
            return in.readLine();
        }catch (IOException e){
            System.out.println("Não foi possivel ler novas mensagens!");
        }
        return null;
    }
}
