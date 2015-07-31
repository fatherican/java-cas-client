package org.jasig.cas.client.servlet;

import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by kai on 2015/7/29.
 */
public class ValidateLoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        PrintWriter out = response.getWriter();
        try {
            String state = request.getParameter("state");
            String message = request.getParameter("message");
            String callback = request.getParameter("callback");
            String st = request.getParameter("st");
            request.setAttribute("state",state);
            request.setAttribute("message",message);
            request.setAttribute("callback",callback);
            request.setAttribute("serviceTicket",st);
            request.getRequestDispatcher("/WEB-INF/ssoResult.jsp").forward(request, response);
        }finally {
            if(out != null){
                out.close();
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
       doPost(req, resp);
    }
}
