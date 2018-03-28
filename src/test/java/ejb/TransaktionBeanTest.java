/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.embeddable.EJBContainer;
import jpa.Benutzer;
import jpa.Kategorie;
import jpa.Transaktion;
import jpa.TransaktionsArten;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;

/**
 *
 * @author Fabio Kraemer
 */
public class TransaktionBeanTest {
    public static EJBContainer container;
            
    public TransaktionBeanTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
        System.out.println("Opening the container");
        container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        
    }
    
    @AfterClass
    public static void tearDownClass() {
        container.close();
        System.out.println("Closing the container");
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of findeMitId method, of class TransaktionBean.
     */
    @Test
    public void testFindeMitId() throws Exception {
        System.out.println("findeMitId");
        Long id = null;
       
            TransaktionBean instance = (TransaktionBean)container.getContext().lookup("java:global/classes/TransaktionBean");
            Transaktion expResult = null;
            Transaktion result = instance.findeMitId(id);
            assertEquals(expResult, result);
        
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of findeAlle method, of class TransaktionBean.
     */
    @Test
    @Ignore
    public void testFindeAlle_0args() throws Exception {
        System.out.println("findeAlle");
        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        TransaktionBean instance = (TransaktionBean)container.getContext().lookup("java:global/classes/TransaktionBean");
        List<Transaktion> expResult = null;
        List<Transaktion> result = instance.findeAlle();
        assertEquals(expResult, result);
        container.close();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of speichernNeu method, of class TransaktionBean.
     */
    @Test
    @Ignore
    public void testSpeichernNeu() throws Exception {
        System.out.println("speichernNeu");
        Transaktion entity = null;
        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        TransaktionBean instance = (TransaktionBean)container.getContext().lookup("java:global/classes/TransaktionBean");
        Transaktion expResult = null;
        Transaktion result = instance.speichernNeu(entity);
        assertEquals(expResult, result);
        container.close();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of aktualisieren method, of class TransaktionBean.
     */
    @Test
    @Ignore
    public void testAktualisieren() throws Exception {
        System.out.println("aktualisieren");
        Transaktion entity = null;
        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        TransaktionBean instance = (TransaktionBean)container.getContext().lookup("java:global/classes/TransaktionBean");
        Transaktion expResult = null;
        Transaktion result = instance.aktualisieren(entity);
        assertEquals(expResult, result);
        container.close();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of löschen method, of class TransaktionBean.
     */
    @Test
    @Ignore
    public void testLöschen() throws Exception {
        System.out.println("l\u00f6schen");
        Transaktion entity = null;
        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        TransaktionBean instance = (TransaktionBean)container.getContext().lookup("java:global/classes/TransaktionBean");
        instance.löschen(entity);
        container.close();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of suche method, of class TransaktionBean.
     */
    @Test
    @Ignore
    public void testSuche() throws Exception {
        System.out.println("suche");
        String suchtext = "";
        Kategorie kategorie = null;
        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        TransaktionBean instance = (TransaktionBean)container.getContext().lookup("java:global/classes/TransaktionBean");
        List<Transaktion> expResult = null;
        List<Transaktion> result = instance.suche(suchtext, kategorie);
        assertEquals(expResult, result);
        container.close();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of findeAlle method, of class TransaktionBean.
     */
    @Test
    @Ignore
    public void testFindeAlle_3args() throws Exception {
        System.out.println("findeAlle");
        Date vonDatum = null;
        Date bisDatum = null;
        TransaktionsArten art = null;
        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        TransaktionBean instance = (TransaktionBean)container.getContext().lookup("java:global/classes/TransaktionBean");
        List<Transaktion> expResult = null;
        List<Transaktion> result = instance.findeAlle(vonDatum, bisDatum, art);
        assertEquals(expResult, result);
        container.close();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of findeAlle method, of class TransaktionBean.
     */
    @Test
    @Ignore
    public void testFindeAlle_4args() throws Exception {
        System.out.println("findeAlle");
        Date vonDatum = null;
        Date bisDatum = null;
        TransaktionsArten art = null;
        Kategorie kategorie = null;
        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        TransaktionBean instance = (TransaktionBean)container.getContext().lookup("java:global/classes/TransaktionBean");
        List<Transaktion> expResult = null;
        List<Transaktion> result = instance.findeAlle(vonDatum, bisDatum, art, kategorie);
        assertEquals(expResult, result);
        container.close();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getStatistikLastYearPerCategory method, of class TransaktionBean.
     */
    @Test
    @Ignore
    public void testGetStatistikLastYearPerCategory() throws Exception {
        System.out.println("getStatistikLastYearPerCategory");
        TransaktionsArten art = null;
        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        TransaktionBean instance = (TransaktionBean)container.getContext().lookup("java:global/classes/TransaktionBean");
        StatistikDaten expResult = null;
        StatistikDaten result = instance.getStatistikLastYearPerCategory(art);
        assertEquals(expResult, result);
        container.close();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getStatistikLastYearPerMonth method, of class TransaktionBean.
     */
    @Test
    @Ignore
    public void testGetStatistikLastYearPerMonth() throws Exception {
        System.out.println("getStatistikLastYearPerMonth");
        TransaktionsArten art = null;
        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        TransaktionBean instance = (TransaktionBean)container.getContext().lookup("java:global/classes/TransaktionBean");
        StatistikDaten expResult = null;
        StatistikDaten result = instance.getStatistikLastYearPerMonth(art);
        assertEquals(expResult, result);
        container.close();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getMonthAndYearFromTo method, of class TransaktionBean.
     */
    @Test
    @Ignore
    public void testGetMonthAndYearFromTo() throws Exception {
        System.out.println("getMonthAndYearFromTo");
        Date from = null;
        Integer months = null;
        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        TransaktionBean instance = (TransaktionBean)container.getContext().lookup("java:global/classes/TransaktionBean");
        Date[][] expResult = null;
        Date[][] result = instance.getMonthAndYearFromTo(from, months);
        assertArrayEquals(expResult, result);
        container.close();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of summiereTransaktionen method, of class TransaktionBean.
     */
    @Test
    @Ignore
    public void testSummiereTransaktionen() throws Exception {
        System.out.println("summiereTransaktionen");
        List<Transaktion> transaktionen = null;
        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        TransaktionBean instance = (TransaktionBean)container.getContext().lookup("java:global/classes/TransaktionBean");
        double expResult = 0.0;
        double result = instance.summiereTransaktionen(transaktionen);
        assertEquals(expResult, result, 0.0);
        container.close();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of importiereXML method, of class TransaktionBean.
     */
    @Test
    @Ignore
    public void testImportiereXML() throws Exception {
        System.out.println("importiereXML");
        File f = null;
        Benutzer aktuellerBenutzer = null;
        EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer();
        TransaktionBean instance = (TransaktionBean)container.getContext().lookup("java:global/classes/TransaktionBean");
        instance.importiereXML(f, aktuellerBenutzer);
        container.close();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
