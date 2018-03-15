package ejb;

import java.util.List;
import javax.ejb.Stateless;
import jpa.Kategorie;

@Stateless
public class KategorieBean extends EntityBean<Kategorie, String> {

    public KategorieBean() {
        super(Kategorie.class);
    }
    
   /* public List<Kategorie> findeAlle() {
        return this.em.createQuery("SELECT c FROM Kategorie").getResultList();
    }*/
    
    public String Test(){
        return "test";
    }
}
