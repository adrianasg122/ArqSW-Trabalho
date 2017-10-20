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
            try {
                response = interpreteRequest(request);
            } catch (SaldoInsuficienteException e) {
                e.getMessage();
            } catch (UsernameInvalidoException e) {
                e.getMessage();
            }

            if (!response.isEmpty())
                out.println(response + "\n");
        }

        terminarConexao();
    }

    private String interpreteRequest(String request) throws SaldoInsuficienteException, UsernameInvalidoException {
        try {
            return runCommand(request);
        } catch (PedidoFalhadoException e) {
            return "EXCEPTION\n" + e.getMessage();
        } catch (ArrayIndexOutOfBoundsException e) {
            return "EXCEPTION\n" + "Os argumentos não foram especificados";
        }
    }

    private String runCommand(String request) throws ArrayIndexOutOfBoundsException, PedidoFalhadoException, SaldoInsuficienteException, UsernameInvalidoException {
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
            case "CONFIRMAR":
                utilizadorLogado(true);
                return notificar(keywords[1]);
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
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new PedidoFalhadoException("Os argumentos dados não são válidos");
        } catch (UsernameInvalidoException e) {
            throw new PedidoFalhadoException(e.getMessage());
        }

        return "OK";
    }

    private String login(String arguments) throws PedidoFalhadoException, UsernameInvalidoException {
        String[] parameters = arguments.split(" ");

        try {
            utilizador = ess.iniciarSessao(parameters[0], parameters[1]);
            utilizador.setSession(cliSocket);
            notificacao = new Notificacao(utilizador, out);
            notificacao.start();
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new PedidoFalhadoException("Os argumentos dados não são válidos");
        } catch (IOException e) {
            throw new PedidoFalhadoException("Não foi possível iniciar sessão");
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

    private String fecharContrato(String desc) throws PedidoFalhadoException, SaldoInsuficienteException {
        try {
            int ativoID = Integer.parseInt(desc);
            ess.fecharContrato(ativoID);
        } catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
            throw new PedidoFalhadoException("Os argumentos dados não são válidos");
        } catch (IdInvalidoException e) {
            throw new PedidoFalhadoException(e.getMessage());
        }

        String message = "O contrato foi fechado!";

        return "OK\n" + message;
    }



    private String listarContratos() throws PedidoFalhadoException {
        Set<Contrato> contratos = ess.consultaPortCFD();
        StringBuilder sb = new StringBuilder();

        for(Contrato auc: contratos)
            sb.append("\n").append(auc.toString());

        return "OK" + sb.toString();
    }

    private String listarAtivosVenda() throws PedidoFalhadoException{
        Set<Ativo> ativos = ess.getAtivosVenda();
        StringBuilder sb = new StringBuilder();

        for(Ativo auc: ativos)
            sb.append("\n").append(auc.toString());

        return "OK" + sb.toString();
    }



    private String listarAtivos() throws PedidoFalhadoException {
        Set<Ativo> ativos = ess.listarAtivos();
        StringBuilder sb = new StringBuilder();

        for(Ativo auc: ativos)
            sb.append("\n").append(auc.toString());

        return "OK" + sb.toString();
    }

    private String notificar(String argument) throws PedidoFalhadoException {
        try {
            int amount = Integer.parseInt(argument);
            utilizador.acknowledge(amount);
        } catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
            throw new PedidoFalhadoException("Os argumentos dados não são válidos");
        }

        return "";
    }

    private void utilizadorLogado(boolean estado) throws PedidoFalhadoException {
        if (estado && utilizador == null)
            throw new PedidoFalhadoException("É necessário iniciar sessão para aceder aos leilões");

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
