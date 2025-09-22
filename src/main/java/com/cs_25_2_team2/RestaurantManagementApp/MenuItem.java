package com.cs_25_2_team2.RestaurantManagementApp;

import java.util.ArrayList;
import java.util.List;

public class MenuItem {
  private final int dishId; // unique identifier for each dish
  private final String dishName; // name of dish (e.g., "French Fries")
  private final List<Ingredient> ingredients = new ArrayList<>(); // list of ingredients
  private final double price; // cost of item
  private final CookedType cookedType; // e.g., "Fried", "Baked", "Soupped"
  private final PotatoType potatoType; // e.g., "Russet", "Yukon Gold", "Sweet"
  private boolean availability;

  public enum CookedType {
    Fried,
    Baked,
    Grilled,
    Mashed,
    Roasted,
    Boiled,
    Steamed,
    Scalloped,
    Soupped
  }

  public enum PotatoType {
    Russet,
    New,
    YukonGold,
    Kennebec,
    AllBlue,
    AdirondackBlue,
    RedBliss,
    GermanButterball,
    RedThumb,
    RussianBanana,
    PurplePeruvian,
    JapaneseSweet,
    HannahSweet,
    JewelYams
  }

  /**
   * Creates a new menu item.
   *
   * @param dishId The unique identifier for the dish.
   * @param dishName The name of the dish.
   * @param description The short description.
   * @param ingredients The list of ingredients.
   * @param price The cost of the item.
   * @param availability The availability of the item.
   */
  public MenuItem(
      int dishId,
      String dishName,
      double price,
      CookedType cookedType,
      PotatoType potatoType,
      boolean availability) {
    this.dishId = dishId;
    this.dishName = dishName;
    this.price = price;
    this.cookedType = cookedType;
    this.potatoType = potatoType;
    this.availability = availability;
  }

  // Getters and setters
  public int getDishId() {
    return dishId;
  }

  public String getDishName() {
    return dishName;
  }

  public List<Ingredient> getIngredients() {
    return ingredients;
  }

  /** Add an ingredient to this menu item without enforcing vegetarian status. */
  public void addIngredient(Ingredient ingredient) {
    ingredients.add(ingredient);
  }

  /**
   * Add an ingredient to this menu item with vegetarian enforcement option. This is an auxiliary
   * method primarily used for special dietary requirements.
   *
   * @param ingredient The ingredient to add
   * @param enforceVegetarian If true, will throw an exception when trying to add a non-vegetarian
   *     ingredient
   * @throws Ingredient.NonVegetarianIngredientException if enforceVegetarian is true and a
   *     non-vegetarian ingredient is added
   */
  public void addIngredient(Ingredient ingredient, boolean enforceVegetarian) {
    if (enforceVegetarian && !ingredient.isVegetarian()) {
      throw new Ingredient.NonVegetarianIngredientException(ingredient.getIngredientName());
    }
    ingredients.add(ingredient);
  }

  public void removeIngredient(Ingredient ingredient) {
    ingredients.remove(ingredient);
  }

  // Calculate final price with toppings
  public double getPrice() {
    double total = price;
    for (Ingredient i : ingredients) {
      total += i.getExtraCost();
    }
    return total;
  }

  public CookedType getCookedType() {
    return cookedType;
  }

  public PotatoType getPotatoType() {
    return potatoType;
  }

  public boolean isAvailable() {
    return availability;
  }

  public void setAvailability(boolean availability) {
    this.availability = availability;
  }

  /**
   * Determines if the dish is vegetarian based on its ingredients.
   *
   * @return True if the dish is vegetarian (contains no meat, fish, poultry, or animal byproducts)
   */
  public boolean isVegetarian() {
    // Base potato dishes are vegetarian by default
    // Check if any ingredient is non-vegetarian
    for (Ingredient ingredient : ingredients) {
      if (!ingredient.isVegetarian()) {
        return false;
      }
    }
    return true;
  }

  /**
   * Checks if this dish contains a specific ingredient by name.
   *
   * @param ingredientName The name of the ingredient to check for
   * @return True if the ingredient is in this dish
   */
  public boolean containsIngredient(String ingredientName) {
    for (Ingredient ingredient : ingredients) {
      if (ingredient.getIngredientName().equalsIgnoreCase(ingredientName)) {
        return true;
      }
    }
    return false;
  }

  @Override
  public String toString() {
    return "MenuItem{"
        + "dishId="
        + dishId
        + ", dishName='"
        + dishName
        + " ("
        + cookedType
        + ", "
        + potatoType
        + ") - $"
        + getPrice()
        + '}';
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null || getClass() != obj.getClass()) return false;

    MenuItem other = (MenuItem) obj;
    return this.dishId == other.dishId; // Two menu items are the same if they have the same ID
  }

  @Override
  public int hashCode() {
    return Integer.hashCode(dishId);
  }
}
