"use client";
import { use, useEffect, useState } from "react";
import BoxTeam from "./components/BoxTeam";
import BoxMenu from "./components/BoxMenu";
import Loading from "./components/Loading";
import Stats from "./components/Stats";
import JoinTelegram from "./components/JoinTelegram";

export default function Home() {
  const [loading, setLoading] = useState(true);
  const [lastMatch, setLastMatch] = useState({});
  const [teams, setTeams] = useState([]);
  const [results, setResults] = useState([]);
  const [win, setWin] = useState(null);

  const getDay = (dateString) => {
    const date = new Date(dateString + "T00:00:00-03:00");
    return date.toLocaleDateString("es-AR", {
      weekday: "short",
      month: "short",
      day: "numeric",
      timeZone: "America/Argentina/Buenos_Aires",
    });
  };
  const fetchTeams = async () => {
    try {
      const response = await fetch(
        `${process.env.NEXT_PUBLIC_BASE_URL}/api/matches?teamA=1&teamB=2&page=0&size=1`
      );
      const res = await response.json();
      setTeams(res.data.content);
      setLastMatch(res.data.content[0]);
    } catch (error) {
      console.error("Error fetching teams:", error);
    }
  };
  const fetchResultsStatistics = async () => {
    const teamA = teams[0].teamA;
    const teamB = teams[0].teamB;

    try {
      const response = await fetch(
        `${process.env.NEXT_PUBLIC_BASE_URL}/api/statistics?teamAId=${teamA.id}&teamBId=${teamB.id}&year=2026`
      );

      if (!response.ok) throw new Error("Error en el servidor");

      const res = await response.json();
      const stats = res.data;

      setResults(stats);

      // Determinar ganador o empate (0-0)
      if (stats.winMatchesTeamA === 0 && stats.winMatchesTeamB === 0) {
        setWin(null);
      } else {
        setWin(stats.winMatchesTeamA > stats.winMatchesTeamB ? teamA : teamB);
      }
    } catch (error) {
      console.error("Error fetching results:", error);
    } finally {
      setLoading(false);
    }
  };
  useEffect(() => {
    fetchTeams();
  }, []);
  useEffect(() => {
    if (teams.length === 0) return;
    fetchResultsStatistics();
  }, [teams]);
  if (loading || teams.length === 0) {
    return <Loading />;
  }
  return (
    <>
      <div className="mt-5 max-w-4xl mx-auto flex flex-col items-center text-center">
        <div className="flex justify-center w-full px-2 ">
          <div className="bg-black shadow-amber-50/50 shadow-2xs p-5 clip-skew w-full">
            {lastMatch.state == "PENDING" ? (
              <div className="flex text-sm justify-center gap-10 md:gap-20 w-full">
                <p>{getDay(lastMatch.date)}</p>
                <p>
                  {lastMatch.location.name.charAt(0).toUpperCase() +
                    lastMatch.location.name.slice(1)}
                </p>
                <p>{lastMatch.time.slice(0, 5)}hs</p>
              </div>
            ) : (
              <div className="flex text-sm justify-center gap-10 md:gap-20 w-full">
                <p>Proximamente...</p>
              </div>
            )}
          </div>
        </div>
      </div>
      <BoxTeam
        teamA={teams[0].teamA}
        teamB={teams[0].teamB}
        win={win}
        results={results}
      />
      <Stats results={results} />
      <BoxMenu teamA={teams[0].teamA} teamB={teams[0].teamB} />
      {/* <JoinTelegram /> */}
    </>
  );
}
