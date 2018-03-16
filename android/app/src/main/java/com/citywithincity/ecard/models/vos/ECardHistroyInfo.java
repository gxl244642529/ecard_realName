package com.citywithincity.ecard.models.vos;

import com.citywithincity.ecard.utils.JsonUtil;
import com.citywithincity.interfaces.IJsonValueObject;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 历史记录
 * @author randy
 *
 */
public class ECardHistroyInfo implements IJsonValueObject{
	public String hisTime;//时间
	public String value;//金额大小
	public String left;//余额
	public String address;//地址
	public String shopName;//商户名称
	public int shopID;//商户id ,如果是0，则不可查看详细情况
	//public boolean add;//添加或减少
	public String type;
	@Override
	public void fromJson(JSONObject obj) throws JSONException {
		address = obj.getString("ADDRESS");
		shopName = obj.getString("SHP_NAME");
		left = obj.getString("LEFT");
		shopID = 0;// obj.getInt("SHP_ID");
		hisTime = JsonUtil.parseTime(obj.getString("TH_TIME"));
		value = String.format("%.2f",
				Double.valueOf(obj.getString("TH_VALUE")));
		type = obj.getString("LX_MC");
		
	}
}
