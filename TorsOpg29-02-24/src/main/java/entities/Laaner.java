package entities;

import java.util.ArrayList;
import java.util.List;

public class Laaner {
    private int laaner_id;
    private String navn;
    private String adresse;
    private int postnr;
    private List<Bog> boger; // Liste til at opbevare bøger lånt af låneren

    public Laaner(String navn, String adresse, int postnr) {
        this.navn = navn;
        this.adresse = adresse;
        this.postnr = postnr;
    }

    public Laaner(int laaner_id, String navn, String address, int zip) {
        this.laaner_id = laaner_id;
        this.navn = navn;
        this.adresse = address;
        this.postnr = zip;
        this.boger = new ArrayList<>(); // Initialiser listen af bøger

    }

    public void addBog(Bog bog) {
        this.boger.add(bog);
    }

    public List<Bog> getBoger() {
        return boger;
    }

    public void setBoger(List<Bog> boger) {
        this.boger = boger;
    }

    public int getLaaner_id() {
        return laaner_id;
    }

    public void setLaaner_id(int laaner_id) {
        this.laaner_id = laaner_id;
    }

    public String getNavn() {
        return navn;
    }

    public void setNavn(String name) {
        this.navn = name;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public int getPostnr() {
        return postnr;
    }

    public void setPostnr(int postnr) {
        this.postnr = postnr;
    }

    @Override
    public String toString() {
        return "Laaner{" +
                "laaner_id=" + laaner_id +
                ", navn='" + navn + '\'' +
                ", adresse='" + adresse + '\'' +
                ", postnr=" + postnr +
                '}';
    }

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (!(o instanceof Laaner)) return false;
//        Laaner laaner = (Laaner) o;
//        return getLaaner_id() == laaner.getLaaner_id() && getZip() == laaner.getZip() && getName().equals(laaner.getName()) && getAddress().equals(laaner.getAddress());
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(getLaaner_id(), getName(), getAddress(), getZip());
//    }

}
