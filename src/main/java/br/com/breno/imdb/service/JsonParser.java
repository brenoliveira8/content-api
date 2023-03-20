package br.com.breno.imdb.service;

import br.com.breno.imdb.model.Content;

import java.util.List;

public interface JsonParser {
    List<? extends Content> parse();
}
