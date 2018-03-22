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
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import jpa.Benutzer;
import jpa.Kategorie;
import jpa.Transaktion;
import jpa.TransaktionsArten;
import javax.ejb.EJB;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.XMLOutputter;
import web.WebUtils;

@Stateless
public class TransaktionBean extends EntityBean<Transaktion, Long> {

    @EJB
    KategorieBean kategorieBean;

    public TransaktionBean() {
        super(Transaktion.class);
    }

    public List<Transaktion> suche(String suchtext, Kategorie kategorie) {
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
                /*   CriteriaBuilder cb = this.em.getCriteriaBuilder();
                
                // SELECT t FROM Angebot t
                CriteriaQuery<Transaktion> query = cb.createQuery(Transaktion.class);
                Root<Transaktion> from = query.from(Transaktion.class);
                query.select(from);
                
                // ORDER BY erstellungsDatum, erstellungsDatum
                query.orderBy(cb.asc(from.get("erstellungsDatum")), cb.asc(from.get("erstellungsDatum")));
                
                query.where(cb.lessThanOrEqualTo(from.<Date>get("erstellungsDatum"), bisDatum));
                query.where(cb.greaterThanOrEqualTo(from.<Date>get("erstellungsDatum"), vonDatum));
                
                
                query.where(cb.equal(from.get("art"), art));*/
                
        return em.createQuery("SELECT t FROM Transaktion t"
        + " WHERE t.art = :art"
        + " AND (t.erstellungsDatum >= :vonDatum) AND (t.erstellungsDatum <= :bisDatum)")
        .setParameter("art", art.toString())
        .setParameter("vonDatum", vonDatum)
        .setParameter("bisDatum", bisDatum)
        .getResultList();
        
        //return em.createQuery(query).getResultList();
    }
    
    public Double[] getSummeLastYear(TransaktionsArten art ){
        
        Calendar calendar =Calendar.getInstance();
        
        calendar.setTime(new Date());
        //heutiger Monat
        int currentMonth = calendar.get(Calendar.MONTH) +1 ;
        //heutiges Jahr
        int currentYear = calendar.get(Calendar.YEAR);
        
        Double [] werte = new Double[12];
        
        int fromMonth, toMonth;
        List <Transaktion> al;
        GregorianCalendar cal = new GregorianCalendar();
        int i = 0;
        Date dateFrom, dateTo;
        
        for (int year = currentYear-1; year <= currentYear; year ++){
            
            fromMonth = currentMonth+1;
            toMonth = 12;
                
            
            
            if (currentYear == year){
                fromMonth = 1;
                toMonth = currentMonth; 
            }
            System.out.print("currentYear " + Integer.toString(currentYear));
            System.out.print("Year " + Integer.toString(year));
            System.out.print("fromMonth " + Integer.toString(fromMonth));
            System.out.print("toMonth " + Integer.toString(toMonth));
           
            
            
            for (int month = fromMonth; month <= toMonth; month++){
                
                cal.set(year, month-1, 1);
                dateFrom = cal.getTime();
                
                cal.set(year, month-1, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
                dateTo = cal.getTime();
                
                System.out.print("month " + Integer.toString(month));
                System.out.print("dateFrom " + dateFrom.toString());
                System.out.print("dateTo " + dateTo.toString());
                
                al = getTransaktionenVonDatumBisDatum(dateFrom, dateTo, art);
            
                Double summe = 0.0;
                for (Transaktion t:  al){
                    summe = summe + t.getBetrag();
                    System.out.print("Summe " + summe);
                }
                werte[i] = summe;
                System.out.print("wert " + Double.toString(werte[i]));
                i = i +1;
                
                
            }
        }
        return werte;
    }

    //importiert Transaktionen aus einer XML-File
    public void importiereXML(File f, Benutzer aktuellerBenutzer) {
        Document doc = null;

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

                //Element erstellungsDatum = children.get(i).getChild("ErstellungsDatum");
                Element art = children.get(i).getChild("Art");

                Element benutzer = children.get(i).getChild("Benutzer");

                //Wenn Kategorie noch nicht vorhanden, automatisch anlegen
                //Kategorie mitgeben welche Art
                Element kategorie = children.get(i).getChild("Kategorie");

                //XML-Werte dem Objekte übergeben
                t.setArt(art.getValue());
                t.setBenutzer(aktuellerBenutzer);
                t.setBetrag(Double.valueOf(betrag.getValue()));
                t.setBeschreibung(beschreibung.getValue());
                t.setBezeichnung(bezeichnung.getValue());
                t.setErstellungsDatum(new Date(System.currentTimeMillis()));

                //Kategorieprüfung - Ist Kategorie schon vorhanden?
                //Suche übergebene Kategorie
                Kategorie aktuelleKategorie = this.kategorieBean.findeMitId(kategorie.getValue());
                //Wenn ungleich null -> Kategorie vorhanden
                if (aktuelleKategorie != null) {
                    t.setKategorie(aktuelleKategorie);
                } else {
                    //Wenn null -> neue Kategorie anlegen
                    //String bezeichnung, String art, Benutzer benutzer
                    aktuelleKategorie = new Kategorie(kategorie.getValue(), art.getValue(), aktuellerBenutzer);
                    this.kategorieBean.speichernNeu(aktuelleKategorie);
                    t.setKategorie(aktuelleKategorie);
                }

                this.speichernNeu(t);

                i++;

            }

        } catch (JDOMException | IOException e) {
            e.printStackTrace();
        }

    }

}
