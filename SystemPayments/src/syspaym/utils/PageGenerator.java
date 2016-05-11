package syspaym.utils;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import syspaym.contracts.*;
import syspaym.contracts.limits.ILimit;

import java.io.*;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Map;

import static syspaym.contracts.EStatesPayment.*;

public class PageGenerator {
    private static final String HTML_DIR = "public_html/templates";
    private static final Configuration CFG = new Configuration();

    public static String getPage(String filename) {
        Writer stream = new StringWriter();
        try {
            Reader r = new FileReader(HTML_DIR + File.separator + filename);
            char[] buf = new char[1024];
            while(r.read(buf) != -1)
            {
                stream.write(buf);
            }

            r.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return stream.toString();
    }

    public static String getPage(String filename, Map<String, Object> data) {
        Writer stream = new StringWriter();
        try {
            Template template = CFG.getTemplate(HTML_DIR + File.separator + filename);
            template.process(data, stream);
        } catch (IOException | TemplateException e) {
            e.printStackTrace();
        }

        return stream.toString();
    }

    public static String CreatePanelWithMenu(String nameActivedServlet, Client client)
    {
        StringBuilder sb = new StringBuilder();

        sb.append("<div id=\"panel\">\n" +
                 "        <div class=\"name\">SystemPayments</div>\n" +
                 "        <div class=\"menu\">\n" +
                 "            <ul>\n");


        if(nameActivedServlet == "accounts")
            sb.append("<li><a class=\"active\" href=\"/accounts\">Accounts</a></li>");
        else
            sb.append("<li><a href=\"/accounts\">Accounts</a></li>");

        if(nameActivedServlet == "payments")
            sb.append("<li><a class=\"active\" href=\"/payments\">Payments</a></li>");
        else
            sb.append("<li><a href=\"/payments\">Payments</a></li>");

        if(client.IsAdmin) {
            if(nameActivedServlet == "limits")
                sb.append("<li><a class=\"active\" href=\"/limits\">Limits</a></li>");
            else
                sb.append("<li><a href=\"/limits\">Limits</a></li>");
        }

        sb.append("</ul>\n"+
                "        </div>\n" +
                "        <a class=\"exit\" href=\"/auth?exit=1\">exit</a>" +
                "        <div class=\"client\">welcome, "+client.Name+"!</div>" +
                  "    </div>");

        return sb.toString();
    }

    public static String CreateAccounts(Map<String, Account> accounts)
    {
        StringBuilder sb = new StringBuilder();

        sb.append("<div class=\"data\"");

        sb.append("<p>Accounts:</p>");

        sb.append("<ul>");

        for(Account acc : accounts.values())
        {
            sb.append("<li><ul>");

            sb.append("<li>");
            sb.append("Account: ");
            sb.append(acc.Name);
            sb.append("</li>");

            sb.append("<li>");
            sb.append("Number: ");
            sb.append(acc.Number);
            sb.append("</li>");

            sb.append("<li>");
            sb.append("Rest: ");
            sb.append(acc.getRest().toString());
            sb.append("</li>");

            sb.append("</ul></li><br/>");
        }

        sb.append("</ul>");

        sb.append("</div>");

        return sb.toString();
    }

    private static void CreatePayment(StringBuilder sb, Payment p)
    {
        sb.append("<tr>");

        sb.append("<td>");
        sb.append(DateHelper.format(p.DateTime, "dd.MM.yyyy HH:mm:ss"));
        sb.append("</td>");

        sb.append("<td>");
        String state = null;
        switch(p.State)
        {
            case New: state = "На обработке"; break;
            case Tbc: state = "На подтверждении"; break;
            case OK: state = "Готов к проведению"; break;
        }
        sb.append(state);
        sb.append("</td>");

        sb.append("<td>");
        sb.append(p.Service.Name);
        sb.append("</td>");

        sb.append("<td>");
        sb.append(p.Account.Number);
        sb.append("</td>");

        sb.append("<td>");
        sb.append(p.Sum.toString());
        sb.append("</td>");

        sb.append("</tr>");
    }

    public static String CreatePayments(Client client, ArrayDeque<Payment> queuePayments, ArrayList<Payment> historyPayments)
    {
        StringBuilder sb = new StringBuilder();

        sb.append("<div class=\"data\"");

        sb.append("<p>Queue payments:</p>");

        sb.append("<table>");
        sb.append("<tr><td>Date</td><td>State</td><td>Service</td><td>Account</td><td>Sum</td></tr>");

        for(Payment p : queuePayments)
        {
            if(!client.IsAdmin)
                if(p.Account.Owner != client)
                    continue;

            CreatePayment(sb, p);
        }

        sb.append("</table><hr/>");

        //---------------------------------------------

        sb.append("<p>History payments:</p>");

        sb.append("<table>");
        sb.append("<tr><td>Date</td><td>State</td><td>Service</td><td>Account</td><td>Sum</td></tr>");

        for(Payment p : historyPayments)
        {
            if(!client.IsAdmin)
                if(p.Account.Owner != client)
                    continue;

            CreatePayment(sb, p);

        }

        sb.append("</table><hr/>");

        sb.append("</div>");

        return sb.toString();
    }

    public static String CreateServicesList(Map<String, Service> services)
    {
        StringBuilder sb = new StringBuilder();

        sb.append("<select id=\"services_list\" class=\"textbox\" name=\"service\">");

        for(Service service:services.values())
        {
            sb.append("<option value=\""+service.Id.toString()+"\">"+service.Name+"</option>");
        }

        sb.append("</select>");

        return sb.toString();
    }

    public static String CreateServicesList(Map<String, Service> services, String selectedId, boolean withAll) {
        StringBuilder sb = new StringBuilder();



        sb.append("<select id=\"services_list\" class=\"textbox\" name=\"service\">");

        if (withAll)
            sb.append("<option value=\"all\"></option>");

        for (Service service : services.values()) {
            sb.append("<option " + ((service.Id.toString().compareTo(selectedId) == 0) ? "selected" : null) + " value=\"" + service.Id.toString() + "\">" + service.Name + "</option>");
        }

        sb.append("</select>");

        return sb.toString();
    }

    public static String CreateAccountsList(Map<String, Account> accounts)
    {
        StringBuilder sb = new StringBuilder();

        sb.append("<select id=\"accounts_list\" class=\"textbox\" name=\"account\">");

        for(Account acc:accounts.values())
        {
            sb.append("<option value=\""+acc.Id.toString()+"\">"+acc.Number+"</option>");
        }

        sb.append("</select>");

        return sb.toString();
    }

    private static void CreateLimit(StringBuilder sb, ILimit limit){
        sb.append("<tr>");

        sb.append("<td>");
        sb.append(DateHelper.format(limit.getBeginTime(), "HH:mm"));
        sb.append("</td>");

        sb.append("<td>");
        sb.append(DateHelper.format(limit.getEndTime(), "HH:mm"));
        sb.append("</td>");

        sb.append("<td>");
        sb.append(limit.getInterval()/**1000*60*/); // интервал в /*минутах*/ миллисекундах
        sb.append("</td>");

        sb.append("<td>");
        sb.append(limit.getServiceName());
        sb.append("</td>");

        sb.append("<td>");
        sb.append(limit.getMaxSum());
        sb.append("</td>");

        sb.append("<td>");
        sb.append(limit.getMaxNumber());
        sb.append("</td>");

        sb.append("<td>");
        sb.append("<a href=\"/limits?edit="+limit.getServiceId().toString()+"\"");
        sb.append("</td>");

        sb.append("</tr>");
    }

    public static String CreateLimits(ArrayList<ILimit> limits) {
        StringBuilder sb = new StringBuilder();

        sb.append("<div class=\"data\"");

        sb.append("<p>Limits:</p>");

        sb.append("<table>");
        sb.append("<tr><td>Begin time</td><td>End time</td><td>Interval</td><td>Service</td><td>Max sum</td><td>Max number</td><td></td></tr>");

        for(ILimit l : limits)
        {
            CreateLimit(sb, l);
        }

        sb.append("</table><hr/>");

        sb.append("</div>");

        return sb.toString();
    }
}
