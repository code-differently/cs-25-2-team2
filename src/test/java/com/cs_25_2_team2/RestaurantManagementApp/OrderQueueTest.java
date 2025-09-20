package com.cs_25_2_team2.RestaurantManagementApp;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.cs_25_2_team2.RestaurantManagementApp.exceptions.OrderNotFoundException;

public class OrderQueueTest {
  private OrderQueue orderQueue;
  private Order order1, order2, order3;
  private Customer customer;

  @BeforeEach
  void setUp() {
    orderQueue = new OrderQueue();
    customer = new Customer(1, "John Doe", "123 Main St", "555-1234");

    // Create test orders
    List<CartItem> items1 = new ArrayList<>();
    items1.add(
        new CartItem(
            new MenuItem(
                1, "Fries", 3.99, MenuItem.CookedType.Fried, MenuItem.PotatoType.Russet, true),
            1));

    List<CartItem> items2 = new ArrayList<>();
    items2.add(
        new CartItem(
            new MenuItem(
                2, "Burger", 8.99, MenuItem.CookedType.Fried, MenuItem.PotatoType.Russet, true),
            1));

    List<CartItem> items3 = new ArrayList<>();
    items3.add(
        new CartItem(
            new MenuItem(
                3, "Salad", 6.99, MenuItem.CookedType.Baked, MenuItem.PotatoType.YukonGold, true),
            1));

    order1 = Order.withId(1, customer, items1, new Date(System.currentTimeMillis()));
    order2 = Order.withId(2, customer, items2, new Date(System.currentTimeMillis()));
    order3 = Order.withId(3, customer, items3, new Date(System.currentTimeMillis()));
  }

  @Test
  @DisplayName("Test basic add and remove operations")
  void testBasicOperations() {
    assertTrue(orderQueue.isEmpty());
    assertEquals(0, orderQueue.size());

    orderQueue.add(order1);
    assertFalse(orderQueue.isEmpty());
    assertEquals(1, orderQueue.size());

    Order removed = orderQueue.remove();
    assertEquals(order1.getId(), removed.getId());
    assertTrue(orderQueue.isEmpty());
  }

  @Test
  @DisplayName("Test priority ordering")
  void testPriorityOrdering() {
    // Add orders with different priorities
    orderQueue.add(order1, 3); // Normal priority
    orderQueue.add(order2, 1); // High priority
    orderQueue.add(order3, 5); // Low priority

    assertEquals(3, orderQueue.size());

    // High priority should come first
    Order first = orderQueue.remove();
    assertEquals(order2.getId(), first.getId());

    // Normal priority should come second
    Order second = orderQueue.remove();
    assertEquals(order1.getId(), second.getId());

    // Low priority should come last
    Order third = orderQueue.remove();
    assertEquals(order3.getId(), third.getId());
  }

  @Test
  @DisplayName("Test FIFO within same priority")
  void testFIFOWithinPriority() throws InterruptedException {
    // Add multiple orders with same priority
    orderQueue.add(order1, 2);
    Thread.sleep(1); // Ensure different timestamps
    orderQueue.add(order2, 2);
    Thread.sleep(1);
    orderQueue.add(order3, 2);

    // Should come out in FIFO order
    assertEquals(order1.getId(), orderQueue.remove().getId());
    assertEquals(order2.getId(), orderQueue.remove().getId());
    assertEquals(order3.getId(), orderQueue.remove().getId());
  }

  @Test
  @DisplayName("Test peek operation")
  void testPeek() {
    assertNull(orderQueue.peek());

    orderQueue.add(order1, 2);
    orderQueue.add(order2, 1); // Higher priority

    Order peeked = orderQueue.peek();
    assertEquals(order2.getId(), peeked.getId());
    assertEquals(2, orderQueue.size()); // Should not remove

    // Peek again should return same order
    Order peekedAgain = orderQueue.peek();
    assertEquals(order2.getId(), peekedAgain.getId());
  }

  @Test
  @DisplayName("Test remove by ID")
  void testRemoveById() {
    orderQueue.add(order1);
    orderQueue.add(order2);
    orderQueue.add(order3);

    Order removed = orderQueue.remove(order2.getId());
    assertEquals(order2.getId(), removed.getId());
    assertEquals(2, orderQueue.size());
    assertFalse(orderQueue.contains(order2.getId()));

    // Try to remove non-existent order
    assertThrows(OrderNotFoundException.class, () -> orderQueue.remove(999));
  }

  @Test
  @DisplayName("Test update priority")
  void testUpdatePriority() {
    orderQueue.add(order1, 3);
    orderQueue.add(order2, 2);

    // order2 should be first (higher priority)
    assertEquals(order2.getId(), orderQueue.peek().getId());

    // Update order2 to lower priority
    orderQueue.updatePriority(order2.getId(), 4);

    // Now order1 should be first
    assertEquals(order1.getId(), orderQueue.peek().getId());
  }

