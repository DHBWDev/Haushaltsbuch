/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web;

import ejb.TransaktionBean;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.*;
import jpa.TransaktionsArten;

/**
 *
 * @author Fabio Kraemer
 */


        
@WebServlet(urlPatterns = {"/app/statistik/"})
public class TransaktionStatistikServlet extends HttpServlet {
    
    @EJB
    TransaktionBean transaktionBean;
     
    public String erzeugeJson(String titel, String farbe, String[] label, Double[] zahlen) {

        JSONObject data1 = new JSONObject();

        JSONObject dataset = new JSONObject();
        dataset.put("label", titel);

        if ("rot".equals(farbe)) {
            dataset.put("backgroundColor", "rgb(255, 0, 0)");
            dataset.put("borderColor", "rgb(255, 0, 0)");
        }

        if ("gruen".equals(farbe)) {
            dataset.put("backgroundColor", "rgb(0, 255, 0)");
            dataset.put("borderColor", "rgb(0, 255, 0)");
        }

        dataset.put("data", zahlen);

        JSONArray array = new JSONArray();

        array.put(dataset);

        data1.put("datasets", array);
        data1.put("labels", label);

        return data1.toString();
    }

    
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        Object [][] ausgaben = transaktionBean.getSummeLastYear(TransaktionsArten.Ausgabe);
        Object [][] einnahmen = transaktionBean.getSummeLastYear(TransaktionsArten.Einnahme);
        
        String[] monatsNamen = new String[ausgaben.length];
        Double[] monatsAusgaben = new Double[ausgaben.length];
        Double[] monatsEinnahmen = new Double[ausgaben.length];
        
        for (int i = 0; i < ausgaben.length;  i++){
            monatsNamen[i] = (String)ausgaben[i][0];
            monatsAusgaben[i] = (Double)ausgaben[i][1];
            monatsEinnahmen[i] = (Double)einnahmen[i][1];
         }
        
        //Variablen an das JSP weiterleiten
        request.setAttribute("monatsausgaben", erzeugeJson(
                "Monatsausgaben",
                "rot", monatsNamen, monatsAusgaben)
        );
        
        request.setAttribute("monatseinnahmen", erzeugeJson(
                "Monatseinnahmen",
                "gruen", monatsNamen, monatsEinnahmen)

        );
        
        
        Object [][] ausgabenKategorien = transaktionBean.getSummeCategoryLastYear(TransaktionsArten.Ausgabe);
        Object [][] einnahmeKategorien = transaktionBean.getSummeCategoryLastYear(TransaktionsArten.Einnahme);
        
        System.out.println("Anzahl Einnahmen: " + Integer.toString(einnahmeKategorien.length));
        
        String [] labelAusgaben = new String [ausgabenKategorien.length];
        Double [] betraegeAusgaben = new Double [ausgabenKategorien.length];
        
        String [] labelEinnahmen = new String [einnahmeKategorien.length];
        Double [] betraegeEinnahmen = new Double [einnahmeKategorien.length];
        
        System.out.println("Anzahl Einnahmen: " + Integer.toString(labelEinnahmen.length));
         
        
        String ausgabeTest = "";
                
        /*if (ausgabenKategorien != null) {
            for (int i = 0; i < ausgabenKategorien.length;  i++){
                if (ausgabenKategorien[i] != null) {
                    ausgabeTest = ausgabeTest + (String) ausgabenKategorien[i][0] + " " + Double.toString((Double)ausgabenKategorien[i][1]) + ", ";
                }else {
                    ausgabeTest = ausgabeTest + "null, ";
                }
            }
            request.setAttribute("test", ausgabeTest);
        }else {
            request.setAttribute("test", "leer");
        }
        
        request.setAttribute("test", ausgabeTest);*/
                
        for (int i = 0; i <= ausgabenKategorien.length-1; i++){
            labelAusgaben[i] = (String) ausgabenKategorien[i][0];
            betraegeAusgaben[i] = (Double) ausgabenKategorien[i][1];
        }
        
        for (int i = 0; i <= einnahmeKategorien.length-1; i++){
            labelEinnahmen[i] = (String) einnahmeKategorien[i][0];
            betraegeEinnahmen[i] = (Double) einnahmeKategorien[i][1];
        }
        
        request.setAttribute("ausgabenkategorien", erzeugeJson("Ausgaben nach Kategorien", "",labelAusgaben, betraegeAusgaben ));
        
        request.setAttribute("einnahmenkategorien", erzeugeJson("Einnahmen nach Kategorien", "",labelEinnahmen, betraegeEinnahmen ));
        
        // Anfrage an die JSP weiterleiten
        request.getRequestDispatcher("/WEB-INF/app/transaktion_statistik.jsp").forward(request, response);
    }
}
