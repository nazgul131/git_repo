package syspaym.servlets;

import syspaym.utils.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Admin on 24.04.2016.
 */
public class AuthServlet extends HttpServlet
{
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("testString", "Test success!");

        response.getWriter().println(PageGenerator.getPage("login.html", pageVariables));
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {

    }
}
