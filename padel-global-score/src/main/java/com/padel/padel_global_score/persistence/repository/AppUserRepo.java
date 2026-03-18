package com.padel.padel_global_score.persistence.repository;

import com.padel.padel_global_score.persistence.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface AppUserRepo extends JpaRepository<AppUser, Long> {

    Optional<AppUser> findByUsername(String username);

}
