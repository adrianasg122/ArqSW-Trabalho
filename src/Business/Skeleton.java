package Business;

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
    private Thread notificacao;

    Skeleton(ESSLda ess, Socket cliSocket) throws IOException {
        this.ess = ess;
        this.cliSocket = cliSocket;
        in = new BufferedReader(new InputStreamReader(cliSocket.getInputStream()));
        out = new PrintWriter(cliSocket.getOutputStream(), true);
        utilizador = null;
        notificacao = null;
    }

    public void run() {
        String request = null;

        while((request = readLine()) != null) {
            String response = null;
            response = interpreteRequest(request);
            if (!response.isEmpty()) {
                System.out.println(response + "\n");
                out.println(response + "\n");
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
// TODO consultar saldo
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
            case "COMPRA":
                utilizadorLogado(true);
                return startContratoCompra(keywords[1]);
            case "VENDA":
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
            if (parametros.length > 2)
                throw new PedidoFalhadoException("O username/password não podem ter espaços");

            ess.registar(parametros[0], parametros[1], Integer.parseInt(parametros[2]));
        } catch (ArrayIndexOutOfBoundsException | UtilizadorInvalidoException e) {
            throw new PedidoFalhadoException("Os argumentos dados não são válidos");
        }
        return "OK";
    }

    private String login(String arguments) throws PedidoFalhadoException {
        String[] parameters = arguments.split(" ");

        try {
            utilizador = ess.iniciarSessao(parameters[0], parameters[1]);
        } catch (ArrayIndexOutOfBoundsException | UtilizadorInvalidoException e) {
            throw new PedidoFalhadoException("Os argumentos dados não são válidos");
        }
        return "OK";
    }

    private String startContratoCompra(String descricao) throws PedidoFalhadoException {
        String[] parametros = descricao.split(" ");
        int contratoID;

        try {
            contratoID = ess.criarContratoCompra(Integer.parseInt(parametros[0]),Float.parseFloat(parametros[1]), Integer.parseInt(parametros[2]), Integer.parseInt(parametros[3]));
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
        } catch (ArrayIndexOutOfBoundsException e) {
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

        return "OK";
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

        return "OK\n" + sb.toString();
    }

    private String listarAtivosVenda() throws PedidoFalhadoException{
        Set<Ativo> ativos = ess.getAtivosVenda();
        StringBuilder sb = new StringBuilder();

        for(Ativo auc: ativos)
            sb.append("\n").append(auc.toString());

        return "OK\n" + sb.toString();
    }



    private String listarAtivos() throws PedidoFalhadoException {
        Set<Ativo> ativos = ess.listarAtivos();
        StringBuilder sb = new StringBuilder();

        for(Ativo auc: ativos)
            sb.append("\n").append(auc.toString());

        return "OK" + sb.toString();
    }

    private String terminarSessao() {
        terminarSessao();
        return "OK";
    }

    private void utilizadorLogado(boolean estado) throws PedidoFalhadoException {
        if (estado && utilizador == null)
            throw new PedidoFalhadoException("É necessário iniciar sessão para aceder aos ativos");

        if (!estado && utilizador != null)
            throw new PedidoFalhadoException("Já existe uma sessão iniciada");
    }

    private void terminarConexao() {
        if (notificacao != null)
            notificacao.interrupt();

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
