"use client";
import React, { useEffect, useState } from "react";
import CardPlayer from "./CardPlayer";
import Loading from "./Loading";

const BoxPlayer = ({ slug }) => {
  const [teams, setTeams] = useState([]);

  const fetchTeams = async () => {
    try {
      const groupRes = await fetch(
        `${process.env.NEXT_PUBLIC_BASE_URL}/api/groups/${slug}`,
      );
      const { data: group } = await groupRes.json();
      setTeams([group.teamA, group.teamB]);
    } catch (error) {
      console.error("Error fetching teams:", error);
    }
  };

  useEffect(() => {
    if (slug) fetchTeams();
  }, [slug]);

  if (teams.length === 0) return <Loading />;

  return (
    <>
      {teams.map((m) => (
        <div
          className="flex flex-col gap-4 justify-center items-center px-2"
          key={m.id}
        >
          <CardPlayer
            name={
              m.leftSide.name.charAt(0).toUpperCase() + m.leftSide.name.slice(1)
            }
            urlPhoto={m.leftSide.urlPhoto}
            strength={m.leftSide.strength}
            weakness={m.leftSide.weakness}
            position="Reves"
          />
          <CardPlayer
            name={
              m.rightSide.name.charAt(0).toUpperCase() +
              m.rightSide.name.slice(1)
            }
            urlPhoto={m.rightSide.urlPhoto}
            strength={m.rightSide.strength}
            weakness={m.rightSide.weakness}
            position="Drive"
          />
        </div>
      ))}
    </>
  );
};

export default BoxPlayer;
