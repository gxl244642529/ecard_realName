package com.damai.core;

public interface OnApiMessageListener {
    /**
     * 服务器通知
     */
    boolean onApiMessage(ApiJob job);
}
