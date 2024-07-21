package br.com.ers.screenmatch;

import br.com.ers.screenmatch.model.EpisodeData;
import br.com.ers.screenmatch.model.SerieData;
import br.com.ers.screenmatch.service.APIConsumer;
import br.com.ers.screenmatch.service.DataConvert;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ScreenmatchApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ScreenmatchApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println("First project  without web");
		var callApi = new APIConsumer();
		var json = callApi.getData("https://www.omdbapi.com/?t=gilmore+girls&apikey=6585022c");
		System.out.println(json);
		DataConvert converter = new DataConvert();
		SerieData data = converter.obtainData(json,SerieData.class);
		System.out.println("#############");
		System.out.println(data);
		json = callApi.getData("https://omdbapi.com/?t=gilmore+girls&season=1&episode=2&apikey=6585022c");
		EpisodeData episodes = converter.obtainData(json,EpisodeData.class);
		System.out.println(episodes);
	}
}
