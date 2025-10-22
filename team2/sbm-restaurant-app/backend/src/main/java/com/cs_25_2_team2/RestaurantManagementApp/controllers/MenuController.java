package com.cs_25_2_team2.RestaurantManagementApp.controllers;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cs_25_2_team2.RestaurantManagementApp.Ingredient;
import com.cs_25_2_team2.RestaurantManagementApp.MenuItem;
import com.cs_25_2_team2.RestaurantManagementApp.services.MenuService;

/**
 * REST Controller for managing restaurant menu.
 * Now properly uses the existing Menu and MenuItem backend classes via MenuService.
 * Maps frontend menu data structure to backend MenuItem objects.
 * 
 * Frontend Structure: { id, name, category, price, calories, toppings[] }
 * Backend Structure: { dishId, dishName, price, cookedType, potatoType, ingredients[], availability }
 * 
 * Base URL: /api/menu
 * CORS enabled for: http://localhost:3000 (Next.js development server)
 * 
 * @author Team 2
 * @version 1.0
 */
@RestController
@RequestMapping("/api/menu")
@CrossOrigin(origins = "http://localhost:3000")
public class MenuController {
    
    @Autowired
    private MenuService menuService;
    
    /**
     * Get all menu items mapped to frontend format
     * Uses MenuService which properly manages Menu and MenuItem classes
     */
    @GetMapping("/menuitems")
    public ResponseEntity<List<Map<String, Object>>> getAllMenuItems() {
        List<MenuItem> items = menuService.getAllMenuItems();
        List<Map<String, Object>> frontendItems = items.stream()
            .map(this::mapToFrontendFormat)
            .collect(Collectors.toList());
        return ResponseEntity.ok(frontendItems);
    }
    
