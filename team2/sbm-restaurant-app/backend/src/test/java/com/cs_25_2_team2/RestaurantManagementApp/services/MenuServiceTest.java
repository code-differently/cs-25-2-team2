package com.cs_25_2_team2.RestaurantManagementApp.services;

import com.cs_25_2_team2.RestaurantManagementApp.MenuItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MenuServiceTest {
    private MenuService menuService;

    @BeforeEach
    void setUp() {
        menuService = new MenuService();
    }

    @Test
    void testGetAllMenuItems() {
        List<MenuItem> items = menuService.getAllMenuItems();
        assertEquals(10, items.size());
    }

    @Test
    void testGetMenuItemById() {
        MenuItem item = menuService.getMenuItemById(1L);
        assertNotNull(item);
        assertEquals("Texas Style Baked Potato", item.getDishName());
    }

    @Test
    void testAddAndRemoveMenuItem() {
        MenuItem newItem = new MenuItem(99, "Test Potato", 1.99, MenuItem.CookedType.Baked, MenuItem.PotatoType.Russet, true);
        menuService.addMenuItem(newItem);
        assertEquals(11, menuService.getMenuItemCount());
        menuService.removeMenuItem(99L);
        assertEquals(10, menuService.getMenuItemCount());
    }

    @Test
    void testUpdateMenuItemAvailability() {
        menuService.updateMenuItemAvailability(1L, false);
        MenuItem item = menuService.getMenuItemById(1L);
        assertFalse(item.isAvailable());
    }

    @Test
    void testGetItemsByCookingType() {
        List<MenuItem> bakedItems = menuService.getItemsByCookingType(MenuItem.CookedType.Baked);
        assertTrue(bakedItems.size() > 0);
    }

    @Test
    void testMenuAvailability() {
        assertTrue(menuService.isMenuAvailable());
        menuService.setMenuAvailability(false);
        assertFalse(menuService.isMenuAvailable());
    }

    @Test
    void testGetMenuLastUpdated() {
        Date lastUpdated = menuService.getMenuLastUpdated();
        assertNotNull(lastUpdated);
    }
}
