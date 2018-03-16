package com.citywithincity.ecard;

import android.content.Context;

import com.citywithincity.crypt.Aes128DataCrypt;
import com.citywithincity.ecard.models.vos.ECardUserInfo;
import com.citywithincity.utils.IoUtil;
import com.citywithincity.utils.MD5Util;
import com.damai.auto.LifeManager;
import com.damai.core.ApiJobImpl;
import com.damai.core.ApiNetwork;
import com.damai.core.Crypt;
import com.damai.core.DMAccount;
import com.damai.core.DMPage;
import com.damai.core.DMServers;
import com.damai.core.ECardApiServerHandler;
import com.damai.dmlib.LibBuildConfig;
import com.damai.push.Push;
import com.damai.util.JsonUtil;
import com.damai.util.SignUtil;
import com.facebook.react.bridge.ReadableMap;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import cn.jpush.android.api.JPushInterface;


/**
 * Created by renxueliang on 17/3/30.
 */

public class NewJavaServerHandler extends ECardApiServerHandler{

    private Aes128DataCrypt aes;
    private int time;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    private String token;
    private String key;
    private String uploadKey;

    private static NewJavaServerHandler instance;

    public static NewJavaServerHandler runningInstance() {
        return instance;
    }

    public NewJavaServerHandler(Context context) {
        super(context);
        aes = new Aes128DataCrypt();
        instance = this;
    }

    @Override
    protected String getUrl(String api) {
        return new StringBuilder(LibBuildConfig.MAX_API_LEN)
                .append(DMServers.getUrl(1)).append("/api/").append(api)
                .toString();
    }

    public Map<String, Object> getInitObject(
            Map<String, Object> jsonObject) {
        jsonObject = new TreeMap<String, Object>(jsonObject);
        jsonObject.put("deviceID", Push.getUdid());
        // jsonObject.put("token", token);
        time = (int) (System.currentTimeMillis() / 1000);

        jsonObject.put("time", time);
        jsonObject.put("rand_str", MD5Util.md5Appkey(String.valueOf(System.currentTimeMillis())));
        time = 16 - time % 16;
        return jsonObject;
    }

    @Override
    protected RequestInfo encodeRequest(Map<String, Object> jsonObject,
                                        int crypt) throws IOException, NoAccessTokenError {
        int retryCount = 0;
        do {
            Map<String, Object> data = getInitObject(jsonObject);
            data.remove("sign");
            if (key != null) {
                data.put("sign", SignUtil.sign(data, key));
            } else {
                data.put("sign", SignUtil.sign(data, Push.getUdid()));
            }
            String post = JsonUtil.toJson(data);
            // 发送

            if (LibBuildConfig.DEBUG) {
                System.out.println(post);
            }

            byte[] postData = null;
            if ((crypt & Crypt.UPLOAD) > 0) {
                if(uploadKey==null){
                    throw new NoAccessTokenError();
                }
                // 增加header
                byte[] src = post.getBytes("UTF-8");
                try {
                    postData = aes.encript(src, uploadKey.getBytes());
                } catch (Exception e) {
                    // 加密失败
                    retryCount++;
                    if (retryCount >= 2) {
                        throw new IOException("参数加密失败,请检查");
                    }
                    continue;
                }

            } else {
                try {
                    postData = post.getBytes("UTF-8");
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException();
                }
            }

            // 如果
            RequestInfo info = new RequestInfo(postData);
            Map<String, String> headers = new HashMap<String, String>();
            headers.put("token", token);
            info.headers = headers;
            // 这里
            Map<String, Object> files = SignUtil.getFiles();
            if (files.size() > 0) {
                try {
                    info.data = handleFiles(postData, files, info.headers);
                } catch (IOException e) {

                    throw new IOException("不能加载文件，请重新选择");
                }
            }
            return info;

        } while (true);

    }

    @Override
    public void clearSession() {
        super.clearSession();

        key = null;
        uploadKey = null;
        token = null;
    }

