package com.padel.padel_global_score.persistence.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "left_side_id", nullable = false)
    private Player leftSide;
    @ManyToOne
    @JoinColumn(name = "right_side_id", nullable = false)
    private Player rightSide;
    @Column(name = "url_photo")
    private String urlPhoto;

}
