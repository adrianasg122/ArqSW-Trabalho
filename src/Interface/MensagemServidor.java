package Interface;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class MensagemServidor {
    private List<String> mensagem;
    private Condition c;
    private ReentrantLock lock;
    private int index;

    public MensagemServidor(Condition c, ReentrantLock lock) {
        this.mensagem = new ArrayList<>();
        this.c = c;
        this.lock = lock;
        this.index = 0;
    }

    public void setMensagem(String msg, ArrayList<String> lista) {
        this.lock.lock();
        try {
            if (lista == null)
                this.mensagem.add(msg);
            else {
                for (String m : lista)
                    this.mensagem.add(m);
            }
            c.signal();
        } finally {
            this.lock.unlock();
        }
    }

    public String getMensagem() {
        this.lock.lock();
        try {
            if (index != mensagem.size())
                return this.mensagem.get((index++));
            else return null;
        } finally {
            this.lock.unlock();
        }
    }

    public Condition getCondition() {
        return this.c;
    }

    public ReentrantLock getLock() {
        return this.lock;
    }
}