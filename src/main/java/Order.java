package com.cs_25_2_team2.RestaurantManagementApp;

import java.sql.Date;
import java.util.List;

public class Order {
    private static int nextId = 1; // Static counter for generating unique IDs
    private final int id; // Order ID
    private final Customer customer; // Customer who placed the order
    private final List<CartItem> items; // Items in the order
    private String status; // Order status (e.g., "Pending", "Completed")
    private final double totalPrice; // Total price of the order
    private final Date createdAt; // Timestamp when the order was created

    public Order(int id, Customer customer, List<CartItem> items, Date createdAt) {
        this.id = nextId++;
        this.customer = customer;
        this.items = items;
        this.status = "Placed";
        this.totalPrice = items.stream().mapToDouble(CartItem::getSubtotal).sum();
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public Customer getCustomer() { 
        return customer; 
    }

    public List<CartItem> getItems() { 
        return items;
    }

    public double getTotalPrice() { 
        return totalPrice;
    }

    public Date getCreatedAt() { 
        return createdAt;
    }

    public String getStatus() { 
        return status;
    }
     
    // Order status updates
    public void updateStatus(String newStatus) {
        this.status = newStatus;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Order #" + id + " for " + customer.getName() + "\n");
        for (CartItem ci : items) {
            sb.append(" - ").append(ci).append("\n");
        }
        sb.append("Total: $").append(totalPrice).append("\n");
        sb.append("Status: ").append(status).append("\n");
        sb.append("Created: ").append(createdAt);
        return sb.toString();
    }
    
}

