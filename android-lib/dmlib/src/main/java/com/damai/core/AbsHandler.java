package com.damai.core;

public abstract class AbsHandler<T extends JobImpl> implements JobHandler<T>, JobCtrl {
	private T currentJob;
	@Override
	public void handleJob(T job) {
		currentJob = job;
		job.setCtrl(this);
		//这里正式执行
		handleJobImpl(job);
		job.setCtrl(null);
	}

	protected abstract void handleJobImpl(T job);

	@Override
	public void shutdown() {
		cancelProgress();
	}

	@Override
	public void cancelJob(JobImpl job) {
		if(currentJob==job){
			cancelProgress();
		}
	}

	protected abstract void cancelProgress();

	
	
}
