package upce.abstrTree;


import upce.ads.AbstrDoubleList;
import upce.ads.KolekceException;

import java.util.Iterator;

public class AbstrFIFO<T> implements FIFO<T> {

    private final AbstrDoubleList<T> list;

    AbstrFIFO() {
        list = new AbstrDoubleList<>();
    }

    @Override
    public void zrus() {
        list.zrus();
    }

    @Override
    public boolean jePrazdny() {
        return list.jePrazdny();
    }

    @Override
    public void vloz(T data) {
        list.vlozPosledni(data);
    }

    @Override
    public T odeber() throws KolekceException {
        return list.odeberPrvni();
    }

    @Override
    public Iterator vytvorIterator() {
        return null;
    }
}
