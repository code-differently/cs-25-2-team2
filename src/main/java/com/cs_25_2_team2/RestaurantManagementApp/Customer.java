package com.cs_25_2_team2.RestaurantManagementApp;

import java.util.List;

public class Customer extends Person {
  private int customerId;
  private final Cart cart;

  public Customer(int customerId, String name, String address, String phoneNumber) {
    super(name, address, phoneNumber);
    this.customerId = customerId;
    this.cart = new Cart(customerId); // Each customer starts with an empty cart linked to their ID
  }

  public int getCustomerId() {
    return customerId;
  }

  public void setCustomerId(int customerId) {
    this.customerId = customerId;
  }

  public Cart getCart() {
    return cart;
  }

  // Convert Cart → Order
  public Order checkout() {
    if (cart.isEmpty()) {
      throw new IllegalStateException("Cart is empty. Cannot checkout.");
    }
    // Create new Order with current cart contents
    Order order =
        Order.createNew(this, cart.getItems(), new java.sql.Date(System.currentTimeMillis()));

    // Clear the cart for future use
    cart.clear();

    return order;
  }

  @Override
  public String toString() {
    return "Customer{id="
        + customerId
        + ", name='"
        + getName()
        + '\''
        + ", address='"
        + getAddress()
        + '\''
        + ", phoneNumber='"
        + getPhoneNumber()
        + '\''
        + '}';
  }

  /**
   * Customer views the menu and gets a formatted display of available items.
   *
   * @param menu The menu to view
   * @return A formatted string of the menu items or a message if no items are available
   */
  public String viewMenu(Menu menu) {
    StringBuilder displayMenu = new StringBuilder();

    if (!menu.isAvailable()) {
      return "Menu is currently unavailable.";
    }

    List<MenuItem> availableItems = menu.getAvailableItems();

    if (availableItems.isEmpty()) {
      return "No items available";
    }

    displayMenu.append("Menu Items:\n");
    for (MenuItem item : availableItems) {
      displayMenu
          .append(item.getDishId())
          .append(". ")
          .append(item.getDishName())
          .append(" (")
          .append(item.getCookedType())
          .append(", ")
          .append(item.getPotatoType())
          .append(") - $")
          .append(item.getPrice())
          .append("\n");
    }

    return displayMenu.toString();
  }
}
