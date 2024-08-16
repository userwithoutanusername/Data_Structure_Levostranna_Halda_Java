package upce.obec;

import upce.abstrHeap.AbstrHeap;
import upce.abstrHeap.eTypComp;
import upce.abstrTreeEnum.eTypProhl;
import upce.ads.KolekceException;

import java.io.*;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Random;
import java.util.Scanner;

import static upce.abstrHeap.eTypComp.BY_NAME;
import static upce.abstrHeap.eTypComp.BY_POPULATION;

public class FrontaObec {

    private AbstrHeap<Obec> heap;

    public FrontaObec() {
        this.heap =  new AbstrHeap<Obec>(Comparator.naturalOrder());
    }

    public void setComparator(eTypComp comparatorType) {
        Comparator<Obec> comparator = null;
        switch (comparatorType) {
            case BY_POPULATION:
                comparator = Comparator.comparingInt(Obec::getCelkem);
                break;
            case BY_NAME:
                comparator = Comparator.comparing(Obec::getObec);
                break;
        }
        this.heap.setComparator(comparator);
    }

    public void vybuduj(Obec[] obce) {
        heap.vybuduj(obce);
    }

    public void reorganizace() {
        heap.prebuduj();
    }

    public void zrus() {
        heap.zrus();
    }

    public boolean jePrazdny() {
        return heap.jePrazdny();
    }

    public void vloz(Obec obec) {
        heap.vloz(obec);
    }

    public Obec odeberMax() {
        return heap.odeberMax();
    }

    public Obec zpristupniMax() {
        return heap.zpristupniMax();
    }

    public void vypis() {
        Iterator<Obec> iterator = heap.iterator();
        while(iterator.hasNext()) {
            System.out.println(iterator.next());
        }
    }

    public Obec generuj() throws KolekceException {
        Random random = new Random();
        int id = random.nextInt(1000);
        int psc = 10000 + random.nextInt(89999);
        String nazevObce = "Obec" + id;
        int pocetMuzu = random.nextInt(5000);
        int pocetZen = random.nextInt(5000);
        Obec temp =  new Obec(id, psc, nazevObce, pocetMuzu, pocetZen, pocetMuzu + pocetZen);
        heap.vloz(temp);
        return temp;
    }


    public Iterator<Obec> vytvorIterator(eTypProhl typProhl) {
        return heap.iterator(typProhl);
    }

    public void ulozData(String filename) throws FileNotFoundException {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filename))) {
            Iterator<Obec> iterator = heap.iterator(eTypProhl.DO_HLOUBKY); // или другой тип обхода, если нужно
            while (iterator.hasNext()) {
                Obec obec = iterator.next();
                pw.println(obec.getId() + "," + obec.getPsc() + "," + obec.getObec() + "," +
                        obec.getPocetMuzu() + "," + obec.getPocetZen() + "," + obec.getCelkem());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void importDat(String filePath) throws FileNotFoundException, KolekceException {
        File file = new File(filePath);
        Scanner scanner = new Scanner(file);

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] parts = line.split(",");
            int id = Integer.parseInt(parts[0]);
            int psc = Integer.parseInt(parts[1]);
            String obec = parts[2];
            int pocetMuzu = Integer.parseInt(parts[3]);
            int pocetZen = Integer.parseInt(parts[4]);
            int celkem = Integer.parseInt(parts[5]);

            Obec newObec = new Obec(id, psc, obec, pocetMuzu, pocetZen, celkem);
            heap.vloz(newObec);
        }

        scanner.close();
    }

}

