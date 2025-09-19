package com.cs_25_2_team2.RestaurantManagementApp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

public class MenuItemTest {
    // Test menu items representing different potato preparations
    private MenuItem frenchFries;
    private MenuItem bakedPotato;
    private MenuItem mashedPotatoes;
    private MenuItem potatoSoup;
    private MenuItem sweetPotatoFries;
    
    // Common ingredients used across different potato dishes
    private Ingredient salt;
    private Ingredient pepper;
    private Ingredient butter;
    private Ingredient cheese;
    private Ingredient bacon;
    private Ingredient sourCream;
    private Ingredient chives;
    private Ingredient garlic;
    private Ingredient cream;

  @BeforeEach
  void setUp() {
    // Create basic ingredients
    salt = new Ingredient("Salt", false, false, 0.0);
    pepper = new Ingredient("Black Pepper", false, false, 0.0);
    butter = new Ingredient("Butter", true, true, 0.50);
    cheese = new Ingredient("Cheddar Cheese", true, true, 0.99);
    bacon = new Ingredient("Bacon Bits", true, true, 1.50);
    sourCream = new Ingredient("Sour Cream", true, true, 0.75);
    chives = new Ingredient("Fresh Chives", false, false, 0.50);
    garlic = new Ingredient("Roasted Garlic", false, false, 0.50);
    cream = new Ingredient("Heavy Cream", true, true, 0.75);

    // Create different potato dishes
    frenchFries = new MenuItem(
        1,
        "Classic French Fries",
        3.99,
        MenuItem.CookedType.Fried,
        MenuItem.PotatoType.Russet,
        true
    );

    bakedPotato = new MenuItem(
        2,
        "Loaded Baked Potato",
        5.99,
        MenuItem.CookedType.Baked,
        MenuItem.PotatoType.Russet,
        true
    );

    mashedPotatoes = new MenuItem(
        3,
        "Creamy Mashed Potatoes",
        4.99,
        MenuItem.CookedType.Mashed,
        MenuItem.PotatoType.YukonGold,
        true
    );

    potatoSoup = new MenuItem(
        4,
        "Potato Leek Soup",
        6.99,
        MenuItem.CookedType.Soupped,
        MenuItem.PotatoType.YukonGold,
        true
    );

    sweetPotatoFries = new MenuItem(
        5,
        "Sweet Potato Fries",
        4.99,
        MenuItem.CookedType.Fried,
        MenuItem.PotatoType.JewelYams,
        true
    );

    // Add base ingredients to items
    frenchFries.addIngredient(salt);
    
    bakedPotato.addIngredient(salt);
    bakedPotato.addIngredient(butter);
    bakedPotato.addIngredient(cheese);
    bakedPotato.addIngredient(bacon);
    bakedPotato.addIngredient(sourCream);
    bakedPotato.addIngredient(chives);

    mashedPotatoes.addIngredient(salt);
    mashedPotatoes.addIngredient(butter);
    mashedPotatoes.addIngredient(pepper);
    mashedPotatoes.addIngredient(garlic);

    potatoSoup.addIngredient(salt);
    potatoSoup.addIngredient(pepper);
    potatoSoup.addIngredient(cream);

    sweetPotatoFries.addIngredient(salt);
  }

  @Test
  void testGetDishId() {
    assertEquals(1, frenchFries.getDishId());
    assertEquals(2, bakedPotato.getDishId());
  }

  @Test
  void testGetDishName() {
    assertEquals("Classic French Fries", frenchFries.getDishName());
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
    assertEquals(5, bakedPotato.getIngredients().size()); // salt, butter, bacon, sourCream, chives remain
  }

  @Test
  void testGetPrice() {
    // Test basic price for french fries (no added cost ingredients)
    assertEquals(3.99, frenchFries.getPrice(), 0.001);

    // Test price with added cost ingredients for baked potato
    // Base price (5.99) + butter (0.50) + cheese (0.99) + bacon (1.50) + sourCream (0.75) + chives (0.50) = 10.23
    assertEquals(10.23, bakedPotato.getPrice(), 0.001);
  }

