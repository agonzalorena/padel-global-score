"use client";
import { useParams } from "next/navigation";
import BoxHistory from "../../components/BoxHistory";
import TitleSections from "../../components/TitleSections";

const Page = () => {
  const { slug } = useParams();
  return (
    <>
      <TitleSections
        title={"Historial"}
        subtitle={"Lo que pasó y por qué dolió"}
      />
      <BoxHistory slug={slug} />
    </>
  );
};

export default Page;
