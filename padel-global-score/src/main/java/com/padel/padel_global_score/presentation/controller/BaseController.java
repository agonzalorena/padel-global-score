package com.padel.padel_global_score.presentation.controller;

import com.padel.padel_global_score.exception.ForbiddenException;
import com.padel.padel_global_score.persistence.entity.Grupo;
import com.padel.padel_global_score.security.CustomUserDetails;
import com.padel.padel_global_score.service.GrupoService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public abstract class BaseController {

    private final GrupoService grupoService;

    protected BaseController(GrupoService grupoService) {
        this.grupoService = grupoService;
    }

    protected void validateGroupAccess(String slug) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
        Grupo grupo = grupoService.getBySlug(slug);
        if (!grupo.getAdmin().getId().equals(userDetails.getUserId())) {
            throw new ForbiddenException("No tenés acceso a este grupo");
        }
    }
}
