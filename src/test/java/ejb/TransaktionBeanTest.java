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
import jpa.Benutzer;
import jpa.Kategorie;
import jpa.Transaktion;
import jpa.TransaktionsArten;
import org.junit.AfterClass;
import org.junit.Assert;
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

        //Testdaten erzeugen
        transaktionBean.speichernNeu(new Transaktion("T", "T", 100.00, new Date(), TransaktionsArten.Ausgabe, null));
        transaktionBean.speichernNeu(new Transaktion("A", "T", 200.00, new Date(), TransaktionsArten.Ausgabe, null));
        transaktionBean.speichernNeu(new Transaktion("T", "T", 500.00, new Date(), TransaktionsArten.Ausgabe, null));
    }
    
    @Test
    public void SummiereTransaktionen_Test() {
        List <Transaktion> t = new ArrayList();
        
        t.add(new Transaktion("", "", 100.00, new Date(), TransaktionsArten.Ausgabe,null));
        t.add(new Transaktion("", "", 100.00, new Date(), TransaktionsArten.Ausgabe,null));
        
        Double result = transaktionBean.summiereTransaktionen(t);
        assertEquals(result, 200.00,1.00);
    }
    
    @Test
    public void getStatistikLastYearPerMonth_Test(){
    StatistikDaten sd = transaktionBean.getStatistikLastYearPerMonth(TransaktionsArten.Ausgabe, new Date());
    Double [] result = {0.00,0.00,0.00,0.00,0.00,0.00,0.00,0.00,0.00,0.00, 0.00,800.00} ;
    Double [] d = sd.getArrayWithWerte();
    
    Assert.assertArrayEquals(result, d);
    }
    
}
