package ru.otus.server;

import com.google.gson.Gson;
import org.eclipse.jetty.ee10.servlet.FilterHolder;
import org.eclipse.jetty.ee10.servlet.ServletContextHandler;
import org.eclipse.jetty.ee10.servlet.ServletHolder;
import org.eclipse.jetty.server.Request;
import ru.otus.dao.UserDao;
import ru.otus.services.TemplateProcessor;
import ru.otus.services.UserAuthService;

import java.util.Arrays;

public class UsersWebServerWithFilterBasedSecurity implements UsersWebServer {
    private final UserAuthService authService;

    public UsersWebServerWithFilterBasedSecurity(
            int port, UserAuthService authService, UserDao userDao, Gson gson, TemplateProcessor templateProcessor) {
        this.authService = authService;
    }

    @Override
    protected Request.Handler applySecurity(ServletContextHandler servletContextHandler, String... paths) {
        servletContextHandler.addServlet(new ServletHolder(new LoginServlet(templateProcessor, authService)), "/login");
        AuthorizationFilter authorizationFilter = new AuthorizationFilter();
        Arrays.stream(paths)
                .forEachOrdered(
                        path -> servletContextHandler.addFilter(new FilterHolder(authorizationFilter), path, null));
        return servletContextHandler;
    }
}
