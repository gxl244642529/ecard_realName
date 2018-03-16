package com.citywithincity.ecard.insurance.models.vos;

import com.citywithincity.auto.Databind;
import com.citywithincity.auto.ViewId;
import com.citywithincity.ecard.R;
import com.citywithincity.interfaces.IJsonValueObject;
import com.citywithincity.utils.MemoryUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

@Databind
public class ContactVos implements Serializable,IJsonValueObject{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8389665243653035301L;
	
	//1- 父母 2-配偶 3-子女
		public static final int PARENTS = 1;
		public static final int SPOUSE = 2;
		public static final int CHILD = 3;
		public static final int NONE = 4;
		
		
		public long id;
		@ViewId(id=R.id.id_name)
		public String name;
		public long cmId;
		@ViewId(id=R.id.id_idcard)
		public String idCard;
		public int relation=NONE;
		
		public boolean selected;
		
		@Override
		public void fromJson(JSONObject json) throws JSONException {
			MemoryUtil.jsonToObject(json, this);
		}
		
//		@ViewId(id=R.id.id_idcard)
//		public String getIdCard() {
//			if (ValidateUtil.is18Idcard(idCard)) {
//				return idCard.substring(0, 4) + "**********" + idCard.substring(14);
//			} else {
//				return idCard;
//			}
//		}
		
		

}
