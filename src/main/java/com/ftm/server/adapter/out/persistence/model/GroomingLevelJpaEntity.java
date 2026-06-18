package com.ftm.server.adapter.out.persistence.model;

import com.ftm.server.domain.entity.GroomingLevel;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "grooming_level")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GroomingLevelJpaEntity extends BaseTimeJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer minScore;
    private Integer maxScore;
    private String normalModeName;
    private String normalModeSummary;
    private String normalModeDescription;
    private String truthModeName;
    private String truthModeSummary;
    private String truthModeDescription;
    private String imagePath;

    @Builder(access = AccessLevel.PRIVATE)
    private GroomingLevelJpaEntity(
            Integer minScore,
            Integer maxScore,
            String normalModeName,
            String normalModeSummary,
            String normalModeDescription,
            String truthModeName,
            String truthModeSummary,
            String truthModeDescription,
            String imagePath) {
        this.minScore = minScore;
        this.maxScore = maxScore;
        this.normalModeName = normalModeName;
        this.normalModeSummary = normalModeSummary;
        this.normalModeDescription = normalModeDescription;
        this.truthModeName = truthModeName;
        this.truthModeSummary = truthModeSummary;
        this.truthModeDescription = truthModeDescription;
        this.imagePath = imagePath;
    }

    public static GroomingLevelJpaEntity from(GroomingLevel groomingLevel) {
        return GroomingLevelJpaEntity.builder()
                .minScore(groomingLevel.getMinScore())
                .maxScore(groomingLevel.getMaxScore())
                .normalModeName(groomingLevel.getNormalModeName())
                .normalModeSummary(groomingLevel.getNormalModeSummary())
                .normalModeDescription(groomingLevel.getNormalModeDescription())
                .truthModeName(groomingLevel.getTruthModeName())
                .truthModeSummary(groomingLevel.getTruthModeSummary())
                .truthModeDescription(groomingLevel.getTruthModeDescription())
                .imagePath(groomingLevel.getImagePath())
                .build();
    }

    public void updateGroomingLevelForDomainEntity(GroomingLevel groomingLevel) {
        this.minScore = groomingLevel.getMinScore();
        this.maxScore = groomingLevel.getMaxScore();
        this.normalModeName = groomingLevel.getNormalModeName();
        this.normalModeSummary = groomingLevel.getNormalModeSummary();
        this.normalModeDescription = groomingLevel.getNormalModeDescription();
        this.truthModeName = groomingLevel.getTruthModeName();
        this.truthModeSummary = groomingLevel.getTruthModeSummary();
        this.truthModeDescription = groomingLevel.getTruthModeDescription();
        this.imagePath = groomingLevel.getImagePath();
    }
}
