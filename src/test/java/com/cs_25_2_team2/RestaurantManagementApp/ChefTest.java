package com.cs_25_2_team2.RestaurantManagementApp;

import java.sql.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.cs_25_2_team2.RestaurantManagementApp.exceptions.OrderNotFoundException;

class ChefTest {

  private Chef chef;
  private Order testOrder;
  private Customer customer;

  @BeforeEach
  void setUp() {
    chef = new Chef("Alice", "456 Chef Ave", "555-5678", "C123");
    customer = new Customer(1, "John Doe", "123 Main St", "555-0001");

    // Create test menu item and cart item
    MenuItem burger =
        new MenuItem(
            1, "Burger", 10.99, MenuItem.CookedType.Grilled, MenuItem.PotatoType.Russet, true);
    CartItem cartItem = new CartItem(burger, 1);
    List<CartItem> items = List.of(cartItem);
    testOrder = new Order(customer, items, new Date(System.currentTimeMillis()));
  }

  @Test
  @DisplayName("Test chef creation")
  void testChefCreation() {
    assertNotNull(chef);
    assertEquals("Alice", chef.getName());
    assertEquals("C123", chef.getId());
  }

  @Test
  @DisplayName("Test chef ID")
  void testGetId() {
    assertEquals("C123", chef.getId());
  }

  @Test
  @DisplayName("Test chef name")
  void testGetName() {
    assertEquals("Alice", chef.getName());
  }

  @Test
  @DisplayName("Test receive order")
  void testReceiveOrder() {
    chef.receiveOrder(testOrder);
    assertEquals(1, chef.getOrderCount());
    assertNotNull(chef.getOrder(testOrder.getId()));
  }

  @Test
  @DisplayName("Test receive null order throws exception")
  void testReceiveNullOrder() {
    assertThrows(IllegalArgumentException.class, () -> chef.receiveOrder(null));
  }

  @Test
  @DisplayName("Test get order")
  void testGetOrder() {
    chef.receiveOrder(testOrder);
    Order retrievedOrder = chef.getOrder(testOrder.getId());
    assertEquals(testOrder, retrievedOrder);
  }

  @Test
  @DisplayName("Test get non-existent order throws exception")
  void testGetNonExistentOrder() {
    assertThrows(OrderNotFoundException.class, () -> chef.getOrder(999));
  }

  @Test
  @DisplayName("Test start preparing order")
  void testStartPreparingOrder() {
    chef.receiveOrder(testOrder);
    chef.startPreparingOrder(testOrder.getId());
    assertEquals(Order.Status.Preparing, testOrder.getStatus());
  }

  @Test
  @DisplayName("Test complete order")
  void testCompleteOrder() {
    chef.receiveOrder(testOrder);
    chef.startPreparingOrder(testOrder.getId());
    chef.completeOrder(testOrder.getId());
    assertEquals(Order.Status.ReadyForDelivery, testOrder.getStatus());
  }

  @Test
  @DisplayName("Test cancel order")
  void testCancelOrder() {
    chef.receiveOrder(testOrder);
    assertEquals(1, chef.getOrderCount());

    chef.cancelOrder(testOrder.getId());
    assertEquals(0, chef.getOrderCount());

    // Order should no longer exist
    assertThrows(OrderNotFoundException.class, () -> chef.getOrder(testOrder.getId()));
  }

  @Test
  @DisplayName("Test update order status")
  void testUpdateOrderStatus() {
    chef.receiveOrder(testOrder);
    assertEquals(Order.Status.Placed, testOrder.getStatus());

    chef.updateOrderStatus(testOrder.getId(), Order.Status.Preparing);
    assertEquals(Order.Status.Preparing, testOrder.getStatus());
  }

  @Test
  @DisplayName("Test add special request")
  void testAddSpecialRequest() {
    chef.receiveOrder(testOrder);

    // This method should work without throwing exceptions
    chef.addSpecialRequest(testOrder.getId(), "No onions please");

    // Verify order still exists
    assertNotNull(chef.getOrder(testOrder.getId()));
  }

  @Test
  @DisplayName("Test get all orders")
  void testGetAllOrders() {
    chef.receiveOrder(testOrder);

    List<Order> allOrders = chef.getAllOrders();
    assertEquals(1, allOrders.size());
    assertTrue(allOrders.contains(testOrder));
  }

  @Test
  @DisplayName("Test get active orders")
  void testGetActiveOrders() {
    chef.receiveOrder(testOrder);
    chef.startPreparingOrder(testOrder.getId());

    List<Order> activeOrders = chef.getActiveOrders();
    assertEquals(1, activeOrders.size());
    assertTrue(activeOrders.contains(testOrder));
  }

  @Test
  @DisplayName("Test get pending orders")
  void testGetPendingOrders() {
    chef.receiveOrder(testOrder);

    List<Order> pendingOrders = chef.getPendingOrders();
    assertEquals(1, pendingOrders.size());
    assertTrue(pendingOrders.contains(testOrder));
  }

  @Test
  @DisplayName("Test get ready orders")
  void testGetReadyOrders() {
    chef.receiveOrder(testOrder);
    chef.startPreparingOrder(testOrder.getId());
    chef.completeOrder(testOrder.getId());

    List<Order> readyOrders = chef.getReadyOrders();
    assertEquals(1, readyOrders.size());
    assertTrue(readyOrders.contains(testOrder));
  }

  @Test
  @DisplayName("Test order counts")
  void testOrderCounts() {
    assertEquals(0, chef.getOrderCount());
    assertEquals(0, chef.getActiveOrderCount());

    chef.receiveOrder(testOrder);
    assertEquals(1, chef.getOrderCount());

    chef.startPreparingOrder(testOrder.getId());
    assertEquals(1, chef.getActiveOrderCount());
  }

  @Test
  @DisplayName("Test chef busy status")
  void testChefBusyStatus() {
    assertFalse(chef.isBusy());

    chef.receiveOrder(testOrder);
    chef.startPreparingOrder(testOrder.getId());
    assertTrue(chef.isBusy());
  }

  @Test
  @DisplayName("Test toString")
  void testToString() {
    String result = chef.toString();
    assertNotNull(result);
    assertTrue(result.contains("Alice"));
    assertTrue(result.contains("C123"));
  }

  @Test
  @DisplayName("Test send to delivery")
  void testSendToDelivery() {
    // Prepare order for delivery
    chef.receiveOrder(testOrder);
    chef.startPreparingOrder(testOrder.getId());
    chef.completeOrder(testOrder.getId());

    // Create delivery staff and test sendToDelivery
    Delivery deliveryStaff = new Delivery("Bob", "789 Delivery St", "555-9999", "D456");
    chef.sendToDelivery(testOrder.getId(), deliveryStaff);
    assertEquals(Order.Status.OutForDelivery, testOrder.getStatus());
  }

  @Test
  @DisplayName("Test send to delivery with null staff throws exception")
  void testSendToDeliveryNullStaff() {
    // Prepare order for delivery
    chef.receiveOrder(testOrder);
    chef.startPreparingOrder(testOrder.getId());
    chef.completeOrder(testOrder.getId());

    // Test error case
    assertThrows(
        IllegalArgumentException.class,
        () -> chef.sendToDelivery(testOrder.getId(), null),
        "Should throw exception when delivery staff is null");
  }
}
