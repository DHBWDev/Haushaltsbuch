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
    private String Art = "";
    
    @ManyToOne
    Benutzer benutzer = null;
    
    //muss optional true sein?
    @OneToMany(mappedBy = "kategorie", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    List<Transaktion> transaktionen = new ArrayList<>();
}
