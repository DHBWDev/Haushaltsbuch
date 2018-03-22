/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web;

import ejb.KategorieBean;
import ejb.TransaktionBean;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;
import javax.ejb.EJB;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import jpa.Kategorie;
import jpa.Transaktion;

/**
 *
 * @author Fabio Kraemer
 */
@WebServlet(urlPatterns = {"/app/transaktionen/"})
public class TransaktionListeServlet extends HttpServlet {
    
    @EJB
    KategorieBean kategorieBean;
    
    @EJB
    TransaktionBean transaktionBean;
    
    @PersistenceContext
        EntityManager em;
    
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Verfügbare Kategorien für Suchfelder ermitteln
        request.setAttribute("kategorien", this.kategorieBean.findeAlle());
        
        // Suchparameter aus der URL auslesen
        String suchtext = request.getParameter("suche_text");
        String sucheKategorie = request.getParameter("suche_kategorie");
        
        // Anzuzeigende Transaktionen suchen
        Kategorie kategorie = null;

        if (sucheKategorie != null) {
            try {
                kategorie = this.kategorieBean.findeMitId(sucheKategorie);
            } catch (NumberFormatException ex) {
                kategorie = null;
            }
        }
       
        List<Transaktion> transaktionen = this.transaktionBean.suche(suchtext, kategorie);
        request.setAttribute("transaktionen", transaktionen);
      
        // Anfrage an die JSP weiterleiten
        request.getRequestDispatcher("/WEB-INF/app/transaktion_liste.jsp").forward(request, response);
    }
}