    private byte[] handleFiles(byte[] src, Map<String, Object> files,
                               Map<String, String> headers) throws IOException {
        StringBuilder sb = new StringBuilder();
        boolean first = true;

        int fileLength = 0;
        for (Map.Entry<String, Object> entry : files.entrySet()) {
            Object obj = entry.getValue();
            if (obj instanceof File) {
                File file = (File) obj;
                fileLength += file.length();
            }else if(obj instanceof byte[]){
                fileLength += ((byte[])obj).length;
            }
        }
        byte[] bytes = new byte[src.length + fileLength];
        System.arraycopy(src, 0, bytes, 0, src.length);
        int start = src.length;
        for (Map.Entry<String, Object> entry : files.entrySet()) {
            if (first) {
                first = false;
            } else {
                sb.append(',');
            }

            Object obj = entry.getValue();
            int len = 0;
            if (obj instanceof File) {
                File file = (File) obj;
                FileInputStream reader = null;
                len = (int) file.length();
                try {
                    reader = new FileInputStream(file);
                    reader.read(bytes, start, len);
                } finally {
                    IoUtil.close(reader);
                }

            } else if (obj instanceof byte[]) {
                byte[] file = (byte[]) obj;
                len = file.length;
                System.arraycopy(file, 0, bytes, start, len);
            }
            sb.append(entry.getKey());
            sb.append(':');
            sb.append(len);
            start += len;
        }

        headers.put("attach", sb.toString());
        headers.put("fileLength", String.valueOf(fileLength));
        headers.put("totalLength", String.valueOf(fileLength + src.length));
        return bytes;

    }

    @Override
    public int positionToPageParam(int position) {
        return position;
    }

    @Override
    public void setPosition(int postion, Map<String, Object> param) {
        param.put("position", postion);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public DMPage<?> createPage() {
        return new DMPage();
    }

    @Override
    protected boolean doLogin(ApiJobImpl job) throws IOException, NoAccessTokenError {
        onLoginError(job);
        return false;
    }


    public void onGetToken(JSONObject result ) throws JSONException {
        key = result.getString("key");
        token = result.getString("token");
        uploadKey = result.getString("pinKey");
        //这里需要保存一下token
    }

    public void onGetToken(ReadableMap result) {
        key = result.getString("key");
        token = result.getString("token");
        uploadKey = result.getString("pinKey");
        //保存一下token
    }

    @Override
    protected boolean doGetAccessToken(ApiJobImpl job) throws IOException,NotLoginError {

        key = null;
        token = null;
        uploadKey = null;
        Map<String, Object> json = new HashMap<String, Object>();
        ApiNetwork.Response response = null;
        try {
            ECardUserInfo account = DMAccount.get();
            json.put("hash", account.getUserHash());
            json.put("pushId", JPushInterface.getRegistrationID(LifeManager.getActivity()));
            response = execute(json, DEFAULT_TIME_OUT,
                    getUrl("passport/token"), Crypt.NONE);

            JSONObject result = new JSONObject(new String(response.data));
            int flag = result.getInt("flag");
            if(flag==0) {
                onGetToken(result.getJSONObject("result"));
                return true;
            }else if(flag==7) {
                onLoginError(job);
                return false;
            }else {
                onLoginError(job);
                return false;
            }
        } catch (NoAccessTokenError e1) {
            return false;
        }catch (JSONException e) {
            return false;
        }
    }

    @Override
    protected ApiNetwork.Response execute(Map<String, Object> params, int timeoutMs,
                                          String url, int crypt) throws IOException, NoAccessTokenError {
        ApiNetwork.Response response = super.execute(params, timeoutMs, url, crypt);
        byte[] buffer = response.data;
        if (crypt >= Crypt.DOWNLOAD) {
            if (buffer[0] == '{' && buffer[buffer.length-1]=='}' && buffer[1]=='"') {
                // 这里进行解析
                return response;
            } else {
                String aesKey = null;
                if (key != null) {
                    // 解密
                    aesKey = key.substring(time, time + 16);
                } else {
                    aesKey = Push.getUdid().substring(time, time + 16);
                }
                try {
                    response.data = aes.decript(buffer, aesKey.getBytes());
                } catch (Exception e) {
                    throw new IOException("数据解密失败");
                }
            }
        }

        return response;
    }
}
