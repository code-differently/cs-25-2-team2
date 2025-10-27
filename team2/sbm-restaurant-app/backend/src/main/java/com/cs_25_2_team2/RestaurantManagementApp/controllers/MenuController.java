package com.cs_25_2_team2.RestaurantManagementApp.controllers;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import java.util.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.cs_25_2_team2.RestaurantManagementApp.entities.MenuItemEntity;
import com.cs_25_2_team2.RestaurantManagementApp.repositories.MenuItemRepository;

/**
 * REST Controller for managing restaurant menu items.
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
    
    private MenuItemRepository menuItemRepository;
    
    /**
     * Get all menu items
     */
    @GetMapping
    public List<MenuItemEntity> getAllMenuItems() {
        return (List<MenuItemEntity>) menuItemRepository.findAll();
    }
    
    /**
     * Get specific menu item by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<MenuItemEntity> getMenuItemById(@PathVariable Long id) {
        return menuItemRepository.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Create new menu item
     */
    @PostMapping
    public ResponseEntity<MenuItemEntity> createMenuItem(@RequestBody Map<String, Object> menuItemData) {
        MenuItemEntity newItem = new MenuItemEntity();
        
        // Set basic properties from the map
        if (menuItemData.containsKey("dishName")) {
            newItem.setDishName(menuItemData.get("dishName").toString());
        }
        if (menuItemData.containsKey("price")) {
            String priceStr = menuItemData.get("price").toString();
            newItem.setPrice(new BigDecimal(priceStr));
        }
        if (menuItemData.containsKey("category")) {
            String categoryStr = menuItemData.get("category").toString();
            try {
                // Map frontend category names to backend enum values
                MenuItemEntity.Category category = mapFrontendCategoryToEnum(categoryStr);
                newItem.setCategory(category);
            } catch (IllegalArgumentException e) {
                // Set default if invalid
                newItem.setCategory(MenuItemEntity.Category.MAIN_DISH);
            }
        }
        
        // Set default values
        newItem.setIsAvailable(true);
        newItem.setCreatedAt(LocalDateTime.now());
        
        MenuItemEntity savedItem = menuItemRepository.save(newItem);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedItem);
    }
    
    /**
     * Update menu item
     */
    @PutMapping("/{id}")
    public ResponseEntity<MenuItemEntity> updateMenuItem(@PathVariable Long id, @RequestBody Map<String, Object> menuItemData) {
        if (!menuItemData.containsKey("dishName") && !menuItemData.containsKey("price") && !menuItemData.containsKey("isAvailable")) {
            return ResponseEntity.badRequest().build();
        }
        
        return menuItemRepository.findById(id)
            .map(existingItem -> {
                // Update fields from the map
                if (menuItemData.containsKey("dishName")) {
                    existingItem.setDishName(menuItemData.get("dishName").toString());
                }
                if (menuItemData.containsKey("price")) {
                    String priceStr = menuItemData.get("price").toString();
                    existingItem.setPrice(new BigDecimal(priceStr));
                }
                if (menuItemData.containsKey("isAvailable")) {
                    boolean available = Boolean.parseBoolean(menuItemData.get("isAvailable").toString());
                    existingItem.setIsAvailable(available);
                }
                
                MenuItemEntity savedItem = menuItemRepository.save(existingItem);
                return ResponseEntity.ok(savedItem);
            })
            .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Delete menu item
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMenuItem(@PathVariable Long id) {
        if (menuItemRepository.existsById(id)) {
            menuItemRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * Get menu categories
     */
    @GetMapping("/categories")
    public ResponseEntity<List<String>> getCategories() {
        List<String> categories = Arrays.asList("MAIN_DISH", "SIDE", "SOUP");
        return ResponseEntity.ok(categories);
    }
    
    /**
     * Get items by category
     */
    @GetMapping("/category/{category}")
    public ResponseEntity<List<MenuItemEntity>> getItemsByCategory(@PathVariable String category) {
        try {
            if ("All".equals(category)) {
                return ResponseEntity.ok((List<MenuItemEntity>) menuItemRepository.findAll());
            } else {
                MenuItemEntity.Category cat = mapFrontendCategoryToEnum(category);
                return ResponseEntity.ok(menuItemRepository.findByCategoryAndIsAvailableTrue(cat));
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Helper method to map frontend category strings to backend enum values
     */
    private MenuItemEntity.Category mapFrontendCategoryToEnum(String frontendCategory) {
        switch (frontendCategory.toLowerCase()) {
            case "main":
                return MenuItemEntity.Category.MAIN_DISH;
            case "side":
                return MenuItemEntity.Category.SIDE;
            case "soup":
                return MenuItemEntity.Category.SOUP;
            case "main_dish":  // Also accept backend format
                return MenuItemEntity.Category.MAIN_DISH;
            default:
                throw new IllegalArgumentException("Unknown category: " + frontendCategory);
        }
    }
}
