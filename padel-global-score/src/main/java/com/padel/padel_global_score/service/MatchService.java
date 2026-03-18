package com.padel.padel_global_score.service;

import com.padel.padel_global_score.exception.BadRequestException;
import com.padel.padel_global_score.exception.ResourceNotFoundException;
import com.padel.padel_global_score.persistence.StateMatch;
import com.padel.padel_global_score.persistence.entity.*;
import com.padel.padel_global_score.persistence.repository.MatchRepo;
import com.padel.padel_global_score.presentation.dto.CreateMatchDTO;
import com.padel.padel_global_score.presentation.dto.MatchResultsDTO;
import com.padel.padel_global_score.service.http.NotificationClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class MatchService {
    private final MatchRepo repo;
    private final TeamService teamService;
    private final LocationService locationService;
    private final NotificationClient notificationClient;
    private final GrupoService grupoService;

    public MatchService(
            MatchRepo repo,
            TeamService teamService,
            LocationService locationService,
            NotificationClient notificationClient,
            GrupoService grupoService) {
        this.repo = repo;
        this.teamService = teamService;
        this.locationService = locationService;
        this.notificationClient = notificationClient;
        this.grupoService = grupoService;
    }

    public Match createMatch(CreateMatchDTO dto, String slug) {
        if (dto.teamAId().equals(dto.teamBId())) {
            throw new BadRequestException("The two teams must be different");
        }
        Grupo grupo = grupoService.getBySlug(slug);
        Team teamA = teamService.getTeamById(dto.teamAId());
        Team teamB = teamService.getTeamById(dto.teamBId());
        Location location = locationService.getById(dto.locationId());

        // Validar que los teams pertenecen al grupo
        if (!grupo.getTeamA().getId().equals(teamA.getId()) ||
                !grupo.getTeamB().getId().equals(teamB.getId())) {
            throw new BadRequestException("Teams do not belong to this group");
        }

        // Validar que el grupo no tenga un partido pendiente
        if (repo.search(grupo.getTeamA().getId(), grupo.getTeamB().getId(), null, null, Pageable.unpaged())
                .stream().anyMatch(m -> m.getState() == StateMatch.PENDING)) {
            throw new BadRequestException("There is already a pending match in this group");
        }

        Match match = new Match();
        match.setTeamA(teamA);
        match.setTeamB(teamB);
        match.setDate(dto.date());
        match.setTime(dto.time());
        match.setLocation(location);
        match.setState(StateMatch.PENDING);

        try {
            String nameA = teamA.getLeftSide().getName() + "/" + teamA.getRightSide().getName();
            String nameB = teamB.getLeftSide().getName() + "/" + teamB.getRightSide().getName();
            notificationClient.createNotification(Map.of(
                    "date", match.getDate(),
                    "location", location.getName(),
                    "time", match.getTime(),
                    "teamA", nameA,
                    "teamB", nameB,
                    "chatId", grupo.getTelegramChatId() != null ? grupo.getTelegramChatId() : ""
            ));
        } catch (Exception e) {
            System.err.println("Error sending notification: " + e.getMessage());
        }
        return repo.save(match);
    }

    @Transactional
    public Match finishMatch(Long id, MatchResultsDTO dto, String slug) {
        Match match = getMatchById(id);
        Grupo grupo = grupoService.getBySlug(slug);

        // Validar que el partido pertenece al grupo
        if (!grupo.getTeamA().getId().equals(match.getTeamA().getId()) &&
                !grupo.getTeamB().getId().equals(match.getTeamA().getId())) {
            throw new BadRequestException("Match does not belong to this group");
        }

        match.setState(StateMatch.COMPLETED);
        addSet(dto, match);
        Team winner = determineWinner(dto, match.getTeamA(), match.getTeamB());
        match.setWinner(winner);

        try {
            notificationClient.finishNotification(Map.of(
                    "teamA", match.getTeamA().getLeftSide().getName() + "/" + match.getTeamA().getRightSide().getName(),
                    "teamB", match.getTeamB().getLeftSide().getName() + "/" + match.getTeamB().getRightSide().getName(),
                    "winner", winner.getLeftSide().getName() + "/" + winner.getRightSide().getName(),
                    "results", dto.results(),
                    "location", match.getLocation().getName(),
                    "date", match.getDate(),
                    "time", match.getTime(),
                    "chatId", grupo.getTelegramChatId() != null ? grupo.getTelegramChatId() : ""
            ));
        } catch (Exception e) {
            System.err.println("Error sending notification: " + e.getMessage());
        }
        return repo.save(match);
    }

    @Transactional
    public Match suspendMatch(Long id, MatchResultsDTO dto, String slug) {
        Match match = getMatchById(id);
        Grupo grupo = grupoService.getBySlug(slug);
        if (!grupo.getTeamA().getId().equals(match.getTeamA().getId()) &&
                !grupo.getTeamB().getId().equals(match.getTeamA().getId())) {
            throw new BadRequestException("Match does not belong to this group");
        }
        match.setState(StateMatch.SUSPENDED);
        match.setWinner(null);
        addSet(dto, match);
        return repo.save(match);
    }

    public Match getMatchById(Long id) {
        return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Match not found"));
    }

    public List<Match> getAllMatchesByTeams(String slug) {
        Grupo grupo = grupoService.getBySlug(slug);
        return repo.findByTeams(grupo.getTeamA().getId(), grupo.getTeamB().getId());
    }

    public Page<Match> search(Long winner, Long location, String slug, Pageable pageable) {
        Grupo grupo = grupoService.getBySlug(slug);
        return repo.search(grupo.getTeamA().getId(), grupo.getTeamB().getId(), winner, location, pageable);
    }

    public Match deleteMatch(Long id, String slug) {
        Match match = getMatchById(id);
        Grupo grupo = grupoService.getBySlug(slug);
        if (!grupo.getTeamA().getId().equals(match.getTeamA().getId()) &&
                !grupo.getTeamB().getId().equals(match.getTeamA().getId())) {
            throw new BadRequestException("Match does not belong to this group");
        }
        repo.delete(match);
        return match;
    }

    // privados sin cambios
    private void addSet(MatchResultsDTO dto, Match match) {
        match.getSets().clear();
        dto.results().forEach(res -> {
            Set set = new Set();
            set.setNumberSet(res.numberSet());
            set.setGamesTeamA(res.gamesTeamA());
            set.setGamesTeamB(res.gamesTeamB());
            set.setMatch(match);
            match.getSets().add(set);
        });
    }

    private Team determineWinner(MatchResultsDTO dto, Team teamA, Team teamB) {
        int teamASetsWon = 0;
        int teamBSetsWon = 0;
        for (var res : dto.results()) {
            if (res.gamesTeamA() > res.gamesTeamB()) teamASetsWon++;
            else teamBSetsWon++;
        }
        return teamASetsWon > teamBSetsWon ? teamA : teamB;
    }
}