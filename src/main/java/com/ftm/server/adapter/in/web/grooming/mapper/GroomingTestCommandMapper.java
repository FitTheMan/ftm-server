package com.ftm.server.adapter.in.web.grooming.mapper;

import com.ftm.server.adapter.in.web.grooming.dto.request.GroomingTestSubmissionRequest;
import com.ftm.server.application.command.grooming.SubmitGroomingTestCommand;
import com.ftm.server.infrastructure.security.UserPrincipal;
import java.util.List;

public class GroomingTestCommandMapper {

    public static SubmitGroomingTestCommand toSubmitGroomingTestCommand(
            UserPrincipal userPrincipal, GroomingTestSubmissionRequest request) {
        Long userId = (userPrincipal != null) ? userPrincipal.getId() : null;
        List<SubmitGroomingTestCommand.SubmittedQuestion> submissions =
                request.getSubmissions().stream()
                        .map(
                                item ->
                                        SubmitGroomingTestCommand.SubmittedQuestion.of(
                                                item.getQuestionId(),
                                                item.getGroomingCategory(),
                                                item.getAnswers().stream()
                                                        .map(
                                                                answer ->
                                                                        SubmitGroomingTestCommand
                                                                                .SubmittedQuestion
                                                                                .SelectedAnswer.of(
                                                                                answer
                                                                                        .getAnswerId(),
                                                                                answer.getScore()))
                                                        .toList()))
                        .toList();

        return SubmitGroomingTestCommand.from(userId, submissions);
    }
}
