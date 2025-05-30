package com.ftm.server.application.command.grooming.question;

import com.ftm.server.domain.enums.GroomingCategory;
import lombok.Getter;

@Getter
public class UpdateGroomingTestQuestionCommand {

    private final Long id;
    private final GroomingCategory groomingCategory;
    private final String question;

    private UpdateGroomingTestQuestionCommand(
            Long id, GroomingCategory groomingCategory, String question) {
        this.id = id;
        this.groomingCategory = groomingCategory;
        this.question = question;
    }

    public static UpdateGroomingTestQuestionCommand of(
            Long id, GroomingCategory groomingCategory, String question) {
        return new UpdateGroomingTestQuestionCommand(id, groomingCategory, question);
    }
}
