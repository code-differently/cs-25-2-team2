package com.cs_25_2_team2.RestaurantManagementApp.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

@Service
public class RecipeService {
    private final WebClient.Builder webClientBuilder;

    @Value("${spoonacular.api.key}")
    private String spoonKey;

    public RecipeService(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    public Mono<String> fetchPotatoRecipes(int number) {
        String url = "https://api.spoonacular.com/recipes/complexSearch";
        return webClientBuilder.build()
            .get()
            .uri(uriBuilder -> uriBuilder
                .path(url)
                .queryParam("query", "potato")
                .queryParam("number", number)
                .queryParam("apiKey", spoonKey)
                .build())
            .retrieve()
            .bodyToMono(String.class);
    }
}
