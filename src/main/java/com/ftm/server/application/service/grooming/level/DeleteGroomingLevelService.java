package com.ftm.server.application.service.grooming.level;

import com.ftm.server.application.command.grooming.level.DeleteGroomingLevelCommand;
import com.ftm.server.application.port.in.grooming.level.DeleteGroomingLevelUseCase;
import com.ftm.server.application.port.out.persistence.grooming.DeleteGroomingLevelPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteGroomingLevelService implements DeleteGroomingLevelUseCase {

    private final DeleteGroomingLevelPort deleteGroomingLevelPort;

    @Override
    public void execute(DeleteGroomingLevelCommand command) {

        // 레벨 삭제
        deleteGroomingLevelPort.deleteGroomingLevel(command.getId());
    }
}
