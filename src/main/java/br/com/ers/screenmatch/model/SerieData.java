package br.com.ers.screenmatch.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record SerieData(@JsonAlias("Title") String serieName,@JsonAlias("totalSeasons") Integer season,
                        @JsonAlias("imdbRating") String rating) {

}
