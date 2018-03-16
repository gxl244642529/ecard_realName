package com.citywithincity.ecard.ui.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.citywithincity.ecard.R;
import com.citywithincity.ecard.interfaces.IECardJsonTaskManager;
import com.citywithincity.ecard.models.ECardJsonManager;
import com.citywithincity.ecard.models.ViewManager;

public class BaseFragment extends Fragment {
	protected IECardJsonTaskManager platform = ECardJsonManager.getInstance();
	protected Object _data;
	protected Activity _parent;
	
	private ViewManager viewManager;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        platform.onCreate(getActivity());
    }
	
	
	protected void pushView(BaseFragment fragment)
	{
		viewManager.pushView(fragment);
	}
	
	protected void pupupView(){
		viewManager.popupView();
	}
	
	public ViewManager getViewManager() {
		return viewManager;
	}

	public void setViewManager(ViewManager viewManager) {
		this.viewManager = viewManager;
	}
	
	protected void backButton(View view){
		//后退按钮
			view.findViewById(R.id._title_left).setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					pupupView();//.popBackStack();
				}
			});
	}
	protected void setTitle(View view,String title)
	{
		( (TextView)view.findViewById(R.id._title_text)).setText(title);
	}
	/**
	 * 如果在这里处理，那么返回true
	 */
	public boolean onBackpressed(){
		return false;
	}
	
	
	public void setData(Object data){
		_data = data;
	}

	@Override
	public void onDestroy() {
		platform.onDestroy(getActivity());
		viewManager=null;
		platform=null;
		super.onDestroy();
	}

	@Override
	public void onPause() {
		platform.onPause(getActivity());
		super.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();
		platform.onResume(getActivity());
	}



	@Override
	public void onAttach(Activity activity) {
		_parent=activity;
		super.onAttach(activity);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		
	}



	
}
