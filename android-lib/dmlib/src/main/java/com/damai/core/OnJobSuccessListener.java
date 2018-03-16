package com.damai.core;


public interface OnJobSuccessListener<T extends Job> {
    void onJobSuccess(T job);
}
