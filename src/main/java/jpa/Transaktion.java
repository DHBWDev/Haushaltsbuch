package jpa;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;


@Entity
public class Transaktion implements Serializable {

    @Id
    @GeneratedValue(generator = "transaktion_ids")
    @TableGenerator(name = "transaktion_ids", initialValue = 0, allocationSize = 1)
    private long id;
   
    //@NotNull(message = "Die Bezeichnung darf nicht leer sein.")
    private String bezeichnung;
    
    private String beschreibung;
    
    @NotNull(message= "Bitte geben Sie einen Betrag an.")
    private Double betrag = 0.0;
    
    @NotNull(message = "Das Datum darf nicht leer sein.") 
    @Temporal(TemporalType.TIMESTAMP)
    private Date erstellungsDatum = new Date();
    
    //@NotNull(message = "Die Art darf nicht leer sein")  
    private String art;
    
    @ManyToOne
    private Benutzer benutzer = null;
    
    @ManyToOne
    private Kategorie kategorie = null;
    
    //<editor-fold defaultstate="collapsed" desc="Konstruktoren">
    public Transaktion(){
    }
    
    public Transaktion(String bezeichnung, String beschreibung, Double betrag, Date erstellungsDatum, String art){
        this.bezeichnung = bezeichnung;
        this.beschreibung = beschreibung;
        this.betrag = betrag;
        this.erstellungsDatum = erstellungsDatum;
        this.art = art;
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Getter und Setter">
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
    
    public Double getBetrag() {
        return betrag;
    }
    
    public void setBetrag(Double betrag) {
        this.betrag = betrag;
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
