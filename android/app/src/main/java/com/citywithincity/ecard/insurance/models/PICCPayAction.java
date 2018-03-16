package com.citywithincity.ecard.insurance.models;

import com.citywithincity.auto.Crypt;
import com.citywithincity.ecard.insurance.models.vos.InsurancePaySuccessNotifyVo;
import com.citywithincity.interfaces.DialogListener;
import com.citywithincity.interfaces.ILog;
import com.citywithincity.interfaces.IRequestResult;
import com.citywithincity.models.LogFactory;
import com.citywithincity.models.http.JsonTaskManager;
import com.citywithincity.mvc.ModelHelper;
import com.citywithincity.paylib.ECardPayModel;
import com.citywithincity.paylib.IPayAction;
import com.citywithincity.paylib.IPayActionListener;
import com.citywithincity.paylib.PayType;
import com.citywithincity.utils.Alert;

import org.json.JSONException;
import org.json.JSONObject;

public class PICCPayAction implements IPayAction {
	
	public static final String prePayApi = "i_safe/prePay"; 
	public static final String prePayApi_error = "i_safe/prePay_error"; 
	
	
	public PICCPayAction() {
		// super("i_safe/prePay", "i_safe/payNotify");
		// TODO Auto-generated constructor stub
	}

	@Override
	public void prePay(PayType payType, String orderID,
			final IPayActionListener listener) {
		final IRequestResult<Object> requestResult = new IRequestResult<Object>() {

			@Override
			public void onRequestError(String errMsg, boolean isNetworkError) {
				Alert.alert(ModelHelper.getActivity(), "温馨提示", errMsg);
			}

			@Override
			public void onRequestSuccess(Object result) {
				try {
					JSONObject jsonObject = (JSONObject) result;
					jsonObject.put("fee",
							((float) jsonObject.getInt("fee")) / 100);
					listener.onPrePaySuccess(result);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		JsonTaskManager.getInstance().createValueJsonTask(prePayApi, 1)
				.setListener(requestResult).put("id", orderID)
				.put("type", payType.value()).setCrypt(Crypt.BOTH)
				.setWaitMessage("请稍等...").execute();
	}

	@Override
	public void notifyServer(PayType payType, Object info,
			final IPayActionListener listener) {

		// ModelHelper.getModel(InsuranceModel.class).payNotify(
		// ModelHelper.getModel(ECardPayModel.class).getCashierInfo()
		// .getOrderID(), payType.value());

		final IRequestResult<Object> requestResult = new IRequestResult<Object>() {

			@Override
			public void onRequestError(String errMsg, boolean isNetworkError) {
				Alert.cancelWait();
				Alert.alert(ModelHelper.getActivity(), "温馨提示", errMsg);
				listener.onNotityServerError(errMsg);
				ModelHelper.getActivity().finish();
				LogFactory.setConfig(ILog.Level_Error, ILog.Type_File, ModelHelper.getActivity());
				LogFactory.getLog(ModelHelper.getActivity().getClass().getName()).error("pay_error" + errMsg);
			}

			@Override
			public void onRequestSuccess(Object result) {
				
				JSONObject jsonObject = (JSONObject) result;
				
				LogFactory.setConfig(ILog.Level_Error, ILog.Type_File, ModelHelper.getActivity());
				LogFactory.getLog(ModelHelper.getActivity().getClass().getName()).error("pay_error" + jsonObject);
				
				if (jsonObject.has("error_code") && !jsonObject.isNull("error_code") && !jsonObject.isNull("error")) {
					try {
						
						final String errorString = jsonObject.getString("error");
						
						Alert.confirm(ModelHelper.getActivity(), "提示", errorString, new DialogListener() {
							
							@Override
							public void onDialogButton(int id) {
								listener.onNotityServerError(errorString);
								
							}
						});
					} catch (JSONException e) {
						e.printStackTrace();
					}
				} else {
					InsurancePaySuccessNotifyVo data = new InsurancePaySuccessNotifyVo();
					try {
						data.fromJson(jsonObject);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					ModelHelper.setListData(data);
					listener.onNotifyServerSuccess(result);
					
				}
				
//				{"cmId":47244,
//					"name":"v号宝宝",
//					"addr":null,
//					"idCard":"123548896541235",
//					"phone":"18659210057",
//					"pc":null,
//					"id":"15110906095046200",
//					"inId":"5432",
//					"cmAcc":"18906017811",
//					"createTime":"2015-11-09",
//					"payTime":null,
//					"refundTime":null,
//					"idImg":"\/uploads\/2015-11-09\/b98668137d589f61892af0aaf427767d.jpg",
//					"cardId":"6562437417",
//					"duration":"1",
//					"fee":200,
//					"typeId":"1",
//					"title":"e通卡卡身保",
//					"payStatus":0}
//
//				{"error":"上传图片失败：array (\n  'error' => '<p>The file you are attempting to upload is larger than the permitted size.<\/p>',\n)",
//				"error_code":2}
				
			}
		};

		String id = ModelHelper.getModel(ECardPayModel.class).getCashierInfo()
				.getOrderID();

		JsonTaskManager.getInstance()
				.createValueJsonTask("i_safe/clientNotify", 1)
				.setListener(requestResult).put("id", id).enableAutoNotify()
				.put("type", payType.value()).setCrypt(Crypt.BOTH)
				.execute();
		
		Alert.wait(ModelHelper.getActivity(), "请稍后……");
		Alert.waitCanceledOnTouchOutside(false);
	}

}
