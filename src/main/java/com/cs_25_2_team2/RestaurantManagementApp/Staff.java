package com.cs_25_2_team2.RestaurantManagementApp;

public abstract class Staff extends Person {
  protected String id;

  public Staff(String name, String id) {
    super(name);
    this.id = id;
  }

  public String getId() {
    return id;
  }

  // TODO: Add method to get order contents
}
