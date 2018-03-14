/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web;

import ejb.TransaktionBean;
import java.io.IOException;
import javax.ejb.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet für die Startseite /index.html. Hier wird der Anwender einfach auf
 * die Übersichtsseite weitergeleitet. Falls er noch nicht eingeloggt ist, sorgt
 * der Applikationsserver von alleine dafür, zunächst die Loginseite anzuzeigen.
 */
@WebServlet(urlPatterns = {"/index.html"})
public class IndexServlet extends HttpServlet {

    @EJB
    private TransaktionBean transaktionBean;

    /**
     * GET-Anfrage: Seite anzeigen
     *
     * @param request
     * @param response
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        
        //this.transaktionBean.importiereXML();

        response.sendRedirect(WebUtils.appUrl(request, "/app/transaktionen/"));
    }

}
