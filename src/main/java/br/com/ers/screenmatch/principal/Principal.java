package br.com.ers.screenmatch.principal;

import br.com.ers.screenmatch.model.SeasonData;
import br.com.ers.screenmatch.model.SerieData;
import br.com.ers.screenmatch.service.APIConsumer;
import br.com.ers.screenmatch.service.DataConvert;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Component
public class Principal {

    @Value("${api.address}")
    private  String address ;
    @Value("${api.key}")
    private   String key ;
    private APIConsumer callApi = new APIConsumer();
    private Scanner reader = new Scanner(System.in);
    private DataConvert converter = new DataConvert();


        public void showMenu(){

        System.out.println("Digite o nome da serie");
        var serieName = reader.nextLine();
        serieName = serieName.replace(" ", "+");
        var json = callApi.getData(address + serieName + key);
        SerieData data = converter.obtainData(json,SerieData.class);
        System.out.println(data);

            List<SeasonData> seasons =  new ArrayList<>();

            for( int i=1 ; i<= data.season(); i++){
                json = callApi.getData(address+serieName.replace(" ", "+")+"&season=" + i + key);
                SeasonData seasonData = converter.obtainData(json,SeasonData.class);
                seasons.add(seasonData);
            }
            seasons.forEach(System.out::println);
    }
}
