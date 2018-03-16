package com.damai.jobqueue;

/**
 * 任务
 * @author Randy
 *
 */
public interface Job extends Comparable<Job> {
	
	/**
	 * 任务优先级，用于在任务队列中排序
	 * @author Randy
	 *
	 */
	public static enum Priority {
        LOW,
        NORMAL,
        HIGH,
        IMMEDIATE
    }
	
	
	/**
	 * 取消
	 */
	void cancel();
	/**
	 * 是否已经取消了
	 * @return
	 */
	boolean isCanceled();
	
	/**
	 * 任务优先级
	 * @return
	 */
	Priority getPriority();
	
	
}
