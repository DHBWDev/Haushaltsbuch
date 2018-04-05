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
    
    /**
     * Methode für die systematische Suche von Transaktionen.
     * @param suchtext 
     * @param kategorie
     * @return Liste mit Transaktionen
     */
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
    
    /**
     * Ermittelt alle Transaktionen für einen gewissen Zeitraum und einen bestimmten Benutzer.
     * @param vonDatum  Anfangsdatum
     * @param bisDatum  Enddatum
     * @param art       Art der Transaktion
     * @return  Liste mit Transaktionen
     */
    public List<Transaktion> findeAlle(Date vonDatum, Date bisDatum, TransaktionsArten art) {
        return em.createQuery("SELECT t FROM Transaktion t"
                + " WHERE (t.benutzer = :benutzer)"
                + " AND (t.art = :art)"
                + " AND (t.erstellungsDatum >= :vonDatum) AND (t.erstellungsDatum <= :bisDatum)")
                .setParameter("benutzer", benutzerBean.gibAktuellenBenutzer())
                .setParameter("art", art)
                .setParameter("vonDatum", vonDatum)
                .setParameter("bisDatum", bisDatum)
                .getResultList();
    }
    
    /**
     * Ermittelt alle Transaktionen für einen gewissen Zeitraum und einen bestimmten Benutzer.
     * @param vonDatum  Anfangsdatum
     * @param bisDatum  Enddatum
     * @param art       Art der Transaktion
     * @param kategorie Kategorie
     * @return  Liste mit Transaktionen
     */
    public List<Transaktion> findeAlle(Date vonDatum, Date bisDatum, TransaktionsArten art, Kategorie kategorie) {
        return em.createQuery("SELECT t FROM Transaktion t"
                + " WHERE (t.benutzer = :benutzer)"
                + " AND (t.art = :art)"
                + " AND (t.kategorie = :kategorie)"
                + " AND (t.erstellungsDatum >= :vonDatum) AND (t.erstellungsDatum <= :bisDatum)")
                .setParameter("benutzer", benutzerBean.gibAktuellenBenutzer())
                .setParameter("kategorie", kategorie)
                .setParameter("vonDatum", vonDatum)
                .setParameter("bisDatum", bisDatum)
                .setParameter("art", art)
                .getResultList();
    }
    /**
     * Hier werden alle Transaktionen zu einer Kategorie aus dem letzten Jahr summiert.
     * @param art Art der Transaktion (Ausgabe/Einnahme)
     * @return StatistikDaten Objekt mit Name der Kategorie und der Summe der Transaktionen zur Kategorie
     */
    public StatistikDaten getStatistikLastYearPerCategory(TransaktionsArten art) {
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

        cal.set(currentYear - 1, currentMonth + 1, 1);
        Date fromDate = cal.getTime();

        System.out.println("fromDate " + fromDate);

        cal.set(currentYear, currentMonth, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        Date toDate = cal.getTime();

        System.out.println("toDate " + toDate);

        for (Kategorie kategorie : kategorien) {
            System.out.println("Bezeichnung " + kategorie.getBezeichnung());
            daten.setWert(summiereTransaktionen(findeAlle(fromDate, toDate, art, kategorie)), kategorie.getBezeichnung());
        }

        return daten;
    }
    
    /**
    *Liefert die Transaktionen aus dem gesamten vorherigen Jahr. In dieser Methode
    *wurde explizit mit der Methode createNativeQuery gearbeitet.
    * 
     * @param art Art der Transaktion (Ausgabe oder Einnahme)
     * @param date Dieses Datum markiert den letzten Wert in der Zeitreihe. 
     * @return StatistikDaten Objekt mit Bezeichnung der Monate und dem zugehörigen Werten
    **/
    public StatistikDaten getStatistikLastYearPerMonth(TransaktionsArten art, Date date) {
        StatistikDaten sD = new StatistikDaten();

        GregorianCalendar cal = new GregorianCalendar();

        //Ermitteln des aktuellen Monates und Jahres
        cal.setTime(date);
        //Monat ist 0 basiert. Bsp. Januar = 0
        int currentMonth = cal.get(Calendar.MONTH);
        int currentYear = cal.get(Calendar.YEAR);
        
        Benutzer user = this.benutzerBean.gibAktuellenBenutzer();

        System.out.println("Aktueller Benutzer: " + user.getUsername());
        
        System.out.println("aktueller Monat " + Integer.toString(currentMonth));
        System.out.println("aktuelles Jahr " + Integer.toString(currentYear));

        //Es sollen die Werte des letzten Jahres ermittelt werden
        //Setzen des Monates +1 und Jahres -1 als Anfangsdatum
        int startMonth = currentMonth+1;
        int startYear = currentYear - 1;

        //Jahreswechsel
        if (startMonth >= 12) {
            startMonth = 0;
            startYear = startYear + 1;
        }
        cal.set(startYear, startMonth, 1);
        Date fromDate = cal.getTime();

        //Enddatum = heutiges Datum
        Date toDate = date;

        System.out.println("von Datum " + fromDate);
        System.out.println("bis Datum " + toDate);
        System.out.println("SQL Abfrage");

        //Hier wird eine NativeQuery verwendet, weil SQL Befehle verwendet werden,
        //die so in JPA nicht vorgesehen sind.
        List<Object[]> results = em.createNativeQuery("select SUM(t.BETRAG) as SUMME, MONTH(t.ERSTELLUNGSDATUM) as MONAT \n"
                + "from (select * from HAUSHALTSBUCH.TRANSAKTION t  \n"
                + "where t.ERSTELLUNGSDATUM >= ?1 AND t.ERSTELLUNGSDATUM <= ?2 AND t.BENUTZER_USERNAME = ?3 AND t.ART = ?4) t group by MONTH(t.ERSTELLUNGSDATUM)\n",
                "MonthTransaktionMapping")
                .setParameter(1, fromDate)
                .setParameter(2, toDate)
                .setParameter(3, user.getUsername())
                .setParameter(4, art.ordinal())
                .getResultList();

        //Erzeugen eines Double Arrays für die Beträge. Es kann sein, dass in der SQl Abfrage nicht alle Monate repräsentiert werden.
        //Zum Beispiel wenn es aus einem Monat keine Transaktionen gibt.
        Double[] d = new Double[12];
        System.out.println("Zeilen " + results.size());
        
        //Die sogenannten Lampda Expressions aus JAVA 8 machen bei der Erzeugung eines Embedded Container
        //Ärger --> IndexOutOfBoundException 

        /*
        results.stream().forEach((record) ->{
        System.out.println("Spalten " + record.length);
        
        //Der Wert wird genau auf die passende Position im Array gesetzt. - 0 = Januar
        d[(Integer)record[1]-1] = (Double)record[0];
        
        System.out.println("Betrag " + record[0]);
        System.out.println("Monat " + record[1]);
        });*/
        
        for (Iterator i = results.iterator(); i.hasNext();) {
            Object[] record = (Object[]) i.next();
            System.out.println("Spalten " + record.length);

            //Der Wert wird genau auf die passende Position im Array gesetzt. - 0 = Januar
            d[(Integer) record[1] - 1] = (Double) record[0];

            System.out.println("Betrag " + record[0]);
            System.out.println("Monat " + record[1]);
        }
       
        //Setzen der Statistik Werte. Es soll nicht fix mit dem Januar begonnen werden, 
        //sondern mit dem aktuellen Monat +1 und aktuelles Jahr -1
        int month = startMonth;
        Double wert;
        System.out.println("Setzen der Statistikwerte");
        for (int i = 1; i <= 12; i++) {
            wert = 0.0;

            if (d[month] != null) {
                wert = d[month];
            }

            //Setzen des Statistikwertes
            cal.set(2000, month, 1);
            //Speichern der Daten in ein Statistik Objekt
            sD.setWert(wert, new SimpleDateFormat("MMMM", Locale.GERMAN).format(cal.getTime()));
            
            month = month + 1;

            //Jahreswechsel
            if (month >= 12) {
                month = 0;
            }
        }
        System.out.println("Statistik Daten (Json): " + sD.erzeugeJson());
        return sD;
    }

    //<editor-fold defaultstate="collapsed" desc="Alte Methode getStatistikLastYearPerMonth">
    /**
     *Liefert die Transaktionen aus dem gesamten vorherigen Jahr. In dieser Methode
     *werden die Transaktionen Monat für Monat mit einer Datenbankabfrage
     * ermittelt und aufsummiert. Gerade die Ermittlung des Anfang und Enddatums 
     * eines Monats und der kompletten Zeitspanne macht diese Lösung sehr umständlich.
     * 
     * @param art Art der Transaktion (Ausgabe oder Einnahme)
     * @return 
     */
    
    //Gibt ein StatistikDaten Objekt mit Name des Monats und die Summe der Transaktionen zum Monat zurück
    public StatistikDaten getStatistikLastYearPerMonthOld(TransaktionsArten art) {

        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(new Date());
        //heutiger Monat
        int currentMonth = cal.get(Calendar.MONTH) + 1;
        //heutiges Jahr
        int currentYear = cal.get(Calendar.YEAR);

        StatistikDaten daten = new StatistikDaten();

        int fromMonth, toMonth;
        Date dateFrom, dateTo;

        //Schleife über die Jahre
        for (int year = currentYear - 1; year <= currentYear; year++) {

            //im ersten Jahr: aktueller Monat +1 bis Jahresende
            fromMonth = currentMonth + 1;
            toMonth = 12;

            //im zweiten Jahr: Jahresanfang bis aktueller Monat
            if (currentYear == year) {
                fromMonth = 1;
                toMonth = currentMonth;
            }
            System.out.print("currentYear " + Integer.toString(currentYear));
            System.out.print("Year " + Integer.toString(year));
            System.out.print("fromMonth " + Integer.toString(fromMonth));
            System.out.print("toMonth " + Integer.toString(toMonth));

            //Schleife über die Monate
            for (int month = fromMonth; month <= toMonth; month++) {

                //Setzen des Monates und des Jahres für den ersten des Monates
                cal.set(year, month - 1, 1);
                dateFrom = cal.getTime();

                //Setzen des Monates und des Jahres für den letzten des Monates
                cal.set(year, month - 1, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
                dateTo = cal.getTime();

                System.out.print("month " + Integer.toString(month));
                System.out.print("dateFrom " + dateFrom.toString());
                System.out.print("dateTo " + dateTo.toString());

                daten.setWert(summiereTransaktionen(findeAlle(dateFrom, dateTo, art)), new SimpleDateFormat("MMMM", Locale.GERMAN).format(cal.getTime()));
            }
        }

        return daten;
    }
//</editor-fold>

    /**
     * Bildet die Summe der Beträge aus den Transaktionen.
     * @param transaktionen 
     * @return Summe der Beträge
     */
    public double summiereTransaktionen(List<Transaktion> transaktionen) {
        Double summe = 0.0;
        for (Transaktion t : transaktionen) {
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
                if (art.getValue().equals(TransaktionsArten.Ausgabe.getLabel())) {
                    t.setArt(TransaktionsArten.Ausgabe);
                }
                if (art.getValue().equals(TransaktionsArten.Einnahme.getLabel())) {
                    t.setArt(TransaktionsArten.Einnahme);
                }

                //Wird beim erzeugen eines neuen Transaktionsobjekt gemacht
                //t.setId(UUID.randomUUID().toString());
                
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

                    TransaktionsArten tArt = null;
                    if (art.getValue().equals(TransaktionsArten.Ausgabe.getLabel())) {
                        tArt = TransaktionsArten.Ausgabe;
                    }
                    if (art.getValue().equals(TransaktionsArten.Einnahme.getLabel())) {
                        tArt = TransaktionsArten.Einnahme;
                    }

                    aktuelleKategorie = new Kategorie(kategorie.getValue(), tArt, aktuellerBenutzer);
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
    
    /**
     * Diese Methode wird für das Testen der TransaktionBean verwendet. Die BenutzerBean
     * muss für die Tests angepasst werden.
     * @param benutzerBean 
     * @return BenutzerBean Objekt
     */
    public BenutzerBean mockBenutzerBean (BenutzerBean benutzerBean){
        this.benutzerBean = benutzerBean;
        return this.benutzerBean;
    }
    
    /**
     * Diese Methode wird für das Testen der TransaktionBean verwendet. Die KategorieBean
     * muss für die Tests angepasst werden.
     * @param kategorieBean
     * @return 
     */
    public KategorieBean mockKategorieBean (KategorieBean kategorieBean){
        this.kategorieBean = kategorieBean;
        return this.kategorieBean;
    }

}
