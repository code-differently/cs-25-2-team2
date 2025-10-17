import { useState } from "react";

const dishes = [
  {
    img: "https://source.unsplash.com/800x400/?fries",
    title: "Crispy Golden Fries",
  },
  {
    img: "https://source.unsplash.com/800x400/?baked-potato",
    title: "Loaded Baked Potato",
  },
  {
    img: "https://source.unsplash.com/800x400/?mashed-potatoes",
    title: "Creamy Mashed Potatoes",
  },
];

const Carousel = () => {
  const [index, setIndex] = useState(0);

  const nextSlide = () => setIndex((index + 1) % dishes.length);
  const prevSlide = () => setIndex((index - 1 + dishes.length) % dishes.length);

  return (
    <div className="carousel">
      <button onClick={prevSlide}>‹</button>
      <div className="carousel-slide">
        <img src={dishes[index].img} alt={dishes[index].title} />
        <h3>{dishes[index].title}</h3>
      </div>
      <button onClick={nextSlide}>›</button>
    </div>
  );
};

export default Carousel;
