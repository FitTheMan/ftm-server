package com.ftm.server.domain.entity;

import com.ftm.server.application.command.grooming.level.SaveGroomingLevelCommand;
import com.ftm.server.application.command.grooming.level.UpdateGroomingLevelCommand;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GroomingLevel extends BaseTime {

    private Long id;
    private Integer minScore;
    private Integer maxScore;
    private String mildLevelName;
    private String spicyLevelName;

    @Builder(access = AccessLevel.PRIVATE)
    private GroomingLevel(
            Long id,
            Integer minScore,
            Integer maxScore,
            String mildLevelName,
            String spicyLevelName,
            LocalDateTime createdAt,
            LocalDateTime updatedAt) {
        this.id = id;
        this.minScore = minScore;
        this.maxScore = maxScore;
        this.mildLevelName = mildLevelName;
        this.spicyLevelName = spicyLevelName;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static GroomingLevel of(
            Long id,
            Integer minScore,
            Integer maxScore,
            String mildLevelName,
            String spicyLevelName,
            LocalDateTime createdAt,
            LocalDateTime updatedAt) {
        return GroomingLevel.builder()
                .id(id)
                .minScore(minScore)
                .maxScore(maxScore)
                .mildLevelName(mildLevelName)
                .spicyLevelName(spicyLevelName)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();
    }

    public static GroomingLevel create(SaveGroomingLevelCommand command) {
        return GroomingLevel.builder()
                .minScore(command.getMinScore())
                .maxScore(command.getMaxScore())
                .mildLevelName(command.getMildLevelName())
                .spicyLevelName(command.getSpicyLevelName())
                .build();
    }

    public void update(UpdateGroomingLevelCommand command) {
        if (command.getMinScore() != null) this.minScore = command.getMinScore();
        if (command.getMaxScore() != null) this.maxScore = command.getMaxScore();
        if (command.getMildLevelName() != null) this.mildLevelName = command.getMildLevelName();
        if (command.getSpicyLevelName() != null) this.spicyLevelName = command.getSpicyLevelName();
    }
}
