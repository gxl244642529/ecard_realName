package com.damai.core;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.citywithincity.utils.IoUtil;
import com.citywithincity.utils.ThreadUtil;

public class ApiNetwork {
	
	public static class Response{
		
		public Response(byte[] data,int contentLen,Map<String, String> headers){
			this.data = data;
			this.contentLen = contentLen;
			this.headers = headers;
		}
		
		public byte[] data;
		public int contentLen;
		public Map<String, String> headers;
	}
	
	
	private ByteArrayPool pool;
	HttpURLConnection connection;
	private static final int MAX_RETRY_TIMES = 0;
	private String cookie;
	public ApiNetwork(ByteArrayPool pool){
		this.pool = pool;
	}
	public static final String SetCookie = "Set-Cookie";
	public Response execute(int timeoutMs,String url,Map<String, String> extraHeaders,byte[] content) throws IOException{
		int retryTimes = 0;
		while(true){
			try{
				connection = HttpUtil.createConnection(url, timeoutMs);
				HttpUtil.postData(connection,  content,cookie, extraHeaders);
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
		        Map<String, String> headers = new HashMap<String, String>();
		        for (Entry<String, List<String>> entry : connection.getHeaderFields().entrySet()) {
					String key = entry.getKey();
					if(key!=null){
						if(SetCookie.equals(key)){
							String value = entry.getValue().get(0);
							cookie = value;
						}
						headers.put(key, entry.getValue().get(0));
					}
				}
		        
		        PoolingByteArrayOutputStream bytes = new PoolingByteArrayOutputStream(pool, contentLen);
		    	byte[] buffer = null;
				try {
					buffer = pool.getBuf(1024);
					int count;
					while ((count = inputStream.read(buffer)) != -1) {
						bytes.write(buffer, 0, count);
					}
					return new Response(bytes.toByteArray(),contentLen,headers);
				} finally {
					IoUtil.close(inputStream);
					pool.returnBuf(buffer);
					IoUtil.close(bytes);
				}
			}catch (IOException e) {
				++retryTimes;
				timeoutMs *= 2;
				if(retryTimes >= MAX_RETRY_TIMES){
					throw new IOException("网络超时,请稍后重试");
				}
			}catch(Exception e){
				throw new IOException("访问网络发生异常");
			}finally{
				connection = null;
			}
		}
	}

	public void cancel() {
		if(connection!=null){
			ThreadUtil.run(new Runnable() {
				@Override
				public void run() {
					if(connection!=null){
						try{
							connection.disconnect();
						}catch(Exception e){
							
						}
					}
				}
			});
			
		}
		
	}

	public void clearSession() {
		cookie = null;
	}
}
