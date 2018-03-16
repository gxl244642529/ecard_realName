package com.citywithincity.ecard.models;

import com.citywithincity.auto.ApiValue;

import java.util.Map;

public interface SystemModel {

	String FEEDBACK="feedback";
	
	@ApiValue(api=FEEDBACK,params={""})
	void feedback(Map<String, Object> content);
}
