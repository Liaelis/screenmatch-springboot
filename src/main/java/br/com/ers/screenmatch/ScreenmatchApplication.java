package br.com.ers.screenmatch;

import br.com.ers.screenmatch.model.EpisodeData;
import br.com.ers.screenmatch.model.SeasonData;
import br.com.ers.screenmatch.model.SerieData;
import br.com.ers.screenmatch.principal.Principal;
import br.com.ers.screenmatch.service.APIConsumer;
import br.com.ers.screenmatch.service.DataConvert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class ScreenmatchApplication implements CommandLineRunner {
	@Autowired
	Principal principal;
	public static void main(String[] args) {
		SpringApplication.run(ScreenmatchApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

	principal.showMenu();
	}
}
