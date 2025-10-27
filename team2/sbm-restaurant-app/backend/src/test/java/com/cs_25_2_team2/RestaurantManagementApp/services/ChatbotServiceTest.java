package com.cs_25_2_team2.RestaurantManagementApp.services;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ChatbotServiceTest {
    @Test
    void testChatbotServiceInstantiation() {
        ChatbotService service = new ChatbotService();
        assertNotNull(service);
    }
}
