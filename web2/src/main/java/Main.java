import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import servlet.ApiServlet;
import servlet.LoginServlet;
import servlet.RegistrationServlet;

public class Main {

    public static void main(String[] args) throws Exception {
        ApiServlet apiServlet = new ApiServlet();

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.addServlet(new ServletHolder(apiServlet), "/api/*"); // http://localhost:8080/api
        context.addServlet(new ServletHolder(new LoginServlet()), "/login"); // http://localhost:8080/login
        context.addServlet(new ServletHolder(new RegistrationServlet()), "/register"); // http://localhost:8080/register

        Server server = new Server(8080);
        server.setHandler(context);

        server.start();
        server.join();
    }
}
