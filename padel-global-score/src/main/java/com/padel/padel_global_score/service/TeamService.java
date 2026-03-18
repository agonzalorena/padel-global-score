package com.padel.padel_global_score.service;

import com.padel.padel_global_score.exception.BadRequestException;
import com.padel.padel_global_score.exception.ResourceNotFoundException;
import com.padel.padel_global_score.persistence.entity.Grupo;
import com.padel.padel_global_score.persistence.entity.Player;
import com.padel.padel_global_score.persistence.entity.Team;
import com.padel.padel_global_score.persistence.repository.TeamRepo;
import com.padel.padel_global_score.presentation.dto.CreateTeamDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeamService {
    private final TeamRepo repo;
    private final PlayerService playerService;

    public TeamService(TeamRepo repo, PlayerService playerService) {
        this.repo = repo;
        this.playerService = playerService;
    }

    public Team createTeam(CreateTeamDTO dto) {
        if (dto.leftSideId().equals(dto.rightSideId())) {
            throw new BadRequestException("A team must have two different players");
        }
        Player left = playerService.getPlayerById(dto.leftSideId());
        Player right = playerService.getPlayerById(dto.rightSideId());

        if (repo.findByPlayers(left.getId(), right.getId()).isPresent()) {
            throw new BadRequestException("Team already exists");
        }
        Team team = new Team();
        if (dto.urlPhoto() != null) team.setUrlPhoto(dto.urlPhoto());
        team.setLeftSide(left);
        team.setRightSide(right);
        return repo.save(team);
    }

    public Team getTeamByPlayers(Long leftId, Long rightId) {
        return repo.findByPlayers(leftId, rightId)
                .orElseThrow(() -> new ResourceNotFoundException("Team not found"));
    }

    public Team getTeamById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Team not found"));
    }
}