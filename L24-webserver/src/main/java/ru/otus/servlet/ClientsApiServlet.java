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

public class ClientsApiServlet extends HttpServlet {

    private final transient DBServiceClient dbServiceClient;
    private final transient Gson gson;

    public ClientsApiServlet(DBServiceClient dbServiceClient, Gson gson) {
        this.dbServiceClient = dbServiceClient;
        this.gson = gson;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final Client client = extractClient(req);
        final Client newClient = dbServiceClient.saveClient(client);

        resp.setContentType("application/json; charset=UTF-8");

        final ServletOutputStream outputStream = resp.getOutputStream();
        outputStream.print(gson.toJson(newClient.getId()));
    }

    private static Client extractClient(HttpServletRequest request) throws IOException {
        final ObjectMapper mapper = new ObjectMapper();
        final ClientRequest clientRequest = mapper.readValue(request.getInputStream(), ClientRequest.class);

        final String name = clientRequest.name();
        final String addressStr = clientRequest.address();
        final String phoneStr = clientRequest.phone();

        final Address address = new Address(null, addressStr);
        final Phone phone = new Phone(null, phoneStr);

        return new Client(null, name, address, List.of(phone));
    }
}
