import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

/**
 * Created by Admin on 24.04.2016.
 */
public class Main
{
    public static void main(String[] args) throws Exception {
        Server server = new Server(8080);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        server.setHandler(context);

        context.addServlet(new ServletHolder(new AuthServlet()), "/auth");
        context.addServlet(new ServletHolder(new AccountsServlet()), "/accounts");
        context.addServlet(new ServletHolder(new LimitsServlet()), "/limits");
        context.addServlet(new ServletHolder(new PaymentsServlet()), "/payments");

        server.start();
        server.join();
    }
}
