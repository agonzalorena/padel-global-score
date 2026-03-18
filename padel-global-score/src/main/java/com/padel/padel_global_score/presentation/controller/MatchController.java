package com.padel.padel_global_score.presentation.controller;

import com.padel.padel_global_score.presentation.dto.CreateMatchDTO;
import com.padel.padel_global_score.presentation.dto.MatchResultsDTO;
import com.padel.padel_global_score.presentation.dto.response.SuccessResponse;
import com.padel.padel_global_score.service.GrupoService;
import com.padel.padel_global_score.service.MatchService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/groups/{slug}/matches")
public class MatchController extends BaseController{
    private final MatchService service;

    public MatchController(MatchService service, GrupoService grupoService) {
        super(grupoService);
        this.service = service;
    }

    @PostMapping("")
    public ResponseEntity<SuccessResponse> createMatch(
            @PathVariable String slug,
            @Valid @RequestBody CreateMatchDTO dto) {
        validateGroupAccess(slug); // ← solo en POST/PUT/DELETE
        return ResponseEntity.status(201)
                .body(new SuccessResponse(201, service.createMatch(dto, slug)));
    }

    @PutMapping("/finish/{id}")
    public ResponseEntity<SuccessResponse> finishMatch(
            @PathVariable String slug,
            @PathVariable Long id,
            @Valid @RequestBody MatchResultsDTO dto) {
        validateGroupAccess(slug);
        return ResponseEntity.status(201)
                .body(new SuccessResponse(201, service.finishMatch(id, dto, slug)));
    }

    @PutMapping("/suspend/{id}")
    public ResponseEntity<SuccessResponse> suspendMatch(
            @PathVariable String slug,
            @PathVariable Long id,
            @Valid @RequestBody MatchResultsDTO dto) {
        validateGroupAccess(slug);
        return ResponseEntity.status(201)
                .body(new SuccessResponse(201, service.suspendMatch(id, dto, slug)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<SuccessResponse> deleteMatch(
            @PathVariable String slug,
            @PathVariable Long id) {
        validateGroupAccess(slug);
        return ResponseEntity.status(200)
                .body(new SuccessResponse(200, service.deleteMatch(id, slug)));
    }

    @GetMapping("")
    public ResponseEntity<SuccessResponse> search(
            @PathVariable String slug,
            @RequestParam(required = false) Long winner,
            @RequestParam(required = false) Long location,
            Pageable pageable) {
        return ResponseEntity.status(200)
                .body(new SuccessResponse(200, service.search(winner, location, slug, pageable)));
    }
}