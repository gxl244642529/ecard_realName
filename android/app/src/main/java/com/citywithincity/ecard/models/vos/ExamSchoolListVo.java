package com.citywithincity.ecard.models.vos;

import com.citywithincity.auto.Databind;
import com.citywithincity.auto.ViewId;
import com.citywithincity.ecard.R;
import com.citywithincity.interfaces.IJsonValueObject;

import org.json.JSONException;
import org.json.JSONObject;

@Databind
public class ExamSchoolListVo implements IJsonValueObject{
	
	@ViewId(id=R.id._text_view)
	public String name;
	public String code;

	@Override
	public void fromJson(JSONObject json) throws JSONException {
		name = json.getString("NAME");
		code = json.getString("CODE");
	}

}
