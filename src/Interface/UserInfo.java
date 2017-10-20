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

        synchronized public int getcomando() {
            return comando;
        }

        synchronized public void setcomando(int comando) {
            this.comando = comando;
        }

        synchronized public boolean islog() {
            return log;
        }

        synchronized public void setlog(boolean estado) {
            log = estado;
        }

        synchronized public void setresposta(boolean sucesso, String conteudo) {
            this.sucesso = sucesso;
            this.conteudo = conteudo;

            resposta = 1 - resposta;
            notifyAll();
        }

        synchronized public boolean getrespostaStatus() {
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

        synchronized public String getnotificacoes() {
            String msg;
            StringBuilder sb = new StringBuilder();

            while((msg = notificacoes.pollFirst()) != null)
                sb.append(msg).append("\n");

            if (sb.length() > 0)
                sb.deleteCharAt(sb.length() - 1);

            return sb.toString();
        }

        synchronized public int getNumberOfnotificacoes() {
            return notificacoes.size();
        }
    }

