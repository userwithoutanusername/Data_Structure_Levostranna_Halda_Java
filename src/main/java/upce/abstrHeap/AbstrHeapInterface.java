package upce.abstrHeap;

import upce.abstrTreeEnum.eTypProhl;
import upce.ads.KolekceException;

import java.util.Iterator;

public interface AbstrHeapInterface<T extends Comparable<T>> extends Iterable<T> {
    void vybuduj(T[] obj);

    void prebuduj();

    void zrus();

    boolean jePrazdny();

    void vloz(T vloz);

    T zpristupniMax();

    T odeberMax();

    Iterator iterator(eTypProhl typ);
}
