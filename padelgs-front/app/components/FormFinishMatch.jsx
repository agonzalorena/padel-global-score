"use client";
import React, { useState } from "react";

const FormFinishMatch = ({ pendingMatch, slug }) => {
  const [actionMatch, setActionMatch] = useState("finish");
  const [error, setError] = useState(null);

  const handleSubmit = async (e) => {
    e.preventDefault();
    const formData = new FormData(e.target);

    const results = [1, 2, 3].map((n) => ({
      numberSet: n,
      gamesTeamA: parseInt(formData.get(`setA_${n}`)),
      gamesTeamB: parseInt(formData.get(`setB_${n}`)),
    }));

    const token = document.cookie
      .split("; ")
      .find((row) => row.startsWith("token="))
      ?.split("=")[1];

    try {
      const response = await fetch(
        `${process.env.NEXT_PUBLIC_BASE_URL}/api/groups/${slug}/matches/${actionMatch}/${pendingMatch.id}`,
        {
          method: "PUT",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`,
          },
          body: JSON.stringify({ results }),
        },
      );
      if (!response.ok) {
        const errorData = await response.json();
        setError(errorData.message);
        throw new Error("Network response was not ok");
      }
      alert("Resultado cargado exitosamente");
      window.location.reload();
    } catch (error) {
      alert("Error cargando resultado");
    }
  };

  return (
    <div className="w-full max-w-md">
      <p className="text-center text-sm text-muted-foreground">
        Último partido:{" "}
        <span className="text-white font-bold">
          {new Date(pendingMatch.date).toLocaleDateString()} -{" "}
          {pendingMatch.location.name}
        </span>
      </p>

      <div className="flex flex-col gap-4 mt-4 bg-card/50 p-4 rounded-lg">
        <h2 className="font-semibold">Cargar resultado</h2>
        {error && <p className="text-xs text-red-500">{error}</p>}

        <form onSubmit={handleSubmit} className="flex flex-col gap-3">
          {/* Header */}
          <div className="grid grid-cols-3 text-xs text-muted-foreground text-center">
            <span></span>
            <span>
              {pendingMatch.teamA.leftSide.name}/
              {pendingMatch.teamA.rightSide.name}
            </span>
            <span>
              {pendingMatch.teamB.leftSide.name}/
              {pendingMatch.teamB.rightSide.name}
            </span>
          </div>

          {[1, 2, 3].map((n) => (
            <div key={n} className="grid grid-cols-3 gap-2 items-center">
              <span className="text-center text-sm text-muted-foreground">
                Set {n}
              </span>
              <input
                type="number"
                name={`setA_${n}`}
                min="0"
                max="7"
                required
                placeholder="0"
                className="bg-card p-2 rounded-lg text-center w-full"
              />
              <input
                type="number"
                name={`setB_${n}`}
                min="0"
                max="7"
                required
                placeholder="0"
                className="bg-card p-2 rounded-lg text-center w-full"
              />
            </div>
          ))}

          <div className="flex items-center gap-2 mt-1">
            <input
              type="checkbox"
              id="suspend-match"
              onChange={(e) =>
                setActionMatch(e.target.checked ? "suspend" : "finish")
              }
            />
            <label htmlFor="suspend-match" className="text-sm">
              Suspender partido
            </label>
          </div>

          <button
            type="submit"
            className="mt-2 p-2 shadow-xs shadow-white/10 rounded-xl"
          >
            Cargar
          </button>
        </form>
      </div>
    </div>
  );
};

export default FormFinishMatch;
