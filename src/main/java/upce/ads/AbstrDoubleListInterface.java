package upce.ads;

import java.io.Serializable;

public interface AbstrDoubleListInterface<T> extends Iterable<T>, Serializable {

    void zrus();

    boolean jePrazdny();

    void vlozPrvni(T data);

    void vlozPosledni(T data);

    void vlozNaslednika(T data) throws KolekceException;

    void vlozPredchudce(T data) throws KolekceException;

    T zpristupniAktualni() throws KolekceException;

    T zpristupniPrvni() throws KolekceException;

    T zpristupniPosledni() throws KolekceException;

    T zpristupniNaslednika() throws KolekceException;

    T zpristupniPredchudce() throws KolekceException;

    T odeberAktualni() throws KolekceException;

    T odeberPrvni() throws KolekceException;

    T odeberPosledni() throws KolekceException;

    T odeberNaslednika() throws KolekceException;

    T odeberPredchudce() throws KolekceException;
}
