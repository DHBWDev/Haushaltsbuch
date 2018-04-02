/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb;



import java.util.Date;
import javax.persistence.EntityManager;
import jpa.Transaktion;
import jpa.TransaktionsArten;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;
/**
 *
 * @author Fabio Kraemer
 */


public class TransaktionBeanTest {
    
    private TransaktionBean transaktionBean; 
    /*
    @Before
    public void setUp(){
    transaktionBean = new TransaktionBean();
    transaktionBean.em = mock(EntityManager.class);
    }
    
    @Test
    public void TestSpeichereNeu(){
    Transaktion expected = new Transaktion("TT", "TT", 100.00, new Date(), TransaktionsArten.Ausgabe);
    transaktionBean.speichernNeu(expected);
    verify(transaktionBean.em,times(1)).persist(expected);
    }
    */
}
