package Servidor;

public interface Subject {

    public void registerObserverCompra(Observer observer);
    public void registerObserverVenda(Observer observer);
    public void removeObserver(Observer observer);
    public void notifyObserversCompra();
    public void notifyObserversVenda();
}
