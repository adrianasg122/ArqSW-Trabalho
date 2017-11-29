package Servidor;

import java.util.ArrayList;
import java.util.List;

public class NotificationBuffer {

    private List<String> notificacoes;

    public NotificationBuffer() {
        this.notificacoes = new ArrayList<>();
    }

    public void add(String s) {
        notificacoes.add(s);
    }

    public String ler() {
        StringBuilder str = new StringBuilder();

        if (!notificacoes.isEmpty()){
            for (String s : notificacoes) {
                str.append(s).append("\n");
            }
            notificacoes.clear();
            return str.toString() + "§";
        }
        return "";
    }

}
