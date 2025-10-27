package com.cs_25_2_team2.RestaurantManagementApp.entities;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class MenuItemEntityTest {
    @Test
    void testGettersAndSetters() {
        MenuItemEntity item = new MenuItemEntity();
        item.setDishId(1L);
        item.setRestaurantId(2L);
        item.setDishName("Fries");
    item.setCategory(MenuItemEntity.Category.MAIN_DISH);
        item.setPrice(BigDecimal.valueOf(2.99));
        item.setCookedType(MenuItemEntity.CookedType.Fried);
        item.setPotatoType(MenuItemEntity.PotatoType.Russet);
        item.setIsAvailable(true);
        item.setDescription("Tasty fries");
        item.setCreatedAt(LocalDateTime.now());
        item.setUpdatedAt(LocalDateTime.now());
        item.setIngredients(List.of());
        item.setOrderItems(List.of());
        item.setCartItems(List.of());
        assertEquals(1L, item.getDishId());
        assertEquals(2L, item.getRestaurantId());
        assertEquals("Fries", item.getDishName());
    assertEquals(MenuItemEntity.Category.MAIN_DISH, item.getCategory());
        assertEquals(BigDecimal.valueOf(2.99), item.getPrice());
        assertEquals(MenuItemEntity.CookedType.Fried, item.getCookedType());
        assertEquals(MenuItemEntity.PotatoType.Russet, item.getPotatoType());
        assertTrue(item.getIsAvailable());
        assertEquals("Tasty fries", item.getDescription());
    }
}
