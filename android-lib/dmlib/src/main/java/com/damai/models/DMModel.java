package com.damai.models;

import java.util.HashMap;
import java.util.Map;

import com.citywithincity.interfaces.IDestroyable;
import com.citywithincity.models.cache.CachePolicy;
import com.damai.core.ApiJob;
import com.damai.core.DMLib;

public class DMModel implements IDestroyable{

	
	private Map<String,ApiJob> map ;
	
	public DMModel(){
		map = new HashMap<String, ApiJob>();
	}
	
	protected ApiJob createJob(String api){
		if(map.containsKey(api)){
			ApiJob job= map.get(api);
			if(!job.isDestroied()) {
				return job;
			}
		}
		ApiJob job = DMLib.getJobManager().createObjectApi(api);
		job.setCachePolicy(CachePolicy.CachePolity_NoCache);
		map.put(api, job);

		return job;
		
	}

	@Override
	public void destroy() {
		if(map!=null){
			map.clear();
			map = null;
		}
	}
}
