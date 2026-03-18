package com.padel.padel_global_score.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name="grupo")
@Data
public class Grupo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String slug;

    @Column(name = "telegram_chat_id")
    private String telegramChatId;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "admin_id", nullable = false)
    private AppUser admin;

    @ManyToOne
    @JoinColumn(name = "team_a_id", nullable = false)
    private Team teamA;

    @ManyToOne
    @JoinColumn(name = "team_b_id", nullable = false)
    private Team teamB;

}
