package main;
import accounts.AccountService;
import org.eclipse.jetty.rewrite.handler.RewriteHandler;
import org.eclipse.jetty.rewrite.handler.RewriteRegexRule;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import servlets.SignInServlet;
import servlets.SignUpServlet;

public class Main {
    public static void main(String[] args) throws Exception {

        AccountService  accountService = new AccountService();
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.addServlet(new ServletHolder(new SignInServlet( accountService)), "/signin");
        context.addServlet(new ServletHolder(new SignUpServlet( accountService)),"/signup");

        RewriteHandler rewriteHandler = new RewriteHandler();
        rewriteHandler.addRule(new RewriteRegexRule("/main", "/index.html"));

        ResourceHandler resource_handler = new ResourceHandler();
        resource_handler.setResourceBase(".idea/templates");

        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{resource_handler, context});
        handlers.setHandlers(new Handler[]{rewriteHandler, resource_handler, context});
        Server server = new Server(8080);
        server.setHandler(rewriteHandler);
        server.setHandler(handlers);

        server.start();
        java.util.logging.Logger.getGlobal().info("Server started");
        server.join();
    }
}