package com.damai.jobqueue;

/**
 * 任务队列管理
 * @author Randy
 *
 * @param <T>
 */
public interface JobQueueManager<T extends Job> extends JobQueue<T> {
	void addJobHandler(JobHandler<T> jobHandler);
}
