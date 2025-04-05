package com.ftm.server.application.vo.grooming;

import com.ftm.server.domain.entity.GroomingLevel;
import lombok.Getter;

@Getter
public class GroomingLevelVo {

    private final Long groomingLevelId;
    private final String mildLevelName;
    private final String spicyLevelName;

    private GroomingLevelVo(GroomingLevel groomingLevel) {
        this.groomingLevelId = groomingLevel.getId();
        this.mildLevelName = groomingLevel.getMildLevelName();
        this.spicyLevelName = groomingLevel.getSpicyLevelName();
    }

    public static GroomingLevelVo from(GroomingLevel groomingLevel) {
        return new GroomingLevelVo(groomingLevel);
    }
}
