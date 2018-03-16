package com.citywithincity.models.http;


import com.damai.core.ApiJob;

public class IntegerJsonTask extends AbsValueJsonTask<Integer> {

	protected IntegerJsonTask(ApiJob apiJob){
		super(apiJob);
	}

	@Override
	protected Integer onParserParseResult(Object result) {
		if(result instanceof String)
		{
			return  Integer.valueOf((String)result);
		}else if(result instanceof Integer){

			return (Integer)result;
		}else if(result instanceof Boolean){
			//boolean
			Boolean r = (Boolean)result;
			if(r){
				return   1;
			}else{
				return  0;
			}
		}else{
			throw new Error("格式错误");
		}
	}

	/*
	@Override
	protected void onParserParseResult(Object result) {

	}*/

}
