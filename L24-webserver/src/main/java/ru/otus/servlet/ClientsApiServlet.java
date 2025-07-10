package ru.otus.servlet;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.otus.crm.model.Address;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.Phone;
import ru.otus.crm.service.DBServiceClient;
import ru.otus.model.ClientRequest;

import java.io.IOException;
import java.util.List;

@SuppressWarnings({"java:S1989"})
public class ClientsApiServlet extends HttpServlet {
    private final DBServiceClient dbServiceClient;
    private final transient Gson gson;

    public ClientsApiServlet(DBServiceClient dbServiceClient, Gson gson) {
        this.dbServiceClient = dbServiceClient;
        this.gson = gson;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final Client client = extractClient(request);
        final Client newClient = dbServiceClient.saveClient(client);

        response.setContentType("application/json;charset=UTF-8");

        final ServletOutputStream out = response.getOutputStream();
        out.print(gson.toJson(newClient.getId()));
    }

    private static Client extractClient(HttpServletRequest request) throws IOException {
        final ObjectMapper mapper = new ObjectMapper();
        final ClientRequest clientRequest = mapper.readValue(request.getInputStream(), ClientRequest.class);

        final String name = clientRequest.name();
        final String street = clientRequest.address();
        final String number = clientRequest.phone();

        final Address address = new Address(null, street);
        final Phone phone = new Phone(null, number);
        final Client newClient = new Client(null, name, address, List.of(phone));

        return newClient;
    }
}
