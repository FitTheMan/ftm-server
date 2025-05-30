package com.ftm.server.application.service.grooming.level;

import com.ftm.server.application.command.grooming.level.SaveGroomingLevelCommand;
import com.ftm.server.application.port.in.grooming.level.SaveGroomingLevelUseCase;
import com.ftm.server.application.port.out.persistence.grooming.SaveGroomingLevelPort;
import com.ftm.server.domain.entity.GroomingLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SaveGroomingLevelService implements SaveGroomingLevelUseCase {

    private final SaveGroomingLevelPort saveGroomingLevelPort;

    @Transactional
    @Override
    public void execute(SaveGroomingLevelCommand command) {
        GroomingLevel groomingLevel = GroomingLevel.create(command);

        // 그루밍 레벨 저장
        saveGroomingLevelPort.saveGroomingLevel(groomingLevel);
    }
}
