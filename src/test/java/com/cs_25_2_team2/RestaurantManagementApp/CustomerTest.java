package com.cs_25_2_team2.RestaurantManagementApp;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/** Test class for the Customer functionality. */
public class CustomerTest {

  private Customer customer;
  private MenuItem frenchFries;
  private MenuItem bakedPotato;
  private Menu menu;

  @BeforeEach
  void setUp() {
    // Create a new customer for testing
    customer = new Customer(
        1,
        "John Doe",
        "123 Main St",
        "555-123-4567"
    );

    // Create test menu items with base ingredients
    frenchFries = new MenuItem(
        1,
        "Classic French Fries",
        3.99,
        MenuItem.CookedType.Fried,
        MenuItem.PotatoType.Russet,
        true    // Set as available
    );
    frenchFries.setAvailability(true); // Explicitly set availability
    frenchFries.addIngredient(new Ingredient("Salt", false, false, 0.0));

    bakedPotato = new MenuItem(
        2,
        "Loaded Baked Potato",
        4.99,
        MenuItem.CookedType.Baked,
        MenuItem.PotatoType.YukonGold,
        true    // Set as available
    );
    bakedPotato.setAvailability(true); // Explicitly set availability
    bakedPotato.addIngredient(new Ingredient("Salt", false, false, 0.0));
    bakedPotato.addIngredient(new Ingredient("Butter", true, true, 0.50));
    bakedPotato.addIngredient(new Ingredient("Cheddar Cheese", true, true, 0.99));

    // Set up menu
    menu = new Menu();
    menu.addMenuItem(frenchFries);
    menu.addMenuItem(bakedPotato);
  }

  @Test
  void testGetCustomerId() {
    assertEquals(1, customer.getCustomerId(), "Customer ID should be 1");
  }

  @Test
  @DisplayName("Test Customer inheritance from Person")
  void testInheritance() {
    assertTrue(customer instanceof Person, "Customer should be an instance of Person");
    
    // Test that inherited methods from Person work correctly
    customer.setName("Jane Smith");
    customer.setAddress("456 Oak Ave");
    customer.setPhoneNumber("555-987-6543");
    
    assertEquals("Jane Smith", customer.getName(), "Name should be set through Person's method");
    assertEquals("456 Oak Ave", customer.getAddress(), "Address should be set through Person's method");
    assertEquals("555-987-6543", customer.getPhoneNumber(), "Phone number should be set through Person's method");
  }

  @Test
  void testGetName() {
    assertEquals("John Doe", customer.getName(), "Customer name should be 'John Doe'");
  }

  @Test
  void testGetAddress() {
    assertEquals("123 Main St", customer.getAddress(), "Customer address should be '123 Main St'");
  }

  @Test
  void testGetPhoneNumber() {
    assertEquals(
        "555-123-4567", customer.getPhoneNumber(), "Phone number should be '555-123-4567'");
  }

  @Test
  void testSetName() {
    customer.setName("Jane Smith");
    assertEquals(
        "Jane Smith", customer.getName(), "Customer name should be updated to 'Jane Smith'");
  }

  @Test
  void testSetAddress() {
    customer.setAddress("456 Oak Ave");
    assertEquals(
        "456 Oak Ave",
        customer.getAddress(),
        "Customer address should be updated to '456 Oak Ave'");
  }

  @Test
  void testSetPhoneNumber() {
    customer.setPhoneNumber("555-987-6543");
    assertEquals(
        "555-987-6543",
        customer.getPhoneNumber(),
        "Phone number should be updated to '555-987-6543'");
  }

  @Test
  void testSetCustomerId() {
    customer.setCustomerId(99);
    assertEquals(99, customer.getCustomerId(), "Customer ID should be updated to 99");
  }

  @Test
  void testToString() {
    String customerString = customer.toString();
    assertTrue(customerString.contains("id=1"), "toString should contain the customer ID");
    assertTrue(
        customerString.contains("name='John Doe'"), "toString should contain the customer name");
    assertTrue(
        customerString.contains("address='123 Main St'"), "toString should contain the address");
    assertTrue(
        customerString.contains("phoneNumber='555-123-4567'"),
        "toString should contain the phone number");
  }

