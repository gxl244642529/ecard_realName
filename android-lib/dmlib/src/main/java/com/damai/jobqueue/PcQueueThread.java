package com.damai.jobqueue;

import java.util.concurrent.BlockingQueue;

public class PcQueueThread<T extends Job> extends ServiceThread implements
		JobQueue<T> {

	private BlockingQueue<T> mQueue;
	private JobHandler<T> hander;

	public PcQueueThread(BlockingQueue<T> queue, JobHandler<T> hander) {
		mQueue = queue;
		this.hander = hander;
	}

	public void setHandler(JobHandler<T> hander) {
		this.hander = hander;
	}

	public synchronized void stop() {
		if (!isRunning)
			return;
		isRunning = false;
		thread.interrupt();
	}

	protected boolean repetitionRun() {
		T job;
		try {
			// Take a request from the queue.
			job = mQueue.take();
		} catch (InterruptedException e) {
			// We may have been interrupted because it was time to quit.
			if (!isRunning) {
				return false;
			}
			return true;
		}
		//如果任务被取消了
		if (job.isCanceled())
			return true;
		// 这里已经拿到任务
		hander.onExecuteJob(job);

		return true;
	}

	@Override
	public void add(T job) {
		mQueue.add(job);
	}
}
