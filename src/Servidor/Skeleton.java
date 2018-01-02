package Servidor;

import java.net.Socket;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.Set;

/**
 * Classe que interpreta os pedidos de cada cliente, retornando os
 * respetivos resultados
 */
public class Skeleton extends Thread {
    private Utilizador utilizador;
    private Socket cliSocket;
    private PrintWriter out;
    private BufferedReader in;
    private ESSLda ess;
    private Updater updater;

    Skeleton(ESSLda ess, Socket cliSocket) throws IOException {
        this.ess = ess;
        this.cliSocket = cliSocket;
        in = new BufferedReader(new InputStreamReader(cliSocket.getInputStream()));
        out = new PrintWriter(cliSocket.getOutputStream(), true);
        utilizador = null;
    }

    public void run() {
        String request;

        while((request = readLine()) != null) {
            String response;
            response = interpreteRequest(request);
            if (!response.isEmpty()) {
                System.out.println("Resposta servidor: " + response);
                out.println(response);
                out.flush();

            }
        }
        terminarConexao();
    }

    private String interpreteRequest(String request){
        try {
               return runCommand(request);
        } catch (PedidoFalhadoException e) {
            return "EXCEPTION\n" + e.getMessage();
        } catch (ArrayIndexOutOfBoundsException e) {
            return "EXCEPTION\n" + "Os argumentos não foram especificados";
        }
    }

    private String runCommand(String request) throws ArrayIndexOutOfBoundsException, PedidoFalhadoException {
        String[] keywords = request.split(" ", 2);

        switch(keywords[0].toUpperCase()) {
            case "REGISTAR":
                utilizadorLogado(false);
                return registar(keywords[1]);
            case "LOGIN":
                utilizadorLogado(false);
                return login(keywords[1]);
            case "LISTARATIVOS":
                utilizadorLogado(true);
                return listarAtivos();
            case "SALDO":
                utilizadorLogado(true);
                return getSaldo();
            case "NOME":
                utilizadorLogado(true);
                return porNome(keywords[1]);
            case "COMPRAR":
                utilizadorLogado(true);
                return startContratoCompra(keywords[1]);
            case "VENDER":
                utilizadorLogado(true);
                return startContratoVenda(keywords[1]);
            case "LISTARCONTRATOS":
                utilizadorLogado(true);
                return listarContratos();
            case "LISTARVALORESVENDA":
                utilizadorLogado(true);
                return listarAtivosVenda();
            case "ENCERRAR":
                utilizadorLogado(true);
                return fecharContrato(keywords[1]);
            case "SEGUIR":
                utilizadorLogado(true);
                return seguir(keywords[1]);
            case "NOTIFICAR":
                utilizadorLogado(true);
                return notificar();
            case "TERMINAR":
                utilizadorLogado(true);
                return terminarSessao();
            default:
                throw new PedidoFalhadoException(keywords[0] + " não é um comando válido");
        }
    }

    private String registar(String argumentos) throws PedidoFalhadoException {
        String[] parametros = argumentos.split(" ");

        try {
            ess.registar(parametros[0], parametros[1], Float.parseFloat(parametros[2]));
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new PedidoFalhadoException("O username/password não podem ter espaços");
        } catch (UtilizadorInvalidoException e) {
            throw new PedidoFalhadoException(e.getMessage());
        }
        return "OK";
    }

    private String login(String arguments) throws PedidoFalhadoException {
        String[] parameters = arguments.split(" ");

        try {
            utilizador = ess.iniciarSessao(parameters[0], parameters[1]);
        } catch (ArrayIndexOutOfBoundsException | UtilizadorInvalidoException e) {
            throw new PedidoFalhadoException(e.getMessage());
        }
        return "OK\n";
    }

    private String porNome(String nome) throws PedidoFalhadoException {
        String [] a = nome.split(" ");
        String res = ess.porNome(a[0]);


        return "OK\n" + res + "\n§";

    }

    private String startContratoCompra(String descricao) throws PedidoFalhadoException {
        String[] parametros = descricao.split(" ");
        int contratoID;

        try {
            contratoID = ess.criarContratoCompra(Integer.parseInt(parametros[0]),Float.parseFloat(parametros[1]), Float.parseFloat(parametros[2]), Integer.parseInt(parametros[3]));
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new PedidoFalhadoException("Os argumentos dados não são válidos");
        }

        return "OK\n" + contratoID;
    }

    private String startContratoVenda(String descricao) throws PedidoFalhadoException {
        String[] parametros = descricao.split(" ");
        int contratoID;

        try {
            contratoID = ess.criarContratoVenda(Integer.parseInt(parametros[0]),Float.parseFloat(parametros[1]), Float.parseFloat(parametros[2]), Integer.parseInt(parametros[3]));
        } catch (SaldoInsuficienteException |ArrayIndexOutOfBoundsException e) {
            throw new PedidoFalhadoException("Os argumentos dados não são válidos");
        }

        return "OK\n" + contratoID;
    }

    private String fecharContrato(String desc) throws PedidoFalhadoException {
        try {
            int ativoID = Integer.parseInt(desc);
            ess.fecharContrato(ativoID);
        } catch (ArrayIndexOutOfBoundsException | NumberFormatException | SaldoInsuficienteException e) {
            throw new PedidoFalhadoException("Os argumentos dados não são válidos");
        }

        return "OK\n";
    }

    private String getSaldo() {
        String res = Float.toString(ess.getSaldoUtilizador());
        return "OK\n" + res;
    }



    private String listarContratos() throws PedidoFalhadoException {
        Set<Contrato> contratos = ess.consultaPortCFD();
        StringBuilder sb = new StringBuilder();

        for(Contrato auc: contratos)
            sb.append("\n").append(auc.toString());

        return "OK" + sb.toString() + "\n§";
    }

    private String listarAtivosVenda() throws PedidoFalhadoException{
        Set<Ativo> ativos = ess.getAtivosVenda();
        StringBuilder sb = new StringBuilder();

        for(Ativo auc: ativos)
            sb.append("\n").append(auc.toString());

        return "OK" + sb.toString() + "\n§";
    }



    private String listarAtivos() throws PedidoFalhadoException {
        Set<Ativo> ativos = ess.listarAtivos();
        StringBuilder sb = new StringBuilder();

        for(Ativo auc: ativos)
            sb.append("\n").append(auc.toString());

        return "OK" + sb.toString() + "\n§";
    }

    private String seguir(String str) {
            String[] parametros = str.split(" ");
            float price = ess.seguir(Integer.parseInt(parametros[0]), Float.parseFloat(parametros[1]));
        return "OK\n" + price;
    }

    private String notificar() {
        ess.notificar();
        return "OK";
    }

    private String terminarSessao() {
        ess.terminarSessao();
        return "OK";
    }

    private void utilizadorLogado(boolean estado) throws PedidoFalhadoException {
        if (estado == true && utilizador == null)
            throw new PedidoFalhadoException("É necessário iniciar sessão para aceder aos ativos");

        if (estado == false && utilizador != null)
            throw new PedidoFalhadoException("Já existe uma sessão iniciada");
    }

    private void terminarConexao() {
        try {
            cliSocket.close();
        } catch (IOException e) {
            System.out.println("Couldn't close client socket... Client won't care");
        }
    }

    private String readLine() {
        String line = null;

        try {
            line = in.readLine();
        } catch(IOException e) {
            terminarConexao();
        }

        return line;
    }
}
