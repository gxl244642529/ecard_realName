package com.citywithincity.ecard.ui.activity;

import android.os.Bundle;
import android.view.View;

import com.citywithincity.activities.BaseActivity;
import com.citywithincity.ecard.R;
import com.citywithincity.ecard.models.PickCardModel;
import com.citywithincity.ecard.models.vos.LostCardDetailInfo;
import com.citywithincity.ecard.models.vos.PickCardDetailInfo;
import com.citywithincity.libs.LibConfig;
import com.citywithincity.models.OnItemClickListenerOpenActivity;
import com.citywithincity.mvc.ModelHelper;
import com.citywithincity.widget.StateListView;
import com.citywithincity.widget.TabPaneView;
import com.citywithincity.widget.TabPaneView.OnTabPaneViewSwitchListener;

public class GoodCardQueryActivity extends BaseActivity implements
		OnTabPaneViewSwitchListener {

	@Override
	protected void onSetContent(Bundle savedInstanceState) {
		setContentView(R.layout.activity_good_card_query);
		setTitle("拾卡失卡信息查询");

		TabPaneView tabPaneView = (TabPaneView) findViewById(R.id._tab_pane_view);
		tabPaneView.setListener(this);
		tabPaneView.setCurrent(0);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onTabPaneViewSwitch(View view, int index, boolean firstSwitch) {
		if (firstSwitch) {
			if (index == 0) {
				StateListView<PickCardDetailInfo> listView = (StateListView<PickCardDetailInfo>) view;
				listView.setItemRes(R.layout.item_pick_card);
				listView.setOnItemClickListener(new OnItemClickListenerOpenActivity<PickCardDetailInfo>(
						PickCardDetailActivity.class));

				listView.setTask(ModelHelper.getModel(PickCardModel.class)
						.getPickCardList(LibConfig.StartPosition));

			} else {
				StateListView<LostCardDetailInfo> listView = (StateListView<LostCardDetailInfo>) view;
				listView.setItemRes(R.layout.item_lost_card);
				listView.setOnItemClickListener(new OnItemClickListenerOpenActivity<LostCardDetailInfo>(
						LostCardDetailActivity.class));
				listView.setTask(ModelHelper.getModel(PickCardModel.class)
						.getLostCardList(LibConfig.StartPosition));

			}
		}

	}

}
