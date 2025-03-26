package com.ftm.server.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "grooming_level")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GroomingLevel extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer minScore;

    private Integer maxScore;

    private String mildLevelName;

    private String spicyLevelName;

    @Builder(access = AccessLevel.PRIVATE)
    private GroomingLevel(
            Integer minScore, Integer maxScore, String mildLevelName, String spicyLevelName) {
        this.minScore = minScore;
        this.maxScore = maxScore;
        this.mildLevelName = mildLevelName;
        this.spicyLevelName = spicyLevelName;
    }
}
