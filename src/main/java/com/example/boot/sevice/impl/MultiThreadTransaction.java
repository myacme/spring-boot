package com.example.boot.sevice.impl;

import com.example.boot.mapper.MyMapper;
import com.example.boot.sevice.TransactionSevice;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author MyAcme
 */
@Service
public class MultiThreadTransaction implements TransactionSevice {

    @Resource
    private DataSourceTransactionManager transactionManager;

    @Resource
    private MyMapper mapper;

    private final ExecutorService executor = new ThreadPoolExecutor(
            4,
            4,
            0L,
            TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>(2),
            new ThreadPoolExecutor.CallerRunsPolicy()
    );


    // 使用 CountDownLatch 协调线程
    public void testTransaction() throws InterruptedException {
        List<Future<Boolean>> futures = new ArrayList<>();
        List<TransactionStatus> statuses = new ArrayList<>();
        // 提交任务
        for (int i = 0; i < 4; i++) {
            int finalI = i;
            Callable<Boolean> task = () -> {
                TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
                statuses.add(status); // 注意：实际需线程安全集合
                try {
                    // 执行业务操作（如数据库更新）
                    mapper.insert(finalI);
                    return true; // 模拟成功
                } catch (Exception e) {
                    transactionManager.rollback(status);
                    return false;
                }
            };
            futures.add(executor.submit(task));
        }

        // 检查结果
        boolean allSuccess = futures.stream().allMatch(future -> {
            try {
                return future.get();
            } catch (InterruptedException | ExecutionException e) {
                return false;
            }
        });
        // 全局提交或回滚
        if (allSuccess) {
            statuses.forEach(transactionManager::commit);
        } else {
            statuses.forEach(transactionManager::rollback);
        }
    }
}
