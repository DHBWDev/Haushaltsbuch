package ejb;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.ejb.Stateless;
import jpa.Benutzer;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import jpa.Kategorie;
import jpa.Transaktion;
import jpa.TransaktionsArten;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.XMLOutputter;
import web.WebUtils;

@Stateless
public class TransaktionBean extends EntityBean<Transaktion, Long> {

    public TransaktionBean() {
        super(Transaktion.class);
    }
    
    public List<Transaktion> suche(String suchtext, Kategorie kategorie){
        // Hilfsobjekt zum Bauen des Query
        CriteriaBuilder cb = this.em.getCriteriaBuilder();
        
        // SELECT t FROM Angebot t
        CriteriaQuery<Transaktion> query = cb.createQuery(Transaktion.class);
        Root<Transaktion> from = query.from(Transaktion.class);
        query.select(from);

        // ORDER BY erstellungsDatum, erstellungsDatum
        query.orderBy(cb.asc(from.get("erstellungsDatum")), cb.asc(from.get("erstellungsDatum")));
        
        // WHERE t.bezeichnung LIKE :search
        if (suchtext != null && !suchtext.trim().isEmpty()) {
            query.where(cb.like(from.get("bezeichnung"), "%" + suchtext + "%"));
        }
        
        // WHERE t.category = :category
        if (kategorie != null) {
            query.where(cb.equal(from.get("kategorie"), kategorie));
        }
        
        return em.createQuery(query).getResultList();
    }
    
    public List<Transaktion> getTransaktionenVonDatumBisDatum(Date vonDatum, Date bisDatum, TransaktionsArten art){
                // Hilfsobjekt zum Bauen des Query
        CriteriaBuilder cb = this.em.getCriteriaBuilder();
        
        // SELECT t FROM Angebot t
        CriteriaQuery<Transaktion> query = cb.createQuery(Transaktion.class);
        Root<Transaktion> from = query.from(Transaktion.class);
        query.select(from);

        // ORDER BY erstellungsDatum, erstellungsDatum
        query.orderBy(cb.asc(from.get("erstellungsDatum")), cb.asc(from.get("erstellungsDatum")));
        
        query.where(cb.greaterThanOrEqualTo(from.<Date>get("erstellungsDatum"), vonDatum));
        query.where(cb.greaterThanOrEqualTo(from.<Date>get("erstellungsDatum"), bisDatum));
        query.where(cb.equal(from.get("art"), art));
        
        return em.createQuery(query).getResultList();
    }
    
    public Double[] getSummeVonMonatBisMonat(Date vonDatum, Date bisDatum,TransaktionsArten art ){
        
        Calendar calendar =Calendar.getInstance();
        
        calendar.setTime(vonDatum);
        int fromMonth = calendar.get(Calendar.MONTH) +1 ;
        
        calendar.setTime(bisDatum);
        int toMonth = calendar.get(Calendar.MONTH) +1;
        
        Double [] werte = new Double[toMonth-fromMonth +1];
        
        int i = 0;
        for (int m = fromMonth; m <= toMonth;m++){
            
            List <Transaktion> al = getTransaktionenVonDatumBisDatum(new Date(), WebUtils.parseDate("12.07.2019"), art);
            
            Double summe = 0.0;
            for (Transaktion t:  al){
                summe = summe + t.getBetrag();
            }
            
            werte [i] = summe;
            i ++;
        } 
        return werte;
    }
    
    //importiert Transaktionen aus einer XML-File
    public void importiereXML() {
        Document doc = null;
        File f = new File("Transaktionen.xml");

        try {
            // Das Dokument erstellen
            SAXBuilder builder = new SAXBuilder();
            doc = builder.build(f);
            XMLOutputter fmt = new XMLOutputter();

            // Wurzelelement ausgeben
            Element root = doc.getRootElement();

            List<Element> children = root.getChildren();
    
            int i = 0;
            while (i < children.size()) {
                Transaktion t = new Transaktion();

                Element bezeichnung = children.get(i).getChild("Bezeichnung");

                Element beschreibung = children.get(i).getChild("Beschreibung");

                Element betrag = children.get(i).getChild("Betrag");

                Element erstellungsDatum = children.get(i).getChild("ErstellungsDatum");

                Element art = children.get(i).getChild("Art");

                Element benutzer = children.get(i).getChild("Benutzer");

                //Wenn Kategorie noch nicht vorhanden, automatisch anlegen
                //Kategorie mitgeben welche Art
                Element kategorie = children.get(i).getChild("Kategorie");

                //XML-Werte dem Objekte übergeben
                t.setArt(art.getValue());
                t.setBenutzer(null);
                t.setBeschreibung(beschreibung.getValue());
                t.setBezeichnung(bezeichnung.getValue());
                t.setErstellungsDatum(null);
                t.setKategorie(null);

                this.speichernNeu(t);

                i++;

            }

        } catch (JDOMException | IOException e) {
            e.printStackTrace();
        }

    }

}
