package com.ftm.server.adapter.out.transaction;

import com.ftm.server.application.port.out.transcation.AfterRollbackExecutorPort;
import com.ftm.server.common.annotation.Adapter;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Adapter
public class AfterRollbackExecutorAdapter implements AfterRollbackExecutorPort {
    @Override
    public void doAfterRollback(Runnable task) { // transaction rollback 이후에  task 실행
        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(
                    new TransactionSynchronization() {
                        @Override
                        public void afterCompletion(int status) {
                            if (status == TransactionSynchronization.STATUS_ROLLED_BACK) {
                                task.run();
                            }
                        }
                    });
        }
    }
}
