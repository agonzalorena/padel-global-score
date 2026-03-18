package com.padel.padel_global_score.persistence.repository;

import com.padel.padel_global_score.persistence.entity.Grupo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GrupoRepo extends JpaRepository<Grupo, Long> {
    Optional<Grupo> findBySlug(String slug);
    List<Grupo> findByAdminId(Long adminId);
}
