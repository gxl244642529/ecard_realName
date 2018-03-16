package com.damai.core;

import java.io.IOException;

public interface Network {
    //发起网络任务
    /**
     * 从网络下载数据
     * @param job
     * @return
     * @throws IOException
     */
    byte[] execute(HttpJobImpl job,OnJobProgressListener<JobImpl> listener) throws IOException;

    void cancel();

}
