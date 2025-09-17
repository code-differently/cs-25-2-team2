package com.cs_25_2_team2.RestaurantManagementApp;

public class Customer {
  private int customerId;
  private String customerName;
  private String address;
  private String phoneNumber;

  public Customer(int customerId, String customerName, String address, String phoneNumber) {
    this.customerId = customerId;
    this.customerName = customerName;
    this.address = address;
    this.phoneNumber = phoneNumber;
  }

  public int getCustomerId() {
    return customerId;
  }

  public String getName() {
    return customerName;
  }

  public String getAddress() {
    return address;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setName(String name) {
    this.customerName = name;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  public void setCustomerId(int customerId) {
    this.customerId = customerId;
  }

  @Override
  public String toString() {
    return "Customer{id="
        + customerId
        + ", name='"
        + customerName
        + '\''
        + ", address='"
        + address
        + '\''
        + ", phoneNumber='"
        + phoneNumber
        + '\''
        + '}';
  }
}
