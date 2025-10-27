package com.cs_25_2_team2.RestaurantManagementApp.api;

import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class ChatbotTest {
    @Test
    void testGetMenuItemsReturnsNonEmptyList() {
        List<Map<String, Object>> items = Chatbot.getMenuItems();
        assertNotNull(items);
        assertFalse(items.isEmpty());
    }

    @Test
    void testMenuItemFieldsExist() {
        List<Map<String, Object>> items = Chatbot.getMenuItems();
        for (Map<String, Object> item : items) {
            assertTrue(item.containsKey("id"));
            assertTrue(item.containsKey("name"));
            assertTrue(item.containsKey("category"));
            assertTrue(item.containsKey("price"));
            assertTrue(item.containsKey("calories"));
            assertTrue(item.containsKey("image"));
            assertTrue(item.containsKey("toppings"));
        }
    }
}
