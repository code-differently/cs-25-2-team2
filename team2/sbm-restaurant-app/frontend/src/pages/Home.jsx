import React from 'react';
import MenuList from '../components/MenuList';

const Home = () => {
  // Mock data for testing
  const mockMenuItems = [
    {
      id: 1,
      name: "Spud Supreme Burger",
      description: "Juicy beef burger with crispy potato chips and special sauce",
      price: 14.99
    },
    {
      id: 2,
      name: "Loaded Potato Fries",
      description: "Golden fries loaded with cheese, bacon, and green onions",
      price: 8.99
    },
    {
      id: 3,
      name: "Munch Wrap",
      description: "Crispy wrap filled with seasoned ground beef and fresh veggies",
      price: 11.99
    },
    {
      id: 4,
      name: "Spud Smoothie",
      description: "Refreshing fruit smoothie with a hint of sweet potato",
      price: 6.99
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
          <MenuList items={mockMenuItems} />
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
