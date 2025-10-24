"use client";

import React, { useState, useEffect } from "react";
import { useRouter } from "next/navigation";
import { authService } from "../../services/authService";
import { cartService } from "../../services/cartService";
import CartRowItem from "../../components/cart/CartRowItem";
import Link from "next/link";
import { ArrowLeft, ShoppingBag, AlertCircle, CreditCard } from "lucide-react";
import "./cartpage.scss";

export default function CartComponent() {
  const router = useRouter();
  const [cart, setCart] = useState([]);
  const [specialInstructions, setSpecialInstructions] = useState("");
  const [user, setUser] = useState(null);
  const [error, setError] = useState(null);

    // Test Payment Handler for dev/testing
    const handleTestPayment = () => {
      // Navigate to payment page with test flag
      router.push('/payment?test=1');
    };

  // Load cart data and user information
  useEffect(() => {
    const loadData = async () => {
      // Load cart from localStorage using the spud_cart key
      const savedCart = cartService.getCart();
      setCart(savedCart);
      
      // Check if user is logged in
      const currentUser = authService.getCurrentUser();
      setUser(currentUser);
    };
    loadData();
  }, []);

  // Cart item quantity update function
  const updateQuantity = (itemId, newQuantity) => {
    const updatedCart = cartService.updateQuantity(itemId, newQuantity);
    setCart(updatedCart);
  };

  // Remove item from cart function
  const removeFromCart = (itemId) => {
    const updatedCart = cartService.removeItem(itemId);
    setCart(updatedCart);
  };

  // Handle proceed to payment button click
  const handleProceedToPayment = async () => {
    if (!user) {
      // Redirect to login page if not logged in with payment redirect
      router.push(`/auth/login?redirect=${encodeURIComponent('/payment')}`);
      return;
    }

    // Validate cart before proceeding
    const validation = cartService.validateCartForPayment();
    
    if (!validation.valid) {
      setError(validation.error);
      return;
    }

    // Navigate to payment page
    router.push('/payment');
  };

  const cartTotals = cartService.getCartTotals(cart);
  const isEmpty = cart.length === 0;

  return (
    <div className="cart-page min-h-screen p-6 md:p-10">
      <div className="max-w-7xl mx-auto">
        <div className="mb-8">
          <Link href="/menus" className="back-button">
            <ArrowLeft className="w-4 h-4 mr-2" />
            Back to Menu
          </Link>
          
          <h1 className="text-4xl md:text-5xl font-bold mb-2">
            Your Cart
          </h1>
          <p className="text-lg mb-6">
            Review and modify your order
          </p>
        </div>

        {isEmpty ? (
          <div className="empty-cart">
            <ShoppingBag className="w-16 h-16 mx-auto mb-4" />
            <h2 className="text-2xl font-bold mb-2">
              Your cart is empty
            </h2>
            <p className="mb-6">
              Add some delicious potato dishes to get started!
            </p>
            <Link href="/menus" className="browse-menu-button">
              Browse Menu
            </Link>
          </div>
        ) : (
          <div className="grid lg:grid-cols-3 gap-6">
            <div className="lg:col-span-2 space-y-4">
              {cart.map((item) => (
                <CartRowItem
                  key={item.id}
                  item={item}
                  updateQuantity={updateQuantity}
                  removeFromCart={removeFromCart}
                />
              ))}

              <div className="special-instructions-container">
                <h3 className="text-lg font-bold mb-2">Special Instructions</h3>
                <textarea
                  placeholder="Any special requests? (e.g., extra crispy, no salt, etc.)"
                  value={specialInstructions}
                  onChange={(e) => setSpecialInstructions(e.target.value)}
                  rows={4}
                  className="w-full p-3 rounded-lg border"
                />
              </div>
            </div>

            <div>
              <div className="order-summary">
                <h3 className="text-xl font-bold mb-4">Order Summary</h3>
                
                <div className="space-y-2">
                  <div className="flex justify-between">
                    <span>Subtotal</span>
                    <span>${cartTotals.subtotal.toFixed(2)}</span>
                  </div>
                  <div className="separator my-2"></div>
                  <div className="flex justify-between font-bold">
                    <span>Total</span>
                    <span>${cartTotals.total.toFixed(2)}</span>
                  </div>
                </div>
              </div>
              
              {!user && (
                <div className="login-alert mt-4">
                  <AlertCircle className="h-4 w-4 mr-2" />
                  <p>You'll need to sign in to complete your order</p>
                </div>
              )}

              {error && (
                <div className="error-alert mt-4">
                  <AlertCircle className="h-4 w-4 mr-2" />
                  <p>{error}</p>
                </div>
              )}

              {/* Payment flow - go directly to payment */}
              <div className="checkout-options mt-4">
                <button
                  onClick={handleProceedToPayment}
                  className="payment-button w-full flex items-center justify-center gap-2"
                >
                  <CreditCard className="w-4 h-4" />
                  Pay with Credit Card
                </button>

              </div>
            </div>
          </div>
        )}
      </div>
    </div>
  );
}