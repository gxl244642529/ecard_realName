package com.citywithincity.ecard.models.vos;

import android.text.TextUtils;

import com.citywithincity.auto.Databind;
import com.citywithincity.auto.ViewId;
import com.citywithincity.ecard.R;
import com.citywithincity.ecard.utils.JsonUtil;
import com.citywithincity.interfaces.IJsonValueObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;


/**
 * e通卡列表信息
 * @author randy
 *
 */
@Databind
public class ECardInfo implements IJsonValueObject,Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5780699331310346001L;

	@ViewId(id=R.id.my_ecard_id)
	public String id;//卡号
	
	@ViewId(id=R.id.my_ecard_remark)
	public String name;//备注名字
	
	@ViewId(id=R.id.ecard_card_type)
	public String type;//e通卡类型
	
	@ViewId(id=R.id.ecard_card_create)
	public String expireTime;//有效期
	
	public int bindTime;//绑定时间
	
	public String barCode;
	
	
	public String cmName;
	
	public String cmPhone;
	
	public int cmSex;
	
	public String cmIdCode;
	
	public int state;
	
	public String cmAddress;
	
	@ViewId(id=R.id._title_text)
	public String getLabel(){
		return TextUtils.isEmpty(name) ? id : name;
	}
	
	@Override
	public void fromJson(JSONObject obj) throws JSONException {
		id = obj.getString("CARDID");
		name = obj.getString("CARD_NAME");
		if (name == null || name.equals("null")) {
			name = "";
		}
		type = obj.getString("CARD_TYPE");
		expireTime = obj.getString("EXPIRE_TIME");
		if(obj.has("BIND_TIME")){
			bindTime = obj.getInt("BIND_TIME");
		}
		
		if (obj.has("CI_CODE")) {
			barCode=obj.getString("CI_CODE");
			
			if(barCode.equals("null")){
				barCode="";
			}
			
			if(!TextUtils.isEmpty(barCode)){
				
				cmName = JsonUtil.getString(obj, "NAME");
				cmSex = JsonUtil.getInt(obj,"SEX");
				cmPhone = JsonUtil.getString(obj,"PHONE");
				cmIdCode =JsonUtil.getString(obj,"IDCODE");
				state = obj.getInt("STATE");
				cmAddress = JsonUtil.getString(obj,"ADDR");
			}
		}
		
	}
	

	
	
	
	
}
