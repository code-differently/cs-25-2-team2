package com.cs_25_2_team2.RestaurantManagementApp.entities;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class OrderItemCustomizationEntityTest {
    @Test
    void testGettersAndSetters() {
        OrderItemCustomizationEntity oic = new OrderItemCustomizationEntity();
        oic.setCustomizationId(1L);
        OrderItemEntity orderItem = new OrderItemEntity();
        oic.setOrderItem(orderItem);
        IngredientEntity ingredient = new IngredientEntity();
        oic.setIngredient(ingredient);
        oic.setCustomizationType(OrderItemCustomizationEntity.CustomizationType.Extra);
        oic.setQuantity(new java.math.BigDecimal("2.5"));
        oic.setAdditionalCost(new java.math.BigDecimal("1.25"));
        oic.setNotes("Extra cheese");
        java.time.LocalDateTime now = java.time.LocalDateTime.now();
        oic.setCreatedAt(now);
        assertEquals(1L, oic.getCustomizationId());
        assertEquals(orderItem, oic.getOrderItem());
        assertEquals(ingredient, oic.getIngredient());
        assertEquals(OrderItemCustomizationEntity.CustomizationType.Extra, oic.getCustomizationType());
        assertEquals(new java.math.BigDecimal("2.5"), oic.getQuantity());
        assertEquals(new java.math.BigDecimal("1.25"), oic.getAdditionalCost());
        assertEquals("Extra cheese", oic.getNotes());
        assertEquals(now, oic.getCreatedAt());
    }
}
