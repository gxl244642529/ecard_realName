package com.damai.core;


public interface OnJobProgressListener<T extends Job> {
	void onJobProgress(T job);
}
