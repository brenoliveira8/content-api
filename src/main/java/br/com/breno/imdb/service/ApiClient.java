package br.com.breno.imdb.service;

import java.io.IOException;

public interface ApiClient {
    String getBody() throws IOException, InterruptedException;
}
