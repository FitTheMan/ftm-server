package com.ftm.server.application.service.grooming.level;

import com.ftm.server.application.command.grooming.level.UpdateGroomingLevelCommand;
import com.ftm.server.application.port.in.grooming.level.UpdateGroomingLevelUseCase;
import com.ftm.server.application.port.out.persistence.grooming.LoadGroomingLevelPort;
import com.ftm.server.application.port.out.persistence.grooming.UpdateGroomingLevelPort;
import com.ftm.server.application.query.FindByIdQuery;
import com.ftm.server.common.exception.CustomException;
import com.ftm.server.common.response.enums.ErrorResponseCode;
import com.ftm.server.domain.entity.GroomingLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UpdateGroomingLevelService implements UpdateGroomingLevelUseCase {

    private final LoadGroomingLevelPort loadGroomingLevelPort;
    private final UpdateGroomingLevelPort updateGroomingLevelPort;

    @Transactional
    @Override
    public void execute(UpdateGroomingLevelCommand command) {
        GroomingLevel groomingLevel =
                loadGroomingLevelPort
                        .loadGroomingLevelById(FindByIdQuery.of(command.getId()))
                        .orElseThrow(
                                () ->
                                        new CustomException(
                                                ErrorResponseCode.GROOMING_LEVEL_NOT_FOUND));

        // 그루밍 레벨 수정
        groomingLevel.update(command);
        updateGroomingLevelPort.updateGroomingLevel(groomingLevel);
    }
}
