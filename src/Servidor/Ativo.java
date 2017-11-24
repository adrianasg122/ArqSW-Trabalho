package Servidor;


import java.util.ArrayList;

public class Ativo implements Comparable<Ativo>, Subject{


    private int id;
    // ask
    private float precoCompra;
    // bid
    private float precoVenda;
    //qual a entidade responsável
    private String descricao;
    //Contratos a observar este ativo
    public ArrayList<Observer> observersCompra;
    public ArrayList<Observer> observersVenda;



    public Ativo() {//

    }

    public Ativo(int id, float precoCompra, float precoVenda, String descricao) {
        this.id = id;
        this.precoCompra = precoCompra;
        this.precoVenda = precoVenda;
        this.descricao = descricao;
        this.observersCompra = new ArrayList<>();
        this.observersVenda = new ArrayList<>();
    }

    public Ativo(Ativo a) {
        this.id = a.getId();
        this.precoCompra = a.getPrecoCompra();
        this.precoVenda = a.getPrecoVenda();
        this.descricao = a.getDescricao();
        this.observersCompra = a.getObserversCompra();
        this.observersVenda = a.getObserversVenda();
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getPrecoCompra() {
        return precoCompra;
    }

    public void setPrecoCompra(float preco) {
        this.precoCompra = preco;
    }

    public float getPrecoVenda() { return precoVenda; }

    public void setPrecoVenda(float precoVenda) { this.precoVenda = precoVenda; }

    public String getDescricao() { return descricao; }

    public void setDescricao(String descricao) { this.descricao = descricao; }

    public ArrayList<Observer> getObserversCompra() { return observersCompra; }

    public void setObserversCompra(ArrayList<Observer> observersCompra) { this.observersCompra = observersCompra; }

    public ArrayList<Observer> getObserversVenda() { return observersVenda; }

    public void setObserversVenda(ArrayList<Observer> observersVenda) { this.observersVenda = observersVenda; }

    public Ativo clone () {
        return new Ativo(this);
    }

    public synchronized String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("ID: ").append(id).append("\n");
        sb.append("Preço Compra: ").append(precoCompra).append("\n");
        sb.append("Preço Venda: ").append(precoVenda).append("\n");
        sb.append("Entidade: ").append(descricao).append("\n");
        sb.append("**************");

        return sb.toString();
    }

    @Override
    public int compareTo(Ativo o) {
        return this.id - o.id;
    }

    public void notifyObserversCompra() {
        // Chama o método update de todos os observers disponíveis
        if (observersCompra != null) {
            for (Observer o : observersCompra) {
                o.update(this);
            }
        }
    }

    public void notifyObserversVenda() {
        // Chama o método update de todos os observers disponíveis
        if (observersCompra != null) {
            for (Observer o : observersVenda) {
                o.update(this);
            }
        }
    }

    public void registerObserverCompra(Observer o){
        observersCompra.add(o);
    }

    public void registerObserverVenda(Observer o){
        observersVenda.add(o);
    }

    public void removeObserver(Observer o){
        if(observersCompra.contains(o)) observersCompra.remove(o);
        else if (observersVenda.contains(o)) observersVenda.remove(o);
    }
}



