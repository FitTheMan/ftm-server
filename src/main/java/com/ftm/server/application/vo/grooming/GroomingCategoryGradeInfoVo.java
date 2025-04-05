package com.ftm.server.application.vo.grooming;

import com.ftm.server.domain.enums.GroomingCategoryGrade;
import lombok.Getter;

@Getter
public class GroomingCategoryGradeInfoVo {

    private final String grade;
    private final int level;

    private GroomingCategoryGradeInfoVo(String grade, int level) {
        this.grade = grade;
        this.level = level;
    }

    public static GroomingCategoryGradeInfoVo from(GroomingCategoryGrade categoryGrade) {
        return new GroomingCategoryGradeInfoVo(categoryGrade.getGrade(), categoryGrade.getLevel());
    }
}
