package com.citywithincity.ecard.insurance.models.vos;

import android.text.TextUtils;

import com.citywithincity.auto.Databind;
import com.citywithincity.auto.ViewId;
import com.citywithincity.ecard.R;
import com.citywithincity.ecard.insurance.models.InsuranceUtil;
import com.citywithincity.ecard.utils.DateTimeUtil_M;
import com.citywithincity.interfaces.IJsonValueObject;
import com.citywithincity.utils.MemoryUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Databind
public class InsurancePolicyVo implements IJsonValueObject, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String id;
	@ViewId(id = R.id.name)
	public String product_name;
	@ViewId(id=R.id.company)
	public String company;
	public String product_price;
	@ViewId(id=R.id.order_id)
	public String order_id;
	public String company_icon;
	public String safeguard_url;
	public String safeguard;
	public String start_time;
	public String end_time;
	public int status;
	@ViewId(id = R.id.id_status)
	public String statusName;
	public String status_msg;
	@ViewId(id = R.id.ecard_card_number)
	public String e_card_id;
	public String protocol_url;
	public String notice_url;
	public String background_url;
	@ViewId(id = R.id.thumb)
	public String thumb_url;
	public String summary;
	public String service_tel;
	public String protocol_title;
	public String[] summaries;
	public String insurance_id;
	public String claim_result;//理赔结果
								// 1 - 赔付e通卡一张，理赔金额记录在claim_amt中
								// 2 - 赔付e通卡一张，金额记在claim_amt中，为0  
	public String claim_amt;//理赔金额
	
	public String name;
	public String addr;
	public String phone;
	public String id_card;
	
	@ViewId(id = R.id.id_image)
	public String detail_url;
	
	public String sample_url;
	
	public String category_code;
	
	public String policy_url;
	
	public String qty;
	
	public Applicant applicants;
	public List<Insurant> insurants;
	
	public String picc_policy_no;
	
	@ViewId(id=R.id.date_starting)
	public String setStartTime() {
		try {
			return DateTimeUtil_M.convertFormat(start_time, "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	@ViewId(id=R.id.date_expiring)
	public String setEndTime() {
		try {
			return DateTimeUtil_M.convertFormat(end_time, "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	// oder_status: 0保障中， 1，2 理赔中 3 已理赔 4 已过期 ，-1信息错误 5已退卡

	@Override
	public void fromJson(JSONObject json) throws JSONException {
		MemoryUtil.jsonToObject(json, this);
		status = json.getInt("status");
		
		if (!(json.get("product_price") instanceof String)) {
			 product_price = String.valueOf(json.getDouble("product_price"));
//			 product_price = String.format("%s.00", product_price);
		}
		
		company_icon = InsuranceUtil.setUrl(company_icon);
		safeguard_url = InsuranceUtil.setUrl(safeguard_url);
		protocol_url = InsuranceUtil.setUrl(protocol_url);
		background_url = InsuranceUtil.setUrl(background_url);
		thumb_url = InsuranceUtil.setUrl(thumb_url);
		notice_url = InsuranceUtil.setUrl(notice_url);
		detail_url = InsuranceUtil.setUrl(detail_url);
		sample_url = InsuranceUtil.setUrl(sample_url);
		
		if (!TextUtils.isEmpty(summary)) {
			int index = summary.indexOf("^");
			summaries = new String[2];
			summaries[0] = summary.substring(0, index);
			summaries[1] = summary.substring(++index, summary.length());
		}
		if (TextUtils.isEmpty(protocol_title)) {
			protocol_title = "《保险条款》";
		}
		
//		if (!json.isNull("policy_url")) {
//			policy_url = InsuranceUtil.setUrl(policy_url);
//		}
		
		switch (status) {
		case 0:
			statusName = "保障中";
			break;
		case 1:
			statusName = "理赔中";
			break;
		case 2:
			statusName = "理赔中";
			break;
		case 3:
			statusName = "已理赔";
			break;
		case 4:
			statusName = "已过期";
			break;
		case 5:
			statusName = "已退/换卡";
			break;
		case 6:
			statusName = "待生效";
			break;
		case 7:
			statusName = "未付款";
			break;
		case -1:
			statusName = "保单提交失败";
			break;

		default:
			statusName = "保障中";
			break;
		}
		
		if (json.has("applicant") && !json.isNull("applicant")) {
			applicants = new Applicant();
			JSONObject jsonObject = json.getJSONObject("applicant");
			applicants.fromJson(jsonObject);
		}
		
		if (json.has("insurant") && !json.isNull("insurant")) {
			JSONArray array = json.getJSONArray("insurant");
			insurants = new ArrayList<Insurant>();
			for (int i = 0; i < array.length(); i++) {
				Insurant data = new Insurant();
				data.fromJson(array.getJSONObject(i));
				insurants.add(data);
			}
		}
		
		float unitPrice = Float.parseFloat(product_price);
		if (json.has("qty")) {
			count = Integer.parseInt(qty);
		}
		
	}
	
	int count;
	
	@ViewId(id = R.id.price)
	public String getPrice() {
		float unitPrice = Float.parseFloat(product_price);
		if (count == 0) {
			return product_price;
		} else {
			return String.format("¥%.2f", unitPrice*count);
		}
	}
	
//	{"id":"113",
//		"product_name":"卡全额保",
//		"company":"PICC",
//		"product_price":"2.50",
//		"order_id":"20151117145843197",
//		"insurance_id":"5433",
//		"company_icon":"http:\/\/www.xm95518.com:8989\/uploads\/company_icon\/picc.gif",
//		"safeguard_url":"http:\/\/www.xm95518.com:8989\/article\/show\/5",
//		"protocol_url":"http:\/\/www.xm95518.com:8989\/article\/show\/10",
//		"notice_url":"http:\/\/www.xm95518.com:8989\/article\/show\/7",
//		"background_url":"http:\/\/www.xm95518.com:8989\/uploads\/prod_bg\/2.jpg",
//		"thumb_url":"http:\/\/www.xm95518.com:8989\/uploads\/others\/thumb2.png",
//		"safeguard":"投保详情",
//		"summary":"轻松一点，保障升级^自动理赔，随心充值",
//		"service_tel":"0592-5195866",
//		"start_time":"2015-11-17 14:58:43",
//		"end_time":"2015-11-20 14:58:42","status":"1","e_card_id":"9803461382"}
	
	
	public static class Applicant implements IJsonValueObject,Serializable{
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		public String policy_id;
		public String BUYER_NAME;
		public String BUYER_ID;
		public String BUYER_MOBILE;
		public String BUY_HOME_ADDR;
		public String CAR_NO;
		public String VIN;
		public String identical;
		public String create_time;
		
	@Override
	public void fromJson(JSONObject json) throws JSONException {
		policy_id = json.getString("policy_id");
		BUYER_NAME = json.getString("BUYER_NAME");
		BUYER_ID = json.getString("BUYER_ID");
		BUYER_MOBILE = json.getString("BUYER_MOBILE");
		BUY_HOME_ADDR = json.getString("BUY_HOME_ADDR");
		CAR_NO = json.getString("CAR_NO");
		VIN = json.getString("VIN");
		identical = json.getString("identical");
		policy_id = json.getString("policy_id");
		create_time = json.getString("create_time");
		
	}
		
	}
	
//	"applicant":{
//		"policy_id":"388",
//		"BUYER_NAME":"喜羊羊",
//		"BUYER_ID":"369852199108080809",
//		"BUYER_MOBILE":"15375872584",
//		"BUY_HOME_ADDR":"福建省厦门市思明区软件园二期",
//		"CAR_NO":null,
//		"VIN":null,
//		"identical":"0",
//		"create_time":"1456738716"},
//		
//		"insurant":[
//		            {"id":"7","policy_id":"388","insurant_name":"嘻嘻嘻","insurant_pid":"369874199108080807",
//		            	"relationship":"2","create_time":"1456738716"},
//		            	
//		            	{"id":"8","policy_id":"388","insurant_name":"发发发","insurant_pid":"258963201508080808",
//		            		"relationship":"3","create_time":"1456738716"}]}

	public static class Insurant implements IJsonValueObject,Serializable{
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		public String id;
		public String policy_id;
		public String insurant_name;
		public String insurant_pid;
		public String relationship;
		public String create_time;
		
		@Override
		public void fromJson(JSONObject json) throws JSONException {
			MemoryUtil.jsonToObject(json, Insurant.this);
		}
		
	}
	
}
