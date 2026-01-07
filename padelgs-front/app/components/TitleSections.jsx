import React from "react";

const TitleSections = ({ title, subtitle }) => {
  return (
    <div className="flex flex-col text-center w-full p-5">
      <h1 className="sm:text-3xl text-2xl font-medium title-font bg-gold-real bg-clip-text text-transparent ">
        {title}
      </h1>
      <p className="text-sm text-white/70">{subtitle}</p>
    </div>
  );
};

export default TitleSections;
