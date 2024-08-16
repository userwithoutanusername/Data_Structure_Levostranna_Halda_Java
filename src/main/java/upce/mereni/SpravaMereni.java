package upce.mereni;

import upce.ads.AbstrDoubleList;
import upce.ads.AbstrDoubleListInterface;
import upce.ads.KolekceException;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Iterator;

public class SpravaMereni {

    private AbstrDoubleList<Mereni> seznam;

    public void ulozData(String jmenoSouboru) throws KolekceException {
        try {
            Soubor.ulozData(jmenoSouboru, seznam);
        } catch (Exception e) {
            throw new KolekceException();
        }
    }

    public void nactiData(String jmenoSouboru) throws KolekceException {
        try {
            seznam = Soubor.nactiData(jmenoSouboru);
        } catch (Exception e) {
            throw new KolekceException();
        }
    }

    public SpravaMereni() {
        seznam = new AbstrDoubleList<>();
    }

    public void vlozMereni(Mereni mereni, enumPozice pozice) throws KolekceException {
        switch (pozice) {
            case PRVNI -> seznam.vlozPrvni(mereni);
            case POSLEDNI -> seznam.vlozPosledni(mereni);
            case NASLEDNIK -> seznam.vlozNaslednika(mereni);
            case PREDCHUDCE -> seznam.vlozPredchudce(mereni);
        }
    }

    public Mereni zpristupniMereni(enumPozice pozice) throws KolekceException {
        switch (pozice) {
            case PRVNI -> {
                return seznam.zpristupniPrvni();
            }
            case POSLEDNI -> {
                seznam.zpristupniPosledni();
            }
            case AKTUALNI -> {
                return seznam.zpristupniAktualni();
            }
            case NASLEDNIK -> {
                return seznam.zpristupniNaslednika();
            }
            case PREDCHUDCE -> {
                return seznam.zpristupniPredchudce();
            }
            default -> {
                return null;
            }
        }
        return null;
    }

    public void odeberMereni(enumPozice pozice) throws KolekceException {
        switch (pozice) {
            case PRVNI -> seznam.odeberPrvni();
            case POSLEDNI -> seznam.odeberPosledni();
            case AKTUALNI -> seznam.odeberAktualni();
            case NASLEDNIK -> seznam.odeberNaslednika();
            case PREDCHUDCE -> seznam.odeberPredchudce();
        }
    }

    public void zrus() {
        seznam.zrus();
    }

    public Iterator<Mereni> iterator() {
        return seznam.iterator();
    }

    public AbstrDoubleListInterface<Mereni> MereniDen(int idSenzoru, LocalDate datum) {
        AbstrDoubleListInterface<Mereni> mereniDne = new AbstrDoubleList<>();
        Iterator<Mereni> iterator = seznam.iterator();

        while (iterator.hasNext()) {
            Mereni mereni = iterator.next();
            if (mereni.getIdSenzor() == idSenzoru && mereni.getCasMereni().toLocalDate().equals(datum)) {
                mereniDne.vlozPosledni(mereni);
            }
        }
        return mereniDne;
    }

    public double maxSpotreba(int idSenzoru, LocalDateTime datumOd, LocalDateTime datumDo) {
        double maxSpotreba = 0.0;
        Iterator<Mereni> iterator = seznam.iterator();

        while (iterator.hasNext()) {
            Mereni mereni = iterator.next();
            if (mereni.getIdSenzor() == idSenzoru && mereni.getCasMereni().isAfter(datumOd)
                    && mereni.getCasMereni().isBefore(datumDo)) {
                double spotreba = 0.0;
                if (mereni instanceof MereniElektrika) {
                    MereniElektrika mereniElektrika = (MereniElektrika) mereni;
                    spotreba = mereniElektrika.getSpotrebaVT() + mereniElektrika.getSpotrebaNT();
                } else if (mereni instanceof MereniVoda) {
                    MereniVoda mereniVoda = (MereniVoda) mereni;
                    spotreba = mereniVoda.getSpotrebaM3();
                }

                if (spotreba > maxSpotreba) {
                    maxSpotreba = spotreba;
                }
            }
        }

        return maxSpotreba;
    }

    public double prumerSpotreba(int idSenzoru, LocalDateTime datumOd, LocalDateTime datumDo) {
        Iterator<Mereni> iterator = seznam.iterator();
        double sumaSpotreba = 0.0;
        int pocet = 0;

        while (iterator.hasNext()) {
            Mereni mereni = iterator.next();
            if (mereni.getIdSenzor() == idSenzoru && mereni.getCasMereni().isAfter(datumOd)
                    && mereni.getCasMereni().isBefore(datumDo)) {
                double spotreba = 0.0;
                if (mereni instanceof MereniElektrika) {
                    MereniElektrika mereniElektrika = (MereniElektrika) mereni;
                    spotreba = mereniElektrika.getSpotrebaVT() + mereniElektrika.getSpotrebaNT();
                } else if (mereni instanceof MereniVoda) {
                    MereniVoda mereniVoda = (MereniVoda) mereni;
                    spotreba = mereniVoda.getSpotrebaM3();
                }

                sumaSpotreba += spotreba;
                pocet++;
            }
        }

        if (pocet == 0) {
            return 0.0;
        }

        return sumaSpotreba / pocet;
    }


}
