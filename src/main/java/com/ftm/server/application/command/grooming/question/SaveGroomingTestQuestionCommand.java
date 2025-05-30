package com.ftm.server.application.command.grooming.question;

import com.ftm.server.domain.enums.GroomingCategory;
import lombok.Getter;

@Getter
public class SaveGroomingTestQuestionCommand {

    private final GroomingCategory groomingCategory;
    private final String question;

    private SaveGroomingTestQuestionCommand(GroomingCategory groomingCategory, String question) {
        this.groomingCategory = groomingCategory;
        this.question = question;
    }

    public static SaveGroomingTestQuestionCommand of(
            GroomingCategory groomingCategory, String question) {
        return new SaveGroomingTestQuestionCommand(groomingCategory, question);
    }
}
