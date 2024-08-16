package upce.ads;

import java.io.Serializable;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class AbstrDoubleList<T> implements AbstrDoubleListInterface<T>, Serializable {

    private Uzel prvni;
    private Uzel aktualni;
    private Uzel posledni;
    private int pocet = 0;

    private class Uzel implements Serializable {

        private Uzel predchozi;
        private final T data;
        private Uzel dalsi;

        public Uzel(T data) {
            this.data = data;
            this.dalsi = null;
            this.predchozi = null;

        }
    }

    @Override
    public void zrus() {
        prvni = null;
        aktualni = null;
        posledni = null;
        pocet = 0;
    }

    @Override
    public boolean jePrazdny() {
        return pocet == 0;
    }

    @Override
    public void vlozPrvni(T data) {
        Uzel novy = new Uzel(data);
        if (jePrazdny()) {
            prvni = novy;
            posledni = novy;
        } else {
            novy.dalsi = prvni;
            prvni.predchozi = novy;
            prvni = novy;
        }
        pocet++;
    }

    @Override
    public void vlozPosledni(T data) {
        Uzel novy = new Uzel(data);
        if (jePrazdny()) {
            prvni = novy;
        } else {
            novy.predchozi = posledni;
            posledni.dalsi = novy;
        }
        posledni = novy;
        pocet++;
    }

    private boolean aktualniTest() {
        return aktualni == null;
    }

    @Override
    public void vlozNaslednika(T data) throws KolekceException {
        if (aktualniTest() || jePrazdny()) {
            throw new KolekceException();
        }
        Uzel novy = new Uzel(data);
        if (aktualni == posledni) {
            vlozPosledni(data);
        } else {
            novy.dalsi = aktualni.dalsi;
            aktualni.dalsi.predchozi = novy;
            aktualni.dalsi = novy;
            novy.predchozi = aktualni;
            pocet++;
        }
    }

    @Override
    public void vlozPredchudce(T data) throws KolekceException {
        if (aktualniTest() || jePrazdny()) {
            throw new KolekceException();
        }
        Uzel novy = new Uzel(data);
        if (aktualni == prvni) {
            vlozPrvni(data);
        } else {
            novy.predchozi = aktualni.predchozi;
            aktualni.predchozi.dalsi = novy;
            aktualni.predchozi = novy;
            novy.dalsi = aktualni;
            pocet++;
        }


    }

    @Override
    public T zpristupniAktualni() throws KolekceException {
        if (jePrazdny()) {
            throw new KolekceException();
        }
        return aktualni.data;
    }

    @Override
    public T zpristupniPrvni() throws KolekceException {
        if (jePrazdny()) {
            throw new KolekceException();
        }
        aktualni = prvni;
        return aktualni.data;
    }

    @Override
    public T zpristupniPosledni() throws KolekceException {
        if (jePrazdny()) {
            throw new KolekceException();
        }
        aktualni = posledni;
        return aktualni.data;
    }

    @Override
    public T zpristupniNaslednika() throws KolekceException {
        if (aktualniTest() || jePrazdny()) {
            throw new KolekceException();
        }
        if (aktualni == posledni) {
            throw new KolekceException();
        }
        aktualni = aktualni.dalsi;
        return aktualni.data;
    }

    @Override
    public T zpristupniPredchudce() throws KolekceException {
        if (aktualniTest() || jePrazdny()) {
            throw new KolekceException();
        }
        if (aktualni == prvni) {
            throw new KolekceException();
        }

        aktualni = aktualni.predchozi;
        return aktualni.data;
    }

    @Override
    public T odeberAktualni() throws KolekceException {
        if (aktualniTest() || jePrazdny()) {
            throw new KolekceException();
        }
        if (aktualni == prvni) {
            return odeberPrvni();
        } else if (aktualni == posledni) {
            return odeberPosledni();
        }

        T data = aktualni.data;
        aktualni.predchozi.dalsi = aktualni.dalsi;
        aktualni.dalsi.predchozi = aktualni.predchozi;
        pocet--;
        aktualni = null;

        return data;
    }

    @Override
    public T odeberPrvni() throws KolekceException {
        if (jePrazdny()) {
            throw new KolekceException();
        }
        T data = prvni.data;
        if (aktualni == prvni) {
            aktualni = null;
        }
        if (pocet == 1) {
            prvni = null;
            posledni = null;
        } else {
            prvni = prvni.dalsi;
            prvni.predchozi = null;
        }
        pocet--;
        return data;
    }

    @Override
    public T odeberPosledni() throws KolekceException {
        if (jePrazdny()) {
            throw new KolekceException();
        }
        T data = posledni.data;
        if (aktualni == posledni) {
            aktualni = null;
        }
        if (pocet == 1) {
            prvni = null;
            posledni = null;
        } else {
            posledni = posledni.predchozi;
            posledni.dalsi = null;
        }
        pocet--;
        return data;
    }

    @Override
    public T odeberNaslednika() throws KolekceException {
        if (aktualniTest() || jePrazdny()) {
            throw new KolekceException();
        }
        if (aktualni == posledni) {
            throw new KolekceException();
        }
        T data = aktualni.dalsi.data;
        if (aktualni.dalsi == posledni) {
            odeberPosledni();
        } else {
            aktualni.dalsi = aktualni.dalsi.dalsi;
            aktualni.dalsi.predchozi = aktualni;
            pocet--;
        }
        return data;
    }

    @Override
    public T odeberPredchudce() throws KolekceException {
        if (aktualniTest() || jePrazdny()) {
            throw new KolekceException();
        }
        if (aktualni == posledni) {
            throw new KolekceException();
        }
        T data = aktualni.predchozi.data;
        if (aktualni.predchozi == prvni) {
            odeberPosledni();
        } else {
            aktualni.predchozi = aktualni.predchozi.predchozi;
            aktualni.predchozi.dalsi = aktualni;
            pocet--;
        }
        return data;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private Uzel aktualni = prvni;

            @Override
            public boolean hasNext() {
                return aktualni != null;
            }

            @Override
            public T next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                T data = aktualni.data;
                aktualni = aktualni.dalsi;
                return data;
            }
        };

    }

}
