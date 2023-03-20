package br.com.breno.imdb.exec;

import br.com.breno.imdb.model.Content;
import br.com.breno.imdb.model.Serie;
import br.com.breno.imdb.service.HTMLGenerator;
import br.com.breno.imdb.service.MarvelApiClient;
import br.com.breno.imdb.service.MarvelSeriesJsonParser;
import br.com.breno.imdb.model.Movie;
import br.com.breno.imdb.service.ImdbApiClient;
import br.com.breno.imdb.service.ImdbMovieJsonParser;
import io.github.cdimascio.dotenv.Dotenv;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class contentGenerator {

    public static void main(String[] args) throws IOException, InterruptedException {

        Dotenv dotenv = Dotenv.load();
        String imdbApiKey = dotenv.get("IMDB_API_KEY");
        String marvelPublicKey = dotenv.get("MARVEL_PUBLIC_KEY");
        String marvelPrivateKey = dotenv.get("MARVEL_PRIVATE_KEY");

        String moviesJson = new ImdbApiClient(imdbApiKey).getBody();
        String seriesJson = new MarvelApiClient(marvelPublicKey, marvelPrivateKey).getBody();

        List<Movie> movies= new ImdbMovieJsonParser(moviesJson).parse();
        List<Serie> series = new MarvelSeriesJsonParser(seriesJson).parse();
        List<? extends Content> contentList = Stream.of(movies, series).flatMap(Collection::stream).collect(Collectors.toList());
        contentList.sort(Comparator.comparing(Content::getTitle));

        PrintWriter moviesWriter = new PrintWriter("movies.html");
        PrintWriter seriesWriter = new PrintWriter("series.html");
        PrintWriter contentWriter = new PrintWriter("content.html");

        new HTMLGenerator(moviesWriter).generate(movies);
        new HTMLGenerator(seriesWriter).generate(series);
        new HTMLGenerator(contentWriter).generate(contentList);

        moviesWriter.close();
        seriesWriter.close();
        contentWriter.close();
    }
}