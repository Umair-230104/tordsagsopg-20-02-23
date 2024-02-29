package entities;

public class Forfatter {

private int forfatter_id;
private String navn;

    public Forfatter(int forfatter_id, String navn) {
        this.forfatter_id = forfatter_id;
        this.navn = navn;
    }

    public int getForfatter_id() {
        return forfatter_id;
    }

    public void setForfatter_id(int forfatter_id) {
        this.forfatter_id = forfatter_id;
    }

    public String getNavn() {
        return navn;
    }

    public void setNavn(String navn) {
        this.navn = navn;
    }

    @Override
    public String toString() {
        return "Forfatter{" +
                "forfatter_id=" + forfatter_id +
                ", navn='" + navn + '\'' +
                '}';
    }
}
