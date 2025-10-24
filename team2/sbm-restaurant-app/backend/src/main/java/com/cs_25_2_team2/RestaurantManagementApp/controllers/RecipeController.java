package com.cs_25_2_team2.RestaurantManagementApp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cs_25_2_team2.RestaurantManagementApp.services.RecipeService;

import reactor.core.publisher.Mono;

@RestController
public class RecipeController {

    @Value("${spoonacular.api.key}")
    private String spoonKey;

    @Autowired
    private RecipeService recipeService;

    // Test endpoint to verify key injection (remove in production)
    @GetMapping("/api/recipes/key")
    public String getKey() {
        return "Spoonacular Key: " + spoonKey;
    }

    // New endpoint to fetch potato recipes
    @GetMapping("/api/recipes/potato")
    public Mono<String> getPotatoRecipes(@RequestParam(defaultValue = "5") int number) {
        return recipeService.fetchPotatoRecipes(number);
    }
}
