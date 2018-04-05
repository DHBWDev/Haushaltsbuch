/* 
 * Copyright (C) 2018 Fabio Krämer, Samuel Haag, Sebastian Greulich
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package web;

import ejb.KategorieBean;
import ejb.TransaktionBean;
import java.io.IOException;
import java.util.List;
import javax.ejb.EJB;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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
