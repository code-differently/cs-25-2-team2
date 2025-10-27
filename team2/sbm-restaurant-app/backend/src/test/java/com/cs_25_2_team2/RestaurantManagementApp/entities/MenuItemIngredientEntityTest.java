package com.cs_25_2_team2.RestaurantManagementApp.entities;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MenuItemIngredientEntityTest {
    @Test
    void testGettersAndSetters() {
        MenuItemIngredientEntity mi = new MenuItemIngredientEntity();
        MenuItemIngredientId id = new MenuItemIngredientId(2L, 3L);
        mi.setId(id);
        MenuItemEntity menuItem = new MenuItemEntity();
        mi.setMenuItem(menuItem);
        IngredientEntity ingredient = new IngredientEntity();
        mi.setIngredient(ingredient);
        mi.setQuantity(new java.math.BigDecimal("2.5"));
        mi.setIsRequired(false);
        assertEquals(id, mi.getId());
        assertEquals(menuItem, mi.getMenuItem());
        assertEquals(ingredient, mi.getIngredient());
        assertEquals(new java.math.BigDecimal("2.5"), mi.getQuantity());
        assertFalse(mi.getIsRequired());
    }
}
