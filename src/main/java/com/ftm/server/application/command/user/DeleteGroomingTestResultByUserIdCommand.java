package com.ftm.server.application.command.user;

import java.util.List;
import lombok.Data;

@Data
public class DeleteGroomingTestResultByUserIdCommand {
    private final List<Long> userIdList;

    public static DeleteGroomingTestResultByUserIdCommand of(List<Long> userIdList) {
        return new DeleteGroomingTestResultByUserIdCommand(userIdList);
    }
}
