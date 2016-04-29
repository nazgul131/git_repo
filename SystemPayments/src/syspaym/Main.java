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
import java.util.*;

import sun.util.resources.CalendarData_th;
import syspaym.contracts.Account;
import syspaym.contracts.Client;
import syspaym.contracts.limits.ILimit;
import syspaym.contracts.Payment;
import syspaym.contracts.Service;
import syspaym.servlets.*;
import syspaym.utils.DateHelper;
import syspaym.utils.Sequence;
import syspaym.utils.Time;

/**
 * Created by Admin on 24.04.2016.
 */
public class Main
{
    public static void main(String[] args) throws Exception {
        ArrayDeque<Payment> queuePayments = new ArrayDeque<Payment>();
        ArrayList<Payment> historyPayments = new ArrayList<Payment>();

        Map<String, Client> clients = new HashMap<String, Client>();
        ArrayList<Service> services = new ArrayList<Service>();

        ArrayList<ILimit> limits = new ArrayList<ILimit>();

        createTestData(clients, services);

        PaymentsHandler ph = new PaymentsHandler(queuePayments, historyPayments, limits);
        Thread phThread = new Thread(ph);
        phThread.start();

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.addServlet(new ServletHolder(new AuthServlet(clients)), "/auth");
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

    /*
    Создание локальных объектов для тестов
     */
    private static void createTestData(Map<String, Client> clients, ArrayList<Service> services)
    {
        Random r = new Random();

        for(int i = 0; i < 4; i++) {

            // добавление нового клиента
            Long id = Sequence.getNextId();
            String name = "test"+id.toString();

            Client client = new Client(name, name, name);
            client.Id = id;

            Account acc = new Account("4081781000000000000"+id.toString(), client);
            acc.kt(r.nextDouble()*1000);
            client.Accounts.add(acc);
            acc = new Account("4081781000000000001"+id.toString(), client);
            acc.kt(r.nextDouble()*100);
            client.Accounts.add(acc);
            acc = new Account("4081781000000000002"+id.toString(), client);
            acc.kt(r.nextDouble()*1000);
            client.Accounts.add(acc);

            clients.put(client.Login, client);

            name = "service_"+name;
            Service service = new Service(name);
            service.Id = Sequence.getNextId();

            services.add(service);
        }
    }
}
