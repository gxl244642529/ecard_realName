package com.citywithincity.ecard.ui.activity;

import android.os.Bundle;
import android.view.View;

import com.citywithincity.activities.BaseActivity;
import com.citywithincity.ecard.R;
import com.citywithincity.ecard.models.ECardModel;
import com.citywithincity.ecard.models.vos.BusinessInfo;
import com.citywithincity.ecard.selling.models.vos.SCardListVo;
import com.citywithincity.interfaces.IArrayJsonTask;
import com.citywithincity.libs.LibConfig;
import com.citywithincity.models.ViewPagerAdapter.OnViewPagerCreateLiserner;
import com.citywithincity.models.cache.CachePolicy;
import com.citywithincity.models.http.JsonTaskManager;
import com.citywithincity.widget.StateListView;
import com.citywithincity.widget.TabPaneView;
import com.citywithincity.widget.TabPaneView.OnTabPaneViewSwitchListener;

public class MyCollectionActivity extends BaseActivity implements OnTabPaneViewSwitchListener, OnViewPagerCreateLiserner {
	
	private TabPaneView _tabPaneView;

	@Override
	protected void onSetContent(Bundle savedInstanceState) {
		setContentView(R.layout.activity_new_my_collection);
		_tabPaneView = (TabPaneView) findViewById(R.id._tab_pane_view);
		_tabPaneView.setListener(this);
		_tabPaneView.setCurrent(0);
		_tabPaneView.setOnViewPagerCreateLiserner(this);
	}
	
	@Override
	public void onViewCreated(View view, int position) {
		
	}

	@Override
	public void onTabPaneViewSwitch(View view, int index, boolean firstSwitch) {
		if(firstSwitch){
			if(index==0){
				@SuppressWarnings("unchecked")
				StateListView<BusinessInfo> stateListView = (StateListView<BusinessInfo>) view;
				stateListView.setItemRes( R.layout.business_collection_grid_item);
				stateListView.setTask(ECardModel.getInstance().getMyBusinessList(LibConfig.StartPosition, 0, null));
			}else{
				@SuppressWarnings("unchecked")
				StateListView<SCardListVo> stateListView = (StateListView<SCardListVo>) view;
				stateListView.setItemRes( R.layout.cardlist_grid_item);
				IArrayJsonTask<SCardListVo> task = JsonTaskManager.getInstance().createArrayJsonTask("s_fav_list", 
						CachePolicy.CachePolity_NoCache, true);
				stateListView.setTask(task);
				task.execute();
			}
		}
	}
	
}
