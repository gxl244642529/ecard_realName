package com.damai.core;

import java.io.IOException;
import java.io.OutputStream;

/**
 * 网络任务
 * @author Randy
 *
 */
public interface INetwork {
	
	public static final int METHOD_GET = 0;
	public static final int METHOD_POST = 1;
	
	
	void setHeader(String key,Object value);
	void cancel();
	void connect(String url,int method,int timeoutMs) throws IOException;
	void send(byte[] data) throws IOException;
	byte[] read() throws IOException;
	void read(OutputStream outputStream) throws IOException;
}
