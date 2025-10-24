package com.cs_25_2_team2.RestaurantManagementApp.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/chatbot")
/**
 * REST API for a simple Chatbot used by the frontend.
 *
 * <p>Base path: /api/chatbot
 *
 * <p>Behavior:
 * - POST /api/chatbot accepts a JSON body { "message": "...", "simulate": true|false }.
 *   - If "simulate" is true (or the message looks like an order query) the reply is generated
 *     locally; otherwise the backend will call the OpenAI API (requires OPENAI_API_KEY).
 * - GET /api/chatbot returns a list of stored conversations (in-memory).
 * - DELETE /api/chatbot/{id} removes a stored conversation.
 *
 * <p>Notes:
 * - Simulated responses are deterministic and safe for frontend development (no OpenAI key
 *   required).
 * - The frontend uses a Next.js proxy at /api/chat which forwards requests to this controller.
 * - Configure the OpenAI API key via the environment variable OPENAI_API_KEY or the
 *   openai.api.key property.
 */
public class OpenAiChatController {

    private final OpenAiService service;

    public OpenAiChatController(OpenAiService service) {
        this.service = service;
    }

    /**
     * List stored conversations.
     *
     * @return a list of conversations in the form [{"id":1, "message":"user:...\nassistant:..."}, ...]
     */
    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> list() {
        return ResponseEntity.ok(service.listConversations());
    }

    /**
     * Send a message to the chatbot.
     *
     * <p>Request JSON:
     * <pre>
     * {
     *   "message": "status of order #42",
     *   "simulate": true      // optional, defaults to false if not present
     * }
     * </pre>
     *
     * <p>Response JSON (200):
     * <pre>
     * {
     *   "id": 1,
     *   "reply": "Order #42 is currently 'Preparing'...",
     *   "simulated": true
     * }
     * </pre>
     *
     * Errors:
     * <ul>
     *   <li>400 if message is missing
     *   <li>502 if upstream (OpenAI) request failed
     * </ul>
     */
    @PostMapping
    public ResponseEntity<?> postMessage(@RequestBody Map<String, Object> body) {
        String message = body.getOrDefault("message", "").toString();
        boolean simulate = false;
        Object simObj = body.get("simulate");
        if (simObj instanceof Boolean) simulate = (Boolean) simObj;
        else if (simObj != null) simulate = Boolean.parseBoolean(simObj.toString());
        if (message == null || message.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "message is required"));
        }
        try {
            Map<String, Object> result = service.sendMessage(message, simulate);
            return ResponseEntity.ok(result);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        } catch (IOException | InterruptedException e) {
            return ResponseEntity.status(502).body(Map.of("error", "OpenAI request failed", "details", e.getMessage()));
        }
    }

    /**
     * Delete a stored conversation by id.
     *
     * @param id conversation id
     * @return 200 {"deleted": id} or 404 if not found
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        Long removed = service.deleteConversation(id);
        if (removed == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(Map.of("deleted", removed));
    }
}
