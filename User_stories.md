# User Stories & Acceptance Criteria

## 1. User Story: Browse Items
**As a user, I want to browse menu items so that I can see what is available.**

### Acceptance Criteria
1. 
    - **Given** the user is on the menu page,
    - **When** the system loads the available menu items,
    - **Then** the user should see a list of all available items.

---
2. 
    - **Given** the menu items are displayed,
    - **When** the user views each item,
    - **Then** the system should show the item’s name, description, and price.
---
3. 
    - **Given** there are no items available,
    - **When** the system loads the menu,
    - **Then** a message should appear stating “No items available.”
---

## 2. User Story: Add to Cart
**As a user, I want to select an item so that I can add it to my cart and see the total.**

### Acceptance Criteria
1. 
    - **Given** the user is viewing the menu,
    - **When** they select a menu item,
    - **Then** the item should be added to their cart.
---
2. 
    - **Given** an item has been added to the cart,
    - **When** the cart is updated,
    - **Then** the system should immediately recalculate and display the updated total for checkout.
---
3. 
    - **Given** an item is unavailable or out of stock,
    - **When** the user tries to add it to the cart,
    - **Then** the system should prevent the action and display an appropriate message.
---
4. 
    - **Given** the user proceeds to checkout,
    - **When** the order is created,
    - **Then** the system should generate an immutable order that cannot be changed after submission.
---

## 3. User Story: Search Criteria
**As a customer, I want to easily search or browse items on the webpage so that I can quickly find what I’m looking for.**

### Acceptance Criteria
1. 
    - **Given** the user is on the items page,
    - **When** the page loads,
    - **Then** a search bar should be visible and ready for input.
---
2. 
    - **Given** the user enters a keyword (such as an item name or description) into the search bar,
    - **When** the search is executed,
    - **Then** the system should filter and display only the matching items.
---
3. 
    - **Given** the user enters a keyword that does not match any item,
    - **When** the search is executed,
    - **Then** the system should display a message stating “No matching items found.”

---

## 4. User Story: Item Editing
**As a customer, I want to be able to edit the items I ordered so they can better fit my criteria.**

### Acceptance Criteria
1. 
    - **Given** the user has added items to their cart,
    - **When** they choose to edit an item,
    - **Then** the system should allow them to modify item options (e.g., quantity, size, or toppings) before checkout.
---
2. 
    - **Given** the user makes changes to an item in their cart,
    - **When** they save the edits,
    - **Then** the updated details should be reflected immediately in the cart’s summary and total.
---
3. 
    - **Given** the user is viewing their current order,
    - **When** they open the order details,
    - **Then** the system should display each item’s name, description, quantity, and price clearly.




