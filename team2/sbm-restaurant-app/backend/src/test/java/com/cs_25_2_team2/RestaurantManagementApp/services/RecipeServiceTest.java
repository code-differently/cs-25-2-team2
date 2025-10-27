package com.cs_25_2_team2.RestaurantManagementApp.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.RequestHeadersUriSpec;
import org.springframework.web.reactive.function.client.WebClient.RequestHeadersSpec;
import reactor.core.publisher.Mono;
import java.util.function.Function;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class RecipeServiceTest {
    @Mock
    private WebClient.Builder webClientBuilder;
    @Mock
    private WebClient webClient;
    @Mock
    @SuppressWarnings("rawtypes")
    private WebClient.RequestHeadersUriSpec uriSpec;
    @Mock
    private WebClient.ResponseSpec responseSpec;

    @InjectMocks
    private RecipeService recipeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        recipeService = new RecipeService(webClientBuilder);
    recipeService.setSpoonKey("test-key");
    }

    @SuppressWarnings("unchecked")
    @Test
    void testFetchPotatoRecipes() {
    when(webClientBuilder.build()).thenReturn(webClient);
    when(webClient.get()).thenReturn(uriSpec);
    when(uriSpec.uri(any(Function.class))).thenReturn(uriSpec);
    when(uriSpec.retrieve()).thenReturn(responseSpec);
    when(responseSpec.bodyToMono(String.class)).thenReturn(Mono.just("{\"results\":[]}"));

        Mono<String> resultMono = recipeService.fetchPotatoRecipes(1);
        String result = resultMono.block();
        assertNotNull(result);
        assertTrue(result.contains("results"));
    }
}