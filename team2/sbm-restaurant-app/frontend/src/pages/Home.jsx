"use client";

import React, { useState } from 'react';
import MenuList from '../components/MenuList';
import CartItem from '../components/CartItem';

const Home = () => {
  const [cart, setCart] = useState([]);
  const [search, setSearch] = useState('');

  const addToCart = (item) => {
    const existingItem = cart.find(cartItem => cartItem.id === item.id);
    
    if (existingItem) {
      // If item already exists, increase quantity
      setCart(cart.map(cartItem => 
        cartItem.id === item.id 
          ? { ...cartItem, quantity: cartItem.quantity + 1 }
          : cartItem
      ));
    } else {
      // If new item, add with quantity 1
      setCart([...cart, { ...item, quantity: 1 }]);
    }
  };

  const updateQuantity = (id, newQuantity) => {
    const quantity = parseInt(newQuantity);
    if (quantity <= 0) {
      removeFromCart(id);
    } else {
      setCart(cart.map(item => 
        item.id === id ? { ...item, quantity: quantity } : item
      ));
    }
  };

  const removeFromCart = (id) => {
    setCart(cart.filter(item => item.id !== id));
  };

  // Mock data for testing - Potato Recipe Menu
  const mockItems = [
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

  // Filter items based on search term
  const filteredItems = mockItems.filter(item =>
    item.name.toLowerCase().includes(search.toLowerCase())
  );

  return (
    <div className="min-h-screen bg-gradient-to-br from-amber-50 to-yellow-100">
      <header className="bg-gradient-to-r from-amber-600 to-yellow-600 text-white shadow-lg" style={{background: 'linear-gradient(to right, #d4af37, #f4d03f)'}}>
        <div className="container mx-auto px-6 py-4">
          <div className="flex justify-between items-center">
            <div>
              <h1 className="text-4xl font-bold mb-2">Welcome to Spud Munch Bunch!</h1>
              <p className="text-amber-100 text-lg">Your journey starts here</p>
            </div>
            <div className="flex gap-4">
              <button className="bg-white text-amber-600 hover:bg-amber-50 font-semibold py-3 px-6 rounded-lg shadow-md transition-colors duration-200 border-2 border-transparent hover:border-amber-200" style={{color: '#d4af37'}}>
                Sign In
              </button>
              <button className="bg-amber-700 text-white hover:bg-amber-800 font-semibold py-3 px-6 rounded-lg shadow-md transition-colors duration-200 border-2 border-amber-700 hover:border-amber-600" style={{backgroundColor: '#d4af37', borderColor: '#d4af37'}}>
                Log In
              </button>
            </div>
          </div>
          <button className="mt-6 text-white font-bold py-3 px-8 rounded-full shadow-lg transition-colors duration-200 transform hover:scale-105" style={{backgroundColor: '#d4af37'}}>
            Get Started
          </button>
        </div>
      </header>
      
      <section className="py-12 px-6">
        <div className="container mx-auto max-w-6xl">
          <h2 className="text-3xl font-bold text-gray-800 mb-8 text-center">Our Featured Menu</h2>
          <div className="mb-8 flex justify-center">
            <input
              type="text"
              placeholder="Search items..."
              value={search}
              onChange={(e) => setSearch(e.target.value)}
              className="w-full max-w-md px-4 py-3 border-2 border-amber-200 rounded-lg focus:outline-none focus:border-amber-500 focus:ring-2 focus:ring-amber-200 text-gray-700 placeholder-gray-400 shadow-sm"
            />
          </div>
          {filteredItems.length === 0 && search ? (
            <div className="text-center py-8">
              <p className="text-gray-500 text-lg">No matching items found</p>
            </div>
          ) : (
            <MenuList items={filteredItems} addToCart={addToCart} />
          )}
        </div>
      </section>

      <section className="bg-white py-12 px-6 border-t-4 border-amber-200">
        <div className="container mx-auto max-w-4xl">
          <h3 className="text-3xl font-bold text-gray-800 mb-8 text-center flex items-center justify-center gap-2">
            🛒 Shopping Cart
          </h3>
          {cart.length === 0 ? (
            <div className="text-center py-12 bg-gray-50 rounded-xl border-2 border-dashed border-gray-200">
              <p className="text-gray-500 text-lg">Your cart is empty</p>
              <p className="text-gray-400 text-sm mt-2">Add some delicious potato dishes to get started!</p>
            </div>
          ) : (
            <div className="space-y-4">
              {cart.map(item => (
                <CartItem 
                  key={item.id} 
                  item={item} 
                  updateQuantity={updateQuantity}
                  removeFromCart={removeFromCart}
                />
              ))}
              <div className="bg-gradient-to-r from-amber-50 to-yellow-50 p-6 rounded-xl border-2 border-amber-200 mt-6">
                <div className="flex justify-between items-center">
                  <div>
                    <h4 className="text-2xl font-bold text-gray-800">
                      Total: ${cart.reduce((sum, item) => sum + (item.price * item.quantity), 0).toFixed(2)}
                    </h4>
                    <p className="text-gray-600 mt-1">
                      Items in cart: {cart.reduce((total, item) => total + item.quantity, 0)}
                    </p>
                  </div>
                  <button className="text-white font-bold py-3 px-8 rounded-lg shadow-lg transition-colors duration-200 transform hover:scale-105" style={{backgroundColor: '#d4af37'}}>
                    Checkout
                  </button>
                </div>
              </div>
            </div>
          )}
        </div>
      </section>
      
      <section className="bg-gray-50 py-16 px-6">
        <div className="container mx-auto max-w-6xl">
          <h2 className="text-3xl font-bold text-gray-800 mb-12 text-center">Why Choose Spud Munch Bunch?</h2>
          <div className="grid md:grid-cols-3 gap-8">
            <div className="bg-white p-8 rounded-xl shadow-lg hover:shadow-xl transition-shadow duration-300 border border-gray-100">
              <div className="text-4xl mb-4">🥔</div>
              <h3 className="text-xl font-bold text-gray-800 mb-3">Fresh Ingredients</h3>
              <p className="text-gray-600">We use only the finest, locally-sourced potatoes and fresh ingredients in all our dishes.</p>
            </div>
            <div className="bg-white p-8 rounded-xl shadow-lg hover:shadow-xl transition-shadow duration-300 border border-gray-100">
              <div className="text-4xl mb-4">👨‍🍳</div>
              <h3 className="text-xl font-bold text-gray-800 mb-3">Expert Chefs</h3>
              <p className="text-gray-600">Our skilled chefs craft each potato dish with passion and decades of culinary expertise.</p>
            </div>
            <div className="bg-white p-8 rounded-xl shadow-lg hover:shadow-xl transition-shadow duration-300 border border-gray-100">
              <div className="text-4xl mb-4">🚚</div>
              <h3 className="text-xl font-bold text-gray-800 mb-3">Fast Delivery</h3>
              <p className="text-gray-600">Enjoy our delicious potato creations delivered hot and fresh to your door in 30 minutes or less.</p>
            </div>
          </div>
        </div>
      </section>
    </div>
  );
};

export default Home;
