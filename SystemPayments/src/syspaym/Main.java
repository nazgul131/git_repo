package syspaym;

import org.eclipse.jetty.security.HashLoginService;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

import syspaym.contracts.Client;
import syspaym.contracts.limits.ILimit;
import syspaym.contracts.Payment;
import syspaym.contracts.Service;
import syspaym.servlets.*;
import syspaym.utils.DateHelper;
import syspaym.utils.Time;

/**
 * Created by Admin on 24.04.2016.
 */
public class Main
{
    public static void main(String[] args) throws Exception {
        ArrayDeque<Payment> queuePayments = new ArrayDeque<Payment>();
        ArrayList<Payment> historyPayments = new ArrayList<Payment>();
        ArrayList<Service> services = new ArrayList<Service>();
        ArrayList<ILimit> limits = new ArrayList<ILimit>();

        PaymentsHandler ph = new PaymentsHandler(queuePayments, historyPayments, limits);
        Thread phThread = new Thread(ph);
        phThread.start();

        //Map<String, Client> clients = new Map<String, Client>();
        //createTestClients(clients);

        //System.out.println(DateHelper.getTime("HH:mm:ss"));
        Time t1 = new Time(10, 59);
        Time t2 = new Time(12, 15);
        System.out.println(t1.compareTo(t2).toString());

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.addServlet(new ServletHolder(new AuthServlet()), "/auth");
        context.addServlet(new ServletHolder(new AccountsServlet()), "/accounts");
        context.addServlet(new ServletHolder(new LimitsServlet()), "/limits");
        context.addServlet(new ServletHolder(new PaymentsServlet(queuePayments)), "/payments");

        ResourceHandler resource_handler = new ResourceHandler();
        resource_handler.setDirectoriesListed(true);
        resource_handler.setResourceBase("public_html");

        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{resource_handler, context});

        Server server = new Server(8080);
        server.setHandler(handlers);

        server.start();
        server.join();
    }

    private static void createTestClients(Map<String, Client> clients)
    {
        Client client1 = new Client("test1");
        client1.Login = "test1";
        client1.PsswHash = "test1".hashCode();
        //client1.Accounts.add()

        clients.put(client1.Login, client1);

        Client client2 = new Client("test2");
        client1.Login = "test2";
        client1.PsswHash = "test2".hashCode();
        clients.put(client2.Login, client1);
    }
}
