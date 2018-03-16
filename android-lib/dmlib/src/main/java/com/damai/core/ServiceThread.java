package com.damai.core;

import android.os.Process;

public abstract class ServiceThread implements Runnable {
    /**
     * 是否正在运行
     */
    protected volatile boolean isRunning;

    /**
     * 线程
     */
    protected Thread thread;

    public ServiceThread(){
        this.thread = new Thread(this);
    }

    /**
     * 启动
     */
    public synchronized void start()
    {
        if(isRunning)return;
        isRunning = true;
        thread.start();
    }


    /**
     * 停止
     */
    public synchronized void stop()
    {
        if(!isRunning)return;
        isRunning = false;
        thread.interrupt();
        while(thread.isAlive()){
            try{Thread.sleep(1);}
            catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    protected abstract boolean repetitionRun();

    @Override
    public void run()
    {
        Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
        while(isRunning)
        {
            try{
                if(!repetitionRun())break;
            }catch(Exception e)
            {
                //当调用线程的thread.interrupt();就退出程序了
                e.printStackTrace();
            }
        }
    }
}
