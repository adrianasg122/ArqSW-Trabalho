package Interface;


import Business.ESSLda;
import Business.Utilizador;

import java.lang.Thread;
import java.io.*;

public class Reader extends Thread{
    private BufferedReader read_socket;
    private ESSLda ess;
    private Utilizador user;
    private MensagemServidor ms;



    public void run(){
        try{
            String input;
            while((input = read_socket.readLine()) != null){
                if(input.equals("Iniciar Sessão")){
                    String user,pass;
                    user = read_socket.readLine();
                    pass = read_socket.readLine();

                    try{
                        this.user = ess.iniciarSessao(user,pass);
                    }
                    catch(Exception e){
                        ms.setMensagem(e.getMessage(),null);
                    }
                }
                else if(input.equals("Registar Utilizador")){
                    String user,pass;
                    user = read_socket.readLine();
                    pass = read_socket.readLine();

                    try{
                        ess.registar(user,pass,0,ms);
                        ms.setMensagem("Registado",null);
                    }
                    catch(Exception e){
                        ms.setMensagem(e.getMessage(),null);
                    }
                }
                else if(input.equals("registar_vendedor")){
                    String user,pass;
                    user = read_socket.readLine();
                    pass = read_socket.readLine();

                    try{
                        g.registarUtilizador(user,pass,1,ms);
                        ms.setMensagem("Registado",null);
                    }
                    catch(Exception e){
                        ms.setMensagem(e.getMessage(),null);
                    }
                }
                else if(input.equals("licitar")){
                    String idLeilao,valor;
                    idLeilao = read_socket.readLine();
                    valor = read_socket.readLine();
                    Double val = Double.parseDouble(valor);
                    try{
                        g.licitarLeilao(idLeilao,u,val);
                        ms.setMensagem("Licitou",null);
                    }
                    catch(Exception e){
                        ms.setMensagem(e.getMessage(),null);
                    }
                }
                else if(input.equals("iniciar_leilao")){
                    String descricao;
                    descricao = read_socket.readLine();
                    Leilao leilao = new Leilao(descricao,u);
                    try{
                        g.adicionarLeilao(leilao,user);
                        ms.setMensagem("Leilao Iniciado",null);
                    }
                    catch(Exception e){
                        ms.setMensagem(e.getMessage(),null);
                    }
                }
                else if(input.equals("consultar_leilao")){
                    try{
                        ms.setMensagem(null,ess.consultarLeiloes(user));
                    }
                    catch(Exception e){
                        ms.setMensagem(e.getMessage(),null);
                    }
                }
                else if(input.equals("encerrar_leilao")){
                    String idLeilao;
                    idLeilao = read_socket.readLine();
                    try{
                        ms.setMensagem(ess.encerrarLeilao(idLeilao,user),null);
                    }
                    catch(Exception e){
                        ms.setMensagem(e.getMessage(),null);
                    }
                }
                else if(input.equals("terminar_sessao")){
                    this.u = null;
                    ms.setMensagem("Terminou sessão",null);
                }
            }
            read_socket.close();
            ms.setMensagem("Sair",null);
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
}