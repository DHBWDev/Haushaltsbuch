package ejb;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import jpa.Kategorie;
import jpa.Transaktion;

@Stateless
public class TransaktionBean extends EntityBean<Transaktion, Long> {

    public TransaktionBean() {
        super(Transaktion.class);
    }

    public List<Transaktion> findeAlle() {
        return this.em.createQuery("SELECT c FROM Transaktion").getResultList();
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

}
