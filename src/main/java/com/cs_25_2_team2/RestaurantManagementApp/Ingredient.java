package com.cs_25_2_team2.RestaurantManagementApp;

public class Ingredient {
  private final String name;
  private final boolean additionalTopping; // true if it's an add-on topping
  private final boolean optional; // true if customer can choose it
  private double extraCost; // additional cost if it's an add-on

  public Ingredient(String name, boolean additionalTopping, boolean optional, double extraCost) {
    this.name = name;
    this.additionalTopping = additionalTopping;
    this.optional = optional;
    this.extraCost = extraCost;
  }

  public String getName() {
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
        + '}';
  }
}
