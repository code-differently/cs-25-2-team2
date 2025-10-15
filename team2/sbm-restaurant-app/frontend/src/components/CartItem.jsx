import React from 'react';

function CartItem({ item, updateQuantity, removeFromCart }) {
  const handleQuantityChange = (newQuantity) => {
    if (newQuantity <= 0) {
      removeFromCart(item.id);
    } else {
      updateQuantity(item.id, newQuantity);
    }
  };

  return (
    <div className="bg-white p-6 rounded-xl shadow-md border border-gray-100 hover:shadow-lg transition-shadow duration-200">
      <div className="flex flex-col md:flex-row md:items-center justify-between gap-4">
        <div className="flex-1">
          <h4 className="text-lg font-semibold text-gray-800 mb-1">{item.name}</h4>
          <p style={{color: '#d4af37'}} className="font-medium">${item.price.toFixed(2)} each</p>
        </div>
        
        <div className="flex items-center gap-4">
          <div className="flex items-center gap-3 bg-gray-50 rounded-lg p-2">
            <button 
              onClick={() => handleQuantityChange(item.quantity - 1)}
              disabled={item.quantity <= 1}
              className="w-8 h-8 rounded-full text-white font-bold flex items-center justify-center transition-colors duration-200 disabled:opacity-50 disabled:cursor-not-allowed"
              style={{backgroundColor: '#d4af37'}}
              onMouseEnter={(e) => !e.target.disabled && (e.target.style.backgroundColor = '#b8941f')}
              onMouseLeave={(e) => !e.target.disabled && (e.target.style.backgroundColor = '#d4af37')}
            >
              -
            </button>
            <span className="w-8 text-center font-semibold text-gray-700">{item.quantity}</span>
            <button 
              onClick={() => handleQuantityChange(item.quantity + 1)}
              className="w-8 h-8 rounded-full text-white font-bold flex items-center justify-center transition-colors duration-200"
              style={{backgroundColor: '#d4af37'}}
              onMouseEnter={(e) => e.target.style.backgroundColor = '#b8941f'}
              onMouseLeave={(e) => e.target.style.backgroundColor = '#d4af37'}
            >
              +
            </button>
          </div>
          
          <div className="text-right">
            <div className="text-lg font-bold text-gray-800">
              ${(item.price * item.quantity).toFixed(2)}
            </div>
            <div className="text-sm text-gray-500">total</div>
          </div>
          
          <button 
            onClick={() => removeFromCart(item.id)}
            className="bg-red-100 hover:bg-red-200 text-red-600 font-medium py-2 px-4 rounded-lg transition-colors duration-200"
          >
            Remove
          </button>
        </div>
      </div>
    </div>
  );
}

export default CartItem;
