package com.cs_25_2_team2.RestaurantManagementApp;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ChefTest {

  @Test
  void testGetName() {
    Chef chef = new Chef("Alice", "C123");
    assertEquals("Alice", chef.getName());
  }

  @Test
  void testGetId() {
    Chef chef = new Chef("Alice", "C123");
    assertEquals("C123", chef.getId());
  }
}
