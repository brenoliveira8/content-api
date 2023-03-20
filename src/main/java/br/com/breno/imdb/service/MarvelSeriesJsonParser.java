package br.com.breno.imdb.service;

import br.com.breno.imdb.model.Serie;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MarvelSeriesJsonParser implements JsonParser {
    private String json;

    public MarvelSeriesJsonParser(String json) {
        this.json = json;
    }

    public List<Serie> parse() {
        String[] seriesArray = parseJsonSeries();
        List<Serie> series = new ArrayList<>(seriesArray.length);
        for (String jsonSerie : seriesArray) {
            series.add(new Serie(
                    parseAttribute(jsonSerie, "title"),
                    parseAttribute(jsonSerie, "startYear"),
                    parseThumbnailAttribute(jsonSerie),
                    parseAttribute(jsonSerie, "rating")
            ));
        }
        return series;
    }

    private String parseThumbnailAttribute(String jsonSerie) {
        Pattern thumbnailPattern = Pattern.compile("\"thumbnail\":\\{\"path\":");
        Matcher thumbnailMatcher = thumbnailPattern.matcher(jsonSerie);

        if (!thumbnailMatcher.find()){
            throw new IllegalStateException("Thumbnail not found");
        }

        int posIniThumbnailAddress = thumbnailMatcher.end();
        String thumbnailAddressStart = jsonSerie.substring(posIniThumbnailAddress);

        Pattern extensionPattern = Pattern.compile("\",\"extension\":\"");
        Matcher extensionMatcher = extensionPattern.matcher(thumbnailAddressStart);

        if(!extensionMatcher.find()){
            throw new IllegalStateException("Thumbnail extension not found");
        }
        int posFinalThumbnailAddress = extensionMatcher.start();
        String thumbnailAddress = thumbnailAddressStart.substring(0, posFinalThumbnailAddress);

        int posIniThumbnailExtension = extensionMatcher.end();
        String thumbnailExtension = thumbnailAddressStart.substring(posIniThumbnailExtension, posIniThumbnailExtension + 3);

        return cleanUp(thumbnailAddress) + "." + thumbnailExtension;
    }

    static Pattern BEGIN_ARRAY = Pattern.compile(".*\"results\":\\[\\{\"id\"");
    static Pattern END_ARRAY = Pattern.compile(".*}}");

    private String[] parseJsonSeries() {

        Matcher matcher = BEGIN_ARRAY.matcher(this.json);
        matcher.find();
        int begin = matcher.end();

        matcher = END_ARRAY.matcher(this.json);
        matcher.find();
        int end = matcher.end();

        String jsonStringSeries = this.json.substring(begin, end);

        return jsonStringSeries.split(",\\{\"id\"");
    }

    private String parseAttribute(String jsonSerie, String attributeName) {

        String attributeValue;

        int posIniAttribute = findInitialPositionOfAttribute(jsonSerie, attributeName);
        attributeValue = jsonSerie.substring(posIniAttribute);

        int posFinalAttribute = findFinalPositionOfAttribute(attributeValue);
        attributeValue = attributeValue.substring(0, posFinalAttribute);

        attributeValue = cleanUp(attributeValue);

        if (attributeValue.isEmpty()) {
            attributeValue = "Not rated";
        }

        return attributeValue;
    }

    private String cleanUp(String attributeValue) {
        if (attributeValue.startsWith("\"")) {
            attributeValue = attributeValue.substring(1);
        }
        if (attributeValue.endsWith(",")) {
            attributeValue = attributeValue.substring(0, attributeValue.length() - 1);
        }
        if (attributeValue.endsWith("\"")) {
            attributeValue = attributeValue.substring(0, attributeValue.length() - 1);
        }

        return attributeValue.trim();
    }

    private int findFinalPositionOfAttribute(String jsonSerie) {
        Pattern finalPattern = Pattern.compile(",");
        Matcher finalMatcher = finalPattern.matcher(jsonSerie);

        if (!finalMatcher.find()) {
            throw new IllegalStateException();
        }

        return finalMatcher.end();
    }

    private int findInitialPositionOfAttribute(String jsonSerie, String attributeName) {
        Pattern beginPattern = Pattern.compile("\"" + attributeName + "\":");
        Matcher beginMatcher = beginPattern.matcher(jsonSerie);

        if (!beginMatcher.find()) {
            throw new IllegalStateException(attributeName + " not found");
        }

        return beginMatcher.end();
    }
}
