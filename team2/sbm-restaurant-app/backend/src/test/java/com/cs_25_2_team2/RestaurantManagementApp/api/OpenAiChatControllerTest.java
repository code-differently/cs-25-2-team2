package com.cs_25_2_team2.RestaurantManagementApp.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OpenAiChatControllerTest {

    private OpenAiService service;
    private OpenAiChatController controller;

    @BeforeEach
    void setUp() {
        service = mock(OpenAiService.class);
        controller = new OpenAiChatController(service);
    }

    @Test
    void testList() {
        List<Map<String, Object>> mockConversations = List.of(Map.of("id", 1, "message", "Hello"));
        when(service.listConversations()).thenReturn(mockConversations);

        ResponseEntity<List<Map<String, Object>>> response = controller.list();
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(mockConversations, response.getBody());
    }

    @Test
    void testPostMessageValid() throws IOException, InterruptedException {
        Map<String, Object> mockResponse = Map.of("id", 1, "reply", "Hi there!", "simulated", true);
        when(service.sendMessage("Hello", true)).thenReturn(mockResponse);

        Map<String, Object> requestBody = Map.of("message", "Hello", "simulate", true);
        ResponseEntity<?> response = controller.postMessage(requestBody);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(mockResponse, response.getBody());
    }

    @Test
    void testPostMessageMissingMessage() {
        Map<String, Object> requestBody = Map.of("simulate", true);
        ResponseEntity<?> response = controller.postMessage(requestBody);

        assertEquals(400, response.getStatusCode().value());
        assertNotNull(response.getBody(), "Response body should not be null");
        assertTrue(response.getBody().toString().contains("error"));
        assertTrue(response.getBody().toString().contains("message is required"));

        @SuppressWarnings("unchecked")
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        assertNotNull(responseBody);
        assertEquals("message is required", responseBody.get("error"));
    }

    @Test
    void testDeleteSuccess() {
        when(service.deleteConversation(1L)).thenReturn(1L);

        ResponseEntity<?> response = controller.delete(1L);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().toString().contains("deleted=1"));
    }

    @Test
    void testDeleteNotFound() {
        when(service.deleteConversation(1L)).thenReturn(null);

        ResponseEntity<?> response = controller.delete(1L);
        assertEquals(404, response.getStatusCode().value());
    }
}