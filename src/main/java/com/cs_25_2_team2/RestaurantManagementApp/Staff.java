package com.cs_25_2_team2.RestaurantManagementApp;

public abstract class Staff extends Person {
  protected String id;

  public Staff(String name, String id, String phoneNumber) {
    super(name, id, phoneNumber);
    this.id = id;
  }

  public String getId() {
    return id;
  }
}
