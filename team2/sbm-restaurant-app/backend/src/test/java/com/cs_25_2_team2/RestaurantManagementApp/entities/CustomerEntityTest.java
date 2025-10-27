package com.cs_25_2_team2.RestaurantManagementApp.entities;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CustomerEntityTest {
    @Test
    void testGettersAndSetters() {
        CustomerEntity customer = new CustomerEntity();
        customer.setCustomerId(1L);
        customer.setUsername("user");
        customer.setPasswordHash("hash");
        customer.setName("Test Name");
        customer.setAddress("Test Address");
        customer.setPhoneNumber("555-5555");
        customer.setEmail("test@example.com");
        java.time.LocalDateTime now = java.time.LocalDateTime.now();
        customer.setCreatedAt(now);
        customer.setUpdatedAt(now);
        java.util.List<OrderEntity> orders = new java.util.ArrayList<>();
        customer.setOrders(orders);
        CartEntity cart = new CartEntity();
        customer.setCart(cart);
        assertEquals(1L, customer.getCustomerId());
        assertEquals("user", customer.getUsername());
        assertEquals("hash", customer.getPasswordHash());
        assertEquals("Test Name", customer.getName());
        assertEquals("Test Address", customer.getAddress());
        assertEquals("555-5555", customer.getPhoneNumber());
        assertEquals("test@example.com", customer.getEmail());
        assertEquals(now, customer.getCreatedAt());
        assertEquals(now, customer.getUpdatedAt());
        assertEquals(orders, customer.getOrders());
        assertEquals(cart, customer.getCart());
    }

    @Test
    void testConstructorWithFields() {
        String username = "user2";
        String name = "Jane Doe";
        String address = "456 Elm St";
        String phone = "555-5678";
        CustomerEntity customer = new CustomerEntity(username, name, address, phone);
        assertEquals(username, customer.getUsername());
        assertEquals(name, customer.getName());
        assertEquals(address, customer.getAddress());
        assertEquals(phone, customer.getPhoneNumber());
    }
}
