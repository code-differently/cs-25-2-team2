package com.cs_25_2_team2.RestaurantManagementApp.entities;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StaffEntityTest {
    @Test
    void testGettersAndSetters() {
        StaffEntity staff = new StaffEntity();

        Long id = 123L;
        staff.setStaffId(id);
        assertEquals(id, staff.getStaffId());

        String username = "staff1";
        staff.setUsername(username);
        assertEquals(username, staff.getUsername());

        String passwordHash = "hash";
        staff.setPasswordHash(passwordHash);
        assertEquals(passwordHash, staff.getPasswordHash());

        String name = "John Staff";
        staff.setName(name);
        assertEquals(name, staff.getName());

        String phone = "555-1234";
        staff.setPhoneNumber(phone);
        assertEquals(phone, staff.getPhoneNumber());

        StaffEntity.StaffRole role = StaffEntity.StaffRole.Chef;
        staff.setRole(role);
        assertEquals(role, staff.getRole());

        Long restaurantId = 456L;
        staff.setRestaurantId(restaurantId);
        assertEquals(restaurantId, staff.getRestaurantId());

        LocalDateTime created = LocalDateTime.now();
        staff.setCreatedAt(created);
        assertEquals(created, staff.getCreatedAt());

        LocalDateTime updated = LocalDateTime.now();
        staff.setUpdatedAt(updated);
        assertEquals(updated, staff.getUpdatedAt());

        List<OrderEntity> chefOrders = new ArrayList<>();
        staff.setOrdersAsChef(chefOrders);
        assertEquals(chefOrders, staff.getOrdersAsChef());

        List<OrderEntity> deliveryOrders = new ArrayList<>();
        staff.setOrdersAsDelivery(deliveryOrders);
        assertEquals(deliveryOrders, staff.getOrdersAsDelivery());
    }

    @Test
    void testConstructorWithFields() {
        String username = "staff2";
        String name = "Jane Staff";
        String phone = "555-5678";
        StaffEntity.StaffRole role = StaffEntity.StaffRole.Delivery;
        StaffEntity staff = new StaffEntity(username, name, phone, role);
        assertEquals(username, staff.getUsername());
        assertEquals(name, staff.getName());
        assertEquals(phone, staff.getPhoneNumber());
        assertEquals(role, staff.getRole());
    }
}
