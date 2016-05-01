package syspaym.utils;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import syspaym.contracts.Account;
import syspaym.contracts.Client;
import syspaym.contracts.EStatesPayment;
import syspaym.contracts.Payment;

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
                 "        <div class=\"name\">SystemPayment</div>\n" +
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

    public static String CreateAccounts(ArrayList<Account> accounts)
    {
        StringBuilder sb = new StringBuilder();

        sb.append("<div class=\"data\"");

        sb.append("<p>Accounts:</p>");

        sb.append("<ul>");

        for(Account acc : accounts)
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
        sb.append(DateHelper.format(p.DateTime, "dd.mm.yyyy"));
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

        sb.append("<p>History payments:</p>");

        sb.append("<table>");
        sb.append("<th><td>Date</td><td>State</td><td>Service</td><td>Account</td><td>Sum</td></th>");

        for(Payment p : historyPayments)
        {
            if(!client.IsAdmin)
                if(p.Account.Owner != client)
                    continue;

            CreatePayment(sb, p);

        }

        sb.append("</table><hr/>");

        //---------------------------------------------

        sb.append("<p>Queue payments:</p>");

        sb.append("<table>");
        sb.append("<th><td>Date</td><td>State</td><td>Service</td><td>Account</td><td>Sum</td></th>");

        for(Payment p : queuePayments)
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

}
