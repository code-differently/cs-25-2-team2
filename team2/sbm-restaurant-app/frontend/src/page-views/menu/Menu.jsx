"use client";
import React, { useState, useEffect } from "react";
import MenuCard from "../../components/menu/MenuCard";
import menuItems from "../../data/menuItems";
import "./menu.scss";
import { cartService } from "../../services/cartService";

export default function MenuPage() {
  const [category, setCategory] = useState("All");
  const [search, setSearch] = useState("");
  const [selectedItem, setSelectedItem] = useState(null);
  const [selectedToppings, setSelectedToppings] = useState([]);
  const [cart, setCart] = useState([]);
  const [isAddingToCart, setIsAddingToCart] = useState(null);

  useEffect(() => {
    setCart(cartService.getCart());
    const handleCartUpdate = () => setCart(cartService.getCart());
    window.addEventListener("cartUpdated", handleCartUpdate);
    return () => window.removeEventListener("cartUpdated", handleCartUpdate);
  }, []);

  const filterItems = menuItems.filter((item) => {
    let shouldShow = true;
    if (category !== "All" && item.category !== category) {
      shouldShow = false;
    }
    if (!item.name.toLowerCase().includes(search.toLowerCase())) {
      shouldShow = false;
    }
    return shouldShow;
  });

  const handleSelectItem = (item) => {
    setSelectedItem(item);
    setSelectedToppings(item.toppings ? [...item.toppings] : []);
  };

  const handleRemoveTopping = (idx) => {
    setSelectedToppings((prev) => prev.filter((_, i) => i !== idx));
  };

  const handleAddToCart = async () => {
    if (!selectedItem) return;
    setIsAddingToCart(selectedItem.id);
    try {
      const itemToAdd = { ...selectedItem, toppings: selectedToppings };
      const updatedCart = cartService.addToCart(itemToAdd, 1);
      setCart(updatedCart);
      setSelectedItem(null);
      console.log(`Added ${selectedItem.name} to cart`);
    } catch (error) {
      console.error("Error adding item to cart:", error);
    } finally {
      setIsAddingToCart(null);
    }
  };

  return (
    <div className="menu-page">
      {/* Header Section */}
      <header className="menu-header">
        <h2>Our Menu</h2>
        <p>Discover every delicious potato creation we offer!</p>
      </header>

      {/* Controls Section */}
      <section className="controls-section">
        <div className="category-filter-buttons">
          {["All", "Main", "Side", "Soup"].map((cat) => (
            <button
              key={cat}
              onClick={() => setCategory(cat)}
              className={category === cat ? "active" : ""}
            >
              {cat}
            </button>
          ))}
        </div>

        <input
          type="text"
          placeholder="Search menu..."
          value={search}
          onChange={(e) => setSearch(e.target.value)}
          className="search-input"
        />
      </section>

      {/* Menu Grid Section */}
      <section className="menu-grid-section">
        {filterItems.length === 0 ? (
          <p className="no-results">No items match your search or filter</p>
        ) : (
          filterItems.map((item) => (
            <MenuCard
              key={item.id}
              item={item}
              onClick={() => handleSelectItem(item)}
            />
          ))
        )}
      </section>

      {/* Modal Section */}
      {selectedItem && (
        <div className="modal-overlay" onClick={() => setSelectedItem(null)}>
          <div className="modal-content" onClick={(e) => e.stopPropagation()}>
            {/* Modal Header */}
            <div className="modal-header">
              <button
                onClick={() => setSelectedItem(null)}
                className="close-button"
              >
                ×
              </button>
            </div>

            {/* Modal Content */}
            <h3>{selectedItem.name}</h3>
            <p>{selectedItem.description}</p>

            {/* Toppings Grid */}
            <div className="modal-toppings-grid">
              {selectedToppings && selectedToppings.length > 0 ? (
                selectedToppings.map((topping, i) => (
                  <div key={i} className="modal-topping-item">
                    <button
                      className="remove-topping"
                      onClick={() => handleRemoveTopping(i)}
                    >
                      ×
                    </button>
                    <div className="modal-topping-image">
                      <img
                        src={topping.image}
                        alt={topping.name}
                        style={{
                          width: "2.5rem",
                          height: "2.5rem",
                          borderRadius: "50%",
                        }}
                      />
                    </div>
                    <p>{topping.name}</p>
                  </div>
                ))
              ) : (
                <p style={{ fontStyle: "italic", color: "#aaa" }}>
                  No toppings
                </p>
              )}
            </div>

            {/* Add Topping Dropdown */}
            <div className="modal-footer" style={{ marginBottom: "1rem" }}>
              <label htmlFor="add-topping-select" className="text-gray-800 dark:text-gray-100 font-medium">Add Topping: </label>
              <select
                id="add-topping-select"
                onChange={(e) => {
                  const toppingName = e.target.value;
                  if (!toppingName) return;

                  const masterToppings = [
                    { name: "Cheese", image: "/images/toppings/cheese.png" },
                    { name: "Bacon", image: "/images/toppings/bacon.webp" },
                    { name: "Sour Cream", image: "/images/toppings/sourcream.png" },
                    { name: "Chicken", image: "/images/toppings/chicken.jpg" },
                    { name: "Pico de Gallo", image: "/images/toppings/picodegallo.png" },
                    { name: "Green Onions", image: "/images/toppings/greenOnions.webp" },
                    { name: "Ranch", image: "/images/toppings/ranch.png" },
                    { name: "Butter", image: "/images/toppings/butter.png" },
                    { name: "Salt", image: "/images/toppings/salt1.png" },
                    { name: "Pepper", image: "/images/toppings/blackpepper.jpg" },
                    { name: "Croutons", image: "/images/toppings/croutons.png" },
                    { name: "Garlic", image: "/images/toppings/garlic.png" }
                  ];

                  const toppingObj = masterToppings.find(
                    (t) => t.name === toppingName
                  );

                  if (
                    toppingObj &&
                    !selectedToppings.some((t) => t.name === toppingObj.name)
                  ) {
                    setSelectedToppings((prev) => [...prev, toppingObj]);
                  }
                  e.target.value = "";
                }}
                defaultValue=""
              >
                <option value="">Select topping...</option>
                {[
                  "Cheese",
                  "Bacon",
                  "Sour Cream",
                  "Chicken",
                  "Pico de Gallo",
                  "Green Onions",
                  "Ranch",
                  "Butter",
                  "Salt",
                  "Pepper",
                  "Croutons",
                  "Garlic",
                ]
                  .filter((t) => !selectedToppings.some((st) => st.name === t))
                  .map((topping) => (
                    <option key={topping} value={topping}>
                      {topping}
                    </option>
                  ))}
              </select>
            </div>

            {/* Add to Cart Button */}
            <button
              onClick={handleAddToCart}
              disabled={isAddingToCart === selectedItem.id}
            >
              {isAddingToCart === selectedItem.id ? "Adding..." : "Add to Cart"}
            </button>
          </div>
        </div>
      )}
    </div>
  );
}
