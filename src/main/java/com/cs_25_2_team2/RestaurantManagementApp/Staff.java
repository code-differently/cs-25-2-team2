package com.cs_25_2_team2.RestaurantManagementApp;

public abstract class Staff extends Person {
  protected String id;
  protected String role;

  public Staff(String name, String address, String phoneNumber, String id, String role) {
    super(name, address, phoneNumber);
    this.id = id;
    this.role = role;
  }

  public String getId() {
    return id;
  }

  public String getRole() {
    return role;
  }

  @Override
  public String toString() {
    return String.format(
        "Staff{name='%s', id='%s', role='%s', phone='%s'}", getName(), id, role, getPhoneNumber());
  }
}
