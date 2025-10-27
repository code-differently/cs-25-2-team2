package com.cs_25_2_team2.RestaurantManagementApp.entities;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class IngredientEntityTest {
    @Test
    void testDefaultValues() {
        IngredientEntity ingredient = new IngredientEntity("Tomato");
        assertEquals("Tomato", ingredient.getName());
        assertFalse(ingredient.getIsAdditionalTopping());
        assertFalse(ingredient.getIsOptional());
        assertEquals(BigDecimal.ZERO, ingredient.getExtraCost());
        assertTrue(ingredient.getIsVegetarian());
    }

    @Test
    void testSettersAndGetters() {
        IngredientEntity ingredient = new IngredientEntity();
        ingredient.setName("Cheese");
        ingredient.setIsAdditionalTopping(true);
        ingredient.setIsOptional(true);
        ingredient.setExtraCost(new BigDecimal("1.50"));
        ingredient.setIsVegetarian(false);
        assertEquals("Cheese", ingredient.getName());
        assertTrue(ingredient.getIsAdditionalTopping());
        assertTrue(ingredient.getIsOptional());
        assertEquals(new BigDecimal("1.50"), ingredient.getExtraCost());
        assertFalse(ingredient.getIsVegetarian());
    }
    
    @Test
    void testGettersAndSetters() {
        IngredientEntity ing = new IngredientEntity();
        ing.setIngredientId(1L);
        ing.setName("Potato");
        ing.setIsAdditionalTopping(true);
        ing.setIsOptional(true);
        ing.setExtraCost(new java.math.BigDecimal("2.5"));
        ing.setIsVegetarian(false);
        java.time.LocalDateTime now = java.time.LocalDateTime.now();
        ing.setCreatedAt(now);
        ing.setUpdatedAt(now);
        java.util.List<MenuItemIngredientEntity> menuItems = new java.util.ArrayList<>();
        ing.setMenuItemIngredients(menuItems);
        java.util.List<OrderItemCustomizationEntity> customizations = new java.util.ArrayList<>();
        ing.setCustomizations(customizations);
        assertEquals(1L, ing.getIngredientId());
        assertEquals("Potato", ing.getName());
        assertTrue(ing.getIsAdditionalTopping());
        assertTrue(ing.getIsOptional());
        assertEquals(new java.math.BigDecimal("2.5"), ing.getExtraCost());
        assertFalse(ing.getIsVegetarian());
        assertEquals(now, ing.getCreatedAt());
        assertEquals(now, ing.getUpdatedAt());
        assertEquals(menuItems, ing.getMenuItemIngredients());
        assertEquals(customizations, ing.getCustomizations());
    }
}
