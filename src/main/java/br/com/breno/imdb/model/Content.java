package br.com.breno.imdb.model;

public interface Content extends Comparable<Content> {
    String getTitle();
    String getUrlImage();
    String getRating();
    String getYear();
    String type();
}
