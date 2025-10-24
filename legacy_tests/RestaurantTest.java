package com.cs_25_2_team2.RestaurantManagementApp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Test class for Restaurant - the central coordinator. Tests demonstrate SOLID principles in
 * action.
 */
public class RestaurantTest {
  private Restaurant restaurant;
  private Chef chef;
  private Delivery delivery;
  private Customer customer;
  private MenuItem menuItem;

  @BeforeEach
  void setUp() {
    restaurant = new Restaurant("Test Restaurant", "123 Test St");

    // Create staff
    chef = new Chef("Test Chef", "456 Chef Ave", "555-CHEF", "CH001");
    delivery = new Delivery("Test Delivery", "789 Delivery Rd", "555-DELV", "DL001");

    // Create customer
    customer = new Customer(1, "John Doe", "123 Main St", "555-1234");

    // Create menu item
    menuItem =
        new MenuItem(
            1, "Test Burger", 9.99, MenuItem.CookedType.Fried, MenuItem.PotatoType.Russet, true);
  }

  @Test
  @DisplayName("Test restaurant creation and basic properties")
  void testRestaurantCreation() {
    assertEquals("Test Restaurant", restaurant.getName());
    assertEquals("123 Test St", restaurant.getAddress());
    assertFalse(restaurant.isOpen());
    assertEquals(0, restaurant.getChefs().size());
    assertEquals(0, restaurant.getDeliveryStaff().size());
  }

  @Test
  @DisplayName("Test staff management - Single Responsibility Principle")
  void testStaffManagement() {
    // Add chef
    restaurant.addChef(chef);
    assertEquals(1, restaurant.getChefs().size());
    assertEquals(chef, restaurant.findStaffById("CH001"));

    // Add delivery staff
    restaurant.addDeliveryStaff(delivery);
    assertEquals(1, restaurant.getDeliveryStaff().size());
    assertEquals(delivery, restaurant.findStaffById("DL001"));

    // Test duplicate ID prevention
    Chef duplicateChef = new Chef("Another Chef", "999 Dup St", "555-DUP", "CH001");
    assertThrows(IllegalArgumentException.class, () -> restaurant.addChef(duplicateChef));
  }

  @Test
  @DisplayName("Test restaurant opening - business rule validation")
  void testRestaurantOpening() {
    // Cannot open without staff
    assertThrows(IllegalStateException.class, () -> restaurant.openRestaurant());

    // Add chef but no delivery
    restaurant.addChef(chef);
    assertThrows(IllegalStateException.class, () -> restaurant.openRestaurant());

    // Add delivery staff - now can open
    restaurant.addDeliveryStaff(delivery);
    restaurant.openRestaurant();
    assertTrue(restaurant.isOpen());

    // Cannot open again
    assertThrows(IllegalStateException.class, () -> restaurant.openRestaurant());
  }

  @Test
  @DisplayName("Test order processing workflow - demonstrates coordination")
  void testOrderProcessing() {
    // Setup restaurant
    restaurant.addChef(chef);
    restaurant.addDeliveryStaff(delivery);
    restaurant.addMenuItem(menuItem);
    restaurant.registerCustomer(customer);
    restaurant.openRestaurant();

    // Customer places order
    customer.getCart().addItem(menuItem, 2);
    Order order = restaurant.processCustomerOrder(customer);

    assertNotNull(order);
    assertEquals(customer, order.getCustomer());
    assertEquals(1, order.getItems().size()); // 1 cart item with quantity 2
    assertTrue(restaurant.getOrderQueue().contains(order.getId()));
  }

  @Test
  @DisplayName("Test menu management - Open/Closed Principle")
  void testMenuManagement() {
    restaurant.addMenuItem(menuItem);

    MenuItem retrievedItem = restaurant.getMenu().getItemById(1);
    assertEquals(menuItem.getDishName(), retrievedItem.getDishName());

    // Test availability update
    restaurant.updateMenuItemAvailability(1, false);
    assertFalse(retrievedItem.isAvailable());

    restaurant.updateMenuItemAvailability(1, true);
    assertTrue(retrievedItem.isAvailable());
  }

  @Test
  @DisplayName("Test polymorphism - Liskov Substitution Principle")
  void testPolymorphism() {
    restaurant.addChef(chef);
    restaurant.addDeliveryStaff(delivery);

    // Both Chef and Delivery extend Staff - polymorphism works
    Staff foundChef = restaurant.findStaffById("CH001");
    Staff foundDelivery = restaurant.findStaffById("DL001");

    assertInstanceOf(Chef.class, foundChef);
    assertInstanceOf(Delivery.class, foundDelivery);

    assertEquals("Chef", foundChef.getRole());
    assertEquals("Delivery", foundDelivery.getRole());
  }

