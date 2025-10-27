package com.cs_25_2_team2.RestaurantManagementApp.entities;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CartEntityTest {
    @Test
    void testGettersAndSetters() {
        CartEntity cart = new CartEntity();
        cart.setCartId(1L);
        CustomerEntity customer = new CustomerEntity();
        cart.setCustomer(customer);
        java.time.LocalDateTime now = java.time.LocalDateTime.now();
        cart.setCreatedAt(now);
        cart.setUpdatedAt(now);
        java.util.List<CartItemEntity> items = new java.util.ArrayList<>();
        cart.setCartItems(items);
        assertEquals(1L, cart.getCartId());
        assertEquals(customer, cart.getCustomer());
        assertEquals(now, cart.getCreatedAt());
        assertEquals(now, cart.getUpdatedAt());
        assertEquals(items, cart.getCartItems());
    }
}
