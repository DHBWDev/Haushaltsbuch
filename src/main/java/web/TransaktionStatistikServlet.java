/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web;

import ejb.StatistikDaten;
import ejb.TransaktionBean;
import java.io.IOException;
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
    
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
       
       StatistikDaten statistik ;
       
       //statistik = this.transaktionBean.getStatistikLastYearPerMonth(TransaktionsArten.Ausgabe);
       statistik = this.transaktionBean.getStatistikLastYearPerMonth(TransaktionsArten.Ausgabe, new Date());
       statistik.setFarbe("rot");
       statistik.setTitel("Ausgaben nach Monat");
       request.setAttribute("monatsausgaben", statistik.erzeugeJson());
       
       
       statistik = this.transaktionBean.getStatistikLastYearPerMonth(TransaktionsArten.Einnahme, new Date());
       statistik.setFarbe("gruen");
       statistik.setTitel("Einnahmen nach Monat");
       request.setAttribute("monatseinnahmen", statistik.erzeugeJson());
       
       
       statistik = this.transaktionBean.getStatistikLastYearPerCategory(TransaktionsArten.Ausgabe);
       statistik.setFarbe("rottoene");
       statistik.setTitel("Ausgaben nach Kategorien");
       request.setAttribute("ausgabenkategorien", statistik.erzeugeJson());
       
       statistik = this.transaktionBean.getStatistikLastYearPerCategory(TransaktionsArten.Einnahme);
       statistik.setFarbe("gruentoene");
       statistik.setTitel("Einnahmen nach Kategorien");
       request.setAttribute("einnahmenkategorien", statistik.erzeugeJson());
       
       request.getRequestDispatcher("/WEB-INF/app/transaktion_statistik.jsp").forward(request, response);
    }
}
