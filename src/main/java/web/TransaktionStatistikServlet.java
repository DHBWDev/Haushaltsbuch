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
import jpa.TransaktionsArten;

/**
 *
 * @author Fabio Kraemer
 */


        
@WebServlet(urlPatterns = {"/app/statistik/"})
public class TransaktionStatistikServlet extends HttpServlet {
    @EJB
    TransaktionBean transaktionBean;
    
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        //Variablen an das JSP weiterleiten
        
        String daten = "[10, 100, 200]";
        
        request.setAttribute("daten", daten);
        
        Double[] test = transaktionBean.getSummeVonMonatBisMonat(new Date(), WebUtils.parseDate("12.07.2019"), TransaktionsArten.Ausgabe);
        
        if (test.length > 0){
            request.setAttribute("test", "test" + test[0].toString());
        } else {
            request.setAttribute("test", "leer");
        }
        // Anfrage an die JSP weiterleiten
        request.getRequestDispatcher("/WEB-INF/app/transaktion_statistik.jsp").forward(request, response);
    }
}
