package upce.obec;

import upce.abstrTree.AbstrTable;
import upce.abstrTreeEnum.eTypProhl;
import upce.ads.KolekceException;
import upce.mereni.Soubor;

import java.io.*;
import java.util.Iterator;
import java.util.Random;
import java.util.Scanner;


public class SpravaObec {

    private AbstrTable<String, Obec> tree;

    public SpravaObec() {
        this.tree = new AbstrTable<>();
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
            tree.vloz(obec, newObec);
        }

        scanner.close();
    }

    public Obec najdi(String obec) {
        return tree.najdi(obec);
    }

    public void vloz(Obec obec) throws KolekceException {
        tree.vloz(obec.getObec(), obec);
    }

    public void odeber(String obec) throws KolekceException {
        tree.odeber(obec);
    }

    public Iterator<Obec> vytvorIterator(eTypProhl typ) {
        return tree.vytvorIterator(typ);
    }

    public Obec generuj() throws KolekceException {
        Random random = new Random();
        int id = random.nextInt(1000);
        int psc = 10000 + random.nextInt(89999);
        String nazevObce = "Obec" + id;
        int pocetMuzu = random.nextInt(5000);
        int pocetZen = random.nextInt(5000);
        Obec temp =  new Obec(id, psc, nazevObce, pocetMuzu, pocetZen, pocetMuzu + pocetZen);
        tree.vloz(temp.getObec(), temp);
        return temp;
    }

    public void zrus() {
        tree.zrus();
    }

    public void ulozData(String filename) throws FileNotFoundException {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filename))) {
            Iterator<Obec> iterator = tree.vytvorIterator(eTypProhl.DO_HLOUBKY); // или другой тип обхода, если нужно
            while (iterator.hasNext()) {
                Obec obec = iterator.next();
                pw.println(obec.getId() + "," + obec.getPsc() + "," + obec.getObec() + "," +
                        obec.getPocetMuzu() + "," + obec.getPocetZen() + "," + obec.getCelkem());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
