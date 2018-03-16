package com.citywithincity.ecard.models;

import com.citywithincity.interfaces.IJsonTaskListener;
import com.citywithincity.models.vos.AddressVo;

import java.util.List;

public class DistanceParser<T extends AddressVo> implements IJsonTaskListener<T> {

	@Override
	public Object beforeParseData() {
		
		return null;
	}

	@Override
	public void afterParseData(List<T> data, Object result) {
		
	}

	
	
}
