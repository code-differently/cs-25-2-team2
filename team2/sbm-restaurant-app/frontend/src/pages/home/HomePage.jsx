import Carousel from "../components/Carousel";
import TeamSection from "../components/TeamSection";

const Home = () => {
  return (
    <div className="home">
      {/* Hero Carousel */}
      <Carousel />

      {/* Our Story Section */}
      <section className="our-story">
        <h2>Our Story</h2>
        <p>
          Spud Munch Bunch began with one simple mission — to share our love of
          potatoes in every delicious form possible. From crispy fries to loaded
          baked spuds, we believe every potato deserves to shine.
        </p>
      </section>

      {/* Why We're Here Section */}
      <section className="why-here">
        <h2>Why We're Here</h2>
        <p>
          We're here to make comfort food fun, fast, and friendly. Our team of
          tech-savvy tater lovers works to bring fresh ideas and fresh fries to
          your door.
        </p>
      </section>

      {/* Meet Our Team Section */}
      <TeamSection />
    </div>
  );
};

export default Home;
