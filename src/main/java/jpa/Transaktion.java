package jpa;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import jpa.Benutzer;
import jpa.Kategorie;

@Entity
public class Transaktion implements Serializable{
    @Id
    @GeneratedValue
    private long id;
    
    
    @NotNull(message = "Die Bezeichnung darf nicht leer sein.")
    String bezeichnung;
    
    String beschreibung;
    
    @NotNull(message = "Das Datum darf nicht leer sein.")  
    Date erstellungsDatum;
    
    @NotNull(message = "Die Art darf nicht leer sein")  
    String art;
    
    @ManyToOne
    Benutzer benutzer = null;
    
    @ManyToOne(optional = true)
    Kategorie kategorie = null;
}
