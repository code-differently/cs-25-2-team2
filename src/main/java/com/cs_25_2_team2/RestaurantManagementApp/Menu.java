package com.cs_25_2_team2.RestaurantManagementApp;

import java.awt.MenuItem;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/** Represents a menu. */
public class Menu {
  private final List<MenuItem> items;
  private final Date lastUpdated;
  private boolean itemAvailability = true;

  /** Creates a new menu. */
  public Menu() {
    this.items = new ArrayList<>();
    this.lastUpdated = new Date();
  }

  /**
   * Adds a menu item to the menu.
   *
   * @param item The menu item to add.
   */
  public void addMenuItem(MenuItem item) {
    items.add(item);
  }

  /**
   * Gets all menu items.
   *
   * @return The list of menu items.
   */
  public List<MenuItem> getItems() {
    return items;
  }

  /**
   * Gets the last updated date.
   *
   * @return The last updated date.
   */
  public Date getLastUpdated() {
    return lastUpdated;
  }

  /**
   * Gets the menu availability.
   *
   * @return True if the menu is available, false otherwise.
   */
  public boolean isAvailable() {
    return itemAvailability; // Fixed variable name
  }

  /**
   * Sets the menu availability.
   *
   * @param availability The availability to set.
   */
  public void setAvailability(boolean availability) {
    this.itemAvailability = availability;
  }
}
