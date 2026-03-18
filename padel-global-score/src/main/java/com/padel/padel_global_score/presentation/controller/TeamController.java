package com.padel.padel_global_score.presentation.controller;

import com.padel.padel_global_score.presentation.dto.CreateTeamDTO;
import com.padel.padel_global_score.presentation.dto.response.SuccessResponse;
import com.padel.padel_global_score.service.TeamService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/teams")
public class TeamController {
    private final TeamService service;

    public TeamController(TeamService service) {
        this.service = service;
    }

    @PostMapping("")
    ResponseEntity<SuccessResponse> createTeam(@Valid @RequestBody CreateTeamDTO dto) {
        return ResponseEntity.status(201)
                .body(new SuccessResponse(201, service.createTeam(dto)));
    }

    @GetMapping("/player")
    ResponseEntity<SuccessResponse> getTeamByPlayers(
            @RequestParam Long leftId,
            @RequestParam Long rightId) {
        return ResponseEntity.status(200)
                .body(new SuccessResponse(200, service.getTeamByPlayers(leftId, rightId)));
    }

    @GetMapping("/{id}")
    ResponseEntity<SuccessResponse> getTeamById(@PathVariable Long id) {
        return ResponseEntity.status(200)
                .body(new SuccessResponse(200, service.getTeamById(id)));
    }
}