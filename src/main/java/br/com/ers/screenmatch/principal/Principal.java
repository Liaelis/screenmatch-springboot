package br.com.ers.screenmatch.principal;

import br.com.ers.screenmatch.model.Episode;
import br.com.ers.screenmatch.model.EpisodeData;
import br.com.ers.screenmatch.model.SeasonData;
import br.com.ers.screenmatch.model.SerieData;
import br.com.ers.screenmatch.service.APIConsumer;
import br.com.ers.screenmatch.service.DataConvert;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

@Component
public class Principal {

    @Value("${api.address}")
    private String address;
    @Value("${api.key}")
    private String key;
    private APIConsumer callApi = new APIConsumer();
    private Scanner reader = new Scanner(System.in);
    private DataConvert converter = new DataConvert();


    public void showMenu() {

        System.out.println("Digite o nome da serie");
        var serieName = reader.nextLine();
        serieName = serieName.replace(" ", "+");
        var json = callApi.getData(address + serieName + key);
        SerieData data = converter.obtainData(json, SerieData.class);
        System.out.println(data);

        List<SeasonData> seasons = new ArrayList<>();

        for (int i = 1; i <= data.season(); i++) {
            json = callApi.getData(address + serieName.replace(" ", "+") + "&season=" + i + key);
            SeasonData seasonData = converter.obtainData(json, SeasonData.class);
            seasons.add(seasonData);
        }
        seasons.forEach(t -> t.episodes().forEach(e -> System.out.println(e.Title())));
        List<EpisodeData> episodes = seasons.stream().flatMap(t -> t.episodes().stream())
                .collect(Collectors.toList());

        episodes.stream().filter( e -> !e.rating().equalsIgnoreCase("N/A"))
                .sorted(Comparator.comparing(EpisodeData::rating).reversed())
                .limit(5)
                .forEach(System.out::println);

        List<Episode> processedEpisodes = seasons.stream()
                .flatMap(t -> t.episodes().stream()
                .map(d -> new Episode(t.seasonNumber(),d))).collect(Collectors.toList());

        processedEpisodes.forEach(System.out::println);

        System.out.println(" A partir de que ano vc deseja ver os episodios");
        Integer year = Integer.valueOf(reader.nextLine());
        reader.nextLine();
        LocalDate findDate = LocalDate.of(year,1,1);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        processedEpisodes.stream().filter(e -> e.getReleaseDate() != null &&
                e.getReleaseDate().isAfter(findDate)).forEach( e -> System.out.println(
                      "Temporada "+ e.getSeasonNumber()+
                       " Episodio "+ e.getTitle()+
                              " Data de Lançamento "+e.getReleaseDate().format(formatter)
        ));

    }
}
