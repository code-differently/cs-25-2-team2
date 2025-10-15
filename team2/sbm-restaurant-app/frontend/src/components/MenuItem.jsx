function MenuItem({ item, addToCart }) {
  return (
    <div className="bg-white rounded-xl shadow-lg hover:shadow-xl transition-all duration-300 border border-gray-100 overflow-hidden transform hover:-translate-y-1">
      <div className="p-6">
        <h3 className="text-xl font-bold text-gray-800 mb-3">{item.name}</h3>
        <p className="text-gray-600 mb-4 leading-relaxed">{item.description}</p>
        <div className="flex justify-between items-center">
          <span className="text-2xl font-bold" style={{color: '#d4af37'}}>${item.price}</span>
          <button 
            onClick={() => addToCart(item)}
            className="text-white font-semibold py-2 px-6 rounded-lg shadow-md transition-colors duration-200 transform hover:scale-105"
            style={{backgroundColor: '#d4af37'}}
            onMouseEnter={(e) => e.target.style.backgroundColor = '#b8941f'}
            onMouseLeave={(e) => e.target.style.backgroundColor = '#d4af37'}
          >
            Add to Cart
          </button>
        </div>
      </div>
    </div>
  );
}

export default MenuItem;
