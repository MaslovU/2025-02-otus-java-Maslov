package ru.otus.server;

import com.google.gson.Gson;
import org.eclipse.jetty.ee10.servlet.FilterHolder;
import org.eclipse.jetty.ee10.servlet.ServletContextHandler;
import org.eclipse.jetty.ee10.servlet.ServletHolder;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ResourceHandler;
import ru.otus.crm.service.DBServiceClient;
import ru.otus.helpers.FileSystemHelper;
import ru.otus.services.TemplateProcessor;
import ru.otus.services.UserAuthService;
import ru.otus.servlet.AuthorizationFilter;
import ru.otus.servlet.ClientsApiServlet;
import ru.otus.servlet.ClientsServlet;
import ru.otus.servlet.LoginServlet;

import java.util.Arrays;

public class UsersWebServerWithFilterBasedSecurity implements UsersWebServer {

    private final UserAuthService authService;
    private final TemplateProcessor templateProcessor;
    private final Server server;
    private final Gson gson;
    private final DBServiceClient dbServiceClient;

    private static final String CLIENTS_PAGE = "/clients";
    private static final String START_WELCOME_PAGE = "index.html";
    private static final String COMMON_RESOURCE_DIR = "static";
    private static final String CLIENTS_API = "/api/clients/*";

    public UsersWebServerWithFilterBasedSecurity(
            int port, UserAuthService authService, Gson gson,
            TemplateProcessor templateProcessor,
            DBServiceClient dbServiceClient) {
        this.authService = authService;
        this.templateProcessor = templateProcessor;
        server = new Server(port);
        this.gson = gson;
        this.dbServiceClient = dbServiceClient;
    }

    @Override
    public void start() throws Exception {
        if (server.getHandlers().isEmpty()) {
            initContext();
        }
        server.start();
    }

    @Override
    public void join() throws Exception {
        server.join();
    }

    @Override
    public void stop() throws Exception {
        server.stop();
    }

    protected Handler applySecurity(ServletContextHandler servletContextHandler, String... paths) {
        servletContextHandler.addServlet(new ServletHolder(
                new LoginServlet(templateProcessor, authService)), "/login");
        AuthorizationFilter authorizationFilter = new AuthorizationFilter();
        Arrays.stream(paths)
                .forEachOrdered(
                        path -> servletContextHandler.addFilter(
                                new FilterHolder(authorizationFilter), path, null));
        return servletContextHandler;
    }

    private void initContext() {
        ResourceHandler resourceHandler = createResourceHandler();
        ServletContextHandler servletContextHandler = createServletContextHandler();

        Handler.Sequence sequence = new Handler.Sequence();
        sequence.addHandler(resourceHandler);
        sequence.addHandler(applySecurity(servletContextHandler, CLIENTS_PAGE));

        server.setHandler(sequence);
    }

    private ResourceHandler createResourceHandler() {
        ResourceHandler resourceHandler = new ResourceHandler();

        resourceHandler.setDirAllowed(false);
        resourceHandler.setWelcomeFiles(START_WELCOME_PAGE);
        resourceHandler.setBaseResourceAsString(
                FileSystemHelper.localFileNameOrResourceNameToFullPath(COMMON_RESOURCE_DIR));

        return resourceHandler;
    }

    private ServletContextHandler createServletContextHandler() {
        ServletContextHandler servletContextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);

        servletContextHandler.addServlet(new ServletHolder(
                new ClientsServlet(templateProcessor, dbServiceClient)), "/clients/*");
        servletContextHandler.addServlet(new ServletHolder(new ClientsApiServlet(dbServiceClient, gson)), CLIENTS_API);

        return servletContextHandler;
    }
}