  @Test
  void testGetIngredients() {
    assertEquals(1, frenchFries.getIngredients().size());
    assertEquals(6, bakedPotato.getIngredients().size()); // salt, butter, cheese, bacon, sourCream, chives
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
  @DisplayName("Test comparison of similar menu items")
  void testMenuItemComparison() {
    MenuItem similarFries = new MenuItem(
        42, "Classic French Fries", 3.99, 
        MenuItem.CookedType.Fried, MenuItem.PotatoType.Russet, true
    );
    similarFries.addIngredient(salt);

    assertEquals(frenchFries.getDishName(), similarFries.getDishName());
    assertEquals(frenchFries.getPrice(), similarFries.getPrice(), 0.001);
    assertEquals(frenchFries.getCookedType(), similarFries.getCookedType());
    assertEquals(frenchFries.getPotatoType(), similarFries.getPotatoType());
  }

  @ParameterizedTest
  @EnumSource(MenuItem.CookedType.class)
  @DisplayName("Test all cooking methods are supported")
  void testCookingMethods(MenuItem.CookedType cookType) {
    MenuItem testItem = new MenuItem(
        99,
        "Test Potato",
        5.99,
        cookType,
        MenuItem.PotatoType.Russet,
        true
    );
    assertEquals(cookType, testItem.getCookedType());
  }

  @ParameterizedTest
  @EnumSource(MenuItem.PotatoType.class)
  @DisplayName("Test all potato types are supported")
  void testPotatoTypes(MenuItem.PotatoType potatoType) {
    MenuItem testItem = new MenuItem(
        99,
        "Test Potato",
        5.99,
        MenuItem.CookedType.Baked,
        potatoType,
        true
    );
    assertEquals(potatoType, testItem.getPotatoType());
  }

  @Test
  @DisplayName("Test vegetarian status of different potato dishes")
  void testVegetarianStatus() {
    // French fries with just salt should be vegetarian
    assertTrue(frenchFries.isVegetarian(), "French fries should be vegetarian");

    // Loaded baked potato has bacon which is non-vegetarian
    assertFalse(bakedPotato.isVegetarian(), "Loaded baked potato should not be vegetarian");

    // Mashed potatoes with butter and garlic should be vegetarian
    assertTrue(mashedPotatoes.isVegetarian(), "Mashed potatoes should be vegetarian");

    // Sweet potato fries with just salt should be vegetarian
    assertTrue(sweetPotatoFries.isVegetarian(), "Sweet potato fries should be vegetarian");
  }

  @Test
  @DisplayName("Test ingredient modifications")
  void testIngredientModifications() {
    MenuItem customPotato = new MenuItem(
        6,
        "Custom Potato",
        4.99,
        MenuItem.CookedType.Baked,
        MenuItem.PotatoType.Russet,
        true
    );
    
    customPotato.addIngredient(butter);
    assertTrue(customPotato.containsIngredient("Butter"));
    
    customPotato.removeIngredient(butter);
    assertFalse(customPotato.containsIngredient("Butter"));
  }

  @Test
  @DisplayName("Test price calculations with ingredients")
  void testPriceCalculations() {
    // Basic french fries price
    assertEquals(3.99, frenchFries.getPrice(), 0.001);
    
    // Loaded baked potato with all toppings
    double expectedPrice = 5.99 + 0.50 + 0.99 + 1.50 + 0.75 + 0.50; // Base + butter + cheese + bacon + sour cream + chives
    assertEquals(expectedPrice, bakedPotato.getPrice(), 0.001);
  }

  @Test
  @DisplayName("Test availability management")
  void testAvailabilityManagement() {
    assertTrue(frenchFries.isAvailable(), "New items should be available by default");
    
    frenchFries.setAvailability(false);
    assertFalse(frenchFries.isAvailable(), "Item should be unavailable after setting to false");
    
    frenchFries.setAvailability(true);
    assertTrue(frenchFries.isAvailable(), "Item should be available after setting to true");
  }

  @Test
  @DisplayName("Test string representation")
  void testStringRepresentation() {
    String friesString = frenchFries.toString();
    assertTrue(friesString.contains("dishId=1"));
    assertTrue(friesString.contains("Classic French Fries"));
    assertTrue(friesString.contains("Fried"));
    assertTrue(friesString.contains("Russet"));
    assertTrue(friesString.contains("$3.99"));
  }
}
