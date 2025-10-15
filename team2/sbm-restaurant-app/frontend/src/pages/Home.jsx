"use client";

import React, { useState } from 'react';
import MenuList from '../components/MenuList';

const Home = () => {
  const [cart, setCart] = useState([]);

  const addToCart = (item) => {
    setCart([...cart, item]);
  };

  // Mock data for testing - Potato Recipe Menu
  const mockMenuItems = [
    {
      id: 1,
      name: "Classic Loaded Baked Potato",
      description: "Fluffy baked russet potato topped with melted cheddar, crispy bacon bits, sour cream, and fresh chives",
      price: 12.99
    },
    {
      id: 2,
      name: "Garlic Herb Roasted Baby Potatoes",
      description: "Golden baby potatoes roasted with rosemary, thyme, garlic, and olive oil until crispy outside and tender inside",
      price: 9.99
    },
    {
      id: 3,
      name: "Truffle Parmesan Potato Gratin",
      description: "Layers of thinly sliced potatoes baked in cream sauce with truffle oil and topped with parmesan cheese",
      price: 16.99
    },
    {
      id: 4,
      name: "Spiced Sweet Potato Fries",
      description: "Hand-cut sweet potato fries seasoned with paprika, cumin, and a hint of cinnamon, served with chipotle aioli",
      price: 8.99
    },
    {
      id: 5,
      name: "Potato Gnocchi in Brown Butter Sage",
      description: "House-made potato gnocchi tossed in brown butter with crispy sage leaves and grated pecorino romano",
      price: 14.99
    },
    {
      id: 6,
      name: "Hasselback Potatoes",
      description: "Swedish-style sliced potatoes roasted with herbs, butter, and breadcrumbs until golden and accordion-like",
      price: 11.99
    }
  ];
  return (
    <div className="home">
      <header className="hero">
        <div className="auth-buttons">
          <button>Sign In</button>
          <button>Log In</button>
        </div>
        <h1>Welcome to Spud Munch Bunch!</h1>
        <p>Your journey starts here</p>
        <button className="cta-button">Get Started</button>
      </header>
      
      <section className="menu-preview">
        <div className="container">
          <h2>Our Featured Menu</h2>
          <MenuList items={mockMenuItems} addToCart={addToCart} />
        </div>
      </section>

      <section className="cart-summary">
        <div className="container">
          <h3>Cart Total: ${cart.reduce((sum, item) => sum + item.price, 0).toFixed(2)}</h3>
          <p>Items in cart: {cart.length}</p>
        </div>
      </section>
      
      <section className="features">
        <div className="container">
          <h2>Features</h2>
          <div className="feature-grid">
            <div className="feature-item">
              <h3>Feature One</h3>
              <p>Description of the first feature</p>
            </div>
            <div className="feature-item">
              <h3>Feature Two</h3>
              <p>Description of the second feature</p>
            </div>
            <div className="feature-item">
              <h3>Feature Three</h3>
              <p>Description of the third feature</p>
            </div>
          </div>
        </div>
      </section>
    </div>
  );
};

export default Home;
