package entities;

import java.sql.Date;

public class Udlaan {
    private int bog_id;
    private int laaner_id;
    private Date udlaansdato;

    public Udlaan(int laaner_id, int bog_id) {
        this.laaner_id = laaner_id;
        this.bog_id = bog_id;
    }

    public Udlaan(int bog_id, int laaner_id, Date udlaansdato) {
        this.bog_id = bog_id;
        this.laaner_id = laaner_id;
        this.udlaansdato = udlaansdato;
    }

    public int getBog_id() {
        return bog_id;
    }

    public void setBog_id(int bog_id) {
        this.bog_id = bog_id;
    }

    public int getLaaner_id() {
        return laaner_id;
    }

    public void setLaaner_id(int laaner_id) {
        this.laaner_id = laaner_id;
    }

    public Date getUdlaansdato() {
        return udlaansdato;
    }

    public void setUdlaansdato(Date udlaansdato) {
        this.udlaansdato = udlaansdato;
    }

    @Override
    public String toString() {
        return "Udlaan{" +
                "bog_id=" + bog_id +
                ", laaner_id=" + laaner_id +
                ", udlaansdato=" + udlaansdato +
                '}';
    }
}
