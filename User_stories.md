# User Stories & Acceptance Criteria

## 1. User Story: Browse Items
**As a user, I want to browse an item so that I can see what is available.**

### Acceptance Criteria
- The system displays a list of available menu items.  
- Each menu item shows its **name**, **description**, and **price**.  
- If no items are available, the system shows a *“No items available”* message.  

---

## 2. User Story: Add to Cart
**As a user, I want to select an item so that I can add it to my cart and see total.**

### Acceptance Criteria
- The user can select a menu item and add it to their cart.  
- The cart updates immediately with the added item and calculates total for Checkout.  
- The system prevents adding unavailable or out-of-stock items.
- The check out process creates an immutable order.

---

## 3. User Story: Deliver Order 
**As a delivery person, I want to mark an order as delivered after completing a delivery so that the customer knows their order has been delivered successfully.**

### Acceptance Criteria
- When I receive notification that an order is ready for pickup and delivery, I will collect the order and deliver it to the correct customer.    
- I can mark the order as **"Delivered"** in the system once I have completed the delivery.  
- The customer receives a confirmation (in-app or message) that their order has been delivered.  


## 4. User Story: Search Criteria
**As a customer I want to easily search/browse items on the webpage.**

### Acceptance Criteria
- The system provides a **search bar** on the items page.
- The user can type a **keyword** (e.g., item name or description) to filter results.
- If no matching items are found, the system displays a message: “No matching items found.”

## 5. User Story: Item Editing
**As a customer I want to be able to edit the items I ordered so they can better fit my criteria.**

### Acceptance Criteria
- The user can edit item options before checkout.
- The user can save changes to the order and see them reflected immediately in the cart.
- The user can view the details of items in their current order (name, description, quantity, price, etc.).

## 6. User Story: Chef
**As a Chef I want to check how many ingredients I have.**

### Acceptance Criteria 
- Each ingredient displays its name, current quantity, unit of measurement (e.g., lbs, oz, pieces), and status (e.g., in stock, low, out of stock).
- The system updates ingredient quantities automatically when items are used in an order.
- The system provides an inventory view showing all available ingredients.



