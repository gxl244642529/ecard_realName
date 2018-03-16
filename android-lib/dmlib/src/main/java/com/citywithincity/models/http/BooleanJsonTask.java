package com.citywithincity.models.http;


import com.damai.core.ApiJob;

class BooleanJsonTask extends AbsValueJsonTask<Boolean> {
	
	
	protected BooleanJsonTask(ApiJob apiJob){
		super(apiJob);
	}

	@Override
	protected Boolean onParserParseResult(Object result) {
		if(result==null){
			return false;
		}
		if(result instanceof String)
		{
			return (Boolean.valueOf((String)result));
		}else if(result instanceof Boolean){
			return ((Boolean)result);
		}else{
			return ((Integer)result!=0);
		}
	}
	

/*
	@Override
	protected void onParserParseResult(Object result) {

		
	}*/

}
