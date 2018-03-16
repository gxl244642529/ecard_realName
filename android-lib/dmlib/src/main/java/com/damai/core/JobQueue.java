package com.damai.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

import android.annotation.SuppressLint;


public class JobQueue {
    /**
     * 任务队列,按照优先级处理任务
     */
    BlockingDeque<JobImpl> jobQueue;

    /**
     * 任务处理器
     */
    private Map<Integer, List<JobHandler<? extends JobImpl>>> map;
    /**
     * 线程池
     */
    protected JobThread[] queueThreads;

    /**
     * 是否正在运行
     */
    private volatile boolean isRunning;

    @SuppressLint("UseSparseArrays")
    public JobQueue(){
        map = new HashMap<Integer, List<JobHandler<? extends JobImpl>>>();
        this.jobQueue = new LinkedBlockingDeque<JobImpl>();
    }
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void register(int handlerType, JobHandler<? extends JobImpl> handler) {
        List<JobHandler<? extends JobImpl>> list = map.get(handlerType);
        if(list==null){
            list = new ArrayList();
            map.put(handlerType, list);
        }
        list.add(handler);
    }
    /**
     * 启动
     */
    public void start(){
        if(isRunning)return;
        int size = 0 ;
        List<ArrayContainer<JobHandler<JobImpl>>> container = new ArrayList<ArrayContainer<JobHandler<JobImpl>>>();
        boolean first = true;
        for (Entry<Integer, List<JobHandler<? extends JobImpl>>> entry : map.entrySet()) {
            if(first){
                size = entry.getValue().size();
                for(int i=0 ; i < size; ++i){
                    container.add(new ArrayContainer<JobHandler<JobImpl>>());
                }
            }
            int index = 0;
            for (JobHandler<? extends JobImpl> handler : entry.getValue()) {
                ArrayContainer<JobHandler<JobImpl>> arrayContainer = container.get(index);
                arrayContainer.register(entry.getKey(), handler);
                ++index;
            }
        }

        queueThreads = new JobThread[size];
        for (int i = 0; i < size; i++) {
            ArrayContainer<JobHandler<JobImpl>> arrayContainer = container.get(i);
            @SuppressWarnings("unchecked")
            JobHandler<JobImpl>[] array = new JobHandler[arrayContainer.size()];
            arrayContainer.toArray(array);
            queueThreads[i] = new JobThread(jobQueue,array);
            queueThreads[i].start();
        }
        isRunning = true;
    }
    /**
     * 停止
     */
    public void stop(){
        if(!isRunning)return;

        for (int i = 0; i < queueThreads.length; i++) {
            queueThreads[i].stop();
            queueThreads[i] = null;
        }
    }

    /**
     * 增加任务
     * @param job
     */
    public void add(JobImpl job){
        jobQueue.add(job);
    }


}
