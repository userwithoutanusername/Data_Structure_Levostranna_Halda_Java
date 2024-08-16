package upce.obec;

public class Obec implements Comparable<Obec> {

    private int id;
    private int psc;
    private String obec;
    private int pocetMuzu;
    private int pocetZen;
    private int celkem;

    public Obec(int id, int psc, String obec, int pocetMuzu, int pocetZen, int celkem) {
        this.id = id;
        this.psc = psc;
        this.obec = obec;
        this.pocetMuzu = pocetMuzu;
        this.pocetZen = pocetZen;
        this.celkem = celkem;
    }

    public int getCelkem(){
        return pocetMuzu+pocetZen;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPsc() {
        return psc;
    }

    public void setPsc(int psc) {
        this.psc = psc;
    }

    public String getObec() {
        return obec;
    }

    public void setObec(String obec) {
        this.obec = obec;
    }

    public int getPocetMuzu() {
        return pocetMuzu;
    }

    public void setPocetMuzu(int pocetMuzu) {
        this.pocetMuzu = pocetMuzu;
    }

    public int getPocetZen() {
        return pocetZen;
    }

    public void setPocetZen(int pocetZen) {
        this.pocetZen = pocetZen;
    }



    @Override
    public String toString() {
        return "Kraj{" +
                "id=" + id +
                ", psc=" + psc +
                ", obec='" + obec + '\'' +
                ", pocetMuzu=" + pocetMuzu +
                ", pocetZen=" + pocetZen +
                ", celkem" + celkem +
                '}';
    }

    @Override
    public int compareTo(Obec o) {
        return 0;
    }
}
