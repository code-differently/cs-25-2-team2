package com.cs_25_2_team2.RestaurantManagementApp.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class OpenAiService {

    private final OpenAiConfig config;
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    // simple in-memory store of conversation messages
    private final Map<Long, String> conversations = new ConcurrentHashMap<>();
    private final AtomicLong idSeq = new AtomicLong(1);

    public OpenAiService(OpenAiConfig config) {
        this.config = config;
    }

    public List<Map<String, Object>> listConversations() {
    List<Map<String, Object>> list = conversations.entrySet().stream()
        .map(e -> Map.<String, Object>of("id", e.getKey(), "message", e.getValue()))
        .collect(Collectors.toCollection(ArrayList::new));
    return Collections.unmodifiableList(list);
    }

    public Long deleteConversation(Long id) {
        return conversations.remove(id) != null ? id : null;
    }

    public Map<String, Object> sendMessage(String message) throws IOException, InterruptedException {
        return sendMessage(message, false);
    }

    public Map<String, Object> sendMessage(String message, boolean simulate) throws IOException, InterruptedException {
        // If simulation requested or message looks like an order query, handle locally
        if (simulate || looksLikeOrderQuery(message)) {
            String reply = simulateOrderReply(message);
            Long id = idSeq.getAndIncrement();
            conversations.put(id, "user: " + message + "\nassistant: " + reply);
            return Map.of("id", id, "reply", reply, "simulated", true);
        }

        String apiKey = config.getApiKey();
        if (apiKey == null || apiKey.isEmpty()) {
            throw new IllegalStateException("OpenAI API key not configured (set OPENAI_API_KEY or openai.api.key)");
        }

        // Build request body for Chat Completions (gpt-4o or gpt-4o-mini depending on availability).
        // Use model 'gpt-4o-mini' as a safe default for API compatibility.
        var body = mapper.createObjectNode();
        body.put("model", "gpt-4o-mini");
        var messages = mapper.createArrayNode();
        var userMsg = mapper.createObjectNode();
        userMsg.put("role", "user");
        userMsg.put("content", message);
        messages.add(userMsg);
        body.set("messages", messages);

        var request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.openai.com/v1/chat/completions"))
                .timeout(Duration.ofSeconds(30))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + apiKey)
                .POST(HttpRequest.BodyPublishers.ofString(mapper.writeValueAsString(body)))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() >= 400) {
            throw new IOException("OpenAI API returned status " + response.statusCode() + ": " + response.body());
        }

        JsonNode root = mapper.readTree(response.body());
        JsonNode choices = root.path("choices");
        StringBuilder sb = new StringBuilder();
        if (choices.isArray()) {
            for (JsonNode c : choices) {
                String text = c.path("message").path("content").asText();
                if (text == null || text.isEmpty()) text = c.path("text").asText();
                if (text != null) sb.append(text);
            }
        }

        String assistantReply = sb.toString();
        Long id = idSeq.getAndIncrement();
        conversations.put(id, "user: " + message + "\nassistant: " + assistantReply);

        return Map.of("id", id, "reply", assistantReply, "simulated", false);
    }

    private boolean looksLikeOrderQuery(String message) {
        String m = message == null ? "" : message.toLowerCase();
        return m.contains("order") || m.contains("status") || m.contains("what's in my order") || m.contains("what is in my order") || m.contains("items in my order") || m.matches(".*order\\s+#?\\d+.*");
    }

    private String simulateOrderReply(String message) {
        // Basic heuristic: if contains an order number, echo a mocked status; otherwise provide a general answer.
        String m = message == null ? "" : message.toLowerCase();
        var idMatch = java.util.regex.Pattern.compile("\\b(order\\s+#?)(\\d+)\\b").matcher(m);
        if (idMatch.find()) {
            String id = idMatch.group(2);
            return String.format("Order #%s is currently 'Preparing'. Items: Margherita Pizza x1, Caesar Salad x1. Estimated ready in 12 minutes.", id);
        }
        if (m.contains("what's in") || m.contains("what is in") || m.contains("items in")) {
            return "Your order contains: Margherita Pizza x1, Caesar Salad x1, Garlic Knots x3. Total: $27.50.";
        }
        if (m.contains("status")) {
            return "Your order is in 'Preparing' state. Assigned chef: Alice. Estimated ready in 12 minutes.";
        }
        return "I can help with order status and contents. Please provide an order number or ask 'what's in my order' or 'order status'.";
    }
}
