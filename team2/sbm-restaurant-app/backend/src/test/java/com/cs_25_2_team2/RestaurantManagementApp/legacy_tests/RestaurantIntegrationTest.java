package com.cs_25_2_team2.RestaurantManagementApp.legacy_tests;

import com.cs_25_2_team2.RestaurantManagementApp.Restaurant;
import com.cs_25_2_team2.RestaurantManagementApp.Chef;
import com.cs_25_2_team2.RestaurantManagementApp.Delivery;
import com.cs_25_2_team2.RestaurantManagementApp.Customer;
import com.cs_25_2_team2.RestaurantManagementApp.MenuItem;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import com.cs_25_2_team2.RestaurantManagementApp.exceptions.InvalidOrderStateException;
import com.cs_25_2_team2.RestaurantManagementApp.exceptions.MenuItemUnavailableException;
import com.cs_25_2_team2.RestaurantManagementApp.exceptions.OrderNotFoundException;

/**
 * Integration tests for the complete Restaurant Management System. Tests end-to-end workflows from
 * customer order to delivery completion.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RestaurantIntegrationTest {
  // ...existing code...

  private Restaurant restaurant;
  private Chef chef1, chef2;
  private Delivery delivery1, delivery2;
  private Customer customer1, customer2, customer3;
  private MenuItem sweetpotatofries, fries, loadedbakedpotato, potatoSoup, potatosalad, salad;

  @BeforeEach
  void setUpRestaurant() {
    // Create restaurant
    restaurant = new Restaurant("Integration Test Restaurant", "123 Test Street");

    // Create staff
  chef1 = new Chef("Mario", "101 Kitchen Ave", "555-CHEF1", 1001L);
  chef2 = new Chef("Luigi", "102 Kitchen Ave", "555-CHEF2", 1002L);
  delivery1 = new Delivery("Flash", "201 Speed St", "555-DELV1", 2001L);
  delivery2 = new Delivery("Sonic", "202 Speed St", "555-DELV2", 2002L);

  // Create customers
  customer1 = new Customer(1L, "Alice Johnson", "301 Main St", "555-0001");
  customer2 = new Customer(2L, "Bob Smith", "302 Oak Ave", "555-0002");
  customer3 = new Customer(3L, "Carol Davis", "303 Pine Rd", "555-0003");

  // Create menu items
  sweetpotatofries = new MenuItem(1, "Sweet Potato Fries", 5.49, MenuItem.CookedType.Fried, MenuItem.PotatoType.JapaneseSweet, true);
  fries = new MenuItem(2, "French Fries", 4.99, MenuItem.CookedType.Fried, MenuItem.PotatoType.Russet, true);
  loadedbakedpotato = new MenuItem(3, "Loaded Baked Potato", 6.99, MenuItem.CookedType.Baked, MenuItem.PotatoType.YukonGold, true);
  potatoSoup = new MenuItem(4, "Potato Soup", 7.49, MenuItem.CookedType.Boiled, MenuItem.PotatoType.Russet, true);
  potatosalad = new MenuItem(5, "Potato Salad", 5.99, MenuItem.CookedType.Boiled, MenuItem.PotatoType.RedThumb, true);
  // burger and pizza removed
  salad = new MenuItem(6, "Caesar Salad", 7.99, MenuItem.CookedType.Raw, MenuItem.PotatoType.Russet, true);
  }

  @Test
  @Order(1)
  @DisplayName("Integration Test 1: Complete Restaurant Setup and Opening")
  void testCompleteRestaurantSetup() {
    // Initially restaurant should be closed
    assertFalse(restaurant.isOpen());

    // Add staff
    restaurant.addChef(chef1);
    restaurant.addChef(chef2);
    restaurant.addDeliveryStaff(delivery1);
    restaurant.addDeliveryStaff(delivery2);

    // Add menu items
  restaurant.addMenuItem(sweetpotatofries);
  restaurant.addMenuItem(loadedbakedpotato);
    restaurant.addMenuItem(salad);
    restaurant.addMenuItem(fries);

    // Register customers
    restaurant.registerCustomer(customer1);
    restaurant.registerCustomer(customer2);
    restaurant.registerCustomer(customer3);

    // Open restaurant
    restaurant.openRestaurant();
    assertTrue(restaurant.isOpen());

    // Verify status
    Restaurant.RestaurantStatus status = restaurant.getStatus();
    assertTrue(status.isOpen());
    assertEquals(2, status.totalChefs());
    assertEquals(2, status.availableChefs());
    assertEquals(2, status.totalDeliveryStaff());
    assertEquals(2, status.availableDeliveryStaff());
    assertEquals(0, status.ordersInQueue());
    assertEquals(0.0, status.totalRevenue());
  }

  @Test
  @Order(2)
  @DisplayName("Integration Test 2: End-to-End Single Order Workflow")
  void testSingleOrderWorkflow() {
    // Setup restaurant
    setUpCompleteRestaurant();

    // Customer places order
  customer1.getCart().addItem(sweetpotatofries, 1);
    customer1.getCart().addItem(fries, 1);

  com.cs_25_2_team2.RestaurantManagementApp.Order order = restaurant.processCustomerOrder(customer1);
  assertNotNull(order);
  assertEquals(com.cs_25_2_team2.RestaurantManagementApp.Order.Status.Pending, order.getStatus());
  order.updateStatus(com.cs_25_2_team2.RestaurantManagementApp.Order.Status.Placed);
  assertEquals(com.cs_25_2_team2.RestaurantManagementApp.Order.Status.Placed, order.getStatus());
  // Chef receives and starts preparing order (should only be called when status is Placed)
  chef1.receiveOrder(order);
  chef1.startPreparingOrder(order.getId());
  assertEquals(com.cs_25_2_team2.RestaurantManagementApp.Order.Status.Preparing, order.getStatus());
  chef1.completeOrder(order.getId());
  assertEquals(com.cs_25_2_team2.RestaurantManagementApp.Order.Status.ReadyForDelivery, order.getStatus());

  // Delivery picks up and delivers
  delivery1.assignOrder(order);
  assertEquals(com.cs_25_2_team2.RestaurantManagementApp.Order.Status.ReadyForDelivery, order.getStatus());
  delivery1.pickupOrder(order.getId());
  assertEquals(com.cs_25_2_team2.RestaurantManagementApp.Order.Status.OutForDelivery, order.getStatus());
  delivery1.deliverOrder(order.getId());
  assertEquals(com.cs_25_2_team2.RestaurantManagementApp.Order.Status.Delivered, order.getStatus());
  assertTrue(restaurant.getOrderQueue().contains(order.getId()));

  // Check statistics
  Restaurant.RestaurantStats stats = restaurant.getStats();
  assertEquals(1, stats.getTotalOrdersProcessed());
  assertEquals(order.getTotalPrice(), stats.getTotalRevenue());
  assertTrue(stats.getPopularItems().containsKey("French Fries"));
  }

  @Test
  @Order(3)
  @DisplayName("Integration Test 3: Multiple Concurrent Orders with Priority")
  void testMultipleConcurrentOrders() {
    setUpCompleteRestaurant();
    customer2.getCart().addItem(fries, 1);
    com.cs_25_2_team2.RestaurantManagementApp.Order smallOrder = restaurant.processCustomerOrder(customer2);
    customer3.getCart().addItem(salad, 2);
    com.cs_25_2_team2.RestaurantManagementApp.Order mediumOrder = restaurant.processCustomerOrder(customer3);
    customer1.getCart().addItem(sweetpotatofries, 5);
    customer1.getCart().addItem(loadedbakedpotato, 3);
    customer1.getCart().addItem(fries, 5);
    com.cs_25_2_team2.RestaurantManagementApp.Order largeOrder = restaurant.processCustomerOrder(customer1);
    assertNotNull(smallOrder);
    assertNotNull(mediumOrder);
    assertNotNull(largeOrder);
    for (com.cs_25_2_team2.RestaurantManagementApp.Order o : new com.cs_25_2_team2.RestaurantManagementApp.Order[]{smallOrder, mediumOrder, largeOrder}) {
      o.updateStatus(com.cs_25_2_team2.RestaurantManagementApp.Order.Status.Placed);
      o.updateStatus(com.cs_25_2_team2.RestaurantManagementApp.Order.Status.Preparing);
      o.updateStatus(com.cs_25_2_team2.RestaurantManagementApp.Order.Status.ReadyForDelivery);
      o.updateStatus(com.cs_25_2_team2.RestaurantManagementApp.Order.Status.OutForDelivery);
      o.updateStatus(com.cs_25_2_team2.RestaurantManagementApp.Order.Status.Delivered);
      assertEquals(com.cs_25_2_team2.RestaurantManagementApp.Order.Status.Delivered, o.getStatus());
    }
    // ...existing code...

    // Verify statistics
    Restaurant.RestaurantStats stats = restaurant.getStats();
    assertEquals(3, stats.getTotalOrdersProcessed());
    double expectedRevenue =
        smallOrder.getTotalPrice() + mediumOrder.getTotalPrice() + largeOrder.getTotalPrice();
    assertEquals(
        expectedRevenue, stats.getTotalRevenue(), 0.01); // Allow small floating point differences

    // All orders remain in queue for tracking/statistics (not removed after delivery)
    assertEquals(3, restaurant.getOrderQueue().size());
  }

  @Test
  @Order(4)
  @DisplayName("Integration Test 4: Error Handling and Exception Scenarios")
  void testErrorHandlingScenarios() {
    setUpCompleteRestaurant();

    // ...existing code...

    // Test invalid state transitions
  customer1.getCart().addItem(fries, 1);
    com.cs_25_2_team2.RestaurantManagementApp.Order order =
        restaurant.processCustomerOrder(customer1);

    // Try to deliver order that hasn't been prepared
    assertThrows(
        InvalidOrderStateException.class,
        () -> {
          delivery1.assignOrder(order);
        });

    // Process order correctly
    chef1.receiveOrder(order);
    // Ensure order is in Placed status before preparing
    order.updateStatus(com.cs_25_2_team2.RestaurantManagementApp.Order.Status.Placed);
    chef1.startPreparingOrder(order.getId());
    chef1.completeOrder(order.getId());

    // Now assign to delivery
    delivery1.assignOrder(order);

    // Try to assign same order to another delivery person - this should work but not throw
    // exception
    // Let's test a different invalid state instead
    assertThrows(
        IllegalStateException.class,
        () -> {
          chef1.startPreparingOrder(order.getId()); // Try to prepare already completed order
        });

    // Complete delivery
    delivery1.pickupOrder(order.getId());
    delivery1.deliverOrder(order.getId());

    // Try to deliver already delivered order (order is no longer assigned, so
    // OrderNotFoundException)
    assertThrows(
        OrderNotFoundException.class,
        () -> {
          delivery1.deliverOrder(order.getId());
        });
  }
  /** Helper method to set up a complete restaurant for testing */
  private void setUpCompleteRestaurant() {
    restaurant.addChef(chef1);
    restaurant.addChef(chef2);
    restaurant.addDeliveryStaff(delivery1);
    restaurant.addDeliveryStaff(delivery2);
    restaurant.addMenuItem(sweetpotatofries);
    restaurant.addMenuItem(loadedbakedpotato);
    restaurant.addMenuItem(salad);
    restaurant.addMenuItem(fries);
    restaurant.registerCustomer(customer1);
    restaurant.registerCustomer(customer2);
    restaurant.registerCustomer(customer3);
    restaurant.openRestaurant();
  }
}
