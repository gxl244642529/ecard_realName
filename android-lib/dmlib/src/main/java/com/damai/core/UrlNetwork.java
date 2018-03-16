package com.damai.core;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.citywithincity.utils.IoUtil;
import com.citywithincity.utils.ThreadUtil;

public class UrlNetwork implements Network {

	protected final ByteArrayPool mPool;
	HttpURLConnection connection ;
	
	public UrlNetwork(ByteArrayPool pool){
		mPool = pool;
	}
	
	
	@Override
	public byte[] execute(HttpJobImpl job,OnJobProgressListener<JobImpl> listener) throws IOException {
		
		URL url = new URL(job.url);
		connection = (HttpURLConnection) url.openConnection();

		int timeoutMs = job.timeoutMs;
		connection.setConnectTimeout(timeoutMs);
		connection.setReadTimeout(timeoutMs);
		connection.setUseCaches(false);
		connection.setDoInput(true);
		
		int code = connection.getResponseCode();
		if(code==-1){
			throw new IOException("Cannot get response code!");
		}
		InputStream inputStream;
        try {
            inputStream = connection.getInputStream();
        } catch (IOException ioe) {
            inputStream = connection.getErrorStream();
        }
        int contentLen = connection.getContentLength();
        if(contentLen<0){
        	contentLen = 1024;
        }
        PoolingByteArrayOutputStream bytes = new PoolingByteArrayOutputStream(mPool, contentLen);
    	byte[] buffer = null;
    	job.setTotalBytes(contentLen);
		try {
			buffer = mPool.getBuf(1024);
			int count;
			int current = 0;
			while ((count = inputStream.read(buffer)) != -1) {
				bytes.write(buffer, 0, count);
				current += count;
				job.setCurrentBytes(current);
				if(listener!=null){
					listener.onJobProgress(job);
				}
			}
			return bytes.toByteArray();
		} finally {
			IoUtil.close(inputStream);
			mPool.returnBuf(buffer);
			IoUtil.close(bytes);
			connection = null;
		}
	}

	@Override
	public void cancel() {
		if(connection!=null){
			ThreadUtil.run(new Runnable() {
				
				@Override
				public void run() {
					if(connection!=null){
						
						try{
							connection.disconnect();
						}catch(Exception e){
							e.printStackTrace();
						}
						
					}
					
				}
			});
			
		}
	}


	

}
