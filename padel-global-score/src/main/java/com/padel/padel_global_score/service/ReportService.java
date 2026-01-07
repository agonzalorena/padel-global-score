package com.padel.padel_global_score.service;

import com.padel.padel_global_score.exception.ResourceNotFoundException;
import com.padel.padel_global_score.persistence.entity.Match;
import com.padel.padel_global_score.persistence.repository.MatchRepo;
import com.padel.padel_global_score.presentation.dto.StatsDTO;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportService {
    private final MatchRepo matchRepo;
    private final TeamService teamService;

    public ReportService(MatchRepo matchRepo, TeamService teamService) {
        this.matchRepo = matchRepo;
        this.teamService = teamService;
    }

    public StatsDTO getStatsReport(Long teamAId, Long teamBId, int year) {
        //chequear que existan los equipos?
        teamService.getTeamById(teamAId);
        teamService.getTeamById(teamBId);
        //chequear que hayan jugado partidos?
        if (matchRepo.findByTeamsAndYear(teamAId, teamBId,year).isEmpty()) {
            //error no hay partidos entre esos equipos
            return new StatsDTO(
                    0L,
                    0L,
                    0L,
                    0L,
                    0L,
                    0L,
                    0L,
                    0L,
                    0L,
                    0L,
                    0L,
                    0L,
                    0L,
                    0L,
                    0L,
                    0L
            );
        }
        Long winGamesTeamA = matchRepo.countWonGamesByTeam(teamAId, teamBId, true);
        Long winGamesTeamB = matchRepo.countWonGamesByTeam(teamAId, teamBId, false);
        long totalGames = winGamesTeamA + winGamesTeamB;
        Long winSetsTeamA = matchRepo.countWonSetsByTeam(teamAId, teamBId, true);
        Long winSetsTeamB = matchRepo.countWonSetsByTeam(teamAId, teamBId, false);
        Long totalSets = winSetsTeamA + winSetsTeamB;
        Long winTiebreaksTeamA = matchRepo.countWonTiebreaksByTeam(teamAId, teamBId, true);
        Long winTiebreaksTeamB = matchRepo.countWonTiebreaksByTeam(teamAId, teamBId, false);
        Long totalTiebreaks = winTiebreaksTeamA + winTiebreaksTeamB;
        System.out.println(getMaxWinningStreaks(teamAId, teamBId));
        Long maxStreakTeamA = getMaxWinningStreaks(teamAId, teamBId).get(teamAId);
        Long maxStreakTeamB = getMaxWinningStreaks(teamAId, teamBId).get(teamBId);
        Long winMatchesTeamA = matchRepo.countWonMatchesByTeam(teamAId, teamBId, true);
        Long winMatchesTeamB = matchRepo.countWonMatchesByTeam(teamAId, teamBId, false);
        long totalMatches = winMatchesTeamA + winMatchesTeamB;
        Long currentStreakTeamA = getCurrentWinningStreaks(teamAId, teamBId).get(teamAId);
        Long currentStreakTeamB = getCurrentWinningStreaks(teamAId, teamBId).get(teamBId);

        return new StatsDTO(
                winGamesTeamA,
                winGamesTeamB,
                totalGames,
                winSetsTeamA,
                winSetsTeamB,
                totalSets,
                winMatchesTeamA,
                winMatchesTeamB,
                totalMatches,
                winTiebreaksTeamA,
                winTiebreaksTeamB,
                totalTiebreaks,
                maxStreakTeamA,
                maxStreakTeamB,
                currentStreakTeamA,
                currentStreakTeamB
        );
    }

    public Map<Long, Long> getMaxWinningStreaks(Long teamAId, Long teamBId) {
        List<Match> matches = matchRepo.findByTeams(teamAId, teamBId);

        long streakA = 0, maxA = 0;
        long streakB = 0, maxB = 0;

        for (Match m : matches) {
            if (m.getWinner() == null) continue;
            Long winnerId = m.getWinner().getId();

            if (winnerId.equals(teamAId)) {
                streakA++;
                maxA = Math.max(maxA, streakA);
                streakB = 0;
            } else if (winnerId.equals(teamBId)) {
                streakB++;
                maxB = Math.max(maxB, streakB);
                streakA = 0;
            }
        }

        Map<Long, Long> result = new HashMap<>();
        result.put(teamAId, maxA);
        result.put(teamBId, maxB);

        return result;
    }

    public Map<Long, Long> getCurrentWinningStreaks(Long teamAId, Long teamBId) {
        List<Match> matches = matchRepo.findByTeams(teamAId, teamBId);

        long streakA = 0;
        long streakB = 0;

        // Recorremos los partidos en orden inverso (del más reciente al más antiguo)
        for (int i = matches.size() - 1; i >= 0; i--) {
            Match m = matches.get(i);
            if (m.getWinner() == null) continue;
            Long winnerId = m.getWinner().getId();

            if (winnerId.equals(teamAId)) {
                streakA++;
                // Si el ganador es el equipo A, reiniciamos la racha del equipo B
                streakB = 0;
            } else if (winnerId.equals(teamBId)) {
                streakB++;
                // Si el ganador es el equipo B, reiniciamos la racha del equipo A
                streakA = 0;
            } else {
                // Si no hay ganador, terminamos la búsqueda
                break;
            }
        }

        Map<Long, Long> result = new HashMap<>();
        result.put(teamAId, streakA);
        result.put(teamBId, streakB);

        return result;
    }


}
