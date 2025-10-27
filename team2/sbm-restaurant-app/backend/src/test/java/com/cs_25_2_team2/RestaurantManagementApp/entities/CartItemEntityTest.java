package com.cs_25_2_team2.RestaurantManagementApp.entities;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CartItemEntityTest {
    @Test
    void testGettersAndSetters() {
        CartItemEntity item = new CartItemEntity();
        item.setCartItemId(1L);
        CartEntity cart = new CartEntity();
        item.setCart(cart);
        MenuItemEntity menuItem = new MenuItemEntity();
        item.setMenuItem(menuItem);
        item.setQuantity(4);
        assertEquals(1L, item.getCartItemId());
        assertEquals(cart, item.getCart());
        assertEquals(menuItem, item.getMenuItem());
        assertEquals(4, item.getQuantity());
    }
}
