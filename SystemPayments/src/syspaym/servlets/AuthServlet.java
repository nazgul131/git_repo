package syspaym.servlets;

import syspaym.contracts.Client;
import syspaym.contracts.Payment;
import syspaym.utils.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Admin on 24.04.2016.
 */
public class AuthServlet extends HttpServlet
{
    private Map<String, Client> _clients;

    public AuthServlet(Map<String, Client> clients)
    {
        _clients = clients;
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        //Map<String, Object> pageVariables = new HashMap<>();
        //pageVariables.put("testString", "Test success!");

        response.getWriter().println(PageGenerator.getPage("auth.html"));
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        Map<String, Object> pageVariables = new HashMap<>();
        boolean isSuccess = false;

        String login = request.getParameter("login");
        Integer pssw = request.getParameter("password").hashCode();

        Client client = _clients.get(login);
        if (client != null) {
            if(client.IsAuthorized) {
                pageVariables.put("message", "Already authorized!");
            }

            if (client.PsswHash.equals(pssw)) {
                isSuccess = true;
                client.IsAuthorized = true;
                pageVariables.put("message", "Success!");
                response.sendRedirect("index.html");
            }else pageVariables.put("message", "Wrong login or password!");
        }else pageVariables.put("message", "Wrong login or password!");

        if (!isSuccess) {
            response.getWriter().println(PageGenerator.getPage("auth.html", pageVariables));
        }
    }
}
