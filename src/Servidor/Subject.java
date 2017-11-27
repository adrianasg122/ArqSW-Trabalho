package Servidor;

public interface Subject {


    void registerObserverCompra(Observer observer);
    void registerObserverVenda(Observer observer);
    void removeObserver(Observer observer);
    void notifyObserversCompra();
    void notifyObserversVenda();
    void notifySeguidores();
    void registarSeguidor(Observer observer);
    void removeSeguidor(Observer observer);


}
