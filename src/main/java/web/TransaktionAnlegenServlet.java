/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web;

import ejb.BenutzerBean;
import ejb.TransaktionBean;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.input.sax.XMLReaders;

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

        File xmlFile = new File(path + File.separator + fileName);

        OutputStream out = null;
        InputStream filecontent = null;
        //final PrintWriter writer = response.getWriter();
        HttpSession session = request.getSession();

        try {

            out = new FileOutputStream(xmlFile);

            filecontent = filePart.getInputStream();

            int read = 0;
            final byte[] bytes = new byte[1024];

            while ((read = filecontent.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            //Validierung der XML-Datei
            //returns true or false
            if (validiereXML(xmlFile, path) == false) {
                throw new JDOMException();
            }

            //XML-Datei in DB importieren
            this.transaktionBean.importiereXML(xmlFile, this.benutzerBean.gibAktuellenBenutzer());

            // Browser auffordern, die aktuelle Seite neuzuladen
            session.setAttribute("name", "Einlesen war erfolgreich!");
            response.sendRedirect(request.getContextPath() + TransaktionAnlegenServlet.URL);

        } catch (FileNotFoundException fne) {
            session.setAttribute("name", "XML nicht gefunden!");
            response.sendRedirect(request.getContextPath() + TransaktionAnlegenServlet.URL);

        } catch (JDOMException ex) {
            session.setAttribute("name", "Keine gültige XML!");
            response.sendRedirect(request.getContextPath() + TransaktionAnlegenServlet.URL);
        } finally {

            if (out != null) {
                out.close();
            }
            if (filecontent != null) {
                filecontent.close();
            }
            /*if (writer != null) {
            writer.close();
            }*/

            //Hochgeladene Datei wieder löschen 
            xmlFile.delete();
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

    //Methode validiert XML gegen eine DTD
    private boolean validiereXML(File xmlFile, String path) throws FileNotFoundException, IOException, JDOMException {
        //Erstellen einer seperaten XML zur Validierung
        File validationXML = new File(path + File.separator + "Validation.xml");
        BufferedReader inputStream = new BufferedReader(new FileReader(xmlFile));
        //Validierungsstring
        String validationString
                = "<!DOCTYPE Transaktionsverzeichnis [\n"
                + "<!ELEMENT Transaktionsverzeichnis (Transaktion)*>\n"
                + "<!ELEMENT Transaktion (Bezeichnung, Beschreibung, Betrag, ErstellungsDatum, Art, Kategorie)>\n"
                + "<!ELEMENT Bezeichnung (#PCDATA)>\n"
                + "<!ELEMENT Beschreibung (#PCDATA)>\n"
                + "<!ELEMENT Betrag (#PCDATA)>\n"
                + "<!ELEMENT ErstellungsDatum (#PCDATA)>\n"
                + "<!ELEMENT Art (#PCDATA)>\n"
                + "<!ELEMENT Kategorie (#PCDATA)>\n"
                + "]>";

        // if validationXML doesnt exists, then create it
        if (!validationXML.exists()) {
            validationXML.createNewFile();
        }

        //eingelesenes XML-File in seperate XML speichern
        FileWriter filewriter = new FileWriter(validationXML.getAbsoluteFile());
        BufferedWriter outputStream = new BufferedWriter(filewriter);

        String count;

        while ((count = inputStream.readLine()) != null) {
            outputStream.write(count.replace("ï»¿", ""));
            outputStream.newLine();
            System.out.println("Count:" + count.replace("ï»¿", ""));
            if (count.contains("?xml")) {
                outputStream.write(validationString);
                outputStream.newLine();
            }

        }
        outputStream.flush();
        outputStream.close();
        inputStream.close();

        //Sax Builder erzeugen
        SAXBuilder builder = new SAXBuilder(XMLReaders.DTDVALIDATING);

        Document jdomDocValidatedTrue = builder.build(validationXML);

        //XML prüfen
        return jdomDocValidatedTrue.hasRootElement();
    }

}
