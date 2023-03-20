package br.com.breno.imdb.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ImdbApiClient implements ApiClient{
    private final String apiKey;

    public ImdbApiClient(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getBody() throws IOException, InterruptedException {
        URI apiImdbUri = URI.create("https://imdb-api.com/en/API/Top250Movies/" + this.apiKey);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(apiImdbUri).build();

        return client.send(request, HttpResponse.BodyHandlers.ofString()).body();
    }
}