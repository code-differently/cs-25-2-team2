package com.cs_25_2_team2.RestaurantManagementApp.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RecipeController {

    // Inject the Spoonacular API key from application.yml
    @Value("${spoonacular.api.key}")
    private String spoonKey;

    // Test endpoint to verify key injection (remove in production)
    @GetMapping("/api/recipes/key")
    public String getKey() {
        return "Spoonacular Key: " + spoonKey;
    }
}
