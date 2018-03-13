package ejb;

import java.util.List;
import javax.ejb.Stateless;
import jpa.Transaktion;

@Stateless
public class TransaktionBean extends EntityBean<Transaktion, Long> {

    public TransaktionBean() {
        super(Transaktion.class);
    }

    public List<Transaktion> findeAlle() {
        return this.em.createQuery("SELECT c FROM Transaktion").getResultList();
    }

}
