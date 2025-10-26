package com.cs_25_2_team2.RestaurantManagementApp.services;

// Removed: Placeholder ChatMessage DTO. Use Map<String, Object> or a proper DTO for OpenAiChatController.
public class ChatMessage {
	private String sender;
	private String content;

	// Added timestamp field with getter and setter
	private long timestamp;

	public ChatMessage() {}

	public ChatMessage(String sender, String content) {
		this.sender = sender;
		this.content = content;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getContent() {
		this.content = content;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
}
