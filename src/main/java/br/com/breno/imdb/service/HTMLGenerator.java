package br.com.breno.imdb.service;

import br.com.breno.imdb.model.Content;

import java.io.PrintWriter;
import java.util.List;

public class HTMLGenerator {

    PrintWriter writer;

    public HTMLGenerator(PrintWriter writer) {
        this.writer = writer;
    }

    public void generate(List<? extends Content> contents) {
        String title = """
                <title>
                        Movies and Series
                </title>
                """;
        String head = """
                <head>
                    <meta charset="utf-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
                    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.0.0/dist/css/bootstrap.min.css"
                        + "integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
                        </head>
                """;

        String divTemplate = """
                <div class="card text-white bg-dark mb-3" style="max-width: 18rem;">
                    <h4 class="card-header"> %s </h4>
                    <div class="card-body">
                        <p class ="card-text mt-1"> %s </p>
                        <img class="card-img" src=" %s " alt=" %s ">
                        <p class="card-text mt-2">Nota: %s - Ano: %s</p>
                    </div>
                </div>
                """;
        this.writer.println(title);
        this.writer.println(head);
        for (Content content : contents) {
            this.writer.println(String.format(divTemplate, content.getTitle(), content.type(), content.getUrlImage(), "URL da imagem.", content.getRating(), content.getYear()));
        }
    }

}