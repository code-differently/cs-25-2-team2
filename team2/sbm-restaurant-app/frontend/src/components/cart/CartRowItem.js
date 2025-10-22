import React, { useState } from "react";
import { Trash2, Plus, Minus } from "lucide-react";
import { motion } from "framer-motion";

export default function CartRowItem({ item, updateQuantity, removeFromCart }) {
  const [showError, setShowError] = useState(false);
  const subtotal = item.price * item.quantity;

  const handleQuantityChange = (newQuantity) => {
    if (newQuantity < 1) {
      // Show a brief error message instead of removing item
      setShowError(true);
      setTimeout(() => setShowError(false), 2000);
      return;
    }
    updateQuantity(item.id, newQuantity);
  };

  return (
    <motion.div
      initial={{ opacity: 0, x: -15 }}
      animate={{ opacity: 1, x: 0 }}
      exit={{ opacity: 0, x: 15 }}
      transition={{ duration: 0.25 }}
      className="border rounded-lg p-4 relative transition-all duration-300 cart-row-item"
      style={{
        backgroundColor: "var(--theme-surface)",
        borderColor: "var(--theme-border)",
      }}
    >
      {/* Error message popup */}
      {showError && (
        <motion.div
          initial={{ opacity: 0, y: -8 }}
          animate={{ opacity: 1, y: 0 }}
          exit={{ opacity: 0 }}
          className="absolute top-2 right-2 bg-red-500/90 text-white text-xs px-3 py-1 rounded-md shadow"
        >
          Prevents going below 1
        </motion.div>
      )}

      <div className="flex gap-4">
        {/* Image or Placeholder */}
        <div
          className="w-24 h-24 rounded-lg overflow-hidden flex-shrink-0 flex items-center justify-center cart-item-image"
          style={{ backgroundColor: "var(--theme-surface-elevated)" }}
        >
          {item.image_url ? (
            <img
              src={item.image_url}
              alt={item.name}
              className="w-full h-full object-cover"
            />
          ) : (
            <span className="text-3xl">🛒</span>
          )}
        </div>

        {/* Item Info */}
        <div className="flex-1 flex flex-col justify-between">
          <div>
            <h3 className="font-bold text-lg cart-item-title" style={{ color: "var(--theme-text-primary)" }}>
              {item.name}
            </h3>
            <p className="text-sm mb-2 cart-item-description" style={{ color: "var(--theme-text-secondary)" }}>
              {item.description}
            </p>
          </div>

          {/* Quantity + Price + Remove */}
          <div className="flex items-center justify-between gap-4">
            {/* Quantity Controls */}
            <div className="flex items-center gap-2">
              <button
                onClick={() => handleQuantityChange(item.quantity - 1)}
                disabled={item.quantity <= 1}
                className="h-8 w-8 flex items-center justify-center font-bold text-lg disabled:opacity-50 border rounded cart-quantity-btn"
                style={{
                  borderColor: "var(--theme-accent)",
                  color: "var(--theme-accent)",
                  backgroundColor: "transparent",
                  transition: "background-color 0.2s ease",
                }}
                onMouseEnter={(e) => {
                  if (!e.currentTarget.disabled)
                    e.currentTarget.style.backgroundColor = "var(--theme-accent-hover)";
                }}
                onMouseLeave={(e) => {
                  e.currentTarget.style.backgroundColor = "transparent";
                }}
              >
                <Minus className="w-4 h-4" />
              </button>

              <input
                type="number"
                min="1"
                value={item.quantity}
                onChange={(e) =>
                  handleQuantityChange(parseInt(e.target.value) || 1)
                }
                className="w-16 text-center h-8 border rounded cart-quantity-input"
                style={{
                  backgroundColor: "var(--theme-surface-elevated)",
                  borderColor: "var(--theme-accent)",
                  color: "var(--theme-text-primary)",
                }}
              />

              <button
                onClick={() => handleQuantityChange(item.quantity + 1)}
                className="h-8 w-8 flex items-center justify-center font-bold text-lg border rounded cart-quantity-btn"
                style={{
                  borderColor: "var(--theme-accent)",
                  color: "var(--theme-accent)",
                  backgroundColor: "transparent",
                  transition: "background-color 0.2s ease",
                }}
                onMouseEnter={(e) => {
                  e.currentTarget.style.backgroundColor = "var(--theme-accent-hover)";
                }}
                onMouseLeave={(e) => {
                  e.currentTarget.style.backgroundColor = "transparent";
                }}
              >
                <Plus className="w-4 h-4" />
              </button>
            </div>

            {/* Price & Remove */}
            <div className="flex items-center gap-4">
              <div className="text-right cart-price-info">
                <p className="text-xs" style={{ color: "var(--theme-text-secondary)" }}>
                  {typeof item.price === "number"
                    ? `$${item.price.toFixed(2)} each`
                    : `$${parseFloat(item.price).toFixed(2)} each`}
                </p>
                <p
                  className="font-bold text-lg"
                  style={{ color: "var(--theme-accent)" }}
                >
                  {typeof subtotal === "number"
                    ? `$${subtotal.toFixed(2)}`
                    : `$${parseFloat(subtotal).toFixed(2)}`}
                </p>
              </div>

              <button
                onClick={() => removeFromCart(item.id)}
                className="h-8 w-8 hover:bg-red-500/20 rounded"
                style={{
                  color: "#ef4444",
                  backgroundColor: "transparent",
                  border: "none",
                  transition: "background-color 0.2s ease, transform 0.1s ease",
                }}
                onMouseEnter={(e) => {
                  e.currentTarget.style.backgroundColor = "rgba(239,68,68,0.15)";
                  e.currentTarget.style.transform = "scale(1.05)";
                }}
                onMouseLeave={(e) => {
                  e.currentTarget.style.backgroundColor = "transparent";
                  e.currentTarget.style.transform = "scale(1)";
                }}
              >
                <Trash2 className="w-4 h-4" />
              </button>
            </div>
          </div>
        </div>
      </div>
    </motion.div>
  );
}
