package com.cs_25_2_team2.RestaurantManagementApp.exceptions;

/** Exception thrown when attempting to order a menu item that is currently unavailable. */
public class MenuItemUnavailableException extends RuntimeException {
  private final int menuItemId;
  private final String menuItemName;

  public MenuItemUnavailableException(int menuItemId, String menuItemName) {
    super(
        String.format(
            "Menu item '%s' (ID: %d) is currently unavailable", menuItemName, menuItemId));
    this.menuItemId = menuItemId;
    this.menuItemName = menuItemName;
  }

  public int getMenuItemId() {
    return menuItemId;
  }

  public String getMenuItemName() {
    return menuItemName;
  }
}
