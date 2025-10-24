package com.cs_25_2_team2.RestaurantManagementApp.api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAiConfig {

    @Value("${openai.api.key:}")
    private String apiKey;

    public String getApiKey() {
        return apiKey != null && !apiKey.isEmpty() ? apiKey : System.getenv("OPENAI_API_KEY");
    }
}