    /**
     * Get specific menu item by ID
     */
    @GetMapping("/menuitems/{id}")
    public ResponseEntity<Map<String, Object>> getMenuItemById(@PathVariable int id) {
        MenuItem item = menuService.getMenuItemById(id);
        if (item == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(mapToFrontendFormat(item));
    }
    
    /**
     * Create new menu item (Admin only)
     * Maps frontend toppings[] to backend ingredients[]
     * Maps frontend name to backend dishName
     */
    @PostMapping("/menuitems")
    public ResponseEntity<Map<String, Object>> createMenuItem(@RequestBody Map<String, Object> frontendItem) {
        try {
            MenuItem newItem = mapToBackendFormat(frontendItem);
            menuService.addMenuItem(newItem);
            return ResponseEntity.status(HttpStatus.CREATED).body(mapToFrontendFormat(newItem));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Update menu item (Admin only)
     */
    @PutMapping("/menuitems/{id}")
    public ResponseEntity<Map<String, Object>> updateMenuItem(@PathVariable int id, @RequestBody Map<String, Object> frontendItem) {
        MenuItem existingItem = menuService.getMenuItemById(id);
        if (existingItem == null) {
            return ResponseEntity.notFound().build();
        }
        
        try {
            // Remove old item and add updated one
            menuService.removeMenuItem(id);
            frontendItem.put("id", id); // Ensure ID is preserved
            MenuItem updatedItem = mapToBackendFormat(frontendItem);
            menuService.addMenuItem(updatedItem);
            return ResponseEntity.ok(mapToFrontendFormat(updatedItem));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Delete menu item (Admin only)
     */
    @DeleteMapping("/menuitems/{id}")
    public ResponseEntity<String> deleteMenuItem(@PathVariable int id) {
        MenuItem item = menuService.getMenuItemById(id);
        if (item == null) {
            return ResponseEntity.notFound().build();
        }
        
        menuService.removeMenuItem(id);
        return ResponseEntity.ok("Menu item deleted successfully");
    }
    
    /**
     * Get menu categories derived from frontend categories
     */
    @GetMapping("/categories")
    public ResponseEntity<List<String>> getCategories() {
        List<String> categories = Arrays.asList("All", "Main", "Side", "Soup");
        return ResponseEntity.ok(categories);
    }
    
    /**
     * Get items by category (frontend categories)
     */
    @GetMapping("/items/category/{category}")
    public ResponseEntity<List<Map<String, Object>>> getItemsByCategory(@PathVariable String category) {
        List<MenuItem> items = menuService.getAllMenuItems();
        List<Map<String, Object>> filteredItems = items.stream()
            .map(this::mapToFrontendFormat)
            .filter(item -> category.equals("All") || category.equals(item.get("category")))
            .collect(Collectors.toList());
        return ResponseEntity.ok(filteredItems);
    }
    
    /**
     * Maps backend MenuItem to frontend format
     * Backend: dishName -> Frontend: name
     * Backend: ingredients[] -> Frontend: toppings[]
     */
    private Map<String, Object> mapToFrontendFormat(MenuItem item) {
        Map<String, Object> frontendItem = new HashMap<>();
        frontendItem.put("id", item.getDishId());
        frontendItem.put("name", item.getDishName()); // dishName -> name
        frontendItem.put("price", item.getPrice());
        frontendItem.put("category", mapCookedTypeToCategory(item.getCookedType()));
        frontendItem.put("calories", estimateCalories(item));
        
        // Map ingredients[] to toppings[]
        List<Map<String, String>> toppings = item.getIngredients().stream()
            .map(ingredient -> {
                Map<String, String> topping = new HashMap<>();
                topping.put("name", ingredient.getIngredientName());
                topping.put("image", mapIngredientToImage(ingredient.getIngredientName()));
                return topping;
            })
            .collect(Collectors.toList());
        frontendItem.put("toppings", toppings);
        
        return frontendItem;
    }
    
    /**
     * Maps frontend format to backend MenuItem
     * Frontend: name -> Backend: dishName
     * Frontend: toppings[] -> Backend: ingredients[]
     * Intelligently determines CookedType based on dish name and category
     */
    private MenuItem mapToBackendFormat(Map<String, Object> frontendItem) {
        int id = (Integer) frontendItem.get("id");
        String name = (String) frontendItem.get("name"); // name -> dishName
        double price = ((Number) frontendItem.get("price")).doubleValue();
        String category = (String) frontendItem.get("category");
        
        // Intelligently determine CookedType based on dish name and category
        MenuItem.CookedType cookedType = determineCookedType(name, category);
        MenuItem.PotatoType potatoType = determinePotatoType(name); // Determine from name
        
        MenuItem item = new MenuItem(id, name, price, cookedType, potatoType, true);
        
        // Map frontend toppings[] to backend ingredients[]
        @SuppressWarnings("unchecked")
        List<Map<String, String>> toppings = (List<Map<String, String>>) frontendItem.get("toppings");
        if (toppings != null) {
            for (Map<String, String> topping : toppings) {
                String toppingName = topping.get("name");
                Ingredient ingredient = new Ingredient(toppingName, true, true, 0.0); // additionalTopping, optional, extraCost
                item.addIngredient(ingredient);
            }
        }
        
        return item;
    }
    
    /**
     * Intelligently determines CookedType based on dish name and category
     */
    private MenuItem.CookedType determineCookedType(String dishName, String category) {
        String nameLower = dishName.toLowerCase();
        
        // Check dish name for cooking method keywords
        if (nameLower.contains("soup")) {
            return MenuItem.CookedType.Soupped;
        } else if (nameLower.contains("baked")) {
            return MenuItem.CookedType.Baked;
        } else if (nameLower.contains("fried") || nameLower.contains("fries")) {
            return MenuItem.CookedType.Fried;
        } else if (nameLower.contains("mashed")) {
            return MenuItem.CookedType.Mashed;
        } else if (nameLower.contains("grilled")) {
            return MenuItem.CookedType.Grilled;
        } else if (nameLower.contains("roasted")) {
            return MenuItem.CookedType.Roasted;
        } else if (nameLower.contains("hash")) {
            return MenuItem.CookedType.Fried; // Hash browns are typically fried
        } else if (nameLower.contains("hasselback")) {
            return MenuItem.CookedType.Baked; // Hasselback is baked
        }
        
        // Fallback based on category only if name doesn't indicate cooking method
        return switch (category) {
            case "Soup" -> MenuItem.CookedType.Soupped;
            case "Main" -> MenuItem.CookedType.Baked;    // Most main dishes are baked
            case "Side" -> MenuItem.CookedType.Mashed;    // Most sides are mashed
            default -> MenuItem.CookedType.Fried;        // Safe default
        };
    }
    
    /**
     * Determines PotatoType based on dish name
     */
    private MenuItem.PotatoType determinePotatoType(String dishName) {
        String nameLower = dishName.toLowerCase();
        
        if (nameLower.contains("sweet")) {
            return MenuItem.PotatoType.JapaneseSweet;
        } else if (nameLower.contains("yukon")) {
            return MenuItem.PotatoType.YukonGold;
        } else if (nameLower.contains("red")) {
            return MenuItem.PotatoType.RedBliss;
        } else if (nameLower.contains("purple")) {
            return MenuItem.PotatoType.PurplePeruvian;
        }
        
        return MenuItem.PotatoType.Russet; // Default - most common
    }
    
        /**
     * Maps backend CookedType to frontend category
     * Only Soupped has a clear mapping to Soup, others use logical defaults
     */
    private String mapCookedTypeToCategory(MenuItem.CookedType cookedType) {
        return switch (cookedType) {
            case Soupped -> "Soup";  // Only clear mapping
            case Baked, Fried, Grilled -> "Main";  // Typically main dishes
            case Scalloped, Boiled, Steamed -> "Side";     // Typically side dishes
            case Mashed, Roasted -> "Side";            // Typically side dishes
            default -> "Main";  // Default to Main instead of Side
        };
    }
    
    /**
     * Estimates calories based on item type and ingredients
     */
    private String estimateCalories(MenuItem item) {
        int baseCalories = switch (item.getCookedType()) {
            case Baked -> 300;
            case Fried -> 400;
            case Soupped -> 250;
            default -> 300;
        };
        
        int toppingCalories = item.getIngredients().size() * 50;
        return (baseCalories + toppingCalories) + " cal";
    }
    
    /**
     * Maps ingredient name to image path for frontend
     */
    private String mapIngredientToImage(String ingredientName) {
        return switch (ingredientName.toLowerCase()) {
            case "cheese" -> "/images/toppings/cheese.png";
            case "bacon" -> "/images/toppings/bacon.webp";
            case "sour cream" -> "/images/toppings/sourcream.png";
            case "chicken" -> "/images/toppings/chicken.jpg";
            case "pico de gallo" -> "/images/toppings/picodegallo.png";
            case "green onions" -> "/images/toppings/greenOnions.webp";
            case "ranch" -> "/images/toppings/ranch.png";
            case "butter" -> "/images/toppings/butter.png";
            case "salt" -> "/images/toppings/salt1.png";
            case "pepper" -> "/images/toppings/blackpepper.jpg";
            case "croutons" -> "/images/toppings/croutons.png";
            case "garlic" -> "/images/toppings/garlic.png";
            default -> "/images/toppings/default.png";
        };
    }
    
    /**
     * Initialize menu with sample data matching frontend menuItems.js
     */
    private void initializeSampleMenu() {
        // Sample menu items that match frontend data structure
        Map<String, Object> item1 = new HashMap<>();
        item1.put("id", 1);
        item1.put("name", "Texas Style Baked Potato");
        item1.put("category", "Main");
        item1.put("price", 12.99);
        item1.put("toppings", Arrays.asList(
            Map.of("name", "Cheese", "image", "/images/toppings/cheese.png"),
            Map.of("name", "Bacon", "image", "/images/toppings/bacon.webp"),
            Map.of("name", "Sour Cream", "image", "/images/toppings/sourcream.png")
        ));
        
        try {
            // TODO: Fix menu initialization - menu variable not in scope
            // menu.addMenuItem(mapToBackendFormat(item1));
            System.out.println("Sample menu initialization skipped - needs proper Menu service integration");
        } catch (Exception e) {
            System.err.println("Error initializing sample menu: " + e.getMessage());
        }
    }
}