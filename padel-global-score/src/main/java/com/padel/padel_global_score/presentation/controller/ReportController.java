package com.padel.padel_global_score.presentation.controller;

import com.padel.padel_global_score.presentation.dto.response.SuccessResponse;
import com.padel.padel_global_score.service.ReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/statistics")
public class ReportController {
    private final ReportService service;

    public ReportController(ReportService service) {
        this.service = service;
    }
    

    @GetMapping("")
    public ResponseEntity<SuccessResponse> getStatsReport(@RequestParam Long teamAId, @RequestParam Long teamBId, @RequestParam int year) {
        return ResponseEntity.status(200)
                .body(new SuccessResponse(200, service.getStatsReport(teamAId, teamBId, year)));
    }
}
