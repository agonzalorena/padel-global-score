"use client";
import { useParams } from "next/navigation";
import BoxPlayer from "../../components/BoxPlayer";
import TitleSections from "../../components/TitleSections";

const Page = () => {
  const { slug } = useParams();
  return (
    <div className="flex flex-col gap-4 mt-5 min-h-screen">
      <TitleSections
        title={"Jugadores"}
        subtitle={"Los cracks y sus habilidades"}
      />
      <BoxPlayer slug={slug} />
    </div>
  );
};

export default Page;
