"use client";
import { useEffect, useState } from "react";
import Link from "next/link";
import Loading from "./components/Loading";

export default function LandingPage() {
  const [groups, setGroups] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetch(`${process.env.NEXT_PUBLIC_BASE_URL}/api/groups`)
      .then((res) => res.json())
      .then((data) => {
        setGroups(data.data || []);
        setLoading(false);
      })
      .catch(() => setLoading(false));
  }, []);

  if (loading) return <Loading />;

  return (
    <div className="max-w-4xl mx-auto mt-10 px-4">
      <h1 className="text-2xl font-bold text-center mb-8 gold-text">Grupos</h1>
      <div className="flex flex-col gap-4">
        {groups.map((group) => (
          <Link key={group.id} href={`/${group.slug}`}>
            <div className="bg-black shadow-amber-50/50 shadow-2xs p-5 clip-skew w-full hover:opacity-80 transition-opacity cursor-pointer">
              <p className="text-center gold-text text-lg">{group.name}</p>
            </div>
          </Link>
        ))}
      </div>
    </div>
  );
}
