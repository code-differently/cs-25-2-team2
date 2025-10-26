
package com.cs_25_2_team2.RestaurantManagementApp.controllers;

import com.cs_25_2_team2.RestaurantManagementApp.api.OpenAiChatController;
import com.cs_25_2_team2.RestaurantManagementApp.api.OpenAiService;
import com.cs_25_2_team2.RestaurantManagementApp.api.OpenAiConfig;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class ChatbotControllerTest {
    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        OpenAiConfig config = new OpenAiConfig();
        OpenAiService service = new OpenAiService(config);
        OpenAiChatController controller = new OpenAiChatController(service);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void testChatbotSimulatedReply() throws Exception {
        String json = "{\"message\": \"status of order #42\", \"simulate\": true}";
        mockMvc.perform(post("/api/chatbot")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reply").value(org.hamcrest.Matchers.containsString("Order #42")))
                .andExpect(jsonPath("$.simulated").value(true));
    }

    @Test
    void testChatbotMissingMessageError() throws Exception {
        String json = "{\"simulate\": true}";
        mockMvc.perform(post("/api/chatbot")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("message is required"));
    }
}
