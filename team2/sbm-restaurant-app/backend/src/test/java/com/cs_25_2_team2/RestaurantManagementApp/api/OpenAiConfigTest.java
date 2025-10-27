package com.cs_25_2_team2.RestaurantManagementApp.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class OpenAiConfigTest {
    @Autowired
    private OpenAiConfig config;

    @Test
    void testGetApiKeyReturnsPropertyOrEnv() {
        String apiKey = config.getApiKey();
        assertNotNull(apiKey, "API key should not be null if set in properties or env");
        assertFalse(apiKey.isEmpty(), "API key should not be empty");
    }
}
