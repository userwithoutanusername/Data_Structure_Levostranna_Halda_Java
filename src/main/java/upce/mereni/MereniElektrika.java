package upce.mereni;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MereniElektrika extends Mereni {

    private double spotrebaVT;
    private double spotrebaNT;

    public MereniElektrika(int idSenzor, LocalDateTime casMereni, double spotrebaVT, double spotrebaNT) {
        super(idSenzor, enumTypSenzoru.ELEKTRIKA, casMereni);
        this.spotrebaVT = spotrebaVT;
        this.spotrebaNT = spotrebaNT;
    }

    public double getSpotrebaVT() {
        return spotrebaVT;
    }

    public double getSpotrebaNT() {
        return spotrebaNT;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH");
        return String.format("Typ Senzoru: %s, Id: %d, Cas: %s, Spotreba VT: %.2f, Spotreba NT: %.2f",
                getTypSenzoru(),
                getIdSenzor(),
                getCasMereni().format(formatter),
                getSpotrebaVT(),
                getSpotrebaNT());
    }
}
