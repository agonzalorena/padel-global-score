"use client";
import React, { useEffect, useState } from "react";
import CardMatch from "./CardMatch.jsx";
import Loading from "./Loading.jsx";

const BoxHistory = ({ slug }) => {
  const [teams, setTeams] = useState([]);
  const [locations, setLocations] = useState([]);
  const [size, setSize] = useState(4);
  const [totalElements, setTotalElements] = useState(0);
  const [location, setLocation] = useState("all");
  const [winner, setWinner] = useState("all");
  const [loading, setLoading] = useState(true);
  const [scrollPosition, setScrollPosition] = useState(0);
  const [teamA, setTeamA] = useState(null);
  const [teamB, setTeamB] = useState(null);

  const fetchMatches = async () => {
    try {
      setScrollPosition(window.scrollY);
      const query = new URLSearchParams();
      if (location !== "all") query.append("location", location);
      if (winner !== "all") query.append("winner", winner);
      query.append("page", 0);
      query.append("size", size);

      const response = await fetch(
        `${process.env.NEXT_PUBLIC_BASE_URL}/api/groups/${slug}/matches?${query.toString()}`,
      );
      const res = await response.json();
      setTeams(res.data.content);
      if (!teamA && res.data.content.length > 0)
        setTeamA(res.data.content[0].teamA);
      if (!teamB && res.data.content.length > 0)
        setTeamB(res.data.content[0].teamB);
      setTotalElements(res.data.totalElements);
      setLoading(false);
      window.scrollTo(0, scrollPosition);
    } catch (error) {
      console.error("Error fetching matches:", error);
    }
  };

  const fetchLocations = async () => {
    try {
      const response = await fetch(
        `${process.env.NEXT_PUBLIC_BASE_URL}/api/locations`,
      );
      const res = await response.json();
      setLocations(res.data);
    } catch (error) {
      console.error("Error fetching locations:", error);
    }
  };

  useEffect(() => {
    fetchLocations();
    fetchMatches();
  }, []);

  useEffect(() => {
    window.scrollTo(0, scrollPosition);
  }, [teams]);

  useEffect(() => {
    setLoading(true);
    fetchMatches();
  }, [size, location, winner]);

  if (loading) return <Loading />;

  return (
    <section className="px-2">
      <div className="bg-card/40 p-2 flex justify-end gap-2 md:gap-4 text-xs rounded-lg">
        <label>Lugar:</label>
        <select value={location} onChange={(e) => setLocation(e.target.value)}>
          <option value="all">Todos</option>
          {locations.map((loc) => (
            <option key={loc.id} value={loc.id}>
              {loc.name}
            </option>
          ))}
        </select>
        <label>Ganador:</label>
        <select value={winner} onChange={(e) => setWinner(e.target.value)}>
          <option value="all">Todos</option>
          <option value={teamA?.id}>
            {teamA?.leftSide.name} / {teamA?.rightSide.name}
          </option>
          <option value={teamB?.id}>
            {teamB?.leftSide.name} / {teamB?.rightSide.name}
          </option>
        </select>
      </div>

      {teams.map((match) => (
        <div className="match-item" key={match.id}>
          <CardMatch match={match} />
        </div>
      ))}

      <div className="container px-5 py-2 mx-auto">
        {teams.length === 0 && (
          <div className="text-center">
            <p>Sin resultados</p>
          </div>
        )}
        <div className="w-full flex justify-center">
          <button
            className="px-5 py-2 bg-card/50 rounded-xl hover:bg-card/70 disabled:opacity-50"
            onClick={() => setSize(size + 4)}
            disabled={teams.length >= totalElements}
          >
            Ver más
          </button>
        </div>
      </div>
    </section>
  );
};

export default BoxHistory;
