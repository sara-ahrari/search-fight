package com.searchfight.search;

import com.searchfight.search.model.SearchTermResult;
import com.searchfight.search.service.SearchService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import static java.util.Arrays.asList;


@SpringBootApplication
public class SearchApplication implements CommandLineRunner {

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    @Autowired
    private SearchService searchService;

    public static void main(String[] args) {
        SpringApplication.run(SearchApplication.class, args);
    }

    @Override
    public void run(String[] args) {

        var terms =new HashSet<>(asList(args));
        if (terms.size() > 1) {
            System.out.println("\n" + "---------------------WELCOME! HERE ARE THE RESULTS OF YOUR FIGHT------------------" + "\n");
            var results = searchService.search(terms);
            searchService.getEnginesWinner(results);
            System.out.println("Total Winner: " + searchService.getTotalWinner(results));
        } else {
            System.out.println("\n" + "OBS! You have to run the program with at least two distinct words");
        }
    }
}
