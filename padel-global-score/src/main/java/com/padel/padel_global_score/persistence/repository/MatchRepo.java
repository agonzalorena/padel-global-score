package com.padel.padel_global_score.persistence.repository;

import com.padel.padel_global_score.persistence.entity.Match;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatchRepo extends JpaRepository<Match, Long> {

    @Query("SELECT m " +
            "FROM Match m " +
            "WHERE (m.teamA.id = :teamAId " +
            "AND m.teamB.id = :teamBId) OR " +
            "(m.teamA.id = :teamBId " +
            "AND m.teamB.id = :teamAId)" +
            "ORDER BY m.id DESC")
    List<Match> findByTeams(Long teamAId, Long teamBId);

    @Query("SELECT m " +
            "FROM Match m " +
            "WHERE ((m.teamA.id = :teamAId " +
            "AND m.teamB.id = :teamBId) OR " +
            "(m.teamA.id = :teamBId " +
            "AND m.teamB.id = :teamAId)) AND " +
            "YEAR(m.date) = :year" +
            " ORDER BY m.id DESC")
    List<Match> findByTeamsAndYear(Long teamAId, Long teamBId, int year);


    @Query("SELECT m " +
            "FROM Match m " +
            "WHERE (:winner IS NULL OR m.winner.id = :winner) AND " +
            "(:teamAId IS NULL OR m.teamA.id = :teamAId OR m.teamB.id = :teamAId) AND " +
            "(:teamBId IS NULL OR m.teamA.id = :teamBId OR m.teamB.id = :teamBId) AND " +
            "(:locationId IS NULL OR m.location.id = :locationId)" +
            "ORDER BY m.id DESC")
    Page<Match> search(Long teamAId, Long teamBId, Long winner, Long locationId, Pageable pageable);


    @Query("""
                SELECT SUM(
                    CASE
                        WHEN :isTeamA = TRUE THEN s.gamesTeamA
                        ELSE s.gamesTeamB
                    END
                )
                FROM Match m JOIN m.sets s
                WHERE (m.teamA.id = :teamAId AND m.teamB.id = :teamBId)
            """)
    Long countWonGamesByTeam(Long teamAId, Long teamBId, boolean isTeamA);

    @Query("""
                SELECT COUNT(s)
                FROM Match m JOIN m.sets s
                WHERE (m.teamA.id = :teamAId AND m.teamB.id = :teamBId)
                  AND (
                      (:isTeamA = TRUE AND s.gamesTeamA > s.gamesTeamB)
                      OR
                      (:isTeamA = FALSE AND s.gamesTeamB > s.gamesTeamA)
                  )
            """)
    Long countWonSetsByTeam(
            Long teamAId,
            Long teamBId,
            boolean isTeamA
    );

    @Query("""
                SELECT COUNT(s)
                FROM Match m JOIN m.sets s
                WHERE (m.teamA.id = :teamAId AND m.teamB.id = :teamBId)
                  AND (
                      (:isTeamA = TRUE  AND s.gamesTeamA = 7 AND s.gamesTeamB = 6)
                      OR
                      (:isTeamA = FALSE AND s.gamesTeamB = 7 AND s.gamesTeamA = 6)
                  )
            """)
    Long countWonTiebreaksByTeam(
            Long teamAId,
            Long teamBId,
            boolean isTeamA
    );

    @Query("""
                SELECT COUNT(m)
                FROM Match m
                WHERE (m.teamA.id = :teamAId AND m.teamB.id = :teamBId)
                  AND (
                      (:isTeamA = TRUE  AND m.winner.id = m.teamA.id)
                      OR
                      (:isTeamA = FALSE AND m.winner.id = m.teamB.id)
                  )
            """)
    Long countWonMatchesByTeam(
            Long teamAId,
            Long teamBId,
            boolean isTeamA
    );


}
