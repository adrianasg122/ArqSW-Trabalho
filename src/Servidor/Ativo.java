package Servidor;


import java.util.ArrayList;

public class Ativo implements Comparable<Ativo>, Subject{


    private int id;
    private float price;
    // ask
    private float precoCompra;
    // bid
    private float precoVenda;
    //qual a entidade responsável
    private String descricao;
    //Contratos a observar este ativo
    public ArrayList<Observer> observersCompra;
    public ArrayList<Observer> observersVenda;
    //Utilizadores a seguir o ativo
    public ArrayList<Observer> seguidores;


    public Ativo() {//

    }

    public Ativo(int id, float price,  float precoCompra, float precoVenda, String descricao) {
        this.id = id;
        this.price = price;
        this.precoCompra = precoCompra;
        this.precoVenda = precoVenda;
        this.descricao = descricao;
        this.observersCompra = new ArrayList<>();
        this.observersVenda = new ArrayList<>();
        this.seguidores = new ArrayList<>();
    }

    public Ativo(Ativo a) {
        this.id = a.getId();
        this.price = getPrice();
        this.precoCompra = a.getPrecoCompra();
        this.precoVenda = a.getPrecoVenda();
        this.descricao = a.getDescricao();
        this.observersCompra = a.getObserversCompra();
        this.observersVenda = a.getObserversVenda();
        this.seguidores = new ArrayList<>();
    }


    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
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

    public ArrayList<Observer> getObserversCompra() {
        return observersCompra;
    }

    public void setObserversCompra(ArrayList<Observer> observersCompra) { this.observersCompra = observersCompra; }

    public ArrayList<Observer> getObserversVenda() { return observersVenda; }

    public void setObserversVenda(ArrayList<Observer> observersVenda) { this.observersVenda = observersVenda; }

    public ArrayList<Observer> getSeguidores() { return seguidores; }

    public void setSeguidores(ArrayList<Observer> seguidores) { this.seguidores = seguidores; }

    public Ativo clone () {
        return new Ativo(this);
    }

    public synchronized String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("ID: ").append(id).append("\n");
        sb.append("Price: ").append(price).append("\n");
        sb.append("Preço Compra: ").append(precoCompra).append("\n");
        sb.append("Preço Venda: ").append(precoVenda).append("\n");
        sb.append("Entidade: ").append(descricao).append("\n");
        sb.append("A seguir ").append(seguidores.toString()).append("\n");
        sb.append("**************");

        return sb.toString();
    }

    @Override
    public int compareTo(Ativo o) {
        return this.id - o.id;
    }

    public void notifyObserversCompra() {
            for (Observer o : observersCompra) {
                o.update(this);
            }
    }

    public void notifyObserversVenda() {
            for (Observer o : observersVenda) {
                o.update(this);
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

    public void notifySeguidores(){
        if(seguidores != null)
            if (seguidores.size() != 0)
                for (Observer o : seguidores)
                    o.update(this);
    }

    public void registarSeguidor(Observer o) {
        if(seguidores != null)
            seguidores.add(o);
        else {
            seguidores = new ArrayList<>();
            seguidores.add(o);
        }
    }

    public void removeSeguidor(Observer o) {
        seguidores.remove(o);
    }
}



