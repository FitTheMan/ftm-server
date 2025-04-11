package com.ftm.server.application.vo.grooming;

import com.ftm.server.domain.entity.GroomingTestAnswer;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GroomingTestAnswerInfoVo {

    private Long groomingTestAnswerId;
    private String answer;
    private Integer score;

    private GroomingTestAnswerInfoVo(GroomingTestAnswer groomingTestAnswer) {
        this.groomingTestAnswerId = groomingTestAnswer.getId();
        this.answer = groomingTestAnswer.getAnswer();
        this.score = groomingTestAnswer.getScore();
    }

    public static GroomingTestAnswerInfoVo from(GroomingTestAnswer groomingTestAnswer) {
        return new GroomingTestAnswerInfoVo(groomingTestAnswer);
    }
}
