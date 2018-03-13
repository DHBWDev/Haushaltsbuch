package ejb;

import java.util.List;
import jpa.Kategorie;

public class KategorieBean extends EntityBean<Kategorie, Long> {

    public KategorieBean() {
        super(Kategorie.class);
    }
    
    public List<Kategorie> findeAlle() {
        return this.em.createQuery("SELECT c FROM Kategorie").getResultList();
    }
}
