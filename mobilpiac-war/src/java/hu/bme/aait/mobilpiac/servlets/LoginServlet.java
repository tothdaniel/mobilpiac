/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hu.bme.aait.mobilpiac.servlets;

import hu.bme.aait.mobilpiac.beans.UserSessionBean;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONObject;

/**
 *
 * @author Daniel
 */
public class LoginServlet extends HttpServlet {

    @EJB
    UserSessionBean us;
    
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            List<String> result = us.login(request.getParameter("login_name"), request.getParameter("password"));
            JSONObject obj = new JSONObject();
            if(result.get(0).equals("true"))
            {
                int d = request.getParameter("stay_login").equals("true") ? 24*7 : 1;
                Cookie cookie1 = new Cookie("login_name", request.getParameter("login_name"));
                cookie1.setMaxAge(60 * 60 * d); //1*d hour
                response.addCookie(cookie1);
                Cookie cookie2 = new Cookie("password", request.getParameter("password"));
                cookie2.setMaxAge(60 * 60 * d); //1*d hour
                response.addCookie(cookie2);
                obj.put("result",true);
            }
            else
            {
                obj.put("result",false);
            }
            obj.put("message", result.get(1));
            
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            out.print(obj);

        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
