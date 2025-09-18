package com.cs_25_2_team2.RestaurantManagementApp;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MenuItemTest {
  private MenuItem frenchFries;
  private MenuItem bakedPotato;
  private MenuItem sweetPotato;
  private MenuItem sweetPotatoFries;
  private MenuItem loadedMashedPotato;

  private Ingredient salt;
  private Ingredient cheese;
  private Ingredient bacon;
  private Ingredient butter;
  private Ingredient sourCream;
  private Ingredient chives;
  private Ingredient cinnamon;

  @BeforeEach
  void setUp() {
    // Create test menu items
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

    // Create test ingredients
    salt = new Ingredient("Salt", false, false, 0.0);
    cheese = new Ingredient("Cheddar Cheese", true, true, 0.99);
    bacon = new Ingredient("Bacon Bits", true, true, 1.50);

    // Add ingredients to items
    frenchFries.addIngredient(salt);
    bakedPotato.addIngredient(salt);
    bakedPotato.addIngredient(cheese);
    bakedPotato.addIngredient(bacon);
  }

  @Test
  void testGetDishId() {
    assertEquals(1, frenchFries.getDishId());
    assertEquals(2, bakedPotato.getDishId());
  }

  @Test
  void testGetDishName() {
    assertEquals("French Fries", frenchFries.getDishName());
    assertEquals("Loaded Baked Potato", bakedPotato.getDishName());
  }

  @Test
  void testAddIngredient() {
    // Test adding an ingredient
    Ingredient ketchup = new Ingredient("Ketchup", false, false, 0.0);
    frenchFries.addIngredient(ketchup);
    assertTrue(frenchFries.getIngredients().contains(ketchup));
    assertEquals(2, frenchFries.getIngredients().size());

    // Test the overloaded addIngredient method with vegetarian enforcement
    MenuItem vegetarianDish =
        new MenuItem(
            5,
            "Vegetarian Fries",
            4.99,
            MenuItem.CookedType.Fried,
            MenuItem.PotatoType.Russet,
            true);

    // Adding vegetarian ingredient with enforcement should work
    vegetarianDish.addIngredient(ketchup, true);
    assertEquals(1, vegetarianDish.getIngredients().size());

    // Adding non-vegetarian with enforcement should throw exception
    Exception exception =
        assertThrows(
            Ingredient.NonVegetarianIngredientException.class,
            () -> {
              vegetarianDish.addIngredient(bacon, true);
            });

    // Adding non-vegetarian without enforcement should work
    vegetarianDish.addIngredient(bacon);
    assertEquals(2, vegetarianDish.getIngredients().size());
    assertFalse(vegetarianDish.isVegetarian(), "Dish with bacon should not be vegetarian");
  }

  @Test
  void testRemoveIngredient() {
    // Test removing an ingredient
    bakedPotato.removeIngredient(cheese);
    assertFalse(bakedPotato.getIngredients().contains(cheese));
    assertEquals(2, bakedPotato.getIngredients().size());
  }

  @Test
  void testGetPrice() {
    // Test basic price for french fries (no added cost ingredients)
    assertEquals(3.99, frenchFries.getPrice(), 0.001);

    // Test price with added cost ingredients for baked potato
    // The price is calculated as: base price (5.99) + cheese (0.99) + bacon (1.50) = 8.48
    assertEquals(8.48, bakedPotato.getPrice(), 0.001);
  }

  @Test
  void testGetIngredients() {
    assertEquals(1, frenchFries.getIngredients().size());
    assertEquals(3, bakedPotato.getIngredients().size());
    assertTrue(bakedPotato.getIngredients().contains(cheese));
  }

  @Test
  void testGetCookedType() {
    assertEquals(MenuItem.CookedType.Fried, frenchFries.getCookedType());
    assertEquals(MenuItem.CookedType.Baked, bakedPotato.getCookedType());
  }

  @Test
  void testGetPotatoType() {
    assertEquals(MenuItem.PotatoType.Russet, frenchFries.getPotatoType());
    assertEquals(MenuItem.PotatoType.Russet, bakedPotato.getPotatoType());
  }

  @Test
  void testContainsIngredient() {
    // Test ingredient detection
    assertTrue(frenchFries.containsIngredient("Salt"));
    assertTrue(bakedPotato.containsIngredient("Bacon Bits"));
    assertFalse(frenchFries.containsIngredient("Cheese"));

    // Test case insensitivity
    assertTrue(bakedPotato.containsIngredient("cheddar cheese"));
  }

  @Test
  void testHasSamePropertiesAs() {
    MenuItem similarFries =
        new MenuItem(
            42, "French Fries", 3.99, MenuItem.CookedType.Fried, MenuItem.PotatoType.Russet, true);
    similarFries.addIngredient(salt);

    // Test that they're considered to have the same properties
    assertTrue(frenchFries.hasSamePropertiesAs(similarFries));

    // Create a slightly different item
    MenuItem differentFries =
        new MenuItem(
            1,
            "French Fries",
            4.99, // Different price
            MenuItem.CookedType.Fried,
            MenuItem.PotatoType.Russet,
            true);

    // Test that they're considered different
    assertFalse(frenchFries.hasSamePropertiesAs(differentFries));
  }

  @Test
  void testIsVegetarian() {
    // French fries with just salt should be vegetarian
    assertTrue(frenchFries.isVegetarian(), "French fries should be vegetarian");

    // Baked potato has bacon which is non-vegetarian
    assertFalse(bakedPotato.isVegetarian(), "Baked potato with bacon should not be vegetarian");

    // Create a vegetarian version of the baked potato
    MenuItem vegBakedPotato =
        new MenuItem(
            3,
            "Vegetarian Baked Potato",
            5.99,
            MenuItem.CookedType.Baked,
            MenuItem.PotatoType.Russet,
            true);
    vegBakedPotato.addIngredient(salt);
    vegBakedPotato.addIngredient(cheese);
    assertTrue(vegBakedPotato.isVegetarian(), "Vegetarian baked potato should be vegetarian");
  }

  @Test
  void testIsAvailable() {
    // Test initial availability
    assertTrue(frenchFries.isAvailable());
    assertTrue(bakedPotato.isAvailable());
  }

  @Test
  void testSetAvailability() {
    // Test changing availability
    frenchFries.setAvailability(false);
    assertFalse(frenchFries.isAvailable());

    bakedPotato.setAvailability(false);
    // Test setting availability back to true
    frenchFries.setAvailability(true);
    assertTrue(frenchFries.isAvailable());
    frenchFries.setAvailability(true);
    assertTrue(frenchFries.isAvailable());
  }

  @Test
  void testToString() {
    String friesString = frenchFries.toString();
    assertTrue(friesString.contains("dishId=1"));
    assertTrue(friesString.contains("French Fries"));
    assertTrue(friesString.contains("Fried"));
    assertTrue(friesString.contains("Russet"));
  }
}
