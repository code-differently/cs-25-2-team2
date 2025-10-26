package com.cs_25_2_team2.RestaurantManagementApp.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ChatMessageTest {

    private ChatMessage chatMessage;

    @BeforeEach
    void setUp() {
        chatMessage = new ChatMessage();
    }

    @Test
    void testSetAndGetMessage() {
        String message = "Hello, World!";
        chatMessage.setMessage(message);
        assertEquals(message, chatMessage.getMessage());
    }

    @Test
    void testSetAndGetSender() {
        String sender = "User1";
        chatMessage.setSender(sender);
        assertEquals(sender, chatMessage.getSender());
    }

    @Test
    void testSetAndGetTimestamp() {
        long timestamp = System.currentTimeMillis();
        chatMessage.setTimestamp(timestamp);
        assertEquals(timestamp, chatMessage.getTimestamp());
    }
}