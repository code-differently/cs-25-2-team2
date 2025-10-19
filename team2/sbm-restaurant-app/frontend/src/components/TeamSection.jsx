const team = [
  { 
    name: "Trishtan Husser", 
    role: "Head Chef & Potato Specialist", 
    img: "https://picsum.photos/150/150?random=20",
    description: "15+ years perfecting the art of potato cuisine"
  },
  { 
    name: "Dean Walston", 
    role: "Kitchen Manager", 
    img: "https://picsum.photos/150/150?random=21",
    description: "Ensures every dish meets our golden standards"
  },
  { 
    name: "Brooklyn Harden", 
    role: "Delivery Coordinator", 
    img: "https://picsum.photos/150/150?random=22",
    description: "Gets your spuds to you hot and fast"
  },
  { 
    name: "Wayleom Vargas", 
    role: "Customer Experience Lead", 
    img: "https://picsum.photos/150/150?random=23",
    description: "Making sure every spud experience is perfect"
  },
];

const TeamSection = () => (
  <section className="py-16 px-6 bg-gradient-to-b from-amber-50 to-white">
    <div className="container mx-auto max-w-6xl">
      <h2 className="text-4xl font-bold mb-4 text-center text-gray-800">Meet Our Spud Bunch</h2>
      <p className="text-center text-gray-600 mb-12 text-lg max-w-2xl mx-auto">
        The passionate team behind every perfectly crafted potato dish, working together to bring you the ultimate spud experience.
      </p>
      <div className="team-grid">
        {team.map((member, i) => (
          <div className="team-card bg-white rounded-lg shadow-lg hover:shadow-xl transition-all duration-300 p-6 transform hover:-translate-y-2" key={i}>
            <img src={member.img} alt={member.name} className="w-24 h-24 rounded-full mx-auto mb-4 object-cover border-4 border-amber-200" />
            <h3 className="text-xl font-bold text-gray-800 mb-2">{member.name}</h3>
            <p className="text-amber-600 font-semibold mb-3">{member.role}</p>
            <p className="text-gray-600 text-sm leading-relaxed">{member.description}</p>
          </div>
        ))}
      </div>
    </div>
  </section>
);

export default TeamSection;
