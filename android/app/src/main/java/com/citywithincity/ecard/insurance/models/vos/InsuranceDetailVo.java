package com.citywithincity.ecard.insurance.models.vos;

import android.text.TextUtils;

import com.citywithincity.auto.Databind;
import com.citywithincity.auto.ViewId;
import com.citywithincity.ecard.R;
import com.citywithincity.ecard.insurance.models.InsuranceUtil;
import com.citywithincity.ecard.utils.DateTimeUtil_M;
import com.citywithincity.interfaces.IJsonValueObject;
import com.citywithincity.utils.MemoryUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;

@Databind
public class InsuranceDetailVo implements IJsonValueObject,Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
//	@ViewId(id=R.id.name)
	public String title;
	@ViewId(id=R.id.company)
	public String company;
	public String summary;
	public String[] summaries;
	@ViewId(id=R.id.id_image)
	public String background_url;
	public String safeguard;
	public String safeguard_url;
	public String duration;
	public String year_limit;
	public String price;
	public String protocol_title;
	public String protocol_url;
	public String insurance_id;
	public String notice_url;
	public String start_time;
	public String end_time;
	@ViewId(id=R.id.id_image)
	public String detail_url;
	public String service_tel;
	public String ori_price;
	
	public String ticket;
	public int count = 1;//:购买份数
	
	public String typeId;
	
	public int pid_sales_limit;
	
//	"title":"e通卡随心保（6个月）",
//	"company":"PICC",
//	"summary":"小小1元，放心用卡^手续简便，轻松补卡",
//	"background_url":"\/uploads\/prod_bg\/1.jpg",
//	"detail_url":"\/uploads\/prod_bg\/5.jpg",
//	"safeguard_url":"\/article\/show\/4",
//	"safeguard":"产品详情",
//	"service_tel":"0592-95518",
//	"duration":"4",
//	"year_limit":"无",
//	"sales_limit":"0",
//	"pid_sales_limit":"2",
//	"ori_price":"2.00",
//	"price":"1.00",
//	"start_time":"2016-01-22 00:00:00",
//	"end_time":"2016-07-21 23:59:59",
//	"protocol_title":"",
//	"protocol_url":"\/article\/show\/8",
//	"notice_url":"\/article\/show\/6"

	@Override
	public void fromJson(JSONObject json) throws JSONException {
		MemoryUtil.jsonToObject(json, this);
		
		if (json.has("background_url")) {
			background_url = InsuranceUtil.setUrl(background_url);
		}
		if (json.has("safeguard_url")) {
			safeguard_url = InsuranceUtil.setUrl(safeguard_url);
		}
		if (json.has("protocol_url")) {
			protocol_url = InsuranceUtil.setUrl(protocol_url);
		}
		if (json.has("notice_url")) {
			notice_url = InsuranceUtil.setUrl(notice_url);
		}
		if (json.has("detail_url")) {
			detail_url = InsuranceUtil.setUrl(detail_url);
		}
		
		if ((json.get("pid_sales_limit") instanceof String)) {
			pid_sales_limit = Integer.parseInt(json.getString("pid_sales_limit"));
		}
		
		if (!TextUtils.isEmpty(summary)) {
			int index = summary.indexOf("^");
			summaries = new String[2];
			summaries[0] = summary.substring(0, index);
			summaries[1] = summary.substring(++index, summary.length());
		}
		
		if (TextUtils.isEmpty(protocol_title)) {
			protocol_title = "《保险条款》";
		}
	}
	
	@ViewId(id=R.id.date_starting)
	public String getStartTime() {
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
	
//	
//	@ViewId(id=R.id.price)
//	public String setPrice(){
//		return price;
//	}
//	
}
