package com.cs_25_2_team2.RestaurantManagementApp.services;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RestaurantServiceMenuItemPopularityTest {
    @Test
    void testConstructor() {
        RestaurantService.MenuItemPopularity pop = new RestaurantService.MenuItemPopularity(null, 0L);
        assertNotNull(pop);
        assertEquals(0L, pop.orderCount);
    }
}
