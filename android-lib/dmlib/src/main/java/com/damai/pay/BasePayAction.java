package com.damai.pay;


import android.view.View;

import com.damai.core.ApiJob;
import com.damai.core.ApiJobImpl;
import com.damai.core.ApiListener;
import com.damai.core.Crypt;
import com.damai.core.DMLib;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class BasePayAction extends DMPayAction {
	private String prePayApi;
	private String notifyServerApi;
	private Class<?> entityClass;
	/**
	 * 通过模块名称初始化
	 * @param module
	 */
	public BasePayAction(String module,Class<?> entityClass){
		prePayApi = module + "/prePay"; 
		notifyServerApi = module + "/clientNotify";
		this.entityClass = entityClass;
	}






	@Override
	public void prePay(int payType, String api, Map<String,Object> data, View view, final ApiListener listener){
		ApiJob job = createJob(api);
		job.putAll(data);
		job.setServer(1);
		job.setTimeoutMS(8000);
		job.setButton(view);
		job.setCrypt(Crypt.BOTH);
		job.setApiListener(new ApiListener() {
			@Override
			public boolean onApiMessage(ApiJob job) {
				return listener.onApiMessage(job);
			}

			@Override
			public boolean onJobError(ApiJob job) {
				return listener.onJobError(job);
			}

			@Override
			public void onJobSuccess(ApiJob job) {
				//这里需要再次做解析
				JSONObject jsonObject = job.getData();
				try {
					String orderId = jsonObject.getString("orderId");
                    int fee = jsonObject.getInt("fee");
					DMLib.getPayModel().setOrderId(orderId);
                    DMLib.getPayModel().setFee(fee);

                    ((ApiJobImpl)job).setData( jsonObject.getJSONObject("info") );

                    listener.onJobSuccess(job);

				} catch (JSONException e) {
					throw new RuntimeException(e);
				}


			}
		});
		job.setWaitingMessage("请稍候...");
		job.execute();
	}
	@Override
	public void prePay(int payType, String orderId, View button,ApiListener listener) {
		
		ApiJob job = createJob(prePayApi);
		job.put("id", orderId);
		job.put("type", payType);
		job.setServer(1);
		job.setTimeoutMS(8000);
		job.setButton(button);
		job.setCrypt(Crypt.BOTH);
		job.setApiListener(listener);
		job.setWaitingMessage("请稍候...");
		job.execute();
		
	}
	
	@Override
	public void getOrderInfo(String orderId, Object info,boolean showWaiting, ApiListener listener) {
		ApiJob job = createJob(notifyServerApi);
		job.put("id", orderId);
		job.putObject("info", info);
		job.setServer(1);
		job.setTimeoutMS(10000);
		job.setCrypt(Crypt.BOTH);
		job.setApiListener(listener);
		job.setEntity(entityClass);
		if(showWaiting){
			job.setWaitingMessage("正在拉取订单信息...");
		}
		job.execute();
	}
}
