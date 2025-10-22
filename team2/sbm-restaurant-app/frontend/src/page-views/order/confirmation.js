"use client";

import React, { useState, useEffect } from 'react';
import { useRouter } from 'next/navigation';
import { useSearchParams } from 'next/navigation';
import Link from 'next/link';
import { CheckCircle, Loader2, ArrowLeft, ShoppingBag } from 'lucide-react';
import { orderService } from '../../services/orderService';
import './confirmation.scss';

export default function OrderConfirmation() {
  const router = useRouter();
  const searchParams = useSearchParams();
  const orderId = searchParams.get('orderId');
  
  const [order, setOrder] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    // Fetch order details when orderId is available from router query
    async function fetchOrderDetails() {
      if (!orderId) return;
      
      try {
        setLoading(true);
        setError(null);
        const orderData = await orderService.getOrderById(parseInt(orderId));
        setOrder(orderData);
      } catch (err) {
        console.error('Error fetching order:', err);
        setError('Unable to load order details. Please try again later.');
      } finally {
        setLoading(false);
      }
    }

    fetchOrderDetails();
  }, [orderId]);

  if (loading) {
    return (
      <div className="order-confirmation-page min-h-screen flex flex-col items-center justify-center">
        <Loader2 className="h-12 w-12 animate-spin mb-4" />
        <h2 className="text-xl font-bold">Loading your order details...</h2>
      </div>
    );
  }

  if (error || !order) {
    return (
      <div className="order-confirmation-page min-h-screen p-6 md:p-10">
        <div className="max-w-3xl mx-auto confirmation-card rounded-lg p-8 shadow-md">
          <h1 className="text-3xl font-bold mb-4 text-red-600">Something went wrong</h1>
          <p className="mb-6">{error || "We couldn't find your order. Please check your order history or contact support."}</p>
          <div className="flex gap-4">
            <Link href="/menu" className="back-to-menu">
              <ShoppingBag className="w-4 h-4 mr-2" />
              Back to Menu
            </Link>
            <Link href="/account/orders" className="view-orders">
              View My Orders
            </Link>
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className="order-confirmation-page min-h-screen p-6 md:p-10">
      <div className="max-w-3xl mx-auto">
        <div className="confirmation-card rounded-lg overflow-hidden shadow-md">
          <div className="confirmation-header p-6 text-center">
            <CheckCircle className="h-16 w-16 mx-auto mb-4" />
            <h1 className="text-3xl md:text-4xl font-bold mb-2">Order Confirmed!</h1>
            <p className="text-lg">
              Your order #{orderId} has been successfully placed
            </p>
          </div>

          <div className="order-details p-6 border-t border-b">
            <h2 className="text-xl font-bold mb-4">Order Summary</h2>
            
            <div className="order-items space-y-3 mb-6">
              {order.items && order.items.map((item, index) => (
                <div key={index} className="order-item flex justify-between">
                  <div>
                    <span className="font-medium">{item.quantity}x </span>
                    <span>{item.name}</span>
                  </div>
                  <span className="font-medium">${(item.subtotal).toFixed(2)}</span>
                </div>
              ))}
            </div>
            
            <div className="order-totals space-y-1">
              <div className="flex justify-between text-sm">
                <span>Subtotal</span>
                <span>${order.totalPrice.toFixed(2)}</span>
              </div>
              <div className="separator my-2"></div>
              <div className="flex justify-between font-bold">
                <span>Total</span>
                <span>${order.totalPrice.toFixed(2)}</span>
              </div>
            </div>
          </div>

          <div className="order-status p-6">
            <h2 className="text-xl font-bold mb-2">Order Status</h2>
            <div className={`status-badge mb-4 status-${order.status.toLowerCase()}`}>
              {order.status}
            </div>
            
            <p className="mb-6">
              We'll notify you when your order status changes.
            </p>
            
            {order.specialInstructions && (
              <div className="special-instructions mb-6">
                <h3 className="font-bold mb-1">Special Instructions</h3>
                <p className="text-sm">{order.specialInstructions}</p>
              </div>
            )}

            <div className="flex gap-4 mt-8">
              <Link href="/menu" className="back-to-menu">
                <ShoppingBag className="w-4 h-4 mr-2" />
                Order More
              </Link>
              <Link href="/account/orders" className="view-orders">
                View My Orders
              </Link>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}