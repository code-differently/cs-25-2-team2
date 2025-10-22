"use client";

import React, { useState, useEffect } from "react";
import { useRouter } from "next/navigation";
import { useMutation } from "@tanstack/react-query";
import { authService } from "../../services/authService";
import { cartService } from "../../services/cartService";
import CartRowItem from "../../components/cart/CartRowItem";
import Link from "next/link";
import { ArrowLeft, ShoppingBag, AlertCircle } from "lucide-react";
import "./cartpage.scss";

export default function CartComponent() {
  const router = useRouter();
  const [cart, setCart] = useState([]);
  const [specialInstructions, setSpecialInstructions] = useState("");
  const [user, setUser] = useState(null);
  const [error, setError] = useState(null);

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

  // Use React Query for checkout mutation
  const checkoutMutation = useMutation({
    mutationFn: async () => {
      // Create order from cart with special instructions
      return await cartService.checkout(specialInstructions);
    },
    onSuccess: (order) => {
      // Redirect to order confirmation page on success
      router.push(`/orders/confirmation?orderId=${order.id}`);
    },
    onError: (err) => {
      setError(err.message || 'An error occurred during checkout');
    }
  });

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

  // Handle checkout button click
  const handleCheckout = async () => {
    if (!user) {
      // Redirect to login page if not logged in
      router.push(`/auth/login?redirect=${encodeURIComponent('/cart')}`);
      return;
    }

    // Execute the checkout mutation
    checkoutMutation.mutate();
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
                  <div className="flex justify-between">
                    <span>Tax (8%)</span>
                    <span>${cartTotals.tax.toFixed(2)}</span>
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

              <button
                onClick={handleCheckout}
                disabled={checkoutMutation.isPending}
                className="checkout-button w-full mt-4"
              >
                {checkoutMutation.isPending ? "Processing..." : "Place Order"}
              </button>
            </div>
          </div>
        )}
      </div>
    </div>
  );
}