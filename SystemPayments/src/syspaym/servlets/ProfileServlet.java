package syspaym.servlets;

import syspaym.contracts.Client;
import syspaym.utils.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Admin on 24.04.2016.
 */
public class ProfileServlet extends HttpServlet
{
    private Map<String, Client> _sessions;

    public ProfileServlet(Map<String, Client> sessions)
    {
        _sessions = sessions;
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("windows-1251");

        String sessionId = request.getSession().getId();

        if (!_sessions.containsKey(sessionId))
            response.sendRedirect("/auth");
        else {
            Map<String, Object> pageVariables = new HashMap<>();

            Client c = _sessions.get(sessionId);
            pageVariables.put("panel", PageGenerator.CreatePanelWithMenu(null, c));

            response.getWriter().println(PageGenerator.getPage("profile.html", pageVariables));
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {

    }
}
