package com.damai.core;

import com.citywithincity.interfaces.IDestroyable;

/**
 * 表示一向任务
 * @author Randy
 *
 */
public abstract class JobImpl implements Job, IDestroyable {

    protected OnJobSuccessListener<? extends Job> onJobSuccessListener;
    protected OnJobErrorListener<? extends Job> onJobErrorListener;
    protected OnJobProgressListener<? extends Job> onJobProgressListener;

    protected JobCtrl ctrl;

    ///protected boolean isExecuting;
    protected boolean isDestroied;

    /**
     * 取消标志
     */
    protected boolean isCanceled;
    /**
     * 任务类别
     */
    protected int handlerType;

    /**
     * 投递类型
     */
    protected int deliverType;

    /**
     * 任务id，使用范围：如api接口调用，用来标识api
     * http调用，用来标识http的url
     *
     */
    protected String id;

    /**
     * 关联对象
     */
    protected Object related;
    
    public JobImpl(){
    	
    }

    public void init() {
        isCanceled = false;

    }

    @Override
    public void destroy(){
        onJobSuccessListener = null;
        onJobErrorListener = null;
        onJobProgressListener = null;
        setRelated(null);
        setDestroied(true);
    }

    public boolean isCanceled() {
        return isCanceled;
    }

    public void setCanceled(boolean isCanceled) {
        this.isCanceled = isCanceled;
    }
    /**
     * 取消任务
     */
    public void cancel(){
        isCanceled = true;
        if(ctrl!=null){
            ctrl.cancelJob(this);
        }
    }


    public JobCtrl getCtrl() {
        return ctrl;
    }

    public void setCtrl(JobCtrl ctrl) {
        this.ctrl = ctrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public OnJobSuccessListener<? extends Job> getOnJobSuccessListener() {
        return onJobSuccessListener;
    }


    public void setOnJobSuccessListener(OnJobSuccessListener<? extends Job> onJobSuccessListener) {
        this.onJobSuccessListener = onJobSuccessListener;
    }


    public OnJobErrorListener<? extends Job> getOnJobErrorListener() {
        return onJobErrorListener;
    }


    public void setOnJobErrorListener(OnJobErrorListener<? extends Job> onJobErrorListener) {
        this.onJobErrorListener = onJobErrorListener;
    }


    public OnJobProgressListener<? extends Job> getOnJobProgressListener() {
        return onJobProgressListener;
    }


    public void setOnJobProgressListener(OnJobProgressListener<? extends Job> onJobProgressListener) {
        this.onJobProgressListener = onJobProgressListener;
    }

    public boolean isDestroied() {
        return isDestroied;
    }

    public void setDestroied(boolean isDestroied) {
        this.isDestroied = isDestroied;
    }

    public int getHandlerType() {
        return handlerType;
    }

    public void setHandlerType(int handlerType) {
        this.handlerType = handlerType;
    }

    public int getDeliverType() {
        return deliverType;
    }

    public void setDeliverType(int deliverType) {
        this.deliverType = deliverType;
    }

    public Object getRelated() {
        return related;
    }

    public void setRelated(Object related) {
        this.related = related;
    }

}
