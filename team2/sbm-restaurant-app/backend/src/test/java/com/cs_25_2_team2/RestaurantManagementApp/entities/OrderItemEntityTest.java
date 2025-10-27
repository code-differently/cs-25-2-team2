package com.cs_25_2_team2.RestaurantManagementApp.entities;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

class OrderItemEntityTest {
    @Test
    void testGettersAndSetters() {
        OrderItemEntity item = new OrderItemEntity();
        item.setOrderItemId(1L);
        OrderEntity order = new OrderEntity();
        item.setOrder(order);
        MenuItemEntity menuItem = new MenuItemEntity();
        item.setMenuItem(menuItem);
        item.setQuantity(2);
        item.setUnitPrice(new BigDecimal("5.99"));
        item.setSubtotal(new BigDecimal("11.98"));
        item.setSpecialInstructions("No onions");
        LocalDateTime now = LocalDateTime.now();
        item.setCreatedAt(now);
        List<OrderItemCustomizationEntity> customizations = new ArrayList<>();
        item.setCustomizations(customizations);
        assertEquals(1L, item.getOrderItemId());
        assertEquals(order, item.getOrder());
        assertEquals(menuItem, item.getMenuItem());
        assertEquals(2, item.getQuantity());
        assertEquals(new BigDecimal("5.99"), item.getUnitPrice());
        assertEquals(new BigDecimal("11.98"), item.getSubtotal());
        assertEquals("No onions", item.getSpecialInstructions());
        assertEquals(now, item.getCreatedAt());
        assertEquals(customizations, item.getCustomizations());
    }
}
