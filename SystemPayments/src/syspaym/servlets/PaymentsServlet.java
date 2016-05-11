package syspaym.servlets;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

import syspaym.contracts.Account;
import syspaym.contracts.Client;
import syspaym.contracts.Payment;
import syspaym.contracts.Service;
import syspaym.utils.PageGenerator;

/**
 * Created by Admin on 24.04.2016.
 */
public class PaymentsServlet extends HttpServlet
{
    private Map<String, Service> _services;
    private ArrayDeque<Payment> _payments;
    private ArrayList<Payment> _historyPayments;
    private Map<String, Client> _sessions;

    public PaymentsServlet( ArrayDeque<Payment> queuePayments, ArrayList<Payment> historyPayments
                           ,Map<String, Service> services
                           ,Map<String, Client> sessions)
    {
        _services = services;
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
            Client c = _sessions.get(sessionId);
            pageVariables.put("panel", PageGenerator.CreatePanelWithMenu("payments", c));

            String newPayment = request.getParameter("new");
            if (newPayment != null && newPayment.compareTo("1") == 0) {
                pageVariables.put("services_list", PageGenerator.CreateServicesList(_services));
                pageVariables.put("accounts_list", PageGenerator.CreateAccountsList(c.Accounts));

                response.getWriter().println(PageGenerator.getPage("new_payment.html", pageVariables));
            } else {
                pageVariables.put("payments", PageGenerator.CreatePayments(c, _payments, _historyPayments));

                response.getWriter().println(PageGenerator.getPage("payments.html", pageVariables));
            }
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        response.setCharacterEncoding("windows-1251");

        String sessionId = request.getSession().getId();
        if (!_sessions.containsKey(sessionId))
            response.sendRedirect("/auth");

        String message = null;
        Integer time = 3000; // milliseconds

        try {
            Client client = _sessions.get(sessionId);
            Account account = client.Accounts.get(request.getParameter("account"));
            Service service = _services.get(request.getParameter("service"));
            Double sum = Double.parseDouble(request.getParameter("sum"));
            Payment payment = new Payment(new Date(), account, service, sum, null);

            _payments.addLast(payment);

            message = "OK!";
            time = 100;
        }catch (Exception ex){
            message = "FAIL! "+ex.getMessage();
            time = 5000;
        }

        //response.sendRedirect("/payments");

        response.getWriter().println("<html>\n" +
                "<script>\n" +
                "\n" +
                "  setTimeout( 'location=\"/payments\";', "+time.toString()+" );\n" +
                "\n" +
                "</script><body><p>"+message+"</p></body></html>");
    }
}
