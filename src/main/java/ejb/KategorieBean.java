package ejb;

import java.util.List;
import javax.ejb.Stateless;
import jpa.Kategorie;
import jpa.TransaktionsArten;

@Stateless
public class KategorieBean extends EntityBean<Kategorie, String> {

    public KategorieBean() {
        super(Kategorie.class);
    }
    
   /* public List<Kategorie> findeAlle() {
        return this.em.createQuery("SELECT c FROM Kategorie").getResultList();
    }*/
 
    public List<Kategorie> findeAlle(TransaktionsArten art){
        String select = "SELECT k FROM Kategorie k WHERE k.art = :art";
        return em.createQuery(select).setParameter("art", art).getResultList();
    }
    public String Test(){
        return "test";
    }
}