  @Test
  @DisplayName("Test viewing menu when menu is available")
  void testViewMenu() {
    String menuDisplay = customer.viewMenu(menu);
    
    // Basic existence checks
    assertTrue(menuDisplay.contains("Classic French Fries"), "Menu should contain French Fries");
    assertTrue(menuDisplay.contains("Loaded Baked Potato"), "Menu should contain Baked Potato");
    
    // Price checks (including ingredients)
    double expectedFriesPrice = 3.99;  // Base price + $0.00 for salt
    double expectedPotatoPrice = 4.99 + 0.50 + 0.99;  // Base price + butter + cheese
    
    assertTrue(menuDisplay.contains("$" + expectedFriesPrice), 
        "Menu should show French Fries price of $" + expectedFriesPrice);
    assertTrue(menuDisplay.contains("$" + expectedPotatoPrice), 
        "Menu should show Baked Potato price of $" + expectedPotatoPrice + " (including toppings)");
  }

  @Test
  @DisplayName("Test viewing menu when menu is unavailable")
  void testViewUnavailableMenu() {
    menu.setAvailability(false);
    String menuDisplay = customer.viewMenu(menu);
    assertEquals("Menu is currently unavailable.", menuDisplay);
  }

  @Test
  @DisplayName("Test viewing empty menu")
  void testViewEmptyMenu() {
    Menu emptyMenu = new Menu();
    String menuDisplay = customer.viewMenu(emptyMenu);
    assertEquals("No items available", menuDisplay);
  }

  @Test
  @DisplayName("Test cart operations")
  void testCartOperations() {
    Cart cart = customer.getCart();
    assertNotNull(cart, "Cart should be initialized with customer");
    assertEquals(customer.getCustomerId(), cart.getUserId(), "Cart should have customer's ID");
    
    // Test adding items to cart
    cart.addItem(frenchFries, 2);
    cart.addItem(bakedPotato, 1);
    
    List<CartItem> items = cart.getItems();
    assertEquals(2, items.size(), "Cart should have 2 items");
    
    // Verify first item
    CartItem firstItem = items.get(0);
    assertEquals(frenchFries, firstItem.getMenuItem());
    assertEquals(2, firstItem.getQuantity());
    
    // Verify second item
    CartItem secondItem = items.get(1);
    assertEquals(bakedPotato, secondItem.getMenuItem());
    assertEquals(1, secondItem.getQuantity());
  }

  @Test
  @DisplayName("Test checkout process")
  void testCheckout() {
    // Add items to cart
    Cart cart = customer.getCart();
    cart.addItem(frenchFries, 2);
    cart.addItem(bakedPotato, 1);
    
    // Perform checkout
    Order order = customer.checkout();
    
    // Verify order details
    assertNotNull(order, "Order should not be null after checkout");
    assertEquals(customer, order.getCustomer(), "Order should be associated with the correct customer");
    assertEquals(Order.Status.Placed, order.getStatus(), "New order should have 'Placed' status");
    
    List<CartItem> orderItems = order.getItems();
    assertNotNull(orderItems, "Order items list should not be null");
    assertEquals(2, orderItems.size(), "Order should contain 2 items");
    
    // Verify individual items in the order
    CartItem firstItem = orderItems.get(0);
    assertEquals(frenchFries, firstItem.getMenuItem(), "First item should be french fries");
    assertEquals(2, firstItem.getQuantity(), "French fries quantity should be 2");
    
    CartItem secondItem = orderItems.get(1);
    assertEquals(bakedPotato, secondItem.getMenuItem(), "Second item should be baked potato");
    assertEquals(1, secondItem.getQuantity(), "Baked potato quantity should be 1");
    
    // Verify cart is empty after checkout
    assertTrue(cart.isEmpty(), "Cart should be empty after checkout");
    assertEquals(0, cart.getItems().size(), "Cart should have no items after checkout");
    
    // Verify order total price
    double expectedTotal = (frenchFries.getPrice() * 2) + (bakedPotato.getPrice() * 1);
    assertEquals(expectedTotal, order.getTotalPrice(), 0.01, "Order total price should match items total");
  }

  @Test
  @DisplayName("Test checkout with empty cart")
  void testCheckoutEmptyCart() {
    IllegalStateException exception = assertThrows(IllegalStateException.class,
        () -> customer.checkout(),
        "Should throw IllegalStateException when checking out with empty cart");
    
    assertEquals("Cart is empty. Cannot checkout.", exception.getMessage(),
        "Exception message should indicate empty cart");
  }
}
