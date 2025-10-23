import React from "react";
import { format } from "date-fns";
import { Clock, CheckCircle, Package, Truck, XCircle } from "lucide-react";
import "./orderstyle.scss";

// Status configuration to match backend Order.Status enum
// Using global CSS variables for colors
const statusConfig = {
  Placed: {
    label: "Placed",
    color: "var(--status-placed)",
    icon: Clock
  },
  Preparing: {
    label: "Preparing",
    color: "var(--status-preparing)",
    icon: Package
  },
  ReadyForDelivery: {
    label: "Ready for Delivery",
    color: "var(--status-ready)",
    icon: CheckCircle
  },
  OutForDelivery: {
    label: "Out for Delivery",
    color: "var(--status-out-for-delivery)",
    icon: Truck
  },
  Delivered: {
    label: "Delivered",
    color: "var(--status-delivered)",
    icon: CheckCircle
  },
  pending: {
    label: "Pending",
    color: "var(--status-pending)",
    icon: Clock
  }
};

export default function OrderCard({ order, onStatusUpdate, onCancel }) {
  const status = statusConfig[order.status] || statusConfig.pending;
  const StatusIcon = status.icon;

  return (
    <div className="order-card border rounded-lg bg-white shadow-md">
      <div className="p-4 border-b">
        <div className="flex justify-between items-start">
          <div>
            <p className="order-text--id text-sm mb-1">
              Order #{order.id || 'N/A'}
            </p>
            <p className="order-text--date text-xs text-gray-500">
              {order.createdAt ? format(new Date(order.createdAt), "MMM d, yyyy 'at' h:mm a") : 'Date not available'}
            </p>
          </div>
          <span 
            className="order-badge flex items-center gap-1 px-3 py-1 rounded-full text-xs font-medium text-white"
            style={{ backgroundColor: status.color }}
          >
            <StatusIcon className="w-3 h-3" />
            {status.label}
          </span>
        </div>
      </div>
      
      <div className="p-4">
        <div className="space-y-3 mb-4">
          {order.items?.map((item, index) => (
            <div key={index} className="flex justify-between items-center">
              <div>
                <p className="order-text--item-name font-medium">
                  {item.menuItem?.name || item.name} <span className="quantity font-normal">x{item.quantity || 1}</span>
                </p>
              </div>
              <p className="order-text--price font-medium">
                ${(item.subtotal || (item.price * item.quantity) || 0).toFixed(2)}
              </p>
            </div>
          ))}
        </div>

        <div className="order-border pt-3 border-t flex justify-between items-center">
          <span className="order-text--total-label font-bold">Total</span>
          <span className="order-text--total-amount text-xl font-bold">
            ${(order.totalPrice || order.total_amount || order.totalAmount || 0).toFixed(2)}
          </span>
        </div>

        {/* Action buttons (optional - only if parent provides handlers) */}
        {(onStatusUpdate || onCancel) && (
          <div className="order-border pt-3 border-t mt-3 flex gap-2">
            {onCancel && order.status !== 'Delivered' && order.status !== 'OutForDelivery' && (
              <button 
                onClick={() => onCancel(order.id)}
                className="order-button px-3 py-1 text-sm rounded bg-red-500 text-white hover:bg-red-600"
              >
                Cancel Order
              </button>
            )}
          </div>
        )}
      </div>
    </div>
  );
}