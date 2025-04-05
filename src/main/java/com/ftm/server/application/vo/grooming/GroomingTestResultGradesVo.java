package com.ftm.server.application.vo.grooming;

import lombok.Getter;

@Getter
public class GroomingTestResultGradesVo {

    private final GroomingCategoryGradeInfoVo beauty;
    private final GroomingCategoryGradeInfoVo hygiene;
    private final GroomingCategoryGradeInfoVo hair;
    private final GroomingCategoryGradeInfoVo workout;
    private final GroomingCategoryGradeInfoVo fashion;

    private GroomingTestResultGradesVo(
            GroomingCategoryGradeInfoVo beauty,
            GroomingCategoryGradeInfoVo hygiene,
            GroomingCategoryGradeInfoVo hair,
            GroomingCategoryGradeInfoVo workout,
            GroomingCategoryGradeInfoVo fashion) {
        this.beauty = beauty;
        this.hygiene = hygiene;
        this.hair = hair;
        this.workout = workout;
        this.fashion = fashion;
    }

    public static GroomingTestResultGradesVo from(
            GroomingCategoryGradeInfoVo beauty,
            GroomingCategoryGradeInfoVo hygiene,
            GroomingCategoryGradeInfoVo hair,
            GroomingCategoryGradeInfoVo workout,
            GroomingCategoryGradeInfoVo fashion) {
        return new GroomingTestResultGradesVo(beauty, hygiene, hair, workout, fashion);
    }
}
