package com.cs_25_2_team2.RestaurantManagementApp.services;

import com.cs_25_2_team2.RestaurantManagementApp.Menu;
import com.cs_25_2_team2.RestaurantManagementApp.MenuItem;
import com.cs_25_2_team2.RestaurantManagementApp.Ingredient;
import org.springframework.stereotype.Service;
import java.util.*;

/**
 * Service class for Menu management using the existing Menu and MenuItem backend classes.
 * Provides business logic layer between controllers and domain models.
 * 
 * Uses the existing backend architecture:
 * - Menu class for menu management
 * - MenuItem class with dishId, dishName, price, cookedType, potatoType, ingredients[]
 * - Ingredient class for toppings/ingredients
 * 
 * @author Team 2
 * @version 1.0
 */
@Service
public class MenuService {
    
    private final Menu restaurantMenu;
    
    /**
     * Constructor initializes the restaurant menu with sample data
     * In production, this would load from a database
     */
    public MenuService() {
        this.restaurantMenu = new Menu();
        initializeMenuWithSampleData();
    }
    
    /**
     * Get all available menu items
     */
    public List<MenuItem> getAllMenuItems() {
        return restaurantMenu.getAvailableItems();
    }
    
    /**
     * Get menu item by ID
     */
    public MenuItem getMenuItemById(Long dishId) {
        return restaurantMenu.getItemById(dishId.intValue());
    }
    
    /**
     * Get available menu item (throws exception if unavailable)
     */
    public MenuItem getAvailableMenuItemById(Long dishId) {
        return restaurantMenu.getAvailableItemById(dishId.intValue());
    }
    
    /**
     * Add new menu item to the menu
     */
    public void addMenuItem(MenuItem item) {
        restaurantMenu.addMenuItem(item);
    }
    
    /**
     * Remove menu item from the menu
     */
    public void removeMenuItem(Long dishId) {
        restaurantMenu.removeMenuItem(dishId.intValue());
    }
    
    /**
     * Update menu item availability
     */
    public void updateMenuItemAvailability(Long dishId, boolean availability) {
        MenuItem item = restaurantMenu.getItemById(dishId.intValue());
        if (item != null) {
            item.setAvailability(availability);
        }
    }
    
    /**
     * Get menu items by cooking type
     */
    public List<MenuItem> getItemsByCookingType(MenuItem.CookedType cookingType) {
        return restaurantMenu.getItemsByCookingType(cookingType);
    }
    
    /**
     * Get menu items by potato type
     */
    public List<MenuItem> getItemsByPotatoType(MenuItem.PotatoType potatoType) {
        return restaurantMenu.getItemsByPotatoType(potatoType);
    }
    
    /**
     * Get menu availability status
     */
    public boolean isMenuAvailable() {
        return restaurantMenu.isAvailable();
    }
    
    /**
     * Set menu availability
     */
    public void setMenuAvailability(boolean availability) {
        restaurantMenu.setAvailability(availability);
    }
    
    /**
     * Get menu item count
     */
    public int getMenuItemCount() {
        return restaurantMenu.getItemCount();
    }
    
    /**
     * Get menu last updated date
     */
    public Date getMenuLastUpdated() {
        return restaurantMenu.getLastUpdated();
    }
    
