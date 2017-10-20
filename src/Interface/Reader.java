package Interface;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Reader extends Thread {
        private UserInfo client;
        private Socket cliSocket;
        private BufferedReader in;

        Reader(Socket cliSocket, UserInfo client) throws IOException {
            this.client = client;
            this.cliSocket = cliSocket;
            in = new BufferedReader(new InputStreamReader(cliSocket.getInputStream()));
        }

        public void run() {
            String line, header, content;

            while((line = readLine()) != null) {
                header = line;

                if (header.equals("NOTIFICAR"))
                    content = readContent();
                else {
                    switch(client.getCommand()) {
                        case 4: content = lerAtivos();
                            break;
                            //TODO tirar isto e corrigir e acrescentar
                        case 5: content = readNewAuction();
                            break;
                        case 7: content = lerFechoContrato();
                            break;
                        default: content = readContent();
                    }
                }

                giveMessage(header, content);
            }

            System.out.println("\nLigação terminada pelo servidor");
            System.exit(1);
        }

        private void mensagem(String cabecalho, String conteudo) {
            if (cabecalho.equals("EXCECAO"))
                client.setResposta(false, "> " + conteudo;
            else if (cabecalho.equals("OK"))
                client.setResposta(true, conteudo);
            else
                client.addNotification(conteudo);
        }

        private String lerAtivos() {
            String linha;
            StringBuilder sb = new StringBuilder();

            while((linha = readLine()) != null) {
                if (linha.isEmpty())
                    break;

                sb.append(linha).append("\n");
                sb.append(readLine()).append("\n");
                sb.append(readLine()).append("\n\n");
            }

            if (sb.length() == 0)
                return "> Neste momento não existem mais ativos!\n";

            sb.deleteCharAt(sb.length() - 1);
            return sb.toString();
        }

        private String lerFechoContrato() {
            String message = readLine();
            readLine();
            return "> " + message + "\n";
        }

        private String readContent() {
            StringBuilder sb = new StringBuilder();
            String linha;

            while((linha = readLine()) != null) {
                if (linha.isEmpty())
                    break;

                sb.append(linha).append("\n");
            }

            return sb.toString();
        }

        private String readLine() {
            String linha = null;

            try {
                linha = in.readLine();
            } catch (IOException e) {
                System.out.println("Não foi possível ler novas mensagens");
            }

            return linha;
        }
    }


