import React from "react";

const CardStats = ({ name, total, winTeamA, winTeamB, percentage }) => {
  return (
    <div className="bg-card/60 border border-black/60 rounded-lg p-2 w-64 flex flex-col items-center gap-2">
      <h3 className="text-sm text-muted-foreground">
        {name}
        {total != null ? `: ${total}` : ""}
      </h3>
      <div className="flex w-full justify-center ">
        <div className=" border-r-2 pr-2 text-right">
          <p className="font-bold">{winTeamA}</p>
          {total != null && (
            <p className="text-sm font-light">{percentage(winTeamA, total)}%</p>
          )}
        </div>
        <div className="pl-2 text-left">
          <p className="font-bold">{winTeamB}</p>
          {total != null && (
            <p className="text-sm font-light">{percentage(winTeamB, total)}%</p>
          )}
        </div>
      </div>
    </div>
  );
};

export default CardStats;