    /**
     * Initialize menu with sample data that matches frontend expectations
     * This replicates the frontend menuItems.js data using backend classes
     */
    private void initializeMenuWithSampleData() {
        
        // 1. Texas Style Baked Potato
        MenuItem texasStyle = new MenuItem(1, "Texas Style Baked Potato", 12.99, 
            MenuItem.CookedType.Baked, MenuItem.PotatoType.Russet, true);
        texasStyle.addIngredient(new Ingredient("Cheese", true, true, 0.0));
        texasStyle.addIngredient(new Ingredient("Bacon", false, true, 0.0));
        texasStyle.addIngredient(new Ingredient("Sour Cream", true, true, 0.0));
        restaurantMenu.addMenuItem(texasStyle);
        
        // 2. Pollo Mexicano Baked Potato
        MenuItem polloMexicano = new MenuItem(2, "Pollo Mexicano Baked Potato", 13.99,
            MenuItem.CookedType.Baked, MenuItem.PotatoType.Russet, true);
        polloMexicano.addIngredient(new Ingredient("Cheese", true, true, 0.0));
        polloMexicano.addIngredient(new Ingredient("Chicken", false, true, 0.0));
        polloMexicano.addIngredient(new Ingredient("Pico de Gallo", true, true, 0.0));
        restaurantMenu.addMenuItem(polloMexicano);
        
        // 3. CB Ranch Baked Potato
        MenuItem cbRanch = new MenuItem(3, "CB Ranch Baked Potato", 10.99,
            MenuItem.CookedType.Baked, MenuItem.PotatoType.Russet, true);
        cbRanch.addIngredient(new Ingredient("Cheese", true, true, 0.0));
        cbRanch.addIngredient(new Ingredient("Green Onions", true, true, 0.0));
        cbRanch.addIngredient(new Ingredient("Ranch", true, true, 0.0));
        restaurantMenu.addMenuItem(cbRanch);
        
        // 4. Loaded Baked Potato Soup
        MenuItem loadedSoup = new MenuItem(4, "Loaded Baked Potato Soup", 5.99,
            MenuItem.CookedType.Soupped, MenuItem.PotatoType.Russet, true);
        loadedSoup.addIngredient(new Ingredient("Cheese", true, true, 0.0));
        loadedSoup.addIngredient(new Ingredient("Green Onions", true, true, 0.0));
        loadedSoup.addIngredient(new Ingredient("Bacon", false, true, 0.0));
        restaurantMenu.addMenuItem(loadedSoup);
        
        // 5. Plain Jane Baked Potato
        MenuItem plainJane = new MenuItem(5, "Plain Jane Baked Potato", 9.99,
            MenuItem.CookedType.Baked, MenuItem.PotatoType.Russet, true);
        plainJane.addIngredient(new Ingredient("Sour Cream", true, true, 0.0));
        plainJane.addIngredient(new Ingredient("Green Onions", true, true, 0.0));
        plainJane.addIngredient(new Ingredient("Bacon", false, true, 0.0));
        restaurantMenu.addMenuItem(plainJane);
        
        // 6. Loaded Fries
        MenuItem loadedFries = new MenuItem(6, "Loaded Fries", 6.99,
            MenuItem.CookedType.Fried, MenuItem.PotatoType.Russet, true);
        loadedFries.addIngredient(new Ingredient("Cheese", true, true, 0.0));
        loadedFries.addIngredient(new Ingredient("Green Onions", true, true, 0.0));
        loadedFries.addIngredient(new Ingredient("Bacon", false, true, 0.0));
        restaurantMenu.addMenuItem(loadedFries);
        
        // 7. Hash Browns
        MenuItem hashBrowns = new MenuItem(7, "Hash Browns", 5.99,
            MenuItem.CookedType.Fried, MenuItem.PotatoType.Russet, true);
        hashBrowns.addIngredient(new Ingredient("Salt", true, true, 0.0));
        hashBrowns.addIngredient(new Ingredient("Pepper", true, true, 0.0));
        restaurantMenu.addMenuItem(hashBrowns);
        
        // 8. Mashed Potatoes
        MenuItem mashedPotatoes = new MenuItem(8, "Mashed Potatoes", 6.99,
            MenuItem.CookedType.Mashed, MenuItem.PotatoType.YukonGold, true);
        mashedPotatoes.addIngredient(new Ingredient("Butter", true, true, 0.0));
        mashedPotatoes.addIngredient(new Ingredient("Salt", true, true, 0.0));
        mashedPotatoes.addIngredient(new Ingredient("Pepper", true, true, 0.0));
        restaurantMenu.addMenuItem(mashedPotatoes);
        
        // 9. Hasselback Potatoes
        MenuItem hasselback = new MenuItem(9, "Hasselback Potatoes", 5.99,
            MenuItem.CookedType.Baked, MenuItem.PotatoType.Russet, true);
        hasselback.addIngredient(new Ingredient("Cheese", true, true, 0.0));
        hasselback.addIngredient(new Ingredient("Garlic", true, true, 0.0));
        restaurantMenu.addMenuItem(hasselback);
        
        // 10. Gnocchi Soup
        MenuItem gnocchiSoup = new MenuItem(10, "Gnocchi Soup", 5.99,
            MenuItem.CookedType.Soupped, MenuItem.PotatoType.Russet, true);
        gnocchiSoup.addIngredient(new Ingredient("Cheese", true, true, 0.0));
        gnocchiSoup.addIngredient(new Ingredient("Croutons", true, true, 0.0));
        gnocchiSoup.addIngredient(new Ingredient("Bacon", false, true, 0.0));
        restaurantMenu.addMenuItem(gnocchiSoup);
    }
}
