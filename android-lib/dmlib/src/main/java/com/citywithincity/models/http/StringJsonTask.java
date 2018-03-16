package com.citywithincity.models.http;


import com.damai.core.ApiJob;

class StringJsonTask extends AbsValueJsonTask<String> {
	
	public StringJsonTask(ApiJob apiJob){
		super(apiJob);
	}

	@Override
	protected String onParserParseResult(Object data) {
		return String.valueOf(data);
	}

/*
	@Override
	protected void onParserParseResult(Object result) {
		this.data = String.valueOf(result);
	}
*/
}
