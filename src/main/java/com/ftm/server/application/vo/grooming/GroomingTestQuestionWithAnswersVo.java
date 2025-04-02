package com.ftm.server.application.vo.grooming;

import com.ftm.server.domain.entity.GroomingTestQuestion;
import com.ftm.server.domain.enums.GroomingCategory;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GroomingTestQuestionWithAnswersVo {

    private Long groomingTestQuestionId;
    private String question;
    private GroomingCategory groomingCategory;
    private List<GroomingTestAnswerInfoVo> answers;

    private GroomingTestQuestionWithAnswersVo(
            GroomingTestQuestion groomingTestQuestion, List<GroomingTestAnswerInfoVo> answers) {
        this.groomingTestQuestionId = groomingTestQuestion.getId();
        this.question = groomingTestQuestion.getQuestion();
        this.groomingCategory = groomingTestQuestion.getGroomingCategory();
        this.answers = answers;
    }

    public static GroomingTestQuestionWithAnswersVo from(
            GroomingTestQuestion groomingTestQuestion, List<GroomingTestAnswerInfoVo> answers) {
        return new GroomingTestQuestionWithAnswersVo(groomingTestQuestion, answers);
    }
}
