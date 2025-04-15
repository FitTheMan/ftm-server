package com.ftm.server.application.port.out.transcation;

import com.ftm.server.common.annotation.Port;

@Port
public interface AfterRollbackExecutorPort {
    void doAfterRollback(Runnable task);
}
