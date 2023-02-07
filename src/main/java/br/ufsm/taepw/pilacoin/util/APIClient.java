package br.ufsm.taepw.pilacoin.util;

import br.ufsm.taepw.pilacoin.model.Carteira;
import br.ufsm.taepw.pilacoin.model.PilaCoin;
import br.ufsm.taepw.pilacoin.service.Mineracao;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import java.math.BigInteger;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class APIClient {
    HttpRequest request;
    HttpResponse<String> response;
    HttpClient client = HttpClient.newHttpClient();
    String CT = "Content-Type";
    String AJ = "application/json";

    @SneakyThrows
    public BigInteger getDificuldade() {
        request = HttpRequest.newBuilder().GET().uri(URI.create(Mineracao.API_URL + "dificuldade/")).build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String dificuldade = response.body();
        dificuldade = dificuldade.replace("{\"dificuldade\":\"", "").replace("\"}", "");
        return new BigInteger(dificuldade, 16).abs();
    }

    @SneakyThrows
    public void enviaParaValidacao(String json) {
        HttpRequest.BodyPublisher bodyPublisher = HttpRequest.BodyPublishers.ofString(json);
        String uriString = Mineracao.API_URL + "pilacoin/";
        request = HttpRequest.newBuilder().POST(bodyPublisher).uri(URI.create(uriString)).setHeader(CT, AJ).build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        PilaCoin pilaCoin = new ObjectMapper().readValue(response.body(), PilaCoin.class);
        System.out.println(pilaCoin.getStatus());
    }

    @SneakyThrows
    public boolean usuarioExists(String publicKey) {
        HttpRequest.BodyPublisher bodyPublisher = HttpRequest.BodyPublishers.ofString(publicKey);
        String uriString = Mineracao.API_URL + "usuario/findByChave/";
        request = HttpRequest.newBuilder().POST(bodyPublisher).uri(URI.create(uriString)).setHeader(CT, AJ).build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.statusCode() == 200 || response.statusCode() == 201;
    }

    @SneakyThrows
    public boolean novoUsuario(Carteira carteira) {
        String bodyString = "{\"chavePublica\":\"" + carteira.getChavePublica() + "\"," +
                "\"nome\":\"" + carteira.getUsuario() + "\"}";
        HttpRequest.BodyPublisher bodyPublisher = HttpRequest.BodyPublishers.ofString(bodyString);
        String uriString = Mineracao.API_URL + "usuario/";
        request = HttpRequest.newBuilder().POST(bodyPublisher).uri(URI.create(uriString)).setHeader(CT, AJ).build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.statusCode() == 200 || response.statusCode() == 201;
    }
}

