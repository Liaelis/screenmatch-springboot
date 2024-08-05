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
import java.util.*;
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

        System.out.println("Write the serie name");
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
       // seasons.forEach(t -> t.episodes().forEach(e -> System.out.println(e.Title())));
        List<EpisodeData> episodes = seasons.stream().flatMap(t -> t.episodes().stream())
                .collect(Collectors.toList());

        System.out.println("\n Top 10 Movies");
        episodes.stream().filter(e -> !e.rating().equalsIgnoreCase("N/A"))
                .peek(e-> System.out.println("First filter "+e))
                .sorted(Comparator.comparing(EpisodeData::rating).reversed())
                .peek(e-> System.out.println("Ordenation "+e))
                .limit(10)
                .peek(e-> System.out.println("Limit "+e))
                .map(e -> e.Title().toUpperCase())
                .peek(e-> System.out.println("Map "+e))
                .forEach(System.out::println);

        List<Episode> processedEpisodes = seasons.stream()
                .flatMap(t -> t.episodes().stream()
                .map(d -> new Episode(t.seasonNumber(),d))).collect(Collectors.toList());

        processedEpisodes.forEach(System.out::println);

       /* System.out.println("write the episode name");
        var titlePart = reader.nextLine();
        Optional<Episode> firstEpisode = processedEpisodes.stream()
                .filter(e -> e.getTitle().toUpperCase().contains(titlePart.toUpperCase()))
                .findFirst();
        if(firstEpisode.isPresent()) {
            System.out.println("Episode was found");
            System.out.println("Season "+firstEpisode.get().getSeasonNumber());
        }else {
            System.out.println("Episode not found");
        }*/
        /*System.out.println(" A partir de que ano vc deseja ver os episodios");
        Integer year = Integer.valueOf(reader.nextLine());
        reader.nextLine();
        LocalDate findDate = LocalDate.of(year,1,1);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        processedEpisodes.stream().filter(e -> e.getReleaseDate() != null &&
                e.getReleaseDate().isAfter(findDate)).forEach( e -> System.out.println(
                      "Temporada "+ e.getSeasonNumber()+
                       " Episodio "+ e.getTitle()+
                              " Data de Lançamento "+e.getReleaseDate().format(formatter)
        ));*/

        Map<Integer, Double> seasonRating = processedEpisodes.stream()
                .filter(e -> e.getRating()>0.0)
                .collect(Collectors.groupingBy(Episode::getSeasonNumber,
                        Collectors.averagingDouble(Episode::getRating)));
        System.out.println(seasonRating);


        DoubleSummaryStatistics est = processedEpisodes.stream()
                .filter(e -> e.getRating()>0.0)
                .collect(Collectors.summarizingDouble(Episode::getRating));
        System.out.println("Average "+ est.getAverage());
        System.out.println("Best Episode Rating: " + est.getMax());
        System.out.println("Worst Episode Rating: " + est.getMin());
        System.out.println("Amount of Episode Rating: " + est.getCount());



    }
}
