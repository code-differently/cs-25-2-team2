
"use client";
import React, { useState } from "react";
import ToppingItem from "../../components/menu/ToppingItem";
import MenuCard from "../../components/menu/MenuCard";
import menuItems from "../../data/menuItems";
import "./menu.scss";

export default function MenuPage() {
  const [category, setCategory] = useState("All");
  const [search, setSearch] = useState("");
  const [selectedItem, setSelectedItem] = useState(null);


  const filterItems = menuItems.filter((item) => {
    let shouldShow = true;
    if (category !== "All" && item.category !== category) {
      shouldShow = false;
    }
    const itemName = item.name.toLowerCase();
    const searchText = search.toLowerCase();
    if (!itemName.includes(searchText)) {
      shouldShow = false;
    }
    return shouldShow;
  });

  return (
    <div className="menu-page">

      {/* Header Section */}
      <header className="menu-header">
        <h2>Our Menu</h2>
        <p>Discover every delicious potato creation we offer!</p>
      </header>

      {/* Controls Section */}
      <section className="controls-section">
        {/* Category Buttons */}
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

        {/* Search Bar */}
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
              onClick={() => setSelectedItem(item)}
            />
          ))
        )}
      </section>

      {/* Modal for selected item */}
      {selectedItem && (
        <div className="modal-overlay" onClick={() => setSelectedItem(null)}>
          <div className="modal-content" onClick={(e) => e.stopPropagation()}>
            {/* Close button */}
            <div className="modal-header">
              <button 
                onClick={() => setSelectedItem(null)}
                className="close-button"
              >
                <svg className="close-icon" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M6 18L18 6M6 6l12 12" />
                </svg>
              </button>
            </div>

            {/* Image */}
            <div className="modal-image-container">
              <div className="modal-image">
                <div className="modal-image-circle">
                  <svg className="potato-icon-large" fill="currentColor" viewBox="0 0 20 20">
                    <path d="M3 1a1 1 0 000 2h1.22l.305 1.222a.997.997 0 00.01.042l1.358 5.43-.893.892C3.74 11.846 4.632 14 6.414 14H15a1 1 0 000-2H6.414l1-1H14a1 1 0 00.894-.553l3-6A1 1 0 0017 3H6.28l-.31-1.243A1 1 0 005 1H3zM16 16.5a1.5 1.5 0 11-3 0 1.5 1.5 0 013 0zM6.5 18a1.5 1.5 0 100-3 1.5 1.5 0 000 3z" />
                  </svg>
                </div>
              </div>
            </div>

            {/* Content */}
            <div className="modal-body">
              <h2>{selectedItem.name}</h2>
              <p className="modal-meta">{selectedItem.price} — {selectedItem.calories}</p>

              {/* Toppings Grid */}
              <div className="modal-toppings-grid">
                {["Cheese","Ranch","Chicken", "Bacon", "Sour Cream", "Green Onions", "Butter", "Pico de Gallo"].map((topping, i) => (
                  <div key={i} className="modal-topping-item">
                    <button className="remove-topping">×</button>
                    <div className="modal-topping-image">
                      <div className="modal-topping-circle"></div>
                    </div>
                    <p>{topping}</p>
                  </div>
                ))}
              </div>

              {/* Tabs */}
              <div className="modal-tabs">
                <button className="tab">BASES</button>
                <button className="tab active">TOPPINGS</button>
              </div>

              {/* Action Buttons */}
              <div className="modal-actions">
                <button 
                  onClick={() => setSelectedItem(null)}
                  className="btn-cancel"
                >
                  Cancel
                </button>
                <button className="btn-done">I'm done</button>
              </div>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
