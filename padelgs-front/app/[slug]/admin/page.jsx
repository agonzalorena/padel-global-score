"use client";
import React, { useEffect, useState } from "react";
import { useParams } from "next/navigation";
import FormCreateMatch from "../../components/FormCreateMatch";
import FormFinishMatch from "../../components/FormFinishMatch";
import Loading from "../../components/Loading";
import TitleSections from "../../components/TitleSections";

const Page = () => {
  const { slug } = useParams();
  const [loading, setLoading] = useState(true);
  const [pendingMatch, setPendingMatch] = useState(null);
  const [group, setGroup] = useState(null);

  const fetchPendingMatch = async () => {
    try {
      // Primero obtenemos el grupo para saber los teamIds
      const groupRes = await fetch(
        `${process.env.NEXT_PUBLIC_BASE_URL}/api/groups/${slug}`,
      );
      const groupData = await groupRes.json();
      const grp = groupData.data;
      setGroup(grp);

      const token = document.cookie
        .split("; ")
        .find((row) => row.startsWith("token="))
        ?.split("=")[1];

      const lastMatch = await fetch(
        `${process.env.NEXT_PUBLIC_BASE_URL}/api/groups/${slug}/matches?teamA=${grp.teamA.id}&teamB=${grp.teamB.id}&page=0&size=1`,
        {
          headers: { Authorization: `Bearer ${token}` },
        },
      );
      const lastMatchData = await lastMatch.json();
      setPendingMatch(lastMatchData.data.content[0]);
      setLoading(false);
    } catch (error) {
      console.error("Error fetching pending match:", error);
    }
  };

  const logOut = () => {
    document.cookie = "token=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";
    window.location.href = "/login";
  };
  useEffect(() => {
    const token = document.cookie
      .split("; ")
      .find((row) => row.startsWith("token="))
      ?.split("=")[1];

    if (!token) {
      window.location.href = `/${slug}/login`;
      return;
    }

    if (slug) fetchPendingMatch();
  }, [slug]);

  if (loading) return <Loading />;

  return (
    <>
      <TitleSections title={"Panel"} subtitle={"Administre los encuentros"} />
      <FormCreateMatch
        slug={slug}
        group={group}
        hasPendingMatch={pendingMatch?.state === "PENDING"}
      />
      <div className="flex flex-col gap-6 mt-10 max-w-md mx-auto items-center">
        {pendingMatch?.state === "PENDING" ? (
          <FormFinishMatch pendingMatch={pendingMatch} slug={slug} />
        ) : (
          <p className="text-center mt-4 text-sm text-muted-foreground">
            No hay partidos pendientes.
          </p>
        )}
      </div>
      <div className="w-full flex justify-center mt-5">
        <button
          className="shadow-xs shadow-white/10 p-2 rounded-2xl"
          onClick={logOut}
        >
          Cerrar sesion
        </button>
      </div>
    </>
  );
};

export default Page;