  @Test
  @DisplayName("Test update order")
  void testUpdateOrder() {
    orderQueue.add(order1, 2);

    // Create updated order
    List<CartItem> newItems = new ArrayList<>();
    newItems.add(
        new CartItem(
            new MenuItem(
                4, "Pizza", 12.99, MenuItem.CookedType.Baked, MenuItem.PotatoType.Russet, true),
            1));
    Order updatedOrder =
        Order.withId(order1.getId(), customer, newItems, new Date(System.currentTimeMillis()));

    orderQueue.update(order1.getId(), updatedOrder);
    assertEquals(1, orderQueue.size());
    assertTrue(orderQueue.contains(order1.getId()));
  }

  @Test
  @DisplayName("Test contains and get operations")
  void testContainsAndGet() {
    orderQueue.add(order1);
    orderQueue.add(order2);

    assertTrue(orderQueue.contains(order1.getId()));
    assertTrue(orderQueue.contains(order2.getId()));
    assertFalse(orderQueue.contains(999));

    Order retrieved = orderQueue.get(order1.getId());
    assertEquals(order1.getId(), retrieved.getId());

    assertThrows(OrderNotFoundException.class, () -> orderQueue.get(999));
  }

  @Test
  @DisplayName("Test getAll and getByPriority")
  void testGetMethods() {
    orderQueue.add(order1, 1);
    orderQueue.add(order2, 2);
    orderQueue.add(order3, 1);

    List<Order> allOrders = orderQueue.getAll();
    assertEquals(3, allOrders.size());

    List<Order> priority1Orders = orderQueue.getByPriority(1);
    assertEquals(2, priority1Orders.size());

    List<Order> priority3Orders = orderQueue.getByPriority(3);
    assertEquals(0, priority3Orders.size());
  }

  @Test
  @DisplayName("Test iterator")
  void testIterator() {
    orderQueue.add(order1, 3);
    orderQueue.add(order2, 1);
    orderQueue.add(order3, 2);

    Iterator<Order> iterator = orderQueue.iterator();
    assertTrue(iterator.hasNext());

    // Should iterate in priority order: order2 (p1), order3 (p2), order1 (p3)
    Order first = iterator.next();
    assertEquals(order2.getId(), first.getId());

    Order second = iterator.next();
    assertEquals(order3.getId(), second.getId());

    Order third = iterator.next();
    assertEquals(order1.getId(), third.getId());

    assertFalse(iterator.hasNext());
    assertThrows(NoSuchElementException.class, iterator::next);
  }

  @Test
  @DisplayName("Test iterator remove")
  void testIteratorRemove() {
    orderQueue.add(order1);
    orderQueue.add(order2);

    Iterator<Order> iterator = orderQueue.iterator();
    iterator.next();
    iterator.remove();

    assertEquals(1, orderQueue.size());
  }

  @Test
  @DisplayName("Test enhanced for loop")
  void testEnhancedForLoop() {
    orderQueue.add(order1, 2);
    orderQueue.add(order2, 1);

    int count = 0;
    for (Order order : orderQueue) {
      assertNotNull(order);
      count++;
    }
    assertEquals(2, count);
  }

  @Test
  @DisplayName("Test clear operation")
  void testClear() {
    orderQueue.add(order1);
    orderQueue.add(order2);
    orderQueue.add(order3);

    assertEquals(3, orderQueue.size());
    orderQueue.clear();
    assertEquals(0, orderQueue.size());
    assertTrue(orderQueue.isEmpty());
  }

  @Test
  @DisplayName("Test exception handling")
  void testExceptionHandling() {
    // Test null order
    assertThrows(IllegalArgumentException.class, () -> orderQueue.add(null));

    // Test invalid priority
    assertThrows(IllegalArgumentException.class, () -> orderQueue.add(order1, 0));
    assertThrows(IllegalArgumentException.class, () -> orderQueue.add(order1, 6));

    // Test remove from empty queue
    assertThrows(NoSuchElementException.class, () -> orderQueue.remove());

    // Test update with null order
    orderQueue.add(order1);
    assertThrows(IllegalArgumentException.class, () -> orderQueue.update(order1.getId(), null));
  }

  @Test
  @DisplayName("Test toString method")
  void testToString() {
    orderQueue.add(order1, 3);
    orderQueue.add(order2, 1);

    String queueString = orderQueue.toString();
    assertTrue(queueString.contains("OrderQueue"));
    assertTrue(queueString.contains("Order#" + order1.getId()));
    assertTrue(queueString.contains("Order#" + order2.getId()));
  }
}
