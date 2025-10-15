import React from 'react';

const Home = () => {
  return (
    <div className="home">
      <header className="hero">
        <div className="auth-buttons">
          <button>Sign In</button>
          <button>Log In</button>
        </div>
        <h1>Welcome to Spud Munch Bunch!</h1>
        <p>Your journey starts here</p>
        <button className="cta-button">Get Started</button>
      </header>
      
      <section className="features">
        <div className="container">
          <h2>Features</h2>
          <div className="feature-grid">
            <div className="feature-item">
              <h3>Feature One</h3>
              <p>Description of the first feature</p>
            </div>
            <div className="feature-item">
              <h3>Feature Two</h3>
              <p>Description of the second feature</p>
            </div>
            <div className="feature-item">
              <h3>Feature Three</h3>
              <p>Description of the third feature</p>
            </div>
          </div>
        </div>
      </section>
    </div>
  );
};

export default Home;
