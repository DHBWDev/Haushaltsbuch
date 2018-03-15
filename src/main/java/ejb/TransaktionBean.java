package ejb;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.ejb.Stateless;
import jpa.Benutzer;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import jpa.Kategorie;
import jpa.Transaktion;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.XMLOutputter;

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
    
    public List<Transaktion> getTransaktionenVonDatumBisDatum(Date vonDatum, Date bisDatum){
        return em.createQuery("SELECT t FROM Transaktion t "
                            + " WHERE t.erstellungsDatum >= :vonDatum"
                            + " AND t.erstellungsDatum <= :bisDatum"
                            + " ORDER BY t.erstellungsDatum")
                .setParameter("vonDatum", vonDatum)
                .setParameter("bisDatum", bisDatum)
                .getResultList();
    }
    
    public void getSummeVonMonatBisMonat(Integer vonMonat, Integer bisMonat){
        List result;
        
       /* Iterator<String> it = result.listIterator();
        
        while (it.hasNext()){
            System.out.println(it.toString());
        }*/
       
        /*CriteriaBuilder cb = em.getCriteriaBuilder();
       
        CriteriaQuery<Long> query = cb.createQuery(Long.class);
        
        query.select(query.);
        
        result = em.createQuery("SELECT i FROM (SELECT MONTH(ERSTELLUNGSDATUM) AS MONAT, SUM(BETRAG) AS SUMMEBETRAG"
                + "FROM HAUSHALTSBUCH.TRANSAKTION"
                + "GROUP BY MONTH(ERSTELLUNGSDATUM)"
                + "ORDER BY MONAT) i where MONAT >= :vonMonat and MONAT <= :bisMonat")
                .setParameter("vonMonat", vonMonat)
                .setParameter("bisMonat", bisMonat)
                .getResultList();*/
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
