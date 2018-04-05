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
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.embeddable.EJBContainer;
import javax.naming.NamingException;
import jpa.Benutzer;
import jpa.Transaktion;
import jpa.TransaktionsArten;
import org.junit.AfterClass;
import org.junit.Assert;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;
/**
 *
 * @author Fabio Kraemer
 */


public class TransaktionBeanTest {
     private static EJBContainer container;
    
    //
    // EJB-Container starten und stoppen
    //
    @BeforeClass
    public static void setUpClass() throws Exception {
        System.out.println("ejb.TransaktionBeanTest.setUpClass()");
        
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
    //Test vorbereiten
    //
    private TransaktionBean transaktionBean;
    private Benutzer mockedUser; 
    
    @Before
    public void setUp() throws NamingException {
        System.out.println("ejb.TransaktionBeanTest.setUp()");
        
        // EJB-Referenzen besorgen
        transaktionBean = (TransaktionBean) container.getContext().lookup("java:global/classes/TransaktionBean");
        
        //Mocken des aktuellen Benutzers - TEST
        mockedUser = new Benutzer("TEST", "password");
        BenutzerBean benutzerBean = transaktionBean.mockBenutzerBean(Mockito.mock(BenutzerBean.class));
        Mockito.when(benutzerBean.gibAktuellenBenutzer()).thenReturn(mockedUser);
        
        System.out.println("Aktueller Testbenutzer: " + benutzerBean.gibAktuellenBenutzer().getUsername());
   
        erzeugeTestdaten(TransaktionsArten.Ausgabe);
    }
    
     public void speichereTupel(Double betrag, int jahr, int monat, int tag, TransaktionsArten art){
        System.out.println("Datum: " + tag + "." + (monat + 1 ) + "." + jahr + ", Betrag " + betrag + ", Art " + art.getLabel());
        
        Calendar cal = new GregorianCalendar();
        cal.set(jahr, monat, tag);
        transaktionBean.speichernNeu(new Transaktion("TEST", "TEST", betrag, cal.getTime(), art, null, mockedUser));
    }
    
    public void erzeugeTestdaten (TransaktionsArten art){
        System.out.println("ejb.TransaktionBeanTest.erzeugeTestdaten()");
        System.out.println();
        
        //Testdaten erzeugen
        speichereTupel(100.00, 2016, 5, 15, art);
        speichereTupel(200.00, 2016, 7, 11, art); 
        speichereTupel(300.00, 2016, 8, 10, art);
        speichereTupel(200.00, 2016, 8, 15, art); 
        speichereTupel(100.00, 2016, 11, 31, art); 
        speichereTupel(100.00, 2017, 1, 15, art); 
        speichereTupel(500.00, 2017, 2, 15, art);
        speichereTupel(200.00, 2017, 2, 15, art); 
        speichereTupel(300.00, 2017, 2, 15, art);   
        speichereTupel(200.00, 2017, 4, 03, art);
        speichereTupel(200.00, 2017, 4, 03, art); 
        speichereTupel(400.00, 2017, 4, 03, art); 
        
        System.out.println();
    }
    
    @Test
    public void SummiereTransaktionen_Test() {
        System.out.println("ejb.TransaktionBeanTest.SummiereTransaktionen_Test()");

        //Transaktionen erzeugen
        List <Transaktion> t = new ArrayList();
        t.add(new Transaktion("", "", 100.00, new Date(), TransaktionsArten.Ausgabe,null,mockedUser));
        t.add(new Transaktion("", "", 100.00, new Date(), TransaktionsArten.Ausgabe,null,mockedUser));
        
        Double result = transaktionBean.summiereTransaktionen(t);
        assertEquals(result, 200.00,1.00);
    }
    
    @Test
    public void getStatistikLastYearPerMonth_Test(){
        System.out.println("ejb.TransaktionBeanTest.getStatistikLastYearPerMonth_Test()");
        
        //Anfangsdatum setzen
        Calendar cal = new GregorianCalendar();
        cal.set(2017, 04, 05);
        
        StatistikDaten result = transaktionBean.getStatistikLastYearPerMonth(TransaktionsArten.Ausgabe, cal.getTime());
        
        Double [] expectedWerte = {100.00,     //Juni
                            0.00,       //Juli
                            200.00,     //August
                            500.00,     //September
                            0.00,       //Oktober
                            0.00,       //November
                            100.00,     //Dezember
                            0.00,       //Januar
                            100.00,     //Februar
                            1000.00,    //März 
                            0.00,       //April
                            800.00} ;   //Mai
        
        String [] expectedLabel = {"Juni",     
                            "Juli",         
                            "August",      
                            "September",    
                            "Oktober",      
                            "November",     
                            "Dezember",     
                            "Januar",       
                            "Februar",     
                            "März",    
                            "April",       
                            "Mai"} ;   
        
        Assert.assertArrayEquals(expectedWerte, result.getArrayWithWerte());
        Assert.assertArrayEquals(expectedLabel, result.getArrayWithLabels());
       
    }
    
}
