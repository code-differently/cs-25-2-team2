package com.cs_25_2_team2.RestaurantManagementApp;

/**
 * Example class showing how Customer and Menu interact. This is for demonstration purposes only and
 * can be modified or removed.
 */
public class CustomerMenuExample {

  /** Main method to demonstrate Customer interacting with a Menu. */
  public static void main(String[] args) {
    // Create a new menu
    Menu menu = new Menu();

    // Add some menu items
    MenuItem frenchFries =
        new MenuItem(
            1, "French Fries", 3.99, MenuItem.CookedType.Fried, MenuItem.PotatoType.Russet, true);

    MenuItem bakedPotato =
        new MenuItem(
            2,
            "Loaded Baked Potato",
            5.99,
            MenuItem.CookedType.Baked,
            MenuItem.PotatoType.Russet,
            true);

    MenuItem sweetPotatoFries =
        new MenuItem(
            3,
            "Sweet Potato Fries",
            4.99,
            MenuItem.CookedType.Fried,
            MenuItem.PotatoType.JapaneseSweet,
            true);

    // Add ingredients
    Ingredient salt = new Ingredient("Salt", false, false, 0.0);
    Ingredient cheese = new Ingredient("Cheddar Cheese", true, true, 0.99);
    Ingredient bacon = new Ingredient("Bacon Bits", true, true, 1.50);

    frenchFries.addIngredient(salt);
    bakedPotato.addIngredient(salt);
    bakedPotato.addIngredient(cheese);
    bakedPotato.addIngredient(bacon);

    // Add items to menu
    menu.addMenuItem(frenchFries);
    menu.addMenuItem(bakedPotato);
    menu.addMenuItem(sweetPotatoFries);

    // Create a customer
    Customer customer = new Customer(1, "John Doe", "123 Main St", "555-123-4567");

    // Customer views menu
    System.out.println("Customer " + customer.getName() + " views the menu:");
    System.out.println(customer.viewMenu(menu));

    // Make some items unavailable and view again
    bakedPotato.setAvailability(false);
    sweetPotatoFries.setAvailability(false);
    System.out.println("\nAfter some items become unavailable:");
    System.out.println(customer.viewMenu(menu));

    // Make all items unavailable
    frenchFries.setAvailability(false);
    System.out.println("\nWhen no items are available:");
    System.out.println(customer.viewMenu(menu));

    // Make menu itself unavailable
    menu.setAvailability(false);
    System.out.println("\nWhen menu is unavailable:");
    System.out.println(customer.viewMenu(menu));
  }
}
