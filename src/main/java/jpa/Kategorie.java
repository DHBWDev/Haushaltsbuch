/* 
 * Copyright (C) 2018 Fabio Krämer, Samuel Haag, Sebastian Greulich
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
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
    private TransaktionsArten art;
    
    @ManyToOne(targetEntity=Benutzer.class)
    Benutzer benutzer = null;
    
    //muss optional true sein?
    @OneToMany(mappedBy = "kategorie", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    List<Transaktion> transaktionen = new ArrayList<>();
    
    //<editor-fold defaultstate="collapsed" desc="Konstruktoren">
    public Kategorie () {
    }
    
    public Kategorie (String bezeichnung, TransaktionsArten art, Benutzer benutzer){
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
    
    public TransaktionsArten getArt() {
        return art;
    }
    
    public void setArt(TransaktionsArten art) {
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

