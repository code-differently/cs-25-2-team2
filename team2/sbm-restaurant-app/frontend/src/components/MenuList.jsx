import MenuItem from './MenuItem';

function MenuList({ items, addToCart }) {
  if (items.length === 0) return <p>No items available</p>;

  return (
    <div>
      {items.map(item => (
        <MenuItem key={item.id} item={item} addToCart={addToCart} />
      ))}
    </div>
  );
}

export default MenuList;
