/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb;



import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.embeddable.EJBContainer;
import javax.naming.NamingException;
import jpa.Transaktion;
import jpa.TransaktionsArten;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
/**
 *
 * @author Fabio Kraemer
 */


public class TransaktionBeanTest {
    
    //private TransaktionBean transaktionBean; 
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
    private static EJBContainer container;
    
    //
    // EJB-Container starten und stoppen
    //
    @BeforeClass
    public static void setUpClass() throws Exception {
        Map<String, Object> properties = new HashMap<>();
        properties.put(EJBContainer.MODULES, new File("build/jar"));
        container = EJBContainer.createEJBContainer(properties);
        System.out.println("Opening the container");
    }
    
    @AfterClass
    public static void tearDownClass() throws Exception {
        container.close();
        System.out.println("Closing the container");
    }
    
    //
    // EJB-Referenzen besorgen
    //
    private TransaktionBean transaktionBean;
    
    @Before
    public void setUp() throws NamingException {
        // Manueller JNDI-Lookup. Das ist, was @EJB für uns im Hintergrund
        // macht. Jedoch können wir @EJB in einer Testklasse nicht verwenden.
        transaktionBean = (TransaktionBean) container.getContext().lookup("java:global/classes/TransaktionBean");
    }
    
    @Test
    public void test() {
        List <Transaktion> t = new ArrayList();
        
        t.add(new Transaktion("", "", 100.00, new Date(), TransaktionsArten.Ausgabe));
        
        Double result = transaktionBean.summiereTransaktionen(t);
        assertEquals(result, 100.00,1.00);
    }
}
