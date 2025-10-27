package com.cs_25_2_team2.RestaurantManagementApp.controllers;
// Removed: Placeholder ChatbotController. Use OpenAiChatController instead.
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.cs_25_2_team2.RestaurantManagementApp.services.ChatbotService;
import com.cs_25_2_team2.RestaurantManagementApp.services.ChatMessage;

@RestController
@RequestMapping("/api/chatbot/local")
@CrossOrigin(origins = "http://localhost:3000")
public class ChatbotController {
	@Autowired
	private ChatbotService chatbotService;

	@PostMapping
	public ResponseEntity<ChatMessage> chat(@RequestBody ChatMessage message) {
		// For now, just echo the message back
		return ResponseEntity.ok(message);
	}
}
