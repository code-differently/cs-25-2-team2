package com.cs_25_2_team2.RestaurantManagementApp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class DeliveryTest {

  @Test
  void testGetName() {
    Delivery delivery = new Delivery("Bob", "D001", "SomeValue");
    assertEquals("Bob", delivery.getName());
  }

  @Test
  void testGetId() {
    Delivery delivery = new Delivery("Bob", "D001", "SomeValue");
    assertEquals("D001", delivery.getId());
  }
}
