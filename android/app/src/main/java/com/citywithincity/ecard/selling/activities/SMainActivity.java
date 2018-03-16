package com.citywithincity.ecard.selling.activities;

import android.os.Bundle;

import com.citywithincity.activities.BaseActivity;
import com.citywithincity.ecard.DialogIds;
import com.citywithincity.ecard.R;
import com.citywithincity.ecard.selling.fragments.SCardListFragment;
import com.citywithincity.ecard.selling.fragments.SDiyListFragment;
import com.citywithincity.ecard.selling.fragments.SMineFragment;
import com.citywithincity.utils.Alert;
import com.citywithincity.utils.Alert.ICDCDialogListener;
import com.citywithincity.widget.TabHostView;
import com.citywithincity.widget.TabHostView.OnTabChangeListener;


/**
 * 主页，tab，商城首页/diy/预约/我的
 * 
 * 
 * @author Randy
 *
 */
public class SMainActivity extends BaseActivity implements OnTabChangeListener, ICDCDialogListener  {

	private TabHostView tabHost;
	
	@Override
	protected void onSetContent(Bundle savedInstanceState) {
 		setContentView(R.layout.default_tab_host);
 		tabHost = (TabHostView)findViewById(R.id._tab_host);
		tabHost.registerFragment(0, new SCardListFragment());
		tabHost.registerFragment(1, new SDiyListFragment());
		tabHost.registerFragment(3, new SMineFragment());
		tabHost.setCurrent(0);
		
		
		tabHost.setOnTabChangeListener(this);
	}
	
	@Override
	protected void onResume() {
		tabHost.onResume();
		super.onResume();
	}


	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onStop() {
		tabHost.onStop();
		super.onStop();
	}
	
	@Override
	protected void onDestroy() {
		tabHost = null;
		super.onDestroy();
	}

	private boolean first = true;
	@Override
	public void onTabChange(TabHostView view, int index, boolean fromUser) {
		if(index == 1){
			if(first){
				Alert.confirmCheck(getActivity(), DialogIds.DIY, 
						R.string.agree,
						"DIY卡用户协议", 
						R.layout.agreement_text_layout, this);
				first = false;
			}
			
		}
	}
	@Override
	public void onCDCDialogEvent(int buttonId, int dialogId) {
		if(buttonId == R.id._id_cancel){
			finish();
		}
	}
}
