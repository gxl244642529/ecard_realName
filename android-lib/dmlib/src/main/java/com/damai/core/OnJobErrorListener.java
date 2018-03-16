package com.damai.core;


public interface OnJobErrorListener<T extends Job> {
    boolean onJobError(T job);
}
