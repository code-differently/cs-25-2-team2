package com.cs_25_2_team2.RestaurantManagementApp;

public abstract class Person {
  protected String name;

  public Person(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }
}
