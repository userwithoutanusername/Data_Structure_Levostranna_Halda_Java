
package upce.mereni;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MereniVoda extends Mereni {

    private double spotrebaM3;

    public MereniVoda(int idSenzor, LocalDateTime casMereni, double spotrebaM3) {
        super(idSenzor, enumTypSenzoru.VODA, casMereni);
        this.spotrebaM3 = spotrebaM3;
    }

    public double getSpotrebaM3() {
        return spotrebaM3;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH");
        return String.format("Typ Senzoru: %s, Id: %d, Cas: %s, Spotreba M3: %.2f",
                getTypSenzoru(),
                getIdSenzor(),
                getCasMereni().format(formatter),
                getSpotrebaM3());
    }
}
