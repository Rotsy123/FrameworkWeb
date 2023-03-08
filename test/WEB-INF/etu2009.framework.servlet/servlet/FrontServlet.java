package etu2009.framework.servlet;
import java.io.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;

public class FrontServlet extends HttpServlet {
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
            PrintWriter out = response.getWriter();
        String url=request.getServletPath();
        String requete=request.getQueryString();
        if (requete!=null) {
            url=url+"?"+requete;
        }
        out.println("Coucou");
        request.setAttribute("url",url);
        RequestDispatcher dispat=request.getRequestDispatcher("url.jsp");
        dispat.forward(request,response);
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
}