package Interface;

import java.util.ArrayDeque;

public class UserInfo {
        private int comando;
        private int resposta;
        private boolean log;
        private boolean sucesso;
        private String conteudo;
        private ArrayDeque<String> notificacoes;

        public UserInfo() {
            comando = -1;
            resposta = 1;
            log = false;
            sucesso = false;
            conteudo = null;
            notificacoes = new ArrayDeque<>();
        }

        synchronized public int getComando() {
            return comando;
        }

        synchronized public void setComando(int comando) {
            this.comando = comando;
        }

        synchronized public boolean getLog() {
            return log;
        }

        synchronized public void setLog(boolean estado) {
            log = estado;
        }

        synchronized public void setResposta(boolean sucesso, String conteudo) {
            this.sucesso = sucesso;
            this.conteudo = conteudo;

            resposta = 1 - resposta;
            notifyAll();
        }

        synchronized public boolean getRespostaStatus() {
            return sucesso;
        }

        synchronized public String getResponse() {
            int myresposta = resposta;

            while(myresposta == resposta) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    continue;
                }
            }

            return conteudo;
        }

        synchronized public void addNotificacao(String message) {
             notificacoes.addLast(message);
        }

        synchronized public String getNotificacoes() {
            String msg;
            StringBuilder sb = new StringBuilder();

            while((msg = notificacoes.pollFirst()) != null)
                sb.append(msg).append("\n");

            if (sb.length() > 0)
                sb.deleteCharAt(sb.length() - 1);

            return sb.toString();
        }

        synchronized public int getNrNotificacoes() {
            return notificacoes.size();
        }
    }

