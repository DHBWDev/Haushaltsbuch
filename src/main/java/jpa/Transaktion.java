package jpa;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.TableGenerator;
import jpa.Kategorie;

@Entity
public class Transaktion implements Serializable {

    @Id
    @GeneratedValue(generator = "transaktion_ids")
    @TableGenerator(name = "transaktion_ids", initialValue = 0, allocationSize = 1)
    private long id;

    //@NotNull(message = "Die Bezeichnung darf nicht leer sein.")
    String bezeichnung = "";

    String beschreibung = "";

    //@NotNull(message = "Das Datum darf nicht leer sein.")  
    Date erstellungsDatum = new Date(System.currentTimeMillis());

    //@NotNull(message = "Die Art darf nicht leer sein")  
    String art = "";

    @ManyToOne
    Benutzer benutzer = null;

    @ManyToOne(optional = true)
    Kategorie kategorie = null;

    //<editor-fold defaultstate="collapsed" desc="Konstruktoren">
    public Transaktion(String bezeichnung, String beschreibung, Date erstellungsDatum, String art, Benutzer benutzer, Kategorie kategorie) {
        this.bezeichnung = bezeichnung;
        this.beschreibung = beschreibung;
        this.erstellungsDatum = erstellungsDatum;
        this.art = art;
        this.benutzer = benutzer;
        this.kategorie = kategorie;
    }

    public Transaktion() {

    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Setter und Getter">
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getBezeichnung() {
        return bezeichnung;
    }

    public void setBezeichnung(String bezeichnung) {
        this.bezeichnung = bezeichnung;
    }

    public String getBeschreibung() {
        return beschreibung;
    }

    public void setBeschreibung(String beschreibung) {
        this.beschreibung = beschreibung;
    }

    public Date getErstellungsDatum() {
        return erstellungsDatum;
    }

    public void setErstellungsDatum(Date erstellungsDatum) {
        this.erstellungsDatum = erstellungsDatum;
    }

    public String getArt() {
        return art;
    }

    public void setArt(String art) {
        this.art = art;
    }

    public Benutzer getBenutzer() {
        return benutzer;
    }

    public void setBenutzer(Benutzer benutzer) {
        this.benutzer = benutzer;
    }

    public Kategorie getKategorie() {
        return kategorie;
    }

    public void setKategorie(Kategorie kategorie) {
        this.kategorie = kategorie;
    }
    //</editor-fold>

}
