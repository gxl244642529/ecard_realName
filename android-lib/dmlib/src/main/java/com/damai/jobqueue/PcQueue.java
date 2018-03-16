package com.damai.jobqueue;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.PriorityBlockingQueue;


/**
 * 消费者、生产者
 * @author Randy
 *
 * @param <T>
 */
public class PcQueue<T extends Job> implements JobQueueManager<T>{

	/**
	 * 任务队列,按照优先级处理任务
	 */
	PriorityBlockingQueue<T> jobQueue;
	
	/**
	 * 任务处理器
	 */
	private List<JobHandler<T>> jobHandlers;
	/**
	 * 线程池
	 */
	protected PcQueueThread<T>[] queueThreads;
	
	/**
	 * 是否正在运行
	 */
	private volatile boolean isRunning;
	
	
	public PcQueue(){
		jobQueue = new PriorityBlockingQueue<T>();
		jobHandlers = new ArrayList<JobHandler<T>>();
	}
	
	@Override
	public void addJobHandler(JobHandler<T> handler){
		jobHandlers.add(handler);
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public void start() {
		if(isRunning)return;
		int size = jobHandlers.size();
		queueThreads = new PcQueueThread[size];
		for (int i = 0; i < size; i++) {
			queueThreads[i] = new PcQueueThread<T>(jobQueue,jobHandlers.get(i));
			queueThreads[i].start();
		}
		isRunning = true;
	}

	@Override
	public void stop() {
		if(!isRunning)return;
		for (int i = 0; i < queueThreads.length; i++) {
			queueThreads[i].stop();
			queueThreads[i] = null;
		}
	}

	@Override
	public void add(T job) {
		jobQueue.add(job);
	}

	
}
