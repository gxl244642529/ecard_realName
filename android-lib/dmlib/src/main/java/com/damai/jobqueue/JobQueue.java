package com.damai.jobqueue;

/**
 * 任务队列
 * @author Randy
 *
 * @param <T>
 */
public interface JobQueue<T extends Job> {
	
	
	/**
	 * 启动
	 */
	void start();
	/**
	 * 停止
	 */
	void stop();
	
	/**
	 * 增加任务
	 * @param job
	 */
	void add(T job);
}
