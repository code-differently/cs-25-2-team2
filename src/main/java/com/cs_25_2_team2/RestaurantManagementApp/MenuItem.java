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

  // Add/remove ingredients
  public void addIngredient(Ingredient ingredient) {
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
}
