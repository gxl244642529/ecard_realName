package com.damai.core;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

import android.annotation.SuppressLint;

@SuppressLint("NewApi") public class SimpleJobQueue {
	 /**
     * 任务队列,按照优先级处理任务
     */
	private BlockingDeque<JobImpl> jobQueue;
    private JobHandler<? extends JobImpl> handler;
    /**
     * 是否正在运行
     */
    private volatile boolean isRunning;
    /**
     * 线程池
     */
    protected SimpleJobThread queueThread;
    
    @SuppressLint("UseSparseArrays")
    public SimpleJobQueue(JobHandler<? extends JobImpl> handler){
        this.jobQueue = new LinkedBlockingDeque<JobImpl>();
        this.handler = handler;
    }
    
    /**
     * 启动
     */
    @SuppressWarnings("unchecked")
	public void start(){
        if(isRunning)return;
        queueThread = new SimpleJobThread(jobQueue,(JobHandler<JobImpl>) handler);
        queueThread.start();
        isRunning = true;
    }
    /**
     * 停止
     */
    public void stop(){
        if(!isRunning)return;

        queueThread.stop();
        queueThread=null;
    }

    /**
     * 增加任务
     * @param job
     */
    public void add(JobImpl job){
        jobQueue.add(job);
    }
}
