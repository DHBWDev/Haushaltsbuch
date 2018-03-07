/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Fabio Kraemer
 */
@WebServlet(urlPatterns = {"/app/transaktionen/"})
public class TransaktionListeServlet extends HttpServlet {
    
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Anfrage an die JSP weiterleiten
        request.getRequestDispatcher("/WEB-INF/app/transaktion_liste.jsp").forward(request, response);
    }
}