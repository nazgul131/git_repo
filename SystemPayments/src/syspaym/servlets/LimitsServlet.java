package syspaym.servlets;

import syspaym.contracts.Account;
import syspaym.contracts.Client;
import syspaym.contracts.Payment;
import syspaym.contracts.Service;
import syspaym.contracts.limits.ILimit;
import syspaym.contracts.limits.Limit;
import syspaym.utils.DateHelper;
import syspaym.utils.PageGenerator;
import syspaym.utils.Time;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.util.*;

/**
 * Created by Admin on 24.04.2016.
 */
public class LimitsServlet extends HttpServlet
{
    private ArrayList<ILimit> _limits;
    private Map<String, Service> _services;
    private Map<String, Client> _sessions;

    public LimitsServlet(ArrayList<ILimit> limits
                        ,Map<String, Service> services
                        ,Map<String, Client> sessions)
    {
        _limits = limits;
        _services = services;
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
            pageVariables.put("panel", PageGenerator.CreatePanelWithMenu("limits", c));

            String newLimit = request.getParameter("new");
            String editLimit = request.getParameter("edit");
            if (newLimit != null && newLimit.compareTo("1") == 0) {
                pageVariables.put("title", "New limit");
                pageVariables.put("button", "Create");
                pageVariables.put("action", "new");

                pageVariables.put("description", "New limit");
                pageVariables.put("beginTime", "00:00");
                pageVariables.put("endTime", "23:59");
                pageVariables.put("interval", "0");
                pageVariables.put("maxSum", "0");
                pageVariables.put("maxNumber", "0");
                pageVariables.put("services_list", PageGenerator.CreateServicesList(_services, null, true));

                response.getWriter().println(PageGenerator.getPage("edit_limit.html", pageVariables));
                return;
            } else if (editLimit != null) {

                ILimit limit;
                for (ILimit l : _limits) {
                    if (l.getServiceId().toString().compareTo(editLimit) == 0) {

                        pageVariables.put("title", "Edit limit");
                        pageVariables.put("button", "Edit");
                        pageVariables.put("action", "edit");

                        pageVariables.put("description", l.getDescription());
                        pageVariables.put("beginTime", DateHelper.format(l.getBeginTime(), "HH:mm"));
                        pageVariables.put("endTime", DateHelper.format(l.getBeginTime(), "HH:mm"));
                        pageVariables.put("interval", l.getInterval().toString());
                        pageVariables.put("maxSum", l.getMaxSum().toString());
                        pageVariables.put("maxNumber", l.getMaxNumber().toString());
                        pageVariables.put("services_list", PageGenerator.CreateServicesList(_services, editLimit, true));

                        response.getWriter().println(PageGenerator.getPage("edit_limit.html", pageVariables));
                        return;
                    }
                }
            }

            pageVariables.put("limits", PageGenerator.CreateLimits(_limits));

            response.getWriter().println(PageGenerator.getPage("limits.html", pageVariables));
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
            String description = request.getParameter("beginTime");
            Time beginTime = Time.timeFromHHmm(request.getParameter("beginTime"));
            Time endTime = Time.timeFromHHmm(request.getParameter("endTime"));
            Long interval = Long.parseLong(request.getParameter("interval"));
            Double maxSum = Double.parseDouble(request.getParameter("maxSum"));
            Long maxNumber = Long.parseLong(request.getParameter("maxNumber"));

            Service service = null;
            String serviceId = request.getParameter("service");
            if(serviceId.compareTo("all") != 0)
                service = _services.get(serviceId);

            String action = request.getParameter("action");
            if(action != null) {
                if (action.compareTo("new") == 0) {

                } else if(action.compareTo("edit") == 0) {

                }
            }

            /*Client client = _sessions.get(sessionId);
            Account account = client.Accounts.get(request.getParameter("account"));
            Service service = _services.get(request.getParameter("service"));
            Double sum = Double.parseDouble(request.getParameter("sum"));
            Payment payment = new Payment(new Date(), account, service, sum, null);*/

            //_payments.addLast(payment);

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
