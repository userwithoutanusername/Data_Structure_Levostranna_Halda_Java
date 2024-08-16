package upce.mereni;

import upce.ads.AbstrDoubleList;

import java.io.*;

public class Soubor {

    public static AbstrDoubleList nactiData(String jmenoSouboru) throws FileNotFoundException, RuntimeException {
        AbstrDoubleList seznam = new AbstrDoubleList();

        try {
            ObjectInputStream vstupniProud = new ObjectInputStream(new FileInputStream(jmenoSouboru));
            Object nactenyObjekt = vstupniProud.readObject();

            if (nactenyObjekt instanceof AbstrDoubleList) {
                seznam = (AbstrDoubleList) nactenyObjekt;
            } else {
                throw new RuntimeException();
            }

            vstupniProud.close();
        } catch (IOException | ClassNotFoundException vyjimka) {
            throw new RuntimeException(vyjimka);
        }

        return seznam;
    }

    public static void ulozData(String jmenoSouboru, AbstrDoubleList seznam) throws RuntimeException {
        try {
            ObjectOutputStream vystupniProud = new ObjectOutputStream(new FileOutputStream(jmenoSouboru));
            vystupniProud.writeObject(seznam);
            vystupniProud.close();
        } catch (Exception vyjimka) {
            throw new RuntimeException(vyjimka);
        }
    }
}
