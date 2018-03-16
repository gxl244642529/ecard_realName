package com.damai.core;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.citywithincity.utils.IoUtil;

public class HttpUtil {


    public static HttpURLConnection postData(HttpURLConnection connection,
                                             byte[] content,String cookie, Map<String, String> extraHeaders) throws IOException{
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        if(cookie!=null){
            connection.setRequestProperty("Cookie", cookie);
        }
        
        connection.setUseCaches(false);
     //   connection.setRequestProperty("Connection", "Keep-Alive");
        connection.setRequestProperty("Content-Length", String.valueOf(content.length));
        connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
        if(extraHeaders!=null){
            for (Entry<String, String> entry : extraHeaders.entrySet()) {
                connection.setRequestProperty(entry.getKey(), entry.getValue());
            }
            if(extraHeaders.containsKey("attach")){	
            	connection.setChunkedStreamingMode(1024);
             ////   String total = String.valueOf(content.length);
            //    connection.setRequestProperty("totalLength", total);
            }
        }

        OutputStream out = null;
        try {
            // write
            out = new DataOutputStream(connection.getOutputStream());
            out.write(content);
            out.flush();
        } finally {
            IoUtil.close(out);
        }
        return connection;
    }


    /**
     * 创建一个connection
     * @param url
     * @param timeoutMs
     * @param header
     * @param content
     * @return
     * @throws IOException
     */
    public static HttpURLConnection createConnection(String url,int timeoutMs) throws IOException {
        URL netUrl = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) netUrl.openConnection();

        connection.setConnectTimeout(timeoutMs);
        connection.setReadTimeout(timeoutMs);
        connection.setUseCaches(false);
        connection.setDoInput(true);

        return connection;
    }





    public static void downloadData(HttpURLConnection connection,ByteArrayPool pool,OutputStream outputStream) throws IOException{
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
        byte[] buffer = null;
        try {
            buffer = pool.getBuf(1024);
            int count;
            while ((count = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, count);
            }
        } finally {
            IoUtil.close(inputStream);
            pool.returnBuf(buffer);
            connection = null;
        }
    }

    public static byte[] recvData(HttpURLConnection connection,ByteArrayPool pool) throws IOException{
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

        for (Entry<String, List<String>> entry : connection.getHeaderFields().entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue().get(0);
            System.out.print(value);
        }

        PoolingByteArrayOutputStream bytes = new PoolingByteArrayOutputStream(pool, contentLen);
        byte[] buffer = null;
        try {
            buffer = pool.getBuf(1024);
            int count;
            while ((count = inputStream.read(buffer)) != -1) {
                bytes.write(buffer, 0, count);
            }
            return bytes.toByteArray();
        } finally {
            IoUtil.close(inputStream);
            pool.returnBuf(buffer);
            IoUtil.close(bytes);
            connection = null;
        }
    }



}
