package com.damai.jobqueue;

/**
 * 任务处理器
 * @author Randy
 *
 * @param <T>
 */
public interface JobHandler<T> {
	
	/**
	 * 需要处理的任务，在不同的线程中调度任务
	 * @param job
	 */
	void onExecuteJob(T job);
}
