package com.cs_25_2_team2.RestaurantManagementApp.legacy_tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import com.cs_25_2_team2.RestaurantManagementApp.Cart;
import com.cs_25_2_team2.RestaurantManagementApp.CartItem;
import com.cs_25_2_team2.RestaurantManagementApp.MenuItem;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class CartTest {
  private Cart cart;
  private MenuItem frenchFries;
  private MenuItem bakedPotato;
  private MenuItem mashedPotatoes;

  @BeforeEach
  public void setUp() {
  // Initialize cart with user ID
  cart = new Cart(101L);

    // Create menu items for testing
    frenchFries =
        new MenuItem(
            1, "French Fries", 3.99, MenuItem.CookedType.Fried, MenuItem.PotatoType.Russet, true);

    bakedPotato =
        new MenuItem(
            2,
            "Loaded Baked Potato",
            4.99,
            MenuItem.CookedType.Baked,
            MenuItem.PotatoType.YukonGold,
            true);

    mashedPotatoes =
        new MenuItem(
            3,
            "Creamy Mashed Potatoes",
            5.99,
            MenuItem.CookedType.Mashed,
            MenuItem.PotatoType.YukonGold,
            true);
  }

  @Test
  @DisplayName("Test cart initialization")
  void testCartInitialization() {
    assertEquals(101, cart.getUserId(), "Cart should have correct user ID");
    assertTrue(cart.isEmpty(), "New cart should be empty");
    assertEquals(0, cart.getItems().size(), "New cart should have no items");
  }

  @Test
  @DisplayName("Test adding new items to cart")
  void testAddNewItems() {
    // Add single item
    cart.addItem(frenchFries, 2);
    List<CartItem> items = cart.getItems();

    assertEquals(1, items.size(), "Cart should have one item");
    assertEquals(frenchFries, items.get(0).getMenuItem(), "Cart should contain french fries");
    assertEquals(2, items.get(0).getQuantity(), "French fries quantity should be 2");

    // Add another item
    cart.addItem(bakedPotato, 1);
    items = cart.getItems();

    assertEquals(2, items.size(), "Cart should have two items");
    assertEquals(bakedPotato, items.get(1).getMenuItem(), "Second item should be baked potato");
    assertEquals(1, items.get(1).getQuantity(), "Baked potato quantity should be 1");
  }

  @Test
  @DisplayName("Test adding existing item increases quantity")
  void testAddExistingItem() {
    cart.addItem(frenchFries, 2);
    cart.addItem(frenchFries, 3);

    List<CartItem> items = cart.getItems();
    assertEquals(1, items.size(), "Cart should have one item type");
    assertEquals(5, items.get(0).getQuantity(), "Quantity should be sum of both additions");
  }

  @Test
  @DisplayName("Test removing items from cart")
  void testRemoveItems() {
    // Add multiple items
    cart.addItem(frenchFries, 2);
    cart.addItem(bakedPotato, 1);
    cart.addItem(mashedPotatoes, 3);

    assertEquals(3, cart.getItems().size(), "Cart should have three items");

    // Remove one item
    cart.removeItem(1); // Remove french fries

    List<CartItem> items = cart.getItems();
    assertEquals(2, items.size(), "Cart should have two items after removal");
    assertFalse(
        items.stream().anyMatch(item -> item.getMenuItem().getDishId() == 1),
        "French fries should be removed");
  }

  @Test
  @DisplayName("Test cart operations with invalid inputs")
  void testInvalidInputs() {
    // Test adding null item
    IllegalArgumentException nullException =
        assertThrows(
            IllegalArgumentException.class,
            () -> cart.addItem(null, 1),
            "Should throw exception when adding null item");
    assertEquals("MenuItem cannot be null", nullException.getMessage());

    // Test adding item with zero quantity
    IllegalArgumentException zeroException =
        assertThrows(
            IllegalArgumentException.class,
            () -> cart.addItem(frenchFries, 0),
            "Should throw exception when quantity is zero");
    assertEquals("Quantity must be positive", zeroException.getMessage());

    // Test adding item with negative quantity
    IllegalArgumentException negativeException =
        assertThrows(
            IllegalArgumentException.class,
            () -> cart.addItem(frenchFries, -1),
            "Should throw exception when quantity is negative");
    assertEquals("Quantity must be positive", negativeException.getMessage());
  }

  @Test
  @DisplayName("Test cart clear operation")
  void testClearCart() {
    // Add items
    cart.addItem(frenchFries, 2);
    cart.addItem(bakedPotato, 1);
    assertFalse(cart.isEmpty(), "Cart should not be empty after adding items");

    // Clear cart
    cart.clear();
    assertTrue(cart.isEmpty(), "Cart should be empty after clearing");
    assertEquals(0, cart.getItems().size(), "Cart should have no items after clearing");
  }

  @Test
  @DisplayName("Test cart item subtotal calculations")
  void testCartItemSubtotals() {
    cart.addItem(frenchFries, 2); // 3.99 * 2 = 7.98
    cart.addItem(bakedPotato, 1); // 4.99 * 1 = 4.99
    cart.addItem(mashedPotatoes, 3); // 5.99 * 3 = 17.97

    List<CartItem> items = cart.getItems();

    assertEquals(
        7.98, items.get(0).getSubtotal(), 0.01, "French fries subtotal should be price * quantity");
    assertEquals(
        4.99, items.get(1).getSubtotal(), 0.01, "Baked potato subtotal should be price * quantity");
    assertEquals(
        17.97,
        items.get(2).getSubtotal(),
        0.01,
        "Mashed potatoes subtotal should be price * quantity");
  }

  @Test
  @DisplayName("Test cart item quantity updates")
  void testCartItemQuantityUpdates() {
    // Add initial items
    cart.addItem(frenchFries, 2);
    cart.addItem(bakedPotato, 1);

    List<CartItem> items = cart.getItems();
    CartItem fries = items.get(0);

    // Test quantity updates
    fries.setQuantity(5);
    assertEquals(5, fries.getQuantity(), "Quantity should be updated to 5");
    assertEquals(19.95, fries.getSubtotal(), 0.01, "Subtotal should reflect new quantity");

    // Test invalid quantity update
    assertThrows(
        IllegalArgumentException.class,
        () -> fries.setQuantity(-1),
        "Should not allow negative quantity");
  }

  @Test
  @DisplayName("Test removing non-existent item")
  void testRemoveNonExistentItem() {
    cart.addItem(frenchFries, 1);
    int initialSize = cart.getItems().size();

    // Try to remove an item that doesn't exist
    cart.removeItem(999);

    assertEquals(
        initialSize,
        cart.getItems().size(),
        "Cart size should not change when removing non-existent item");
  }

  @Test
  @DisplayName("Test cart operations sequence")
  void testCartOperationsSequence() {
    assertTrue(cart.isEmpty(), "Cart should start empty");

    // Add items
    cart.addItem(frenchFries, 2);
    assertFalse(cart.isEmpty(), "Cart should not be empty after adding items");

    // Remove items
    cart.removeItem(1);
    assertTrue(cart.isEmpty(), "Cart should be empty after removing all items");

    // Add multiple items
    cart.addItem(frenchFries, 1);
    cart.addItem(bakedPotato, 1);
    cart.addItem(mashedPotatoes, 1);
    assertEquals(3, cart.getItems().size(), "Cart should have three items");

    // Clear cart
    cart.clear();
    assertTrue(cart.isEmpty(), "Cart should be empty after clearing");
    assertEquals(0, cart.getItems().size(), "Cart should have no items after clearing");
  }
}
