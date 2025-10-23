-- Restaurant Management System Database Schema

-- 1. RESTAURANT TABLE
CREATE TABLE restaurants (
    restaurant_id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    address TEXT NOT NULL,
    phone_number VARCHAR(20),
    is_open BOOLEAN DEFAULT FALSE
);

-- 2. CUSTOMERS TABLE
CREATE TABLE customers (
    customer_id BIGSERIAL PRIMARY KEY,
    username VARCHAR(12) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    address TEXT,
    phone_number VARCHAR(20),
    email VARCHAR(255) UNIQUE,
    
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 3. STAFF TABLE (Base table for all staff types)
CREATE TABLE staff (
    staff_id BIGSERIAL PRIMARY KEY,
    username VARCHAR(12) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    phone_number VARCHAR(20),
    role VARCHAR(50) NOT NULL CHECK (role IN ('Chef', 'Delivery')),
    restaurant_id BIGINT REFERENCES restaurants(restaurant_id),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 4. MENU ITEMS TABLE
CREATE TABLE menu_items (
    dish_id BIGSERIAL PRIMARY KEY,
    restaurant_id BIGINT REFERENCES restaurants(restaurant_id),
    dish_name VARCHAR(255) NOT NULL,
    category VARCHAR(20) NOT NULL CHECK (category IN ('Main Dish', 'Side', 'Soup')),
    price DECIMAL(10, 2) NOT NULL CHECK (price >= 0),
    cooked_type VARCHAR(50) NOT NULL CHECK (cooked_type IN (
        'Fried', 'Baked', 'Grilled', 'Mashed', 'Roasted', 
        'Boiled', 'Steamed', 'Scalloped', 'Soupped'
    )),
    potato_type VARCHAR(50) NOT NULL CHECK (potato_type IN (
        'Russet', 'New', 'YukonGold', 'Kennebec', 'AllBlue', 
        'AdirondackBlue', 'RedBliss', 'GermanButterball', 'RedThumb', 
        'RussianBanana', 'PurplePeruvian', 'JapaneseSweet', 'HannahSweet', 'JewelYams'
    )),
    is_available BOOLEAN DEFAULT TRUE,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 5. INGREDIENTS TABLE
CREATE TABLE ingredients (
    ingredient_id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    is_additional_topping BOOLEAN DEFAULT FALSE,
    is_optional BOOLEAN DEFAULT FALSE,
    extra_cost DECIMAL(8, 2) DEFAULT 0.00 CHECK (extra_cost >= 0),
    is_vegetarian BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 6. MENU ITEM INGREDIENTS (Many-to-Many relationship)
CREATE TABLE menu_item_ingredients (
    menu_item_id BIGINT REFERENCES menu_items(dish_id) ON DELETE CASCADE,
    ingredient_id BIGINT REFERENCES ingredients(ingredient_id) ON DELETE CASCADE,
    quantity DECIMAL(8, 2) DEFAULT 1.00,
    is_required BOOLEAN DEFAULT TRUE,
    PRIMARY KEY (menu_item_id, ingredient_id)
);

-- 7. ORDERS TABLE
CREATE TABLE orders (
    order_id BIGSERIAL PRIMARY KEY,
    customer_id BIGINT REFERENCES customers(customer_id),
    restaurant_id BIGINT REFERENCES restaurants(restaurant_id),
    total_price DECIMAL(10, 2) NOT NULL CHECK (total_price >= 0),
    status VARCHAR(50) NOT NULL DEFAULT 'Placed' CHECK (status IN (
        'Placed', 'Preparing', 'ReadyForDelivery', 'OutForDelivery', 'Delivered'
    )),
    assigned_chef_id BIGINT REFERENCES staff(staff_id),
    assigned_delivery_id BIGINT REFERENCES staff(staff_id),
    
    -- Payment Information for this order
    credit_card_last_four VARCHAR(4) NOT NULL,
    credit_card_token VARCHAR(255) NOT NULL,
    card_expiry_month INTEGER NOT NULL CHECK (card_expiry_month BETWEEN 1 AND 12),
    card_expiry_year INTEGER NOT NULL CHECK (card_expiry_year >= EXTRACT(YEAR FROM CURRENT_DATE)),
    cardholder_name VARCHAR(255) NOT NULL,
    
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    -- Constraints to ensure staff roles are correct
    CONSTRAINT check_chef_role 
        CHECK (assigned_chef_id IS NULL OR 
               EXISTS (SELECT 1 FROM staff WHERE staff_id = assigned_chef_id AND role = 'Chef')),
    CONSTRAINT check_delivery_role 
        CHECK (assigned_delivery_id IS NULL OR 
               EXISTS (SELECT 1 FROM staff WHERE staff_id = assigned_delivery_id AND role = 'Delivery'))
);

-- 8. ORDER ITEMS TABLE (Items in each order)
CREATE TABLE order_items (
    order_item_id BIGSERIAL PRIMARY KEY,
    order_id BIGINT REFERENCES orders(order_id) ON DELETE CASCADE,
    menu_item_id BIGINT REFERENCES menu_items(dish_id),
    quantity INTEGER NOT NULL CHECK (quantity > 0),
    unit_price DECIMAL(10, 2) NOT NULL CHECK (unit_price >= 0),
    subtotal DECIMAL(10, 2) NOT NULL CHECK (subtotal >= 0),
    special_instructions TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 9. ORDER ITEM CUSTOMIZATIONS TABLE (Customer modifications)
CREATE TABLE order_item_customizations (
    customization_id BIGSERIAL PRIMARY KEY,
    order_item_id BIGINT REFERENCES order_items(order_item_id) ON DELETE CASCADE,
    ingredient_id BIGINT REFERENCES ingredients(ingredient_id),
    customization_type VARCHAR(20) NOT NULL CHECK (customization_type IN ('Add', 'Remove', 'Extra')),
    quantity DECIMAL(8, 2) DEFAULT 1.00 CHECK (quantity > 0),
    additional_cost DECIMAL(8, 2) DEFAULT 0.00 CHECK (additional_cost >= 0),
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 10. CARTS TABLE (Active shopping carts)
CREATE TABLE carts (
    cart_id BIGSERIAL PRIMARY KEY,
    customer_id BIGINT REFERENCES customers(customer_id) ON DELETE CASCADE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    -- Each customer can have only one active cart
    CONSTRAINT unique_customer_cart UNIQUE (customer_id)
);

-- 11. CART ITEMS TABLE
CREATE TABLE cart_items (
    cart_item_id BIGSERIAL PRIMARY KEY,
    cart_id BIGINT REFERENCES carts(cart_id) ON DELETE CASCADE,
    menu_item_id BIGINT REFERENCES menu_items(dish_id),
    quantity INTEGER NOT NULL CHECK (quantity > 0),
    added_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    -- Prevent duplicate items in same cart
    CONSTRAINT unique_cart_menu_item UNIQUE (cart_id, menu_item_id)
);

-- 12. ORDER QUEUE TABLE (First Come First Serve - stays until delivered)
CREATE TABLE order_queue (
    queue_id BIGSERIAL PRIMARY KEY,
    order_id BIGINT REFERENCES orders(order_id) ON DELETE CASCADE,
    added_to_queue_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    started_preparing_at TIMESTAMP NULL,
    ready_for_delivery_at TIMESTAMP NULL,
    out_for_delivery_at TIMESTAMP NULL,
    delivered_at TIMESTAMP NULL,
    
    -- Each order can only be in queue once
    CONSTRAINT unique_order_in_queue UNIQUE (order_id)
);

-- 13. RESTAURANT STATISTICS TABLE
CREATE TABLE restaurant_stats (
    stat_id BIGSERIAL PRIMARY KEY,
    restaurant_id BIGINT REFERENCES restaurants(restaurant_id),
    total_orders_processed INTEGER DEFAULT 0,
    total_revenue DECIMAL(12, 2) DEFAULT 0.00,
    orders_delivered INTEGER DEFAULT 0,
    stat_date DATE DEFAULT CURRENT_DATE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    -- One stat record per restaurant per date
    CONSTRAINT unique_restaurant_stat_date UNIQUE (restaurant_id, stat_date)
);

-- INDEXES FOR PERFORMANCE

-- Indexes for frequently queried columns
CREATE INDEX idx_orders_customer_id ON orders(customer_id);
CREATE INDEX idx_orders_status ON orders(status);
CREATE INDEX idx_orders_created_at ON orders(created_at);
CREATE INDEX idx_order_items_order_id ON order_items(order_id);
CREATE INDEX idx_cart_items_cart_id ON cart_items(cart_id);
CREATE INDEX idx_staff_role ON staff(role);
CREATE INDEX idx_menu_items_available ON menu_items(is_available);
CREATE INDEX idx_order_queue_priority_time ON order_queue(added_to_queue_at);

--TRIGGERS FOR AUTOMATIC UPDATES

-- Function to update timestamp on record modification
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

-- Apply update triggers to relevant tables
CREATE TRIGGER update_customers_updated_at BEFORE UPDATE ON customers FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_staff_updated_at BEFORE UPDATE ON staff FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_menu_items_updated_at BEFORE UPDATE ON menu_items FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_ingredients_updated_at BEFORE UPDATE ON ingredients FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_orders_updated_at BEFORE UPDATE ON orders FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_carts_updated_at BEFORE UPDATE ON carts FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_restaurant_stats_updated_at BEFORE UPDATE ON restaurant_stats FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
