package syspaym.servlets;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import syspaym.contracts.Client;
import syspaym.contracts.Payment;
import syspaym.utils.PageGenerator;

/**
 * Created by Admin on 24.04.2016.
 */
public class PaymentsServlet extends HttpServlet
{
    private ArrayDeque<Payment> _payments;
    private ArrayList<Payment> _historyPayments;
    private Map<String, Client> _sessions;

    public PaymentsServlet( ArrayDeque<Payment> queuePayments, ArrayList<Payment> historyPayments
                           ,Map<String, Client> sessions)
    {
        _payments = queuePayments;
        _historyPayments = historyPayments;
        _sessions = sessions;
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("windows-1251");

        String sessionId = request.getSession().getId();

        if (!_sessions.containsKey(sessionId))
            response.sendRedirect("/auth");
        else {
            Map<String, Object> pageVariables = new HashMap<>();

            String newPayment = request.getParameter("new");
            if (newPayment != null && newPayment.compareTo("1") == 0) {

            } else {
                Client c = _sessions.get(sessionId);
                pageVariables.put("panel", PageGenerator.CreatePanelWithMenu("payments", c));
                pageVariables.put("payments", PageGenerator.CreatePayments(c, _payments, _historyPayments));

                response.getWriter().println(PageGenerator.getPage("payments.html", pageVariables));
            }
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {

    }
}
