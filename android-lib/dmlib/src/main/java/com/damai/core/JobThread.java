package com.damai.core;

import java.util.concurrent.BlockingQueue;

public class JobThread extends ServiceThread {
    private BlockingQueue<JobImpl> queue;
    private JobHandler<JobImpl>[] hander;

    public JobThread(BlockingQueue<JobImpl> queue, JobHandler<JobImpl>[] hander) {
        this.queue = queue;
        this.hander = hander;
    }

    public synchronized void stop() {
        if (!isRunning)
            return;

        for (JobHandler<JobImpl> handler : hander) {
            if(handler!=null){
                handler.shutdown();
            }
        }

        isRunning = false;
        thread.interrupt();
    }

    protected boolean repetitionRun() {
        JobImpl job;
        final BlockingQueue<JobImpl> queue = this.queue;
		final JobHandler<JobImpl>[] hander = this.hander;
        try {
            job = queue.take();
        } catch (InterruptedException e) {
            if (!isRunning) {
                return false;
            }
            return true;
        }
        //如果任务被取消了
        if (job.isCanceled())
            return true;
        // 这里已经拿到任务
        hander[job.getHandlerType()].handleJob(job);

        return true;
    }

    public void add(JobImpl job) {
        queue.add(job);
    }
}
