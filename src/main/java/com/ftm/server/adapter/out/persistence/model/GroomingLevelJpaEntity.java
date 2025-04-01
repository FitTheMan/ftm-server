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
    private String mildLevelName;
    private String spicyLevelName;

    @Builder(access = AccessLevel.PRIVATE)
    private GroomingLevelJpaEntity(
            Integer minScore, Integer maxScore, String mildLevelName, String spicyLevelName) {
        this.minScore = minScore;
        this.maxScore = maxScore;
        this.mildLevelName = mildLevelName;
        this.spicyLevelName = spicyLevelName;
    }

    public static GroomingLevelJpaEntity from(GroomingLevel groomingLevel) {
        return GroomingLevelJpaEntity.builder()
                .minScore(groomingLevel.getMinScore())
                .maxScore(groomingLevel.getMaxScore())
                .mildLevelName(groomingLevel.getMildLevelName())
                .spicyLevelName(groomingLevel.getSpicyLevelName())
                .build();
    }
}
