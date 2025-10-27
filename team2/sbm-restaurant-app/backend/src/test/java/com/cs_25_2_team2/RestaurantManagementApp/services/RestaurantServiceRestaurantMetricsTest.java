package com.cs_25_2_team2.RestaurantManagementApp.services;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RestaurantServiceRestaurantMetricsTest {
    @Test
    void testConstructor() {
        RestaurantService.RestaurantMetrics metrics = new RestaurantService.RestaurantMetrics();
        assertNotNull(metrics);
    }
}
