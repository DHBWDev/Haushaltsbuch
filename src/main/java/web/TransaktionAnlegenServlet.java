/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web;

import ejb.BenutzerBean;
import ejb.TransaktionBean;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

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

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        //Pfade erstellen um die Datei zu speichern
        final String path = System.getProperty("user.home");
        final Part filePart = request.getPart("file");
        final String fileName = getFileName(filePart);

        File file = new File(path + File.separator + fileName);

        OutputStream out = null;
        InputStream filecontent = null;
        final PrintWriter writer = response.getWriter();
        HttpSession session = request.getSession();

        try {

            out = new FileOutputStream(file);
            filecontent = filePart.getInputStream();

            int read = 0;
            final byte[] bytes = new byte[1024];

            while ((read = filecontent.read(bytes)) != -1) {
                out.write(bytes, 0, read);

            }
            //XML-Datei in DB importieren
            this.transaktionBean.importiereXML(file, this.benutzerBean.gibAktuellenBenutzer());

            // Browser auffordern, die aktuelle Seite neuzuladen
            session.setAttribute("name", "Hochladen war erfolgreich!");
            response.sendRedirect(request.getContextPath() + TransaktionAnlegenServlet.URL);

        } catch (FileNotFoundException fne) {

            session.setAttribute("name", "Hochladen war nicht erfolgreich!");
            response.sendRedirect(request.getContextPath() + TransaktionAnlegenServlet.URL);

        } finally {

            if (out != null) {
                out.close();
            }
            if (filecontent != null) {
                filecontent.close();
            }
            if (writer != null) {
                writer.close();
            }
            //Hochgeladene Datei wieder löschen 
            file.delete();
        }

    }

    private String getFileName(final Part part) {
        final String partHeader = part.getHeader("content-disposition");
        LOGGER.log(Level.INFO, "Part Header = {0}", partHeader);
        for (String content : part.getHeader("content-disposition").split(";")) {
            if (content.trim().startsWith("filename")) {
                return content.substring(
                        content.indexOf('=') + 1).trim().replace("\"", "");
            }
        }
        return null;

    }

}
