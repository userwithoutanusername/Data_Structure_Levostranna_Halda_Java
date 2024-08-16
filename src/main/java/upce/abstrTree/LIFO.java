package upce.abstrTree;

import upce.ads.KolekceException;

import java.util.Iterator;

public interface LIFO<T> {
    void zrus();

    boolean jePrazdny();

    void vloz(T data);

    T odeber() throws KolekceException;

    Iterator vytvorIterator();

}
