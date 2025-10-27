package com.cs_25_2_team2.RestaurantManagementApp.legacy_tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import com.cs_25_2_team2.RestaurantManagementApp.Delivery;
import com.cs_25_2_team2.RestaurantManagementApp.Order;

import org.junit.jupiter.api.Test;

public class DeliveryTest {

  @Test
  void testGetName() {
  Delivery delivery = new Delivery("Bob", "123 Delivery St", "555-1234", 1L);
    assertEquals("Bob", delivery.getName());
  }

  @Test
  void testGetId() {
    Delivery delivery = new Delivery("Bob", "123 Delivery St", "555-1234", 2L);
    assertEquals("D002", delivery.getId());
  }

  @Test
  void testGetAssignedOrders() {
  Delivery delivery = new Delivery("Bob", "123 Delivery St", "555-1234", 3L);
    List<Order> orders = delivery.getAssignedOrders();
    assertNotNull(orders);
    assertTrue(orders.isEmpty());
  }

  @Test
  void testGetDeliveredOrders() {
  Delivery delivery = new Delivery("Bob", "123 Delivery St", "555-1234", 4L);
    List<Order> delivered = delivery.getDeliveredOrders();
    assertNotNull(delivered);
    assertTrue(delivered.isEmpty());
  }

  @Test
  void testGetAssignedOrderCount() {
  Delivery delivery = new Delivery("Bob", "123 Delivery St", "555-1234", 5L);
    assertEquals(0, delivery.getAssignedOrderCount());
  }

  @Test
  void testGetDeliveredOrderCount() {
  Delivery delivery = new Delivery("Bob", "123 Delivery St", "555-1234", 6L);
    assertEquals(0, delivery.getDeliveredOrderCount());
  }

  @Test
  void testGetOrdersOutForDelivery() {
  Delivery delivery = new Delivery("Bob", "123 Delivery St", "555-1234", 7L);
    List<Order> outForDelivery = delivery.getOrdersOutForDelivery();
    assertNotNull(outForDelivery);
    assertTrue(outForDelivery.isEmpty());
  }

  @Test
  void testGetOrdersReadyForPickup() {
  Delivery delivery = new Delivery("Bob", "123 Delivery St", "555-1234", 8L);
    List<Order> readyForPickup = delivery.getOrdersReadyForPickup();
    assertNotNull(readyForPickup);
    assertTrue(readyForPickup.isEmpty());
  }

  @Test
  void testToString() {
  Delivery delivery = new Delivery("Bob", "123 Delivery St", "555-1234", 9L);
    String result = delivery.toString();
    assertNotNull(result);
    assertFalse(result.isEmpty());
    assertTrue(result.length() > 10);
  }
}
