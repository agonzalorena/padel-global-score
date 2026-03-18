package com.padel.padel_global_score.persistence.repository;

import com.padel.padel_global_score.persistence.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeamRepo extends JpaRepository<Team, Long> {
    @Query("SELECT t FROM Team t " +
            "WHERE (t.leftSide.id = :leftSideId AND t.rightSide.id = :rightSideId) " +
            "OR (t.leftSide.id = :rightSideId AND t.rightSide.id = :leftSideId)")
    Optional<Team> findByPlayers(Long leftSideId, Long rightSideId);
/*
    List<Team> findByGrupoSlug(String slug);*/

}
