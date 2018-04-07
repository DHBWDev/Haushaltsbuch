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

import ejb.BenutzerBean;
import ejb.TransaktionBean;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.jdom2.JDOMException;

/**
 *
 * @author Samuel
 */
@WebServlet(name = "TransaktionImportierenServlet", urlPatterns = {"/app/anlegen/"})
@MultipartConfig
public class TransaktionAnlegenServlet extends HttpServlet {

    @EJB
    private TransaktionBean transaktionBean;

    @EJB
    private BenutzerBean benutzerBean;

    private final static Logger LOGGER
            = Logger.getLogger(TransaktionAnlegenServlet.class.getCanonicalName());

    public static final String URL = "/app/anlegen/";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //Anfrage an JSP weiterleiten
        request.getRequestDispatcher("/WEB-INF/app/transaktion_anlegen.jsp").forward(request, response);

        //Attribute aus der Session löschen
        HttpSession session = request.getSession();
        session.removeAttribute("name");
    }
    
    /**
     * Transaktionen anlegen und Seite neu laden
     * 
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException 
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession();
        File xmlFile = null;
        try {
            //XML-Datei einlesen
            xmlFile = this.transaktionBean.leseXML(request.getPart("file"));

            //XML-Datei validieren
            this.transaktionBean.validiereXML(xmlFile, System.getProperty("user.home"));

            //XML-Datei in DB importieren
            this.transaktionBean.importiereXML(xmlFile, this.benutzerBean.gibAktuellenBenutzer());

            //Statusausgabe
            session.setAttribute("name", "Einlesen war erfolgreich!");

        } catch (FileNotFoundException fne) {
            session.setAttribute("name", "XML nicht gefunden!");
        } catch (JDOMException ex) {
            session.setAttribute("name", "Keine gültige XML!");
        } catch (IOException ioe) {
            session.setAttribute("name", "Fehler beim Einlesen!");
        } finally {
            //Hochgeladene Datei wieder löschen -> Speicherplatz sparen
            if (xmlFile != null) {
                xmlFile.delete();
            }
            //Browser auffordern, die aktuelle Seite neuzuladen
            response.sendRedirect(request.getContextPath() + TransaktionAnlegenServlet.URL);
        }
    }
}
