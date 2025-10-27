package com.cs_25_2_team2.RestaurantManagementApp.api;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import java.util.Map;

class OpenAiServiceTest {
    @Test
    void testListConversationsEmpty() {
        OpenAiService service = new OpenAiService(new OpenAiConfig());
        List<Map<String, Object>> conversations = service.listConversations();
        assertNotNull(conversations);
        assertTrue(conversations.isEmpty());
    }

    @Test
    void testDeleteConversationReturnsNullForMissing() {
        OpenAiService service = new OpenAiService(new OpenAiConfig());
        assertNull(service.deleteConversation(999L));
    }

    @Test
    void testSimulateOrderReplyBranches() {
        OpenAiService service = new OpenAiService(new OpenAiConfig());
        // Order number
        assertTrue(service.simulateOrderReply("order #42").contains("Order #42"));
        // Chef name
        assertTrue(service.simulateOrderReply("who's the chef").contains("Chef Ramsey"));
        // Items in order
        assertTrue(service.simulateOrderReply("what's in my order").contains("Texas Loaded Baked Potato"));
        // Status
        assertTrue(service.simulateOrderReply("status").contains("being prepared"));
        // Default
        assertTrue(service.simulateOrderReply("random question").contains("I can help"));
    }

    @Test
    void testLooksLikeOrderQueryBranches() {
        OpenAiService service = new OpenAiService(new OpenAiConfig());
        assertTrue(service.looksLikeOrderQuery("order #42"));
        assertTrue(service.looksLikeOrderQuery("status"));
        assertTrue(service.looksLikeOrderQuery("what's in my order"));
        assertTrue(service.looksLikeOrderQuery("items in my order"));
        assertFalse(service.looksLikeOrderQuery("hello world"));
    }

    @Test
    void testSendMessageSimulated() throws Exception {
        OpenAiService service = new OpenAiService(new OpenAiConfig());
        Map<String, Object> result = service.sendMessage("order #42", true);
        assertNotNull(result);
        assertEquals(true, result.get("simulated"));
        assertTrue(result.get("reply").toString().contains("Order #42"));
    }

    @Test
    void testGenerateChatbotResponseSimulated() {
        OpenAiService service = new OpenAiService(new OpenAiConfig());
        String reply = service.generateChatbotResponse("order #42");
        assertNotNull(reply);
        assertTrue(reply.contains("Order #42"));
    }
}
