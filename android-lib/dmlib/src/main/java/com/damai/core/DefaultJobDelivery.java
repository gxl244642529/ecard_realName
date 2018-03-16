package com.damai.core;


/**
 * 任务投递
 * @author Randy
 *
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class DefaultJobDelivery<T extends JobImpl> implements JobListener<T> {
	
	
	protected IJobLife life;
	public DefaultJobDelivery(IJobLife life){
		this.life = life;
	}
	
	@Override
	public void onJobSuccess(T job) {
		OnJobSuccessListener listener = job.onJobSuccessListener;
		if(listener!=null){
			listener.onJobSuccess(job);
		}
		life.onRemoveJob(job);
	}

	@Override
	public void onJobProgress(T job) {
		OnJobProgressListener listener = job.onJobProgressListener;
		if(listener!=null){
			listener.onJobProgress(job);
		}
		
	}

	@Override
	public boolean onJobError(T job) {
		OnJobErrorListener listener = job.onJobErrorListener;
		if(listener!=null){
			listener.onJobError(job);
		}
		life.onRemoveJob(job);
		return true;
	}
	
	

}
