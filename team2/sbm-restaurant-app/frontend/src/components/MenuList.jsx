import MenuItem from './MenuItem';

function MenuList({ items }) {
  if (items.length === 0) return <p>No items available</p>;

  return (
    <div>
      {items.map(item => (
        <MenuItem key={item.id} item={item} />
      ))}
    </div>
  );
}

export default MenuList;
