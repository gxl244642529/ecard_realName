package com.damai.core;


public interface JobListener<T extends Job> extends OnJobSuccessListener<T>,OnJobProgressListener<T>,OnJobErrorListener<T> {
	
}
