package jpa;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class Kategorie implements Serializable {
    @Id
    @Size(min = 2, max = 20, message = "Die Kategorie muss zwischen zwei und 20 Zeichen lang sein.")
    @NotNull(message = "Die Kategorie darf nicht leer sein.")
    private String bezeichnung = "";
    
    //Einnahmen- oder Ausgabenkategorie
    @NotNull(message = "Die Kategorieart darf nicht leer sein")
    private String art = "";
    
    @ManyToOne(targetEntity=Benutzer.class)
    Benutzer benutzer = null;
    
    //muss optional true sein?
    @OneToMany(mappedBy = "kategorie", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    List<Transaktion> transaktionen = new ArrayList<>();
    
    //<editor-fold defaultstate="collapsed" desc="Konstruktoren">
    public Kategorie () {
    }
    
    public Kategorie (String bezeichnung, String art, Benutzer benutzer){
        this.bezeichnung = bezeichnung;
        this.art = art;
        this.benutzer = benutzer;
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Getter und Setter">
    public String getBezeichnung() {
        return bezeichnung;
    }
    
    public void setBezeichnung(String bezeichnung) {
        this.bezeichnung = bezeichnung;
    }
    
    public String getArt() {
        return art;
    }
    
    public void setArt(String art) {
        this.art = art;
    }
    
    /* public Benutzer getBenutzer() {
    return benutzer;
    }
    
    public void setBenutzer(Benutzer benutzer) {
    this.benutzer = benutzer;
    }
    */
    public List<Transaktion> getTransaktionen() {
        return transaktionen;
    }
    
    public void setTransaktionen(List<Transaktion> transaktionen) {
        this.transaktionen = transaktionen;
    }
//</editor-fold> 
}

