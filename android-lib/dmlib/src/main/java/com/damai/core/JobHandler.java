package com.damai.core;

/**
 * 任务处理器
 * @author Randy
 *
 */
public interface JobHandler<T> {
    /**
     * 处理一项任务
     * @param job
     */
    void handleJob(T job);

    /**
     * 停止掉
     */
    void shutdown();
}
