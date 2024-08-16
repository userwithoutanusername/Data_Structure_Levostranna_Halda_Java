package upce.abstrTree;

import upce.abstrTreeEnum.eTypProhl;
import upce.ads.KolekceException;

import java.util.Iterator;

public interface TableInterface <K extends Comparable<K>, V> {
    void zrus();

    boolean jePrazdny();

    void vloz(K key, V value) throws KolekceException;

    V najdi(K key);

    V odeber(K key) throws KolekceException;

    Iterator vytvorIterator (eTypProhl typ);
}
