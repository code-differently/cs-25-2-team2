"use client";

// Main Home Page Export
import React, { useState } from 'react';
import Carousel from "../../components/Carousel";
import TeamSection from "../../components/TeamSection";
import "./homestyle.scss";

export default function HomePage() {

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

      {/* Best Sellers Carousel Section */}
      <section className="py-12 px-6">
        <div className="container mx-auto max-w-6xl">
          <h2 className="menu-title text-3xl font-bold mb-8 text-center">
            Our Best Sellers
          </h2>
          <p className="text-center text-gray-600 mb-8 text-lg">
            Get a taste of our most popular potato creations
          </p>

          {/* Featured Menu Items Carousel */}
          <Carousel />
          
          <div className="text-center mt-8">
            <button className="view-full-menu-btn bg-amber-600 hover:bg-amber-700 text-white font-bold py-3 px-8 rounded-lg shadow-lg transition-all duration-200 transform hover:scale-105">
              View Full Menu
            </button>
          </div>
        </div>
      </section>

      {/* Our Story Section */}
      <section className="py-12 px-6">
        <div className="container mx-auto max-w-4xl text-center">
          <h2 className="text-3xl font-bold mb-8 text-center">Our Story</h2>
          <p className="text-gray-600 mb-4 text-lg">
            Spud Munch Bunch began with one simple mission — to celebrate the
            humble potato in all its golden, crispy, buttery glory. What started
            as a love letter to fries has grown into a full-blown obsession with
            every delicious form a spud can take.
          </p>
          <p className="text-gray-600 text-lg">
            From perfectly seasoned wedges to cheesy loaded baked potatoes and
            crispy tots that crunch just right, we believe every potato deserves
            its moment to shine — and we're here to make sure it does.
          </p>
        </div>
      </section>

      {/* Why We're Here Section */}
      <section className="py-12 px-6 bg-gray-50">
        <div className="container mx-auto max-w-4xl text-center">
          <h2 className="text-3xl font-bold mb-8 text-center">Why We're Here</h2>
          <p className="text-gray-600 text-lg">
            Think of us as your potato-powered delivery crew. At Spud Munch Bunch,
            we blend comfort food with convenience — serving up the tastiest potato
            recipes, snacks, and sides, ready when you are. Fast, friendly, and
            full of flavor — that's how we do spuds.
          </p>
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

      {/* Team Section */}
      <TeamSection />
    </div>
  );
}
