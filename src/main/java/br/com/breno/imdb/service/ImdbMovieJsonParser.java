package br.com.breno.imdb.service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import br.com.breno.imdb.model.Movie;

public class ImdbMovieJsonParser implements JsonParser {

    private String json;

    public ImdbMovieJsonParser(String json) {
        this.json = json;
    }

    public List<Movie> parse(){
        String[] moviesArray = parseJsonMovies();
        List<Movie> movies = new ArrayList<>(moviesArray.length);
        List<String> titles = parseAttribute(moviesArray, 3);
        List<String> urlImages = parseAttribute(moviesArray, 5);
        List<String> imdbRatings = parseAttribute(moviesArray, 7);
        List<String> years = parseAttribute(moviesArray, 4);
        for (int i = 0; i < moviesArray.length; i++) {
            movies.add(new Movie(
                    titles.get(i),
                    years.get(i),
                    urlImages.get(i),
                    imdbRatings.get(i))
            );
        }
        return movies;
    }

    private String[] parseJsonMovies() {
        Matcher matcher = Pattern.compile(".*\\[(.*)\\.*").matcher(this.json);

        if (!matcher.matches()) {
            throw new IllegalArgumentException("no match in " + this.json);
        }

        String[] moviesArray = matcher.group(1).split(",\\{");

        moviesArray[0] = moviesArray[0].substring(1);
        int last = moviesArray.length - 1;
        String lastString = moviesArray[last];
        moviesArray[last] = lastString.substring(0, lastString.length() - 1);
        return moviesArray;
    }

    private List<String> parseAttribute(String[] moviesArray, int pos) {
        return Stream.of(moviesArray).map(e -> e.split("\",\"")[pos]).map(e -> e.split(":\"")[1])
                .map(e -> e.replaceAll("\"", "")).collect(Collectors.toList());
    }
}