  @Test
  @DisplayName("Test restaurant status reporting")
  void testStatusReporting() {
    restaurant.addChef(chef);
    restaurant.addDeliveryStaff(delivery);
    restaurant.openRestaurant();

    Restaurant.RestaurantStatus status = restaurant.getStatus();

    assertTrue(status.isOpen());
    assertEquals(1, status.totalChefs());
    assertEquals(1, status.availableChefs()); // Chef not busy
    assertEquals(1, status.totalDeliveryStaff());
    assertEquals(1, status.availableDeliveryStaff()); // Delivery not busy
    assertEquals(0.0, status.totalRevenue()); // No orders yet
  }

  @Test
  @DisplayName("Test customer registration")
  void testCustomerRegistration() {
    restaurant.registerCustomer(customer);
    assertEquals(1, restaurant.getCustomers().size());
    assertEquals(customer, restaurant.findCustomerById(1));

    // Test duplicate customer ID
    Customer duplicateCustomer = new Customer(1, "Jane Doe", "456 Oak St", "555-5678");
    assertThrows(
        IllegalArgumentException.class, () -> restaurant.registerCustomer(duplicateCustomer));
  }

  @Test
  @DisplayName("Test statistics tracking")
  void testStatisticsTracking() {
    restaurant.addChef(chef);
    restaurant.addDeliveryStaff(delivery);
    restaurant.addMenuItem(menuItem);
    restaurant.registerCustomer(customer);
    restaurant.openRestaurant();

    // Process an order
    customer.getCart().addItem(menuItem, 1);
    Order order = restaurant.processCustomerOrder(customer);

    Restaurant.RestaurantStats stats = restaurant.getStats();
    assertEquals(1, stats.getTotalOrdersProcessed());
    assertEquals(order.getTotalPrice(), stats.getTotalRevenue());
    assertTrue(stats.getPopularItems().containsKey(menuItem.getDishName()));
  }

  @Test
  @DisplayName("Test exception handling")
  void testExceptionHandling() {
    // Test null validations
    assertThrows(IllegalArgumentException.class, () -> restaurant.addChef(null));
    assertThrows(IllegalArgumentException.class, () -> restaurant.addDeliveryStaff(null));
    assertThrows(IllegalArgumentException.class, () -> restaurant.registerCustomer(null));

    // Test closed restaurant
    restaurant.addChef(chef);
    restaurant.addDeliveryStaff(delivery);
    // Don't open restaurant

    customer.getCart().addItem(menuItem, 1);
    assertThrows(IllegalStateException.class, () -> restaurant.processCustomerOrder(customer));
  }

  @Test
  @DisplayName("Test restaurant closing and reporting")
  void testRestaurantClosing() {
    restaurant.addChef(chef);
    restaurant.addDeliveryStaff(delivery);
    restaurant.openRestaurant();

    assertTrue(restaurant.isOpen());

    restaurant.closeRestaurant();
    assertFalse(restaurant.isOpen());
  }

  @Test
  @DisplayName("Test order priority calculation")
  void testOrderPriorityCalculation() {
    restaurant.addChef(chef);
    restaurant.addDeliveryStaff(delivery);
    restaurant.addMenuItem(menuItem);
    restaurant.registerCustomer(customer);
    restaurant.openRestaurant();

    // Small order (should get high priority)
    customer.getCart().addItem(menuItem, 1);
    Order smallOrder = restaurant.processCustomerOrder(customer);

    // Large order
    Customer customer2 = new Customer(2, "Jane Doe", "456 Oak St", "555-5678");
    restaurant.registerCustomer(customer2);
    customer2.getCart().addItem(menuItem, 10); // Large quantity
    restaurant.processCustomerOrder(customer2);

    // Both orders should be in queue
    assertEquals(2, restaurant.getOrderQueue().size());

    // Small order should have higher priority (processed first)
    Order nextOrder = restaurant.getOrderQueue().peek();
    assertEquals(smallOrder.getId(), nextOrder.getId());
  }

  @Test
  @DisplayName("Test restaurant toString method")
  void testRestaurantToString() {
    String result = restaurant.toString();
    assertNotNull(result);
    assertTrue(result.contains("Test Restaurant"));
    assertTrue(result.contains("123 Test St"));
    assertTrue(result.contains("isOpen=false")); // Restaurant starts closed
    assertTrue(result.contains("chefs=0"));
    assertTrue(result.contains("delivery=0"));
  }

