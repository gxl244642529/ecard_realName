package com.damai.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.json.JSONObject;

import android.content.Context;

import com.citywithincity.crypt.Aes128DataCrypt;
import com.citywithincity.utils.IoUtil;
import com.citywithincity.utils.MD5Util;
import com.damai.core.ApiNetwork.Response;
import com.damai.dmlib.LibBuildConfig;
import com.damai.push.Push;
import com.damai.util.JsonUtil;
import com.damai.util.SignUtil;

public class JavaServerHandler extends ECardApiServerHandler {
	private Aes128DataCrypt aes;
	private static int time;
	private static String token;
	private static String key;
	private static String uploadKey;

	public JavaServerHandler(Context context) {
		super(context);
		aes = new Aes128DataCrypt();
	}

	@Override
	protected String getUrl(String api) {
		return new StringBuilder(LibBuildConfig.MAX_API_LEN)
				.append(DMServers.getUrl(1)).append("/business/").append(api)
				.toString();
	}

	public static Map<String, Object> getInitObject(
			Map<String, Object> jsonObject) {
		jsonObject = new TreeMap<String, Object>(jsonObject);
		jsonObject.put("deviceID", Push.getUdid());
		// jsonObject.put("token", token);
		time = (int) (System.currentTimeMillis() / 1000);
		jsonObject.put("time", time);
		jsonObject.put("rand_str",
				MD5Util.md5Appkey(String.valueOf(System.currentTimeMillis())));
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
		for (Entry<String, Object> entry : files.entrySet()) {
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
		for (Entry<String, Object> entry : files.entrySet()) {
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
		Map<String, Object> params = new HashMap<String, Object>();
		DMAccount userInfo = DMAccount.get();
		if (userInfo == null) {
			onLoginError(job);
			return false;
		}
		params.put("account", userInfo.getAccount());
		params.put("pwd", userInfo.getPassword());
		params.put("channel", userInfo.getChannel());
		params.put("platform", "android");

		Response response = execute(params, DEFAULT_TIME_OUT,
				getUrl("gate/login"), Crypt.BOTH);
		Integer userId = null;
		try {
			userId = tryParse(response.data, ErrCode_NotLogin);
		} catch (NotLoginError notLoginError) {

		}
		if (userId != null) {
			// userInfo.userID = userId;
			// userInfo.save();
			// onLoginSuccess();
			// 这里需要再次存储一次

			return true;
		} else {
			// 登录失败
			onLoginError(job);
		}
		return false;
	}

	@Override
	protected boolean doGetAccessToken(ApiJobImpl job) throws IOException,NotLoginError {

		key = null;
		token = null;
		uploadKey = null;
		Map<String, Object> json = new HashMap<String, Object>();
		Response response = null;
		try {
			response = execute(json, DEFAULT_TIME_OUT,
					getUrl("gate/token"), Crypt.DOWNLOAD);
		} catch (NoAccessTokenError e1) {
			throw new IOException("获取accessToken需要accessToken");
		}
		JSONObject result = tryParse(response.data, ErrCode_RequireAccessToken);
		if (result != null) {
			try {
				key = result.getString("key");
				token = result.getString("token");
			} catch (Exception e) {
				throw new IOException(e.getMessage());
			}
			int start = 16 - time;
			uploadKey = key.substring(start, start + 16);

			return true;
		}
		return false;
	}

	@Override
	protected Response execute(Map<String, Object> params, int timeoutMs,
							   String url, int crypt) throws IOException, NoAccessTokenError {
		Response response = super.execute(params, timeoutMs, url, crypt);
		byte[] buffer = response.data;
		if (crypt >= Crypt.DOWNLOAD) {
			if (buffer[0] == '{') {
				// 这里进行解析
				return response;
			} else {
				String aesKey = null;
				if (key != null) {
					// 解密
					aesKey = key.substring(time, time + 16);
				} else {
					aesKey = Push.getUdid()
							.substring(time, time + 16);
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
