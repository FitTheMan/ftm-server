package com.ftm.server.adapter.out.transaction;

import com.ftm.server.application.port.out.transcation.AfterCommitExecutorPort;
import com.ftm.server.common.annotation.Adapter;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Adapter
public class AfterCommitExecutorAdapter implements AfterCommitExecutorPort {
    @Override
    public void doAfterCommit(Runnable task) { // transaction commit 이후에 task를 실행
        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(
                    new TransactionSynchronization() {
                        @Override
                        public void afterCommit() {
                            task.run();
                        }
                    });
        }
    }
}
