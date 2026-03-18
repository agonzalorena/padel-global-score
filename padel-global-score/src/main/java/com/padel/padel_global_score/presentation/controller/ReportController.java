package com.padel.padel_global_score.presentation.controller;

import com.padel.padel_global_score.presentation.dto.response.SuccessResponse;
import com.padel.padel_global_score.service.ReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/groups/{slug}/statistics")
public class ReportController {
    private final ReportService service;

    public ReportController(ReportService service) {
        this.service = service;
    }

    @GetMapping("")
    public ResponseEntity<SuccessResponse> getStatsReport(
            @PathVariable String slug,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {
        return ResponseEntity.status(200)
                .body(new SuccessResponse(200, service.getStatsReport(startDate, endDate, slug)));
    }
}
