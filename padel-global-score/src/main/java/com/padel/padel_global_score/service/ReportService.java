package com.padel.padel_global_score.service;

import com.padel.padel_global_score.persistence.entity.Match;
import com.padel.padel_global_score.persistence.repository.MatchRepo;
import com.padel.padel_global_score.presentation.dto.StatsDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class ReportService {
    private final MatchRepo matchRepo;
    private final TeamService teamService;

    public ReportService(MatchRepo matchRepo, TeamService teamService) {
        this.matchRepo = matchRepo;
        this.teamService = teamService;
    }

    public StatsDTO getStatsReport(Long teamAId, Long teamBId, LocalDate startDate, LocalDate endDate) {
        // 1. Validar equipos (fail-fast)
        teamService.getTeamById(teamAId);
        teamService.getTeamById(teamBId);

        // 2. Traer la lista UNA SOLA VEZ
        List<Match> matches = matchRepo.findByTeamsAndYear(teamAId, teamBId, startDate, endDate);

        // 3. Si no hay partidos, devolver DTO en ceros inmediatamente
        if (matches.isEmpty()) {
            return buildEmptyStats();
        }

        // 4. Calcular estadísticas de conteo (Games, Sets, Matches, Tiebreaks)
        // Usamos Optional.ofNullable(...).orElse(0L) para evitar NullPointerException
        Long winGamesTeamA = safeCount(matchRepo.countWonGamesByTeam(teamAId, teamBId, true, startDate, endDate));
        Long winGamesTeamB = safeCount(matchRepo.countWonGamesByTeam(teamAId, teamBId, false, startDate, endDate));

        Long winSetsTeamA = safeCount(matchRepo.countWonSetsByTeam(teamAId, teamBId, true, startDate, endDate));
        Long winSetsTeamB = safeCount(matchRepo.countWonSetsByTeam(teamAId, teamBId, false, startDate, endDate));

        Long winTiebreaksTeamA = safeCount(matchRepo.countWonTiebreaksByTeam(teamAId, teamBId, true, startDate, endDate));
        Long winTiebreaksTeamB = safeCount(matchRepo.countWonTiebreaksByTeam(teamAId, teamBId, false, startDate, endDate));

        Long winMatchesTeamA = safeCount(matchRepo.countWonMatchesByTeam(teamAId, teamBId, true, startDate, endDate));
        Long winMatchesTeamB = safeCount(matchRepo.countWonMatchesByTeam(teamAId, teamBId, false, startDate, endDate));

        // 5. Calcular Rachas (Pasando la lista que YA tenemos, sin ir a la BD)
        StreakStats streaks = calculateStreaks(matches, teamAId, teamBId);

        // 6. Construir DTO
        return new StatsDTO(
                winGamesTeamA,
                winGamesTeamB,
                winGamesTeamA + winGamesTeamB, // Total Games
                winSetsTeamA,
                winSetsTeamB,
                winSetsTeamA + winSetsTeamB,   // Total Sets
                winMatchesTeamA,
                winMatchesTeamB,
                winMatchesTeamA + winMatchesTeamB, // Total Matches
                winTiebreaksTeamA,
                winTiebreaksTeamB,
                winTiebreaksTeamA + winTiebreaksTeamB, // Total Tiebreaks
                streaks.maxStreakA,
                streaks.maxStreakB,
                streaks.currentStreakA,
                streaks.currentStreakB
        );
    }

    // --- Helper Methods ---

    /**
     * Calcula rachas máximas y actuales en una sola iteración.
     * Asume que la lista 'matches' viene ordenada por fecha (ascendente o descendente).
     * Si viene desordenada, habría que ordenarla primero.
     */
    private StreakStats calculateStreaks(List<Match> matches, Long teamAId, Long teamBId) {
        // PASO CLAVE: Ordenar por fecha (del más antiguo al más nuevo).
        // Si no haces esto, la "Racha Actual" podría ser en realidad la racha del principio de año.
        matches.sort(Comparator.comparing(Match::getDate));

        long currentA = 0, maxA = 0;
        long currentB = 0, maxB = 0;

        for (Match m : matches) {
            // Ignorar partidos sin ganador (pendientes o cancelados)
            if (m.getWinner() == null) continue;

            Long winnerId = m.getWinner().getId();

            if (winnerId.equals(teamAId)) {
                // Ganó A: Sumamos a su racha actual y reseteamos la de B
                currentA++;
                currentB = 0;
                // Actualizamos el récord histórico si la racha actual supera a la máxima
                maxA = Math.max(maxA, currentA);
            } else if (winnerId.equals(teamBId)) {
                // Ganó B: Sumamos a su racha actual y reseteamos la de A
                currentB++;
                currentA = 0;
                // Actualizamos el récord histórico
                maxB = Math.max(maxB, currentB);
            }
        }

        // Al finalizar el bucle (que recorrió hasta la fecha más reciente),
        // las variables 'currentA' y 'currentB' tienen el valor exacto de la racha actual.
        return new StreakStats(maxA, maxB, currentA, currentB);
    }

    private Long safeCount(Long value) {
        return Optional.ofNullable(value).orElse(0L);
    }

    private StatsDTO buildEmptyStats() {
        return new StatsDTO(0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L);
    }

    // Clase interna (o Record en Java 17+) para transportar los datos de rachas
    private record StreakStats(long maxStreakA, long maxStreakB, long currentStreakA, long currentStreakB) {}
}