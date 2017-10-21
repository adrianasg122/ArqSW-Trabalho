package Interface;

import java.util.ArrayDeque;

public class UserInfo {
    private int comando;
    private int resposta;
    private boolean log;
    private boolean sucesso;
    private String conteudo;

    public UserInfo() {
        comando = -1;
        resposta = 1;
        log = false;
        sucesso = false;
        conteudo = null;
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

        while (myresposta == resposta) {
            try {
                wait();
            } catch (InterruptedException e) {
                continue;
            }
        }

        return conteudo;
    }
}
