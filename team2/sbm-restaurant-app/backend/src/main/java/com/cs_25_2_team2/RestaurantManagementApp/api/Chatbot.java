package com.cs_25_2_team2.RestaurantManagementApp.api;

import java.util.List;
import java.util.Map;

public class Chatbot {

    // Define the menu items
    private static final List<Map<String, Object>> MENU_ITEMS = List.of(
        Map.of(
            "id", 1,
            "name", "Texas Loaded Baked Potato",
            "category", "Main",
            "price", 7.99,
            "calories", "650 cal",
            "image", "/images/Texasloadedbakedpotato.png",
            "toppings", List.of(
                Map.of("name", "Cheese", "image", "/images/toppings/cheese.png"),
                Map.of("name", "Bacon", "image", "/images/toppings/bacon.webp"),
                Map.of("name", "Sour Cream", "image", "/images/toppings/sourcream.png")
            )
        ),
        Map.of(
            "id", 2,
            "name", "Aloo Tikki Chaat",
            "category", "Main",
            "price", 14.99,
            "calories", "300 cal",
            "image", "/images/Aloo Tikki.png",
            "toppings", List.of(
                Map.of("name", "Cheese", "image", "/images/toppings/cheese.png"),
                Map.of("name", "Chicken", "image", "/images/toppings/chicken.jpg"),
                Map.of("name", "Pico de Gallo", "image", "/images/toppings/picodegallo.png")
            )
        )
        // ... Add other menu items here ...
    );
    
    // Provide public static access to menu items
    public static List<Map<String, Object>> getMenuItems() {
        return MENU_ITEMS;
    }

    private String callOpenAiApi(String prompt) {
        // Implementation for calling OpenAI API
        return "Response from OpenAI API";
    }
}