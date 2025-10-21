const team = [
  { 
    name: "Trishtan Husser", 
    role: "Mash Master", 
    img: "/images/team/potato-5/trishtan3.png",
    description: "<strong>LinkedIn:</strong><br>Linkedin.com/in/trishtan-sapphire<br><strong>GitHub:</strong><br>Github.com/trishtanhusser<br><strong>Email:</strong><br>Thusser252@gmail.com"
  },
  { 
    name: "Dean Walston", 
    role: "Spud Captain", 
    img: "/images/team/potato-5/dean4.png",
    description: "<strong>LinkedIn:</strong><br>Linkedin.com/in/dean-ah-walston<br><strong>GitHub:</strong><br>Github.com/dahw<br><strong>Email:</strong><br>Dwalston252@gmail.com"
  },
  { 
    name: "Brooklyn Harden", 
    role: "Head Fryer", 
    img: "/images/team/potato-5/brooklyn6.png",
    description: "<strong>LinkedIn:</strong><br>Linkedin.com/in/brooklynharden<br><strong>GitHub:</strong><br>Github.com/brooklynharden<br><strong>Email:</strong><br>Baharden252@gmail.com"
  },
  { 
    name: "Wayleom Vargas", 
    role: "Code Potato", 
    img: "/images/team/potato-5/wayleom5.png",
    description: "<strong>LinkedIn:</strong><br>Linkedin.com/in/wayleomvrubio<br><strong>GitHub:</strong><br>Github.com/wayleom-rubio<br><strong>Email:</strong><br>Wvrubio252@gmail.com"
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
            <p className="text-gray-600 text-sm leading-relaxed" dangerouslySetInnerHTML={{__html: member.description}}></p>
          </div>
        ))}
      </div>
    </div>
  </section>
);

export default TeamSection;
