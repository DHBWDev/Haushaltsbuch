package ejb;

import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.ejb.Stateless;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

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

@Stateless
public class TransaktionBean extends EntityBean<Transaktion, Long> {

    @EJB
    KategorieBean kategorieBean;
    
    @EJB
    BenutzerBean benutzerBean;

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
    
    public List<Transaktion> findeAlle(Date vonDatum, Date bisDatum, TransaktionsArten art){
        return em.createQuery("SELECT t FROM Transaktion t"
        + " WHERE (t.benutzer = :benutzer)"
        + " AND (t.art = :art)"
        + " AND (t.erstellungsDatum >= :vonDatum) AND (t.erstellungsDatum <= :bisDatum)")
        .setParameter("benutzer", benutzerBean.gibAktuellenBenutzer())
        .setParameter("art", art.toString())
        .setParameter("vonDatum", vonDatum)
        .setParameter("bisDatum", bisDatum)
        .getResultList();
    }
    
    public List<Transaktion> findeAlle(Date vonDatum, Date bisDatum, TransaktionsArten art, Kategorie kategorie){
        return em.createQuery("SELECT t FROM Transaktion t"
            + " WHERE (t.benutzer = :benutzer)"
            + " AND (t.art = :art)"
            + " AND (t.kategorie = :kategorie)"
            + " AND (t.erstellungsDatum >= :vonDatum) AND (t.erstellungsDatum <= :bisDatum)")
            .setParameter("benutzer", benutzerBean.gibAktuellenBenutzer())
            .setParameter("kategorie", kategorie)
            .setParameter("vonDatum", vonDatum)
            .setParameter("bisDatum", bisDatum)
            .setParameter("art", art.getLabel())
            .getResultList();
    }
    
    //Gibt ein StatistikDaten Objekt mit Name der Kategorie und die Summe der Transaktionen zur Kategorie zurück
    public StatistikDaten getStatistikLastYearPerCategory(TransaktionsArten art){    
        List<Kategorie> kategorien = kategorieBean.findeAlle(art);
        System.out.println("Anzahl Kategorien " + Integer.toString(kategorien.size()));
        
        StatistikDaten daten = new StatistikDaten();
        
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(new Date());
        //heutiger Monat
        int currentMonth = cal.get(Calendar.MONTH);
        //heutiges Jahr
        int currentYear = cal.get(Calendar.YEAR);
        
        System.out.println("Monat " + Integer.toString(currentMonth) + "Jahr " + Integer.toString(currentYear));
        
        cal.set(currentYear -1 ,currentMonth +1 , 1);
        Date fromDate = cal.getTime();
        
        System.out.println("fromDate " + fromDate);
        
        cal.set(currentYear,currentMonth, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        Date toDate = cal.getTime();
        
        System.out.println("toDate " + toDate);
       
        for (Kategorie kategorie : kategorien) {
            System.out.println("Bezeichnung " + kategorie.getBezeichnung());
            daten.setWert(summiereTransaktionen(findeAlle(fromDate, toDate, art, kategorie)), kategorie.getBezeichnung());
        }
        
        return daten;
    }
    
    //Gibt ein StatistikDaten Objekt mit Name des Monats und die Summe der Transaktionen zum Monat zurück
    public StatistikDaten getStatistikLastYearPerMonth(TransaktionsArten art){  
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(new Date());
        //heutiger Monat
        int currentMonth = cal.get(Calendar.MONTH) +1 ;
        //heutiges Jahr
        int currentYear = cal.get(Calendar.YEAR);
        
        StatistikDaten daten = new StatistikDaten();
   
        int fromMonth, toMonth;
        Date dateFrom, dateTo;
        
        //Schleife über die Jahre
        for (int year = currentYear-1; year <= currentYear; year ++){
            
            //im ersten Jahr: aktueller Monat +1 bis Jahresende
            fromMonth = currentMonth+1;
            toMonth = 12;
           
            //im zweiten Jahr: Jahresanfang bis aktueller Monat
            if (currentYear == year){
                fromMonth = 1;
                toMonth = currentMonth;
            }
            System.out.print("currentYear " + Integer.toString(currentYear));
            System.out.print("Year " + Integer.toString(year));
            System.out.print("fromMonth " + Integer.toString(fromMonth));
            System.out.print("toMonth " + Integer.toString(toMonth));
            
            //Schleife über die Monate
            for (int month = fromMonth; month <= toMonth; month++){
                
                //Setzen des Monates und des Jahres für den ersten des Monates
                cal.set(year, month-1, 1);
                dateFrom = cal.getTime();
                
                //Setzen des Monates und des Jahres für den letzten des Monates
                cal.set(year, month-1, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
                dateTo = cal.getTime();

                System.out.print("month " + Integer.toString(month));
                System.out.print("dateFrom " + dateFrom.toString());
                System.out.print("dateTo " + dateTo.toString());
                
                daten.setWert(summiereTransaktionen(findeAlle(dateFrom, dateTo, art)), new SimpleDateFormat("MMMM", Locale.GERMAN).format(cal.getTime()));
            }
        }
        return daten;
    }
    
    public double summiereTransaktionen(List<Transaktion> transaktionen){
        Double summe = 0.0;
        for (Transaktion t:  transaktionen){
            summe = summe + t.getBetrag();
            System.out.print("Summe " + summe);
        }
        
        return summe;
    }

    //Methode zum Import von Transaktionen aus einer XML-File
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

                Element erstellungsDatum = children.get(i).getChild("ErstellungsDatum");

                Element art = children.get(i).getChild("Art");

                //wird nicht benötigt
                //Element benutzer = children.get(i).getChild("Benutzer");
                //Wenn Kategorie noch nicht vorhanden, automatisch anlegen
                //Kategorie mitgeben welche Art
                Element kategorie = children.get(i).getChild("Kategorie");

                //XML-Werte dem Objekte übergeben
                t.setArt(art.getValue());
                t.setBenutzer(aktuellerBenutzer);
                t.setBetrag(Double.valueOf(betrag.getValue()));
                t.setBeschreibung(beschreibung.getValue());
                t.setBezeichnung(bezeichnung.getValue());
               
    
                 //Datum anlegen
                DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
                Date startDate = new Date();
                try {
                    startDate = df.parse(erstellungsDatum.getValue());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                t.setErstellungsDatum(startDate);

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
