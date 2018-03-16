package com.damai.helper;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class OnItemClickActivity implements OnItemClickListener {

	private final Class<? extends  Activity> clazz;
	public OnItemClickActivity(Class<? > clazz){
		this.clazz = (Class<? extends Activity>) clazz;
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Activity activity = (Activity)view.getContext();
		Object data = parent.getAdapter().getItem(position);
		DataHolder.set(clazz,data);
		activity.startActivity(new Intent(activity,clazz));
	}
}
