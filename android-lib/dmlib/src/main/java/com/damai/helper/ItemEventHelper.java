package com.damai.helper;

import com.citywithincity.interfaces.DialogListener;
import com.citywithincity.models.cache.CachePolicy;
import com.citywithincity.utils.Alert;
import com.damai.auto.LifeManager;
import com.damai.core.ApiJob;
import com.damai.core.DMLib;
import com.damai.lib.R;

public class ItemEventHelper {

	public static interface ItemEventSetter<T>{
		void setParam(ApiJob job,T data);
	}
	
	public static <T> void onItemEvent(final T data,final String api,String confirmMessage,final ItemEventSetter<T> listener){
		Alert.confirm(LifeManager.getActivity(), confirmMessage, new DialogListener() {
			@Override
			public void onDialogButton(int id) {
				if(id==R.id._id_ok){
					ApiJob job = DMLib.getJobManager().createObjectApi(api);
					job.setCachePolicy(CachePolicy.CachePolity_NoCache);
					job.setServer(1);
					job.setCancelable(false);
					listener.setParam(job, data);
					job.execute();
				}
			}
		});
		
	}
}
