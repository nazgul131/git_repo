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
    private Map<String, Client> _sessions;

    public AuthServlet(Map<String, Client> clients, Map<String, Client> sessions)
    {
        _clients = clients;
        _sessions = sessions;
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        response.setCharacterEncoding("windows-1251");

        String sessionId = request.getSession().getId();

        if(_sessions.containsKey(sessionId)) {
            if(request.getParameter("exit").compareTo("1")==0){
                _sessions.remove(sessionId);
                response.sendRedirect("/auth");
            }
            else response.sendRedirect("/profile");
        }
        else {
            Map<String, Object> pageVariables = new HashMap<>();
            pageVariables.put("message", "");

            response.getWriter().println(PageGenerator.getPage("auth.html", pageVariables));
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        response.setCharacterEncoding("windows-1251");

        boolean isSuccess = false;
        String sessionId = request.getSession().getId();

        Map<String, Object> pageVariables = new HashMap<>();

        String login = request.getParameter("login");
        Integer pssw = request.getParameter("password").hashCode();

        response.setStatus(HttpServletResponse.SC_OK);

        Client client = _clients.get(login);
        if (client != null) {
            if(client.IsOnline) {
                pageVariables.put("message", "Already authorized!");
            }

            if (client.PsswHash.equals(pssw)) {
                if(_sessions.containsKey(sessionId))
                    _sessions.remove(sessionId);

                _sessions.put(sessionId, client);

                isSuccess = true;
                client.IsOnline = true;

                response.sendRedirect("/profile");
            }else pageVariables.put("message", "Wrong login or password!");
        }else pageVariables.put("message", "Wrong login or password!");

        if (!isSuccess) {
            response.getWriter().println(PageGenerator.getPage("auth.html", pageVariables));
        }
    }
}
