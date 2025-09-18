package com.cs_25_2_team2.RestaurantManagementApp;

import java.util.ArrayList;
import java.util.List;

public class Cart {
    private int userId;
    private List<CartItem> items;

    public Cart(int userId){
        this.userId = userId;
        this.items = new ArrayList<>();
    }
     
}