package com.ftm.server.application.port.in.grooming;

import com.ftm.server.application.command.grooming.SubmitGroomingTestCommand;
import com.ftm.server.application.vo.grooming.GroomingTestResultVo;
import com.ftm.server.common.annotation.UseCase;

@UseCase
public interface SubmitGroomingTestUseCase {

    GroomingTestResultVo execute(SubmitGroomingTestCommand command);
}
