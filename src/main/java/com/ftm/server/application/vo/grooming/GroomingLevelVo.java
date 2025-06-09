package com.ftm.server.application.vo.grooming;

import com.ftm.server.domain.entity.GroomingLevel;
import lombok.Getter;

@Getter
public class GroomingLevelVo {

    private final Long groomingLevelId;
    private final NormalModeLevel normalMode;
    private final TruthModeLevel truthMode;

    @Getter
    public static class NormalModeLevel {

        private final String name;
        private final String summary;
        private final String description;

        private NormalModeLevel(GroomingLevel groomingLevel) {
            this.name = groomingLevel.getNormalModeName();
            this.summary = groomingLevel.getNormalModeSummary();
            this.description = groomingLevel.getNormalModeDescription();
        }

        public static NormalModeLevel of(GroomingLevel groomingLevel) {
            return new NormalModeLevel(groomingLevel);
        }
    }

    @Getter
    public static class TruthModeLevel {

        private final String name;
        private final String summary;
        private final String description;

        private TruthModeLevel(GroomingLevel groomingLevel) {
            this.name = groomingLevel.getTruthModeName();
            this.summary = groomingLevel.getTruthModeSummary();
            this.description = groomingLevel.getTruthModeDescription();
        }

        public static TruthModeLevel of(GroomingLevel groomingLevel) {
            return new TruthModeLevel(groomingLevel);
        }
    }

    private GroomingLevelVo(GroomingLevel groomingLevel) {
        this.groomingLevelId = groomingLevel.getId();
        this.normalMode = NormalModeLevel.of(groomingLevel);
        this.truthMode = TruthModeLevel.of(groomingLevel);
    }

    public static GroomingLevelVo from(GroomingLevel groomingLevel) {
        return new GroomingLevelVo(groomingLevel);
    }
}