  @Test
  @DisplayName("Test restaurant getOpenedAt")
  void testGetOpenedAt() {
    assertNotNull(restaurant.getOpenedAt());
    // Should be recent time (within last few seconds)
    assertTrue(restaurant.getOpenedAt().isBefore(java.time.LocalDateTime.now().plusSeconds(1)));
  }

  @Test
  @DisplayName("Test remove menu item")
  void testRemoveMenuItem() {
    restaurant.addMenuItem(menuItem);
    assertEquals(1, restaurant.getMenu().getItemCount());

    restaurant.removeMenuItem(1);
    assertEquals(0, restaurant.getMenu().getItemCount());

    // Removing non-existent item should not crash
    restaurant.removeMenuItem(999);
    assertEquals(0, restaurant.getMenu().getItemCount());
  }

  @Test
  @DisplayName("Test processKitchenQueue method")
  void testProcessKitchenQueue() {
    // Set up restaurant with staff and menu
    restaurant.addChef(chef);
    restaurant.addDeliveryStaff(delivery);
    restaurant.addMenuItem(menuItem);
    restaurant.registerCustomer(customer);
    restaurant.openRestaurant();

    // Test 1: Process empty queue (should not throw error)
    restaurant.processKitchenQueue();
    assertTrue(restaurant.isOpen());

    // Test 2: Add order and process queue
    customer.getCart().addItem(menuItem, 1);
    Order order = restaurant.processCustomerOrder(customer);

    // Process the queue - this should move orders to chefs
    restaurant.processKitchenQueue();

    // Verify restaurant is still operational
    assertTrue(restaurant.isOpen());
    assertNotNull(order);
    assertEquals(Order.Status.Preparing, order.getStatus());
  }

  @Test
  @DisplayName("Test completeOrder and deliverOrder methods exception handling")
  void testCompleteAndDeliverOrderMethods() {
    // Set up restaurant
    restaurant.addChef(chef);
    restaurant.addDeliveryStaff(delivery);
    restaurant.registerCustomer(customer);
    restaurant.addMenuItem(menuItem);
    restaurant.openRestaurant();

    // These methods should throw appropriate exceptions for invalid orders
    // Testing exception handling verifies the validation logic
    Exception exception1 =
        assertThrows(
            Exception.class,
            () -> {
              restaurant.completeOrder(999, chef.getId()); // Non-existent order
            },
            "Should throw exception for non-existent order");
    assertNotNull(exception1);

    Exception exception2 =
        assertThrows(
            Exception.class,
            () -> {
              restaurant.deliverOrder(999, delivery.getId()); // Non-existent order
            },
            "Should throw exception for non-existent order");
    assertNotNull(exception2);

    // Verify restaurant is still operational
    assertTrue(restaurant.isOpen());
  }

  @Test
  @DisplayName("Test additional Restaurant methods for coverage")
  void testAdditionalRestaurantMethods() {
    // Test restaurant status methods
    Restaurant.RestaurantStatus status = restaurant.getStatus();
    assertNotNull(status);
    assertFalse(status.isOpen()); // Restaurant starts closed

    // Test getters
    assertEquals("Test Restaurant", restaurant.getName());
    assertEquals("123 Test St", restaurant.getAddress());
    assertNotNull(restaurant.getMenu());
    assertNotNull(restaurant.getOrderQueue());
    assertNotNull(restaurant.getChefs());
    assertNotNull(restaurant.getDeliveryStaff());
    assertNotNull(restaurant.getCustomers());
    assertNotNull(restaurant.getStats());
    assertNotNull(restaurant.getOpenedAt());

    // Test toString
    String restaurantStr = restaurant.toString();
    assertNotNull(restaurantStr);
    assertTrue(restaurantStr.contains("Test Restaurant"));

    // Test customer management
    Customer customer2 = new Customer(2, "Jane Doe", "456 Oak St", "555-5678");
    restaurant.registerCustomer(customer2);
    assertEquals(customer2, restaurant.findCustomerById(2));
    assertNull(restaurant.findCustomerById(999)); // Non-existent customer

    // Test menu operations
    MenuItem menuItem2 =
        new MenuItem(
            2,
            "Test Fries",
            4.99,
            MenuItem.CookedType.Fried,
            MenuItem.PotatoType.JapaneseSweet,
            true);
    restaurant.addMenuItem(menuItem2);
    restaurant.updateMenuItemAvailability(2, false);
    restaurant.removeMenuItem(2);

    // Test staff finding
    restaurant.addChef(chef);
    restaurant.addDeliveryStaff(delivery);
    assertNotNull(restaurant.findStaffById(chef.getId()));
    assertNotNull(restaurant.findStaffById(delivery.getId()));
    assertNull(restaurant.findStaffById("NON_EXISTENT"));
  }
}
