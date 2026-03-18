package com.padel.padel_global_score.presentation.controller;

import com.padel.padel_global_score.persistence.entity.Grupo;
import com.padel.padel_global_score.presentation.dto.response.SuccessResponse;
import com.padel.padel_global_score.security.CustomUserDetails;
import com.padel.padel_global_score.service.GrupoService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/groups")
public class GrupoController {
    private final GrupoService service;

    public GrupoController(GrupoService service) {
        this.service = service;
    }

    // Público: listar todos los grupos (para la landing page)
    @GetMapping("")
    public ResponseEntity<SuccessResponse> getAll() {
        return ResponseEntity.status(200)
                .body(new SuccessResponse(200, service.getAll()));
    }

    // Público: obtener un grupo por slug
    @GetMapping("/{slug}")
    public ResponseEntity<SuccessResponse> getBySlug(@PathVariable String slug) {
        return ResponseEntity.status(200)
                .body(new SuccessResponse(200, service.getBySlug(slug)));
    }


    // Protegido: solo super-admin puede crear grupos
    /*@PostMapping("")
    public ResponseEntity<SuccessResponse> create(@RequestBody Grupo grupo) {
        return ResponseEntity.status(201)
                .body(new SuccessResponse(201, service.create(grupo)));
    }*/
}
