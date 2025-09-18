package com.cs_25_2_team2.RestaurantManagementApp;

public class CartItem {
    private MenuItem menuItem;
    private int quantity;

    public CartItem(MenuItem menuItem, int quantity){
        this.menuItem = menuItem;
        this.quantity = quantity;
    }

    public MenuItem getMenuItem(){
        return menuItem;
    }

    public int getQuantity(){
        return quantity;
    }

    public void setQuantity(int quantity){
        this.quantity = quantity;
    }
    
    public double getSubtotal(){
        return menuItem.getPrice()* quantity;
    }

    @Override
    public String toString(){
        return quantity + " x " + menuItem.getDishName() + " = $"+getSubtotal();
    }
}
