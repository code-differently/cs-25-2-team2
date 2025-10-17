const team = [
  { name: "Developer 1", role: "Frontend Developer", img: "https://via.placeholder.com/150" },
  { name: "Developer 2", role: "Backend Developer", img: "https://via.placeholder.com/150" },
  { name: "Developer 3", role: "UI/UX Designer", img: "https://via.placeholder.com/150" },
  { name: "Developer 4", role: "Project Manager", img: "https://via.placeholder.com/150" },
];

const TeamSection = () => (
  <section className="team">
    <h2>Meet Our Team</h2>
    <div className="team-grid">
      {team.map((member, i) => (
        <div className="team-card" key={i}>
          <img src={member.img} alt={member.name} />
          <h3>{member.name}</h3>
          <p>{member.role}</p>
        </div>
      ))}
    </div>
  </section>
);

export default TeamSection;
