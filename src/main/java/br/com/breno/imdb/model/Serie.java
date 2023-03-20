package br.com.breno.imdb.model;

import java.util.Comparator;

public class Serie implements Content {

    private final String title;
    private final String year;
    private String image;
    private String rating;

    public Serie(String fullTitle, String year, String image, String rating) {
        this.title = fullTitle;
        this.year = year;
        this.image = image;
        this.rating = rating;
    }

    public String getTitle() {
        return this.title;
    }

    public String getYear() {
        return this.year;
    }

    @Override
    public String type() {
        return "Serie";
    }

    public String getUrlImage() {
        return this.image;
    }

    public String getRating() {
        return this.rating;
    }

    @Override
    public String toString() {
        return "title: " + this.getTitle() + " " +
                "year: " + this.getYear() + " " +
                "image path: " + this.getUrlImage() + " " +
                "imDbRating: " + this.getRating();
    }

    @Override
    public int compareTo(Content c) {
        return this.rating.compareTo(c.getRating());
    }
}
