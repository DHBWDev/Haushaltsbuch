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

    public String erzeugeMonatsdaten(String titel, String farbe, String[] monate, Double[] zahlen) {

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
        data1.put("labels", monate);

        return data1.toString();
    }

    public String erzeugeKategoriedaten(String titel, String[] farbe, String[] monate, Double[] zahlen) {

        JSONObject data1 = new JSONObject();

        JSONObject dataset = new JSONObject();
        dataset.put("label", titel);

        dataset.put("backgroundColor", farbe);

        dataset.put("data", zahlen);

        JSONArray array = new JSONArray();

        array.put(dataset);

        data1.put("datasets", array);
        data1.put("labels", monate);

        return data1.toString();
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
       
        
        Double [] werte = transaktionBean.getSummeLastYear(TransaktionsArten.Ausgabe);
        String test = "adajdjlj " ;
        
        if (werte != null) {
            for (int i = 0; i < werte.length;  i++){
                if (werte[i] != null) {
                    test = test + Double.toString(werte[i]) + ", ";
                }else {
                    test = test + "null, ";
                }
            }
            request.setAttribute("test", test);
        }else {
            request.setAttribute("test", "leer");
        }
        
        request.setAttribute("test", test);
        
        
        
        //Variablen an das JSP weiterleiten
        request.setAttribute("monatsausgaben", erzeugeMonatsdaten(
                "Monatsausgaben",
                "rot",
                new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"},
                werte)
        );

        request.setAttribute("monatseinnahmen", erzeugeMonatsdaten(
                "Monatseinnahmen",
                "gruen",
                new String[]{"Januar", "Februar", "März"},
                new Double []{0.0,0.0,0.0})
        );

        request.setAttribute("einnahmekategorien", erzeugeKategoriedaten(
                "Einnahmekategorien",
                new String[]{"#7CFC00", "#C0FF3E", "#00FF7F", "#00EE00", "#008B45", "#BCEE68", "#7CCD7C", "#00CD00"},
                new String[]{"Dividende", "Nebenjob", "Prostitution"},
                new Double[]{10.3, 40.9, 200.0})
        );
        
        request.setAttribute("ausgabekategorien", erzeugeKategoriedaten(
                "Ausgabekategorien",
                new String[]{"#FF0000", "#FF7256", "#CD0000", "#EE4000", "#FF4040", "#FF3030", "#FF6A6A", "#8B2323"},
                new String[]{"Benzin", "Essen & Trinken", "Prostitution"},
                new Double[]{10.3, 40.9, 200.0})
        );

        // Anfrage an die JSP weiterleiten
        request.getRequestDispatcher("/WEB-INF/app/transaktion_statistik.jsp").forward(request, response);
    }
}
