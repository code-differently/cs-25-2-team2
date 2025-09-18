package com.cs_25_2_team2.RestaurantManagementApp;

/**
 * Represents an ingredient in a menu item. Contains information about the ingredient's name, cost,
 * and dietary attributes.
 */
public class Ingredient {
  private final String name;
  private final boolean additionalTopping; // true if it's an add-on topping
  private final boolean optional; // true if customer can choose it
  private double extraCost; // additional cost if it's an add-on
  private boolean isVegetarian; // whether the ingredient is vegetarian

  /** Creates a new ingredient. */
  public Ingredient(String name, boolean additionalTopping, boolean optional, double extraCost) {
    this.name = name;
    this.additionalTopping = additionalTopping;
    this.optional = optional;
    this.extraCost = extraCost;
    // By default, assume ingredients are vegetarian unless specified otherwise
    this.isVegetarian = true;

    // Check if the ingredient is non-vegetarian based on its name
    if (containsNonVegetarianKeywords(name)) {
      this.isVegetarian = false;
    }
  }

  /**
   * Creates a new ingredient with specified vegetarian status.
   *
   * @param name The name of the ingredient
   * @param additionalTopping Whether it's an add-on topping
   * @param optional Whether it's optional
   * @param extraCost The extra cost of the ingredient
   * @param isVegetarian Whether the ingredient is vegetarian
   */
  public Ingredient(
      String name,
      boolean additionalTopping,
      boolean optional,
      double extraCost,
      boolean isVegetarian) {
    this.name = name;
    this.additionalTopping = additionalTopping;
    this.optional = optional;
    this.extraCost = extraCost;
    this.isVegetarian = isVegetarian;
  }

  /**
   * Checks if the ingredient name contains non-vegetarian keywords.
   *
   * @param ingredientName The name of the ingredient to check
   * @return True if the ingredient is likely not vegetarian
   */
  private boolean containsNonVegetarianKeywords(String ingredientName) {
    String lowerCaseName = ingredientName.toLowerCase();

    // List of common non-vegetarian ingredients
    String[] nonVegKeywords = {
      "meat",
      "beef",
      "chicken",
      "pork",
      "lamb",
      "veal",
      "turkey",
      "duck",
      "fish",
      "salmon",
      "tuna",
      "tilapia",
      "cod",
      "anchovy",
      "shellfish",
      "shrimp",
      "crab",
      "lobster",
      "oyster",
      "mussel",
      "clam",
      "scallop",
      "bacon",
      "ham",
      "sausage",
      "pepperoni",
      "prosciutto",
      "salami",
      "gelatin",
      "lard",
      "tallow",
      "suet",
      "animal fat",
      "animal shortening"
    };

    for (String keyword : nonVegKeywords) {
      if (lowerCaseName.contains(keyword)) {
        return true;
      }
    }

    return false;
  }

  public String getIngredientName() {
    return name;
  }

  public boolean isAdditionalTopping() {
    return additionalTopping;
  }

  public boolean isOptional() {
    return optional;
  }

  public double getExtraCost() {
    return extraCost;
  }

  public void setExtraCost(double extraCost) {
    this.extraCost = extraCost;
  }

  public boolean isVegetarian() {
    return isVegetarian;
  }

  public void setVegetarian(boolean vegetarian) {
    this.isVegetarian = vegetarian;
  }

  /** Exception thrown when a non-vegetarian ingredient is added to a vegetarian dish. */
  public static class NonVegetarianIngredientException extends RuntimeException {
    public NonVegetarianIngredientException(String ingredientName) {
      super(
          "Cannot add non-vegetarian ingredient '"
              + ingredientName
              + "' as it contains flesh, by-products, or fats of an animal, including meat, poultry, fish, or shellfish.");
    }
  }

  // Print ingredient details for debugging
  @Override
  public String toString() {
    return "Ingredient{"
        + "name='"
        + name
        + '\''
        + ", additionalTopping="
        + additionalTopping
        + ", optional="
        + optional
        + (extraCost > 0 ? " ($" + extraCost + ")" : "")
        + ", vegetarian="
        + isVegetarian
        + '}';
  }
}
