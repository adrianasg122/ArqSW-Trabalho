package Business;

import java.net.Socket;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.List;
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
    private Thread notificar;

    Skeleton(ESSLda ess, Socket cliSocket) throws IOException {
        this.ess = ess;
        this.cliSocket = cliSocket;
        in = new BufferedReader(new InputStreamReader(cliSocket.getInputStream()));
        out = new PrintWriter(cliSocket.getOutputStream(), true);
        utilizador = null;
        t = null;
    }

    public void run() {
        String request = null;

        while((request = readLine()) != null) {
            String response = interpreteRequest(request);

            if (!response.isEmpty())
                out.println(response + "\n");
        }

        terminarConexao();
    }

    private String interpreteRequest(String request) {
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
            case "COMPRA":
                utilizadorLogado(true);
                return startContratoCompra(keywords[1]);
            case "VENDA":
                utilizadorLogado(true);
                return closeAuction(keywords[1]);
            case "LISTARCONTRATOS":
                utilizadorLogado(true);
                return listarContratos();
            case "LISTARVALORES":
                utilizadorLogado(true);
                return listAuctions();
            case "CONFIRMAR":
                utilizadorLogado(true);
                return notificacao(keywords[1]);
            default:
                throw new PedidoFalhadoException(keywords[0] + " não é um comando válido");
        }
    }

    private String registar(String argumentos) throws PedidoFalhadoException {
        String[] parametros = argumentos.split(" ");

        try {
            if (parametros.length > 2)
                throw new PedidoFalhadoException("O username/password não podem ter espaços");

            ess.registar(parametros[0], parametros[1], parametros[2]);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new PedidoFalhadoException("Os argumentos dados não são válidos");
        } catch (UsernameInvalidoException e) {
            throw new PedidoFalhadoException(e.getMessage());
        }

        return "OK";
    }

    private String login(String arguments) throws PedidoFalhadoException {
        String[] parameters = arguments.split(" ");

        try {
            utilizador = ess.iniciarSessao(parameters[0], parameters[1]);
            utilizador.setSession(cliSocket);

            notificar = new Notificacao(utilizador, out);
            notificar.start();
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new PedidoFalhadoException("Os argumentos dados não são válidos");
        } catch (IOException e) {
            throw new PedidoFalhadoException("Não foi possível iniciar sessão");
        } catch (SemAutorizacaoException e) {
            throw new PedidoFalhadoException(e.getMessage());
        }

        return "OK";
    }

    private String startContratoCompra(String descricao) throws PedidoFalhadoException {
        String[] parametros = descricao.split(" ");
        int contratoID;

        try {
            contratoID = ess.criarContratoCompra(parametros[0],parametros[1], parametros[2], parametros[3]);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new PedidoFalhadoException("Os argumentos dados não são válidos");
        }

        return "OK\n" + contratoID;
    }

    private String startContratoVenda(String descricao) throws PedidoFalhadoException {
        String[] parametros = descricao.split(" ");
        int contratoID;

        try {
            contratoID = ess.criarContratoVenda(parametros[0],parametros[1], parametros[2], parametros[3]);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new PedidoFalhadoException("Os argumentos dados não são válidos");
        }

        return "OK\n" + contratoID;
    }

    private String closeAuction(String argument) throws PedidoFalhadoException {
        Bid bestBid;

        try {
            int auctionID = Integer.parseInt(argument);
            bestBid = ess.closeAuction(utilizador, auctionID);
        } catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
            throw new PedidoFalhadoException("Os argumentos dados não são válidos");
        } catch (SemAutorizacaoException | IdInvalidoException e) {
            throw new PedidoFalhadoException(e.getMessage());
        }

        if (bestBid.buyer() == null)
            return "OK\n" + "Ninguém licitou o item. O item não foi vendido";

        String message = "O item foi vendido a " + bestBid.buyer() + " por " + bestBid.value() + "!";

        return "OK\n" + message;
    }



    private String listarContratos() throws PedidoFalhadoException {
        List<Contrato> contratos = ess.consultaPortCFD();
        StringBuilder sb = new StringBuilder();

        for(Contrato auc: contratos)
            sb.append("\n").append(auc.toString(utilizador));

        return "OK" + sb.toString();
    }

    private String listarPrecosVenda() throws PedidoFalhadoException {
        Set<Ativo> ativos = ess.consultaValorVendaAtivos();
        StringBuilder sb = new StringBuilder();

        for(Ativo auc: ativos)
            sb.append("\n").append(auc.toString(utilizador));

        return "OK" + sb.toString();
    }

    private String listarAtivos() throws PedidoFalhadoException {
        Set<Ativo> ativos = ess.listarAtivos();
        StringBuilder sb = new StringBuilder();

        for(Ativo auc: ativos)
            sb.append("\n").append(auc.toString(utilizador));

        return "OK" + sb.toString();
    }

    private String notificacao(String argument) throws PedidoFalhadoException {
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
        if (notificar != null)
            notificar.interrupt();

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
