import MenuItem from './MenuItem';

function MenuList({ items, addToCart }) {
  if (items.length === 0) return <p className="text-center text-gray-500 text-lg py-8">No items available</p>;

  return (
    <div className="grid md:grid-cols-2 lg:grid-cols-3 gap-6">
      {items.map(item => (
        <MenuItem key={item.id} item={item} addToCart={addToCart} />
      ))}
    </div>
  );
}

export default MenuList;
