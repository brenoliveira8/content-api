package br.com.breno.imdb.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class MarvelApiClient implements ApiClient{
    private final String uri;

    public MarvelApiClient(String apiKey, String privateKey) {
        String filter = "titleStartsWith";
        String filterValue = "x-men";
        String timestamp = String.valueOf(System.currentTimeMillis());
        String hash = HashUtils.getHashMd5(timestamp + privateKey + apiKey);
        this.uri = String.format("https://gateway.marvel.com/v1/public/series?%s=%s&ts=%s&apikey=%s&hash=%s", filter, filterValue, timestamp, apiKey, hash);
    }

    @Override
    public String getBody(){
        try {
            URI marvelApiUri = URI.create(this.uri);

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder().uri(marvelApiUri).build();

            return client.send(request, HttpResponse.BodyHandlers.ofString()).body();
        } catch (IOException | InterruptedException ex){
            throw new IllegalStateException(ex);
        }
    }
}
