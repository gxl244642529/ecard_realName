package com.citywithincity.paylib;

import com.citywithincity.interfaces.IRequestResult;
import com.citywithincity.models.http.JsonTaskManager;
import com.citywithincity.mvc.ModelHelper;
import com.citywithincity.utils.Alert;

public class BasePayAction implements IPayAction   {

	private String prePayApi;
	private String notifyServerApi;
	
	public BasePayAction(String prePayApi,String notifyServerApi){
		this.prePayApi = prePayApi;
		this.notifyServerApi = notifyServerApi;
	}

	@Override
	public void prePay(PayType payType, String orderID,
			final IPayActionListener listener) {
		// TODO Auto-generated method stub
		final IRequestResult<Object> requestResult = new IRequestResult<Object>() {

			@Override
			public void onRequestError(String errMsg, boolean isNetworkError) {
				Alert.alert(ModelHelper.getActivity(), "温馨提示",errMsg);
			}

			@Override
			public void onRequestSuccess(Object result) {
				try {
					listener.onPrePaySuccess(result);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		};
//		JsonTaskManager.getInstance().createValueJsonTask(prePayApi,1).setListener(requestResult).put("id", orderID).put("type", payType.value())
//		.setWaitMessage("请稍等...").setCrypt(Crypt.BOTH).execute();
		JsonTaskManager.getInstance().createValueJsonTask(prePayApi,0).setListener(requestResult).put("id", orderID).put("type", payType.value())
		.setWaitMessage("请稍等...").execute();
	}

	@Override
	public void notifyServer(PayType payType, Object info,
			final IPayActionListener listener) {
		final IRequestResult<Object> requestResult = new IRequestResult<Object>() {

			@Override
			public void onRequestError(String errMsg, boolean isNetworkError) {
				Alert.cancelWait();
				Alert.alert(ModelHelper.getActivity(), "温馨提示",errMsg);
			}

			@Override
			public void onRequestSuccess(Object result) {
				listener.onNotifyServerSuccess(result);
			}
		};
		
		JsonTaskManager.getInstance().createValueJsonTask(notifyServerApi,0)
		.setListener(requestResult).put("pay_info",info).put("type", payType.value())
		.setWaitMessage("请稍等...").execute();
	}
}
