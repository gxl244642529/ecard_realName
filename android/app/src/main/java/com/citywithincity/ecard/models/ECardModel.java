package com.citywithincity.ecard.models;

import android.content.Context;

import com.citywithincity.ecard.interfaces.IDoudouBus;
import com.citywithincity.ecard.interfaces.IECardJsonTaskManager;
import com.citywithincity.ecard.models.DoudouBusModel.IDoudouBusData;
import com.citywithincity.ecard.models.vos.ActionListVo;
import com.citywithincity.ecard.models.vos.BusinessDetail;
import com.citywithincity.ecard.models.vos.BusinessInfo;
import com.citywithincity.ecard.models.vos.ECardInfo;
import com.citywithincity.ecard.models.vos.ECardUserInfo;
import com.citywithincity.ecard.models.vos.HeadAdvVo;
import com.citywithincity.ecard.models.vos.RegisterVerify;
import com.citywithincity.interfaces.IArrayJsonTask;
import com.citywithincity.interfaces.IDetailJsonTask;
import com.citywithincity.interfaces.IJsonValueObject;
import com.citywithincity.interfaces.IListRequsetResult;
import com.citywithincity.interfaces.IObjectJsonTask;
import com.citywithincity.interfaces.IRequestResult;
import com.citywithincity.libs.LibConfig;
import com.citywithincity.models.cache.CachePolicy;
import com.citywithincity.models.http.JsonTaskManager;
import com.citywithincity.utils.Alert;
import com.citywithincity.utils.PackageUtil;
import com.damai.map.LocationInfo;
import com.damai.map.LocationUtil;
import com.damai.push.Push;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class ECardModel {

	private static ECardModel instance;

	public static ECardModel getInstance() {
		if (instance == null) {
			instance = new ECardModel();
		}

		return instance;
	}

	public void getBusinessList(int position, String type, int distance,
								IListRequsetResult<List<BusinessInfo>> listener) {

		IArrayJsonTask<BusinessInfo> task = JsonTaskManager.getInstance()
				.createArrayJsonTask(IECardJsonTaskManager.BUSINESS_LIST, CachePolicy.CachePolicy_TimeLimit, false, BusinessInfo.class)
				.setPosition(position)
				.setListener(listener);
		task.put("type", type)
				.put("distance", distance);
		if (distance > 0) {
			LocationInfo info = LocationUtil.getCachedLocation();
			task.put("lat", info.getLat());
			task.put("lng", info.getLng());
			task.setCondition(new String[] { "lat", "lng", "type", "distance" });
		} else {
			task.setCondition(new String[] { "type", "distance" });
		}

		task.execute();
	}


	public IArrayJsonTask<BusinessInfo> getBusinessAssortmentList(int position, String type,
																  int distance) {
		IArrayJsonTask<BusinessInfo> task = JsonTaskManager.getInstance()
				.createArrayJsonTask("business_list2",
						CachePolicy.CachePolicy_TimeLimit,
						false, BusinessInfo.class);

		task.setPosition(position);
		task.put("type", type);
		task.put("distance", distance);
		if (distance > 0) {
			LocationInfo info = LocationUtil.getCachedLocation();
			task.put("lat", info.getLat());
			task.put("lng", info.getLng());
			task.setCondition(new String[] { "lat", "lng", "type", "distance" });
		} else {
			task.setCondition(new String[] { "type", "distance" });
		}
		task.execute();
		return task;
		//task.execute();
	}


	public IArrayJsonTask<BusinessInfo> getMyBusinessList(int position, int last,
			IListRequsetResult<List<BusinessInfo>> listener) {

		IArrayJsonTask<BusinessInfo> task = ECardJsonManager
				.getInstance().createArrayJsonTask(IECardJsonTaskManager.MY_BUSINESS_LIST,CachePolicy.CachePolicy_TimeLimit, true, BusinessInfo.class);
		task.setListener(listener);
		task.setPosition(position);
		task.put("last", last);
		task.execute();
		return task;
	}

	/**
	 * 商家详情
	 */
	public void getBusinessDetail(int businessID, int shopId,
			IRequestResult<BusinessDetail> listener) {

		IDetailJsonTask<BusinessDetail> task =
				ECardJsonManager
				.getInstance().createDetailJsonTask(IECardJsonTaskManager.BUSINESS_DETAIL, CachePolicy.CachePolicy_TimeLimit,BusinessDetail.class);
		task.setId(businessID).setListener(listener);
		task.put("shop", shopId);
		ECardUserInfo userInfo = ECardJsonManager.getInstance().getUserInfo();
		if (userInfo != null) {
			task.put("userID", userInfo.getId());
		} else {
			task.put("userID", 0);
		}
		task.execute();
	}

	public static class RealBusData implements IJsonValueObject {
		public String name;
		public int line1;
		public int line2;
		public String time;

		@Override
		public void fromJson(JSONObject json) throws JSONException {

			name = json.getString("LINE_NAME");
			line1 = json.getInt("LINE_ID1");
			line2 = json.getInt("LINE_ID2");
			time = json.getString("LINE_TIME");

		}

	}

	private DoudouBusModel doudouBusModel;

	/**
	 * 实时公交数据
	 * 
	 * @param line
	 * @param start
	 * @param end
	 */
	public IDoudouBus getReadBusData(Context context, String line,
			String start, String end, IDoudouBusData listener) {
		if (doudouBusModel == null) {
			doudouBusModel = new DoudouBusModel(context);
		}
		doudouBusModel.setStartEnd(start, end);
		doudouBusModel.setListener(listener);
		IDetailJsonTask<RealBusData> task = ECardJsonManager
				.getInstance().createDetailJsonTask("get_real_bus", CachePolicy.CachePolity_Permanent,RealBusData.class);
		
		task.setId("line", line)
		.setListener(realIRequestResult)
		.execute();

		return doudouBusModel;
	}

	private IRequestResult<RealBusData> realIRequestResult = new IRequestResult<RealBusData>() {

		@Override
		public void onRequestError(String errMsg, boolean isNetworkError) {

			Alert.showShortToast(errMsg);

		}

		@Override
		public void onRequestSuccess(RealBusData result) {
			doudouBusModel.getRealData(result);
		}
	};

	/**
	 * 活动专区
	 */
	public void actionList(int position,
			IListRequsetResult<List<ActionListVo>> listener) {
		ECardJsonManager
				.getInstance()
				.createArrayJsonTask("action_list",
						CachePolicy.CachePolicy_TimeLimit, false,ActionListVo.class)
						.setListener(listener)
				.setPosition(position).execute();
	}

	public void getHeadAdvVo(int position,
			IListRequsetResult<List<HeadAdvVo>> listener) {
		ECardJsonManager
		.getInstance()
				.createArrayJsonTask("head_adv",
						CachePolicy.CachePolicy_TimeLimit, false, HeadAdvVo.class)
						.setListener(listener)
				.setPosition(position).execute();
	}

	public void registerCheck(String phone, IRequestResult<Integer> listener) {
		ECardJsonManager.getInstance()
				.createIntegerJsonTask("register_api/check")
				.setListener(listener)
				.put("phone", phone)
				.setWaitMessage("正在获取验证码……")
				.execute();
	}

	public void registerVerify(Context context, int verifyId, String pass,
			String code, IRequestResult<RegisterVerify> listener) {
		IObjectJsonTask<RegisterVerify> task = ECardJsonManager.getInstance()
				.createObjectJsonTask("register_api/register", CachePolicy.CachePolity_NoCache, RegisterVerify.class)
				.setListener(listener);
		task.put("verify_id", verifyId);//验证码id
		task.put("code", code);//用户输入的验证码
		task.put("platform", "android");
		task.put("app_version", PackageUtil.getVersionCode(JsonTaskManager.getApplicationContext()));
		task.put("pushID", getPushID(context));//推送id
		task.put("pass", pass);
		task.setWaitMessage("正在注册……");
		task.execute();

	}


	public String getPushID(Context context) {
		return Push.getPushId();
	}

	public void getMyECardList(IListRequsetResult<List<ECardInfo>> listener) {
		IArrayJsonTask<ECardInfo> task = ECardJsonManager.getInstance()
				.createArrayJsonTask("ecard_list2",
						CachePolicy.CachePolicy_TimeLimit, 
						true,ECardInfo.class).setListener(listener)
						.setPosition(LibConfig.StartPosition);
		task.execute();

	}
	
	/**
	 * 作用：检测手机号码是否存在,并发送验证码
	 * 参数：phone  手机号码
	 * 结果：int  验证码id
	 */
	public void cpVerify(String phone, IRequestResult<Integer> listener) {
		ECardJsonManager.getInstance()
				.createIntegerJsonTask("security_api/cp_verify")
				.setListener(listener)
				.put("phone", phone)
				.setWaitMessage("正在获取验证码……")
				.execute();
	}
	
	/**
	 * 作用：重置密码
	 * "code":验证码
	 * "verify_id":验证码id
	 * "pass":密码
	 */
	public void cpSubmit(int verifyId, String pass,
			String code, IRequestResult<Boolean> listener) {
//		IObjectJsonTask<RegisterVerify> task = ECardJsonManager.getInstance()
//				.createObjectJsonTask("security_api/cp_submit", CachePolicy.CachePolity_NoCache, RegisterVerify.class)
//				.setListener(listener);
//		task.put("verify_id", verifyId);//验证码id
//		task.put("code", code);//用户输入的验证码
//		task.put("pass", pass);
//		task.execute();
		ECardJsonManager.getInstance()
		.createBooleanJsonTask("security_api/cp_submit")
		.setListener(listener)
		.put("verify_id", verifyId)//验证码id
		.put("code", code)//用户输入的验证码
		.put("pass", pass)
		.setWaitMessage("正在设置密码……")
		.execute();

	}

}
