package com.damai.core;

import com.citywithincity.models.cache.CachePolicy;

public class NetHandler extends AbsHandler<HttpJobImpl> {

    @SuppressWarnings("rawtypes")
	private DataParser[] parsers;
    private Network network;
    private JobListener<JobImpl> listener;
    private Cache cache;
    @SuppressWarnings("rawtypes")
	public NetHandler(Cache cache,ByteArrayPool pool,JobListener<JobImpl> listener,DataParser[] parsers){
        this.listener = listener;
        this.network = new UrlNetwork(pool);
        this.parsers = parsers;
        this.cache = cache;
    }


    @SuppressWarnings("unchecked")
	@Override
    protected void handleJobImpl(HttpJobImpl job) {

        /**
         * 首先检查cache
         */
        try {
            byte[] result = network.execute(job,job.onJobProgressListener == null ? null : listener);
            if (job.isCanceled()) {
                /**
                 * 如果已经被取消了，那么直接退出
                 */
                return;
            }
            Object data = parsers[job.dataType].parseData(job, result);
            // shezhi
            job.setData(data);
            // 通知成功
            JobImpl j = job;
            listener.onJobSuccess(j);
            if(job.cachePolicy!=CachePolicy.CachePolity_NoCache){
                cache.put(job.getCacheKey(), result);
            }
        } catch (Exception e) {
            // 这里出现错误,报错
            if (job.isCanceled()) {
                /**
                 * 如果已经被取消了，那么直接退出
                 */
                return;
            }
            job.setError(new DMError(e));
            listener.onJobError(job);
        }
    }




    @Override
    protected void cancelProgress() {
        network.cancel();
    }

}
