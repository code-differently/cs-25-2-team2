"use client";

// Main Home Page Export
import React, { useState } from 'react';
import "./homestyle.scss";

// Mock data for featured items
const mockItems = [
  { id: 1, name: "Classic Baked Potato", image: "/api/placeholder/300/200" },
  { id: 2, name: "Loaded Potato Skins", image: "/api/placeholder/300/200" },
  { id: 3, name: "Sweet Potato Fries", image: "/api/placeholder/300/200" },
  { id: 4, name: "Potato Gnocchi", image: "/api/placeholder/300/200" },
  { id: 5, name: "Hashmallow Casserole", image: "/api/placeholder/300/200" },
  { id: 6, name: "Spud Burger", image: "/api/placeholder/300/200" }
];

export default function HomePage() {
  const [search, setSearch] = useState('');

  // Filter items based on search term
  const filteredItems = mockItems.filter(item =>
    item.name.toLowerCase().includes(search.toLowerCase())
  );

  return (
    <div className="home-page min-h-screen">
      {/* Hero Header Section */}
      <header className="hero-header bg-gradient-to-r text-white shadow-lg" 
              style={{background: 'linear-gradient(to right, #d4af37, #f4d03f)'}}>
        <div className="container mx-auto px-6 py-4">
          <div className="hero-content flex justify-between items-center">
            <div className="hero-text">
              <h1 className="page-title text-4xl font-bold mb-2">Welcome to Spud Munch Bunch!</h1>
              <p className="page-subtitle text-lg" style={{color: '#fff8e1'}}>Your potato paradise journey starts here</p>
            </div>
            <div className="hero-actions flex gap-4">
              <button className="auth-button bg-white hover:bg-amber-50 font-semibold py-3 px-6 rounded-lg shadow-md transition-colors duration-200 border-2 border-transparent hover:border-amber-200" 
                      style={{color: '#d4af37'}}>
                Sign In
              </button>
              <button className="auth-button font-semibold py-3 px-6 rounded-lg shadow-md transition-colors duration-200 border-2" 
                      style={{backgroundColor: '#d4af37', borderColor: '#d4af37', color: 'white'}}>
                Log In
              </button>
            </div>
          </div>
          <button className="cta-button mt-6 text-white font-bold py-3 px-8 rounded-full shadow-lg transition-all duration-200 transform hover:scale-105" 
                  style={{backgroundColor: '#d4af37'}}>
            Explore Menu
          </button>
        </div>
      </header>

      {/* Menu Section */}
      <section className="py-12 px-6">
        <div className="container mx-auto max-w-6xl">
          <h2 className="menu-title text-3xl font-bold mb-8 text-center">
            Featured Menu Preview
          </h2>
          <p className="text-center text-gray-600 mb-8 text-lg">
            Get a taste of our delicious potato creations
          </p>
          <div className="mb-8 flex justify-center">
            <input
              type="text"
              placeholder="Search items..."
              value={search}
              onChange={(e) => setSearch(e.target.value)}
              className="w-full max-w-md px-4 py-3 border-2 border-amber-200 rounded-lg focus:outline-none focus:border-amber-500 focus:ring-2 focus:ring-amber-200 text-gray-700 placeholder-gray-400 shadow-sm"
            />
          </div>

          {/* Featured Menu Items Grid */}
          {filteredItems.length === 0 && search ? (
            <div className="text-center py-8">
              <p className="empty-text text-gray-500 text-lg">No matching items found</p>
            </div>
          ) : (
            <div className="menu-grid grid md:grid-cols-2 lg:grid-cols-3 gap-6">
              {filteredItems.map(item => (
                <div key={item.id} className="menu-item-card bg-white rounded-lg shadow-md hover:shadow-lg transition-shadow duration-300 border border-gray-200 overflow-hidden">
                  <img 
                    src={item.image} 
                    alt={item.name}
                    className="w-full h-48 object-cover"
                  />
                  <div className="p-4">
                    <h3 className="item-name text-xl font-semibold mb-2 text-gray-800">{item.name}</h3>
                  </div>
                </div>
              ))}
            </div>
          )}
          
          <div className="text-center mt-8">
            <button className="view-full-menu-btn bg-amber-600 hover:bg-amber-700 text-white font-bold py-3 px-8 rounded-lg shadow-lg transition-all duration-200 transform hover:scale-105">
              View Full Menu
            </button>
          </div>
        </div>
      </section>

      {/* Features Section */}
      <section className="features-section py-16 px-6">
        <div className="container mx-auto max-w-6xl">
          <h2 className="features-title text-3xl font-bold mb-12 text-center">
            Why Choose Spud Munch Bunch?
          </h2>
          <div className="features-grid grid md:grid-cols-3 gap-8">
            <div className="feature-card p-8 rounded-xl shadow-lg hover:shadow-xl transition-shadow duration-300 border">
              <div className="feature-icon text-4xl mb-4">🥔</div>
              <h3 className="feature-title text-xl font-bold mb-3">Fresh Ingredients</h3>
              <p className="feature-description">We use only the finest, locally-sourced potatoes and fresh ingredients in all our dishes.</p>
            </div>
            <div className="feature-card p-8 rounded-xl shadow-lg hover:shadow-xl transition-shadow duration-300 border">
              <div className="feature-icon text-4xl mb-4">👨‍🍳</div>
              <h3 className="feature-title text-xl font-bold mb-3">Expert Chefs</h3>
              <p className="feature-description">Our skilled chefs craft each potato dish with passion and decades of culinary expertise.</p>
            </div>
            <div className="feature-card p-8 rounded-xl shadow-lg hover:shadow-xl transition-shadow duration-300 border">
              <div className="feature-icon text-4xl mb-4">🚚</div>
              <h3 className="feature-title text-xl font-bold mb-3">Fast Delivery</h3>
              <p className="feature-description">Enjoy our delicious potato creations delivered hot and fresh to your door in 30 minutes or less.</p>
            </div>
          </div>
        </div>
      </section>
    </div>
  );
}
