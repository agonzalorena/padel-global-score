package com.padel.padel_global_score.service;

import com.padel.padel_global_score.exception.ResourceNotFoundException;
import com.padel.padel_global_score.persistence.entity.Grupo;
import com.padel.padel_global_score.persistence.repository.GrupoRepo;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class GrupoService {

    private final GrupoRepo grupoRepository;

    public GrupoService(GrupoRepo grupoRepository) {
        this.grupoRepository = grupoRepository;
    }

    public Grupo getBySlug(String slug) {
        return grupoRepository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Group not found: " + slug));
    }

    public List<Grupo> getAll() {
        return grupoRepository.findAll();
    }

    public List<Grupo> getByAdminId(Long adminId) {
        return grupoRepository.findByAdminId(adminId);
    }

    public Grupo create(Grupo grupo) {
        return grupoRepository.save(grupo);
    }
}