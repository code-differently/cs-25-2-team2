package com.cs_25_2_team2.RestaurantManagementApp.legacy_tests;
import com.cs_25_2_team2.RestaurantManagementApp.Menu;
import com.cs_25_2_team2.RestaurantManagementApp.MenuItem;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Test class for the Menu functionality. */
public class MenuTest {

  private Menu menu;
  private MenuItem frenchFries;
  private MenuItem bakedPotato;

  @BeforeEach
  void setUp() {
    // Create a new menu
    menu = new Menu();

    // Create some menu items for testing
    frenchFries =
        new MenuItem(
            1, "French Fries", 3.99, MenuItem.CookedType.Fried, MenuItem.PotatoType.Russet, true);

    bakedPotato =
        new MenuItem(
            2,
            "Loaded Baked Potato",
            5.99,
            MenuItem.CookedType.Baked,
            MenuItem.PotatoType.Russet,
            true);
  }

  @Test
  void testAddMenuItem() {
    // Test adding menu items
    menu.addMenuItem(frenchFries);
    assertEquals(1, menu.getItems().size());

    menu.addMenuItem(bakedPotato);
    assertEquals(2, menu.getItems().size());

    assertTrue(menu.getItems().contains(frenchFries));
    assertTrue(menu.getItems().contains(bakedPotato));
  }

  @Test
  void testGetLastUpdated() {
    // Test that lastUpdated is set correctly
    Date now = new Date();
    Date menuDate = menu.getLastUpdated();

    // The menu date should be close to the current time
    // Allow 1 second tolerance for test execution
    long diffMillis = Math.abs(now.getTime() - menuDate.getTime());
    assertTrue(diffMillis < 1000, "Menu's lastUpdated date should be recent");
  }

  @Test
  void testAvailability() {
    // Test initial availability
    assertTrue(menu.isAvailable(), "Menu should be available by default");

    // Test setting availability to false
    menu.setAvailability(false);
    assertFalse(menu.isAvailable(), "Menu should not be available after setting false");

    // Test setting availability back to true
    menu.setAvailability(true);
    assertTrue(menu.isAvailable(), "Menu should be available after setting true");
  }

  @Test
  void testGetItemById() {
    // Add items to menu
    menu.addMenuItem(frenchFries);
    menu.addMenuItem(bakedPotato);

    // Test finding items by ID
    MenuItem foundItem = menu.getItemById(1);
    assertNotNull(foundItem, "Should find menu item with ID 1");
    assertEquals("French Fries", foundItem.getDishName());

    foundItem = menu.getItemById(2);
    assertNotNull(foundItem, "Should find menu item with ID 2");
    assertEquals("Loaded Baked Potato", foundItem.getDishName());

    // Test looking for non-existent ID
    foundItem = menu.getItemById(999);
    assertNull(foundItem, "Should not find menu item with non-existent ID");
  }

  @Test
  void testGetAvailableItems() {
    // Add items with different availability
    menu.addMenuItem(frenchFries); // available
    bakedPotato.setAvailability(false); // not available
    menu.addMenuItem(bakedPotato);

    // Test getting available items
    List<MenuItem> availableItems = menu.getAvailableItems();
    assertEquals(1, availableItems.size(), "Should only return available items");
    assertEquals(frenchFries, availableItems.get(0));
    assertFalse(availableItems.contains(bakedPotato));

    // Make all items available and test again
    bakedPotato.setAvailability(true);
    availableItems = menu.getAvailableItems();
    assertEquals(2, availableItems.size(), "Should return both items now");
    assertTrue(availableItems.contains(frenchFries));
    assertTrue(availableItems.contains(bakedPotato));
  }

  @Test
  void testGetItemsByCookingType() {
    // Create additional menu items with different cooking types
    MenuItem mashedPotatoes =
        new MenuItem(
            3,
            "Mashed Potatoes",
            4.99,
            MenuItem.CookedType.Mashed,
            MenuItem.PotatoType.Russet,
            true);

    // Add all items
    menu.addMenuItem(frenchFries); // Fried
    menu.addMenuItem(bakedPotato); // Baked
    menu.addMenuItem(mashedPotatoes); // Mashed

    // Test filtering by cooking type
    List<MenuItem> friedItems = menu.getItemsByCookingType(MenuItem.CookedType.Fried);
    assertEquals(1, friedItems.size());
    assertTrue(friedItems.contains(frenchFries));

    List<MenuItem> bakedItems = menu.getItemsByCookingType(MenuItem.CookedType.Baked);
    assertEquals(1, bakedItems.size());
    assertTrue(bakedItems.contains(bakedPotato));

    // Test filtering by a cooking type not in the menu
    List<MenuItem> grilledItems = menu.getItemsByCookingType(MenuItem.CookedType.Grilled);
    assertEquals(0, grilledItems.size());
  }

  @Test
  void testGetItemsByPotatoType() {
    // Create menu item with different potato type
    MenuItem sweetPotatoFries =
        new MenuItem(
            3,
            "Sweet Potato Fries",
            4.99,
            MenuItem.CookedType.Fried,
            MenuItem.PotatoType.JapaneseSweet,
            true);

    // Add all items
    menu.addMenuItem(frenchFries); // Russet
    menu.addMenuItem(bakedPotato); // Russet
    menu.addMenuItem(sweetPotatoFries); // JapaneseSweet

    // Test filtering by potato type
    List<MenuItem> russetItems = menu.getItemsByPotatoType(MenuItem.PotatoType.Russet);
    assertEquals(2, russetItems.size());
    assertTrue(russetItems.contains(frenchFries));
    assertTrue(russetItems.contains(bakedPotato));

    List<MenuItem> sweetItems = menu.getItemsByPotatoType(MenuItem.PotatoType.JapaneseSweet);
    assertEquals(1, sweetItems.size());
    assertTrue(sweetItems.contains(sweetPotatoFries));

    // Test filtering by a potato type not in the menu
    List<MenuItem> yukonItems = menu.getItemsByPotatoType(MenuItem.PotatoType.YukonGold);
    assertEquals(0, yukonItems.size());
  }

  @Test
  void testGetItemCount() {
    // Initial count should be 0
    assertEquals(0, menu.getItemCount());

    // Add one item
    menu.addMenuItem(frenchFries);
    assertEquals(1, menu.getItemCount());

    // Add another item
    menu.addMenuItem(bakedPotato);
    assertEquals(2, menu.getItemCount());
  }

  @Test
  void testGetAvailableItemById() {
    menu.addMenuItem(frenchFries);

    // Test getting available item
    MenuItem item = menu.getAvailableItemById(1);
    assertEquals(frenchFries, item);
  }

  @Test
  void testRemoveMenuItem() {
    menu.addMenuItem(frenchFries);
    menu.addMenuItem(bakedPotato);
    assertEquals(2, menu.getItemCount());

    // Remove existing item
    menu.removeMenuItem(1);
    assertEquals(1, menu.getItemCount());
    assertNull(menu.getItemById(1));

    // Try to remove non-existent item (should not crash)
    menu.removeMenuItem(999);
    assertEquals(1, menu.getItemCount());
  }

  @Test
  void testMenuBasicOperations() {
    // Simple test to add coverage for any missed instructions
    Date originalDate = menu.getLastUpdated();
    assertNotNull(originalDate, "Menu should have a last updated date");

    // Test menu availability
    assertTrue(menu.isAvailable(), "Menu should be available by default");

    menu.setAvailability(false);
    assertFalse(menu.isAvailable(), "Menu should not be available after setting to false");

    menu.setAvailability(true);
    assertTrue(menu.isAvailable(), "Menu should be available after setting to true");
  }
}
