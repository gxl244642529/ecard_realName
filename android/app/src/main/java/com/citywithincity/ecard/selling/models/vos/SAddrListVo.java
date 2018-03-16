package com.citywithincity.ecard.selling.models.vos;

import android.view.View;

import com.citywithincity.auto.Databind;
import com.citywithincity.auto.ViewId;
import com.citywithincity.ecard.R;
import com.citywithincity.interfaces.IJsonValueObject;
import com.citywithincity.models.vos.IdObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * 地址列表
 * 
 */
@Databind
public class SAddrListVo extends IdObject implements IJsonValueObject, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	/**
	 * 省
	 */
	public int sheng_id;
	/**
	 * 市
	 */
	public int shi_id;
	/**
	 * 区
	 */
	public int qu_id;
	/**
	 * 省
	 */
	public String sheng;
	/**
	 * 市
	 */
	public String shi;
	/**
	 * 区
	 */
	public String qu;
	/**
	 * 详细地址
	 */
	@ViewId(id=R.id.id_street)
	public String jie;
	/**
	 * 邮编
	 */
	@ViewId(id=R.id.id_zipcode)
	public String pc;
	/**
	 * 是否是默认地址
	 */
	@ViewId(id=R.id.id_default)
	public int def;
	/**
	 * 姓名
	 */
	@ViewId(id=R.id.id_name)
	public String name;
	/**
	 * null
	 */
	@ViewId(id=R.id.id_phone)
	public String phone;
	
	

	@Override
	public void fromJson(JSONObject json) throws JSONException {
		sheng_id = json.getInt("SHENG_ID");
		shi_id = json.getInt("SHI_ID");
		qu_id = json.getInt("QU_ID");
		sheng = json.getString("SHENG");
		shi = json.getString("SHI");
		qu = json.getString("QU");
		jie = json.getString("JIE");
		pc = json.getString("PC");
		def = json.getInt("DEF");
		name = json.getString("NAME");
		phone = json.getString("PHONE");
		id = json.getInt("ID");
	}
	
	
	/**
	 * 省市区
	 * @return
	 */
	@ViewId(id=R.id.id_area)
	public String getArea(){
		if(qu_id>0){
			return String.format("%s%s%s", sheng,shi,qu);
		}else{
			return String.format("%s%s", sheng,shi);
		}
	}
	
	/**
	 * 获取详细地址
	 * @return
	 */
	@ViewId(id=R.id.address)
	public String getAddr(){
		if(qu_id>0){
			return String.format("%s%s%s%s", sheng,shi,qu,jie);
		}else{
			return String.format("%s%s%s", sheng,shi,jie);
		}
	}
	
	@ViewId(id=R.id.address_def,type=ViewId.TYPE_VISIBILITY)
	public int getDefVisiable() {
		if (def == 1) {
			return View.VISIBLE;
		}
		return View.GONE;
	}
}
