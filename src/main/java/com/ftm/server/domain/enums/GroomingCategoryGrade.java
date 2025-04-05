package com.ftm.server.domain.enums;

import com.ftm.server.common.exception.CustomException;
import com.ftm.server.common.response.enums.ErrorResponseCode;
import java.util.Arrays;
import lombok.Getter;

@Getter
public enum GroomingCategoryGrade {
    LOW("무심", 1, 0.0, 0.33),
    MIDDLE("단정", 2, 0.34, 0.66),
    HIGH("완벽", 3, 0.67, 1.0);

    private final String grade;
    private final int level;
    private final double minRatio;
    private final double maxRatio;

    GroomingCategoryGrade(String grade, int level, double minRatio, double maxRatio) {
        this.grade = grade;
        this.level = level;
        this.minRatio = minRatio;
        this.maxRatio = maxRatio;
    }

    public static GroomingCategoryGrade fromScore(int score, int maxScore) {
        if (maxScore == 0) throw new CustomException(ErrorResponseCode.INVALID_MAX_SCORE);
        double ratio = (double) score / maxScore;

        return Arrays.stream(values())
                .filter(g -> ratio >= g.minRatio && ratio <= g.maxRatio)
                .findFirst()
                .orElseThrow(
                        () -> new CustomException(ErrorResponseCode.INVALID_RATIO_FOR_PERCENTAGE));
    }
}
