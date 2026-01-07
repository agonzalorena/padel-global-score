import React, { useRef, useState } from "react";
import confetti from "canvas-confetti";

const CardTeam = ({ team, wins, streak, win, urlTeam }) => {
  const [cold, setCold] = useState(false);
  const confettiRef = useRef(
    confetti.create(null, { resize: true, useWorker: true })
  );
  const throwConfetti = () => {
    const scalar = 4;
    const emojiShape = confetti.shapeFromText({
      text: "🔥",
      scalar, // importante: crear la forma con el scalar que querés usar
      fontFamily: "Apple Color Emoji, Segoe UI Emoji, Noto Color Emoji",
    });

    confettiRef.current({
      particleCount: 15,
      spread: 60,
      origin: { y: 0.6 },
      shapes: [emojiShape],
      scalar,
    });
  };
  const activeEffect = () => {
    if (cold) return; // prevent spamming
    setCold(true);
    setTimeout(() => {
      setCold(false);
    }, 1400);
    if (streak > 0) {
      throwConfetti();
    }
  };
  const teamLeftSideName =
    team.leftSide.name.charAt(0).toUpperCase() + team.leftSide.name.slice(1);
  const teamRightSideName =
    team.rightSide.name.charAt(0).toUpperCase() + team.rightSide.name.slice(1);
  const isWinner = win && win.id === team.id;
  return (
    <button
      onClick={activeEffect}
      id={streak == 0 && cold ? "cold" : ""}
      className={`bg-card p-4 rounded-lg flex flex-col items-center ${
        isWinner ? "border border-primary shadow-lg shadow-primary/30" : ""
      }`}
    >
      <div
        className={`w-20 h-20 mb-2 flex justify-center items-center rounded-full overflow-hidden border-3 ${
          isWinner
            ? "border-primary shadow-xl shadow-primary/20"
            : "border-white/5"
        }`}
      >
        <img className="w-full h-full object-cover" src={urlTeam} alt="" />
      </div>
      <div id="Names" className="text-center text-xl font-extrabold mt-1">
        <h3>{teamLeftSideName}</h3>
        <h3>{teamRightSideName}</h3>
      </div>
      <div id="Wins" className="text-center my-2 flex flex-col gap-1">
        <p className="text-muted-foreground text-sm">TOTAL WINS</p>
        <p
          className={`text-4xl font-bold ${
            isWinner
              ? "bg-gold-real bg-clip-text text-transparent font-black"
              : ""
          }`}
        >
          {wins}
        </p>
      </div>
      <div
        className={`border-t border-white/10 w-full text-center relative ${
          streak > 0 ? "text-primary" : ""
        }`}
      >
        <p className="text-muted-foreground text-sm z-20 relative mt-1">
          RACHA
        </p>
        <div className="min-h-18 flex justify-center items-center">
          {streak > 0 ? (
            <div className="flex justify-center items-center mt-2 ">
              <p className="absolute right-1/2 bottom-0 text-2xl translate-x-1/2 bg-gold-real bg-clip-text text-transparent font-black z-20">
                {streak}
              </p>
              <svg
                fill="#E25822" //FF6600
                viewBox="0 0 32 32"
                version="1.1"
                xmlns="http://www.w3.org/2000/svg"
                className={`absolute animate-pulse w-20 h-20 right-1/2 z-10 translate-x-1/2 opacity-80 filter drop-shadow-[0_-6px_6px_rgba(255,0,0,0.7)] ${
                  streak > 0 ? "block" : "hidden"
                }`}
              >
                <path d="M15.888 31.977c-7.539 0-12.887-5.228-12.887-12.431 0-3.824 2.293-7.944 2.39-8.116 0.199-0.354 0.59-0.547 0.998-0.502 0.404 0.052 0.736 0.343 0.84 0.736 0.006 0.024 0.624 2.336 1.44 3.62 0.548 0.864 1.104 1.475 1.729 1.899-0.423-1.833-0.747-4.591-0.22-7.421 1.448-7.768 7.562-9.627 7.824-9.701 0.337-0.097 0.695-0.010 0.951 0.223 0.256 0.235 0.373 0.586 0.307 0.927-0.010 0.054-1.020 5.493 1.123 10.127 0.195 0.421 0.466 0.91 0.758 1.399 0.083-0.672 0.212-1.386 0.41-2.080 0.786-2.749 2.819-3.688 2.904-3.726 0.339-0.154 0.735-0.104 1.027 0.126 0.292 0.231 0.433 0.603 0.365 0.969-0.011 0.068-0.294 1.938 1.298 4.592 1.438 2.396 1.852 3.949 1.852 6.928 0 7.203-5.514 12.43-13.111 12.43zM6.115 14.615c-0.549 1.385-1.115 3.226-1.115 4.931 0 6.044 4.506 10.43 10.887 10.43 6.438 0 11.11-4.386 11.11-10.431 0-2.611-0.323-3.822-1.567-5.899-0.832-1.386-1.243-2.633-1.439-3.625-0.198 0.321-0.382 0.712-0.516 1.184-0.61 2.131-0.456 4.623-0.454 4.649 0.029 0.446-0.242 0.859-0.664 1.008s-0.892 0.002-1.151-0.364c-0.075-0.107-1.854-2.624-2.637-4.32-1.628-3.518-1.601-7.323-1.434-9.514-1.648 0.96-4.177 3.104-4.989 7.466-0.791 4.244 0.746 8.488 0.762 8.529 0.133 0.346 0.063 0.739-0.181 1.018-0.245 0.277-0.622 0.4-0.986 0.313-0.124-0.030-2.938-0.762-4.761-3.634-0.325-0.514-0.617-1.137-0.864-1.742z"></path>
              </svg>
            </div>
          ) : (
            <div>
              <svg
                viewBox="0 0 1024 1024"
                className="icon h-10 w-10 opacity-20"
                fill="#779ecb"
              >
                <path d="M539.7 463.1V273.5l90.8-91.2c11-11 10.9-28.7 0-39.6-11-11-28.7-10.9-39.6 0L539.7 194V91.3c0-15.1-12.5-27.8-28-27.8-15.6 0-28 12.5-28 27.8V195L432.5 143.8c-10.9-10.9-28.6-11-39.6 0-10.9 10.9-11 28.6 0 39.6l90.8 91.2v188.6l-164.2-94.8L286 244.1c-4-15-19.4-23.8-34.3-19.8-15 4-23.8 19.4-19.8 34.3l18.7 70-88.9-51.4c-13.1-7.6-30.4-3-38.1 10.4-7.8 13.5-3.2 30.5 10.1 38.2l89.8 51.9-70 18.7c-14.9 4-23.8 19.3-19.8 34.3 4 14.9 19.3 23.8 34.3 19.8l124.4-33.1 163.3 94.3-164.2 94.8-124.4-33.1c-15-4-30.3 4.9-34.3 19.8-4 15 4.9 30.3 19.8 34.3l70 18.7-88.9 51.4c-13.1 7.6-17.8 24.8-10.1 38.2 7.8 13.5 24.8 18 38.1 10.4l89.8-51.9-18.7 70c-4 14.9 4.8 30.3 19.8 34.3 14.9 4 30.3-4.8 34.3-19.8l33.6-124.3 163.3-94.3v190.6l-90.8 93c-11 11-10.9 28.7 0 39.6 11 11 28.7 10.9 39.6 0l51.2-51.2v99.9c0 15.1 12.5 27.8 28 27.8 15.6 0 28-12.5 28-27.8v-101l51.2 51.2c10.9 10.9 28.6 11 39.6 0 10.9-10.9 11-28.6 0-39.6l-90.8-93V560.2l165.1 95.3L740 780.6c4 15 19.4 23.8 34.3 19.8 15-4 23.8-19.4 19.8-34.3l-18.7-70 86.6 50c13.1 7.6 30.4 3 38.1-10.4 7.8-13.5 3.2-30.5-10.1-38.2L802.6 647l70-18.7c14.9-4 23.8-19.3 19.8-34.3-4-14.9-19.3-23.8-34.3-19.8l-125.9 32.2L568 511.6l165.1-95.3L859 448.5c15 4 30.3-4.9 34.3-19.8 4-15-4.9-30.3-19.8-34.3l-70-18.7 86.6-50c13.1-7.6 17.8-24.8 10.1-38.2-7.8-13.5-24.8-18-38.1-10.4l-87.4 50.5 18.7-70c4-14.9-4.8-30.3-19.8-34.3-14.9-4-30.3 4.8-34.3 19.8l-35.1 125.1-164.5 94.9z"></path>
              </svg>
            </div>
          )}
        </div>
      </div>
    </button>
  );
};

export default CardTeam;
