package com.cs_25_2_team2.RestaurantManagementApp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class IngredientTest {

  private Ingredient salt;
  private Ingredient cheese;
  private Ingredient bacon;
  private Ingredient butter;
  private Ingredient sourCream;
  private Ingredient chives;
  private Ingredient cinnamon;
  private Ingredient seaSalt;
  private Ingredient blackPepper;
  private Ingredient heavyCream;
  private Ingredient brownSugar;
  private Ingredient egg;
  private Ingredient herb;
  private Ingredient chickenBroth;
  private Ingredient beefBroth;

  @BeforeEach
  void setUp() {
    salt = new Ingredient("Salt", false, false, 0.0);
    cheese = new Ingredient("Cheddar Cheese", true, true, 0.99);
    bacon = new Ingredient("Bacon Bits", true, true, 1.50);
    butter = new Ingredient("Butter", true, true, 0.50);
    sourCream = new Ingredient("Sour Cream", true, true, 0.75);
    chives = new Ingredient("Chives", true, true, 0.25);
    cinnamon = new Ingredient("Cinnamon", true, true, 0.30);
    chickenBroth = new Ingredient("Chicken Broth", true, true, 0.50);
    beefBroth = new Ingredient("Beef Broth", true, true, 0.60);
  }

  @Test
  void testGetIngredientName() {
    assertEquals("Salt", salt.getIngredientName());
    assertEquals("Cheddar Cheese", cheese.getIngredientName());
    assertEquals("Bacon Bits", bacon.getIngredientName());
    assertEquals("Butter", butter.getIngredientName());
    assertEquals("Sour Cream", sourCream.getIngredientName());
    assertEquals("Chives", chives.getIngredientName());
    assertEquals("Cinnamon", cinnamon.getIngredientName());
  }

  @Test
  void testIsAdditionalTopping() {
    assertFalse(salt.isAdditionalTopping());
    assertTrue(cheese.isAdditionalTopping());
    assertTrue(bacon.isAdditionalTopping());
    assertTrue(butter.isAdditionalTopping());
    assertTrue(sourCream.isAdditionalTopping());
    assertTrue(chives.isAdditionalTopping());
    assertTrue(cinnamon.isAdditionalTopping());
  }

  @Test
  void testIsOptional() {
    assertFalse(salt.isOptional());
    assertTrue(cheese.isOptional());
    assertTrue(bacon.isOptional());
    assertTrue(butter.isOptional());
    assertTrue(sourCream.isOptional());
    assertTrue(chives.isOptional());
    assertTrue(cinnamon.isOptional());
  }

  @Test
  void testGetExtraCost() {
    assertEquals(0.0, salt.getExtraCost());
    assertEquals(0.99, cheese.getExtraCost());
    assertEquals(1.50, bacon.getExtraCost());
    assertEquals(0.50, butter.getExtraCost());
    assertEquals(0.75, sourCream.getExtraCost());
    assertEquals(0.25, chives.getExtraCost());
    assertEquals(0.30, cinnamon.getExtraCost());
  }

  @Test
  void testSetExtraCost() {
    cheese.setExtraCost(1.20);
    assertEquals(1.20, cheese.getExtraCost());
    bacon.setExtraCost(1.75);
    assertEquals(1.75, bacon.getExtraCost());
    butter.setExtraCost(0.60);
    assertEquals(0.60, butter.getExtraCost());
    sourCream.setExtraCost(0.80);
    assertEquals(0.80, sourCream.getExtraCost());
    chives.setExtraCost(0.35);
    assertEquals(0.35, chives.getExtraCost());
    cinnamon.setExtraCost(0.40);
    assertEquals(0.40, cinnamon.getExtraCost());
  }

  @Test
  void testToString() {
    assertTrue(salt.toString().contains("Salt"));
    assertTrue(salt.toString().contains("additionalTopping=false"));
    assertTrue(salt.toString().contains("optional=false"));

    assertTrue(cheese.toString().contains("Cheddar Cheese"));
    assertTrue(cheese.toString().contains("additionalTopping=true"));
    assertTrue(cheese.toString().contains("optional=true"));
    assertTrue(cheese.toString().contains("$0.99"));
  }

  @Test
  void testVegetarianDetection() {
    // Vegetarian ingredients
    assertTrue(salt.isVegetarian(), "Salt should be vegetarian");
    assertTrue(cheese.isVegetarian(), "Cheese should be vegetarian");
    assertTrue(butter.isVegetarian(), "Butter should be vegetarian");
    assertTrue(sourCream.isVegetarian(), "Sour Cream should be vegetarian");
    assertTrue(chives.isVegetarian(), "Chives should be vegetarian");
    assertTrue(cinnamon.isVegetarian(), "Cinnamon should be vegetarian");

    // Non-vegetarian ingredients (auto-detected by keyword)
    assertFalse(bacon.isVegetarian(), "Bacon should not be vegetarian");
    assertFalse(chickenBroth.isVegetarian(), "Chicken broth should not be vegetarian");
    assertFalse(beefBroth.isVegetarian(), "Beef broth should not be vegetarian");

    // Test manual setting of vegetarian status
    Ingredient tofu = new Ingredient("Tofu", true, true, 1.0, true);
    assertTrue(tofu.isVegetarian(), "Tofu should be vegetarian");

    // Test changing vegetarian status
    tofu.setVegetarian(false);
    assertFalse(tofu.isVegetarian(), "Tofu should not be vegetarian after changing status");
  }

  @Test
  void testNonVegetarianException() {
    MenuItem vegetarianDish =
        new MenuItem(
            1,
            "Vegetarian Mashed Potatoes",
            4.99,
            MenuItem.CookedType.Mashed,
            MenuItem.PotatoType.Russet,
            true);

    // Add vegetarian ingredients - should work fine
    vegetarianDish.addIngredient(butter);
    vegetarianDish.addIngredient(salt);

    // Adding non-vegetarian without enforcement - should work
    vegetarianDish.addIngredient(bacon);

    // Create a new dish for the exception test
    MenuItem strictVegetarianDish =
        new MenuItem(
            2,
            "Strict Vegetarian Mashed Potatoes",
            5.99,
            MenuItem.CookedType.Mashed,
            MenuItem.PotatoType.YukonGold,
            true);

    // Adding vegetarian ingredient with enforcement - should work
    strictVegetarianDish.addIngredient(butter, true);

    // Adding non-vegetarian with enforcement - should throw exception
    Exception exception =
        assertThrows(
            Ingredient.NonVegetarianIngredientException.class,
            () -> {
              strictVegetarianDish.addIngredient(bacon, true);
            });

    // Verify exception message
    assertTrue(
        exception.getMessage().contains("Cannot add non-vegetarian ingredient 'Bacon Bits'"));
    assertTrue(exception.getMessage().contains("meat"));
  }
}
