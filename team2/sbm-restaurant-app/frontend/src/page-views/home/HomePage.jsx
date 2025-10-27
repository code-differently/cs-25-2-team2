"use client";

// Main Home Page Export
import React, { useState, useEffect } from 'react';
import { useRouter } from 'next/navigation';
import Carousel from "../../components/Carousel";
import TeamSection from "../../components/TeamSection";
import PotatoRecipes from "../../components/PotatoRecipes";
import "./homestyle.scss";

export default function HomePage() {
  const router = useRouter();

  const handleViewMenu = () => {
    router.push('/menus');
  };

  return (
    <div className="home-page min-h-screen">
      {/* Hero Header Section */}
      <header className="hero-header bg-gradient-to-r text-white shadow-lg" 
              style={{background: 'linear-gradient(to right, #d4af37, #f4d03f)'}}>
        <div className="container mx-auto px-6 py-4">
          <div className="hero-content">
            <div className="hero-text text-center">
              <h1 className="page-title text-4xl font-bold mb-2">Welcome to Spud Munch Bunch!</h1>
              <p className="page-subtitle text-lg" style={{color: '#fff8e1'}}> Your ultimate destination for crispy, creamy, golden potato goodness.</p>
            </div>
          </div>
        </div>
      </header>

      {/* Best Sellers Carousel Section */}
      <section className="py-1 px-6">
        <div className="container mx-auto max-w-6xl">
          <div className="w-full flex justify-start mb-0">
            <img src="/images/potatologo.png" alt="Potato Logo" style={{ width: '350px', height: '210px' }} className="object-contain animate-drive-right-repeat" />
          </div>
          <h2 className="menu-title text-3xl font-bold mb-2 text-center">
            Top Taters of The Week
          </h2>
          <p className="text-center text-gray-600 mb-8 text-lg">
           
          </p>

          {/* Featured Menu Items Carousel */}
          <Carousel />
          
          <div className="text-center mt-8">
            <button 
              onClick={handleViewMenu}
              className="view-full-menu-btn bg-amber-600 hover:bg-amber-700 text-white font-bold py-3 px-8 rounded-lg shadow-lg transition-all duration-200 transform hover:scale-105"
            >
              View Full Menu
            </button>
          </div>
        </div>
      </section>

      {/* Our Story Section */}
      <section className="py-12 px-6">
        <div className="container mx-auto max-w-4xl text-center">
          <h2 className="text-3xl font-bold mb-8 text-center">Our Potato Love Story</h2>
          <p className="text-gray-600 mb-4 text-lg">
            Spud Munch Bunch began with one simple mission to celebrate the
            humble potato in all its golden, crispy, buttery glory. What started
            as a love letter to fries has grown into a full-blown obsession with
            every delicious form a spud can take. From perfectly seasoned wedges to cheesy loaded baked potatoes and
            crispy tots that crunch just right, we believe every potato deserves
            it's moment to shine and we're here to make sure it does.
          </p>
        </div>
      </section>

      {/* Why We're Here Section */}
      <section className="py-12 px-6 bg-gray-50 dark:bg-gray-800">
        <div className="container mx-auto max-w-4xl text-center">
          <h2 className="text-3xl font-bold mb-8 text-center text-gray-800 dark:text-gray-100">The World Needed More Tater Love, So We Showed Up</h2>
          <p className="text-gray-600 dark:text-gray-300 text-lg">
            Think of us as your potato-powered delivery crew, here to bring golden, crispy, buttery goodness straight to your door. At Spud Munch Bunch, we take comfort food seriously but we make it fun, fast, and ridiculously tasty. From cheesy loaded baked potatoes to perfectly seasoned fries, tots, and sides, every bite is crafted to satisfy your cravings. Whether you’re a couch potato or just here for the ultimate fry fix, we’ve got you covered with spud-tacular flavors made with love!
          </p>
        </div>
      </section>

      {/* Features Section */}
      <section className="features-section py-16 px-6">
        <div className="container mx-auto max-w-6xl">
          <h2 className="features-title text-3xl font-bold mb-12 text-center">
            Golden, Crispy, and Crafted with Care.
          </h2>
          <div className="features-grid grid md:grid-cols-3 gap-8">
            <div className="feature-card p-8 rounded-xl shadow-lg hover:shadow-xl transition-shadow duration-300 border">
              <div className="feature-icon text-4xl mb-4">🥔</div>
              <h3 className="feature-title text-xl font-bold mb-3">All Potatoes. All the Time.</h3>
              <p className="feature-description">100% spuds. 0% distractions.</p>
            </div>
            <div className="feature-card p-8 rounded-xl shadow-lg hover:shadow-xl transition-shadow duration-300 border">
              <div className="feature-icon text-4xl mb-4">😋</div>
              <h3 className="feature-title text-xl font-bold mb-3">Comfort Food, Delivered Fast.</h3>
              <p className="feature-description">Hot, fresh, and delivered to your door.</p>
            </div>
            <div className="feature-card p-8 rounded-xl shadow-lg hover:shadow-xl transition-shadow duration-300 border">
              <div className="feature-icon text-4xl mb-4">💛</div>
              <h3 className="feature-title text-xl font-bold mb-3">Made with Love & Extra Butter.</h3>
              <p className="feature-description">Crafted by spud lovers, for spud lovers.</p>
            </div>
          </div>
        </div>
      </section>

      {/* Potato Recipes Section */}
      {/*<PotatoRecipes number={2} />

      {/* Team Section */}
      <TeamSection />
    </div>
  );
}
