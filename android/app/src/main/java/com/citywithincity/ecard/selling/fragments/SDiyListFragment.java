package com.citywithincity.ecard.selling.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.citywithincity.auto.Observer;
import com.citywithincity.ecard.R;
import com.citywithincity.ecard.models.ECardJsonManager;
import com.citywithincity.ecard.selling.activities.SCartActivity;
import com.citywithincity.ecard.selling.activities.SDiyActivity;
import com.citywithincity.ecard.selling.activities.SDiyDetailActivity;
import com.citywithincity.ecard.selling.diy.models.DiyCard;
import com.citywithincity.ecard.selling.diy.models.dao.CardManager;
import com.citywithincity.ecard.selling.models.ShopModel;
import com.citywithincity.ecard.selling.models.vos.SDiyListVo;
import com.citywithincity.libs.LibConfig;
import com.citywithincity.models.OnItemClickListenerOpenActivity;
import com.citywithincity.mvc.ModelHelper;
import com.citywithincity.utils.ActivityUtil;
import com.citywithincity.widget.StateListView;
import com.citywithincity.widget.TabFragment;
import com.citywithincity.widget.TabPaneView;
import com.citywithincity.widget.TabPaneView.OnTabPaneViewSwitchListener;
import com.damai.core.DMAccount;

/**
 * diy草稿列表
 * 
 * @author randy
 * 
 */
@SuppressLint("InflateParams")
@Observer
public class SDiyListFragment extends TabFragment implements
		OnClickListener, OnItemClickListener, OnTabPaneViewSwitchListener {


	private DiyAdapter adapter;

	@Override
	public View onCreateView(LayoutInflater inflater) {
		return inflater.inflate(R.layout.s_activity_diy_list, null);
	}
	
	@Override
	protected void onInitFragment(Context context) {
		setTitle(R.string.diy2);
		TabPaneView tabPaneView = (TabPaneView)findViewById(R.id._tab_pane_view);
		tabPaneView.setListener(this);
		tabPaneView.setCurrent(0);

		findViewById(R.id._title_left).setOnClickListener(this);
		findViewById(R.id._title_right).setOnClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (position > 0) {
			DiyCard data = adapter.getItem(position);
			//
			Intent intent = new Intent(getActivity(), SDiyActivity.class);
			ModelHelper.getModel(CardManager.class).setCurrentData(data);
			getContext().startActivity(intent);
		} else {
			ModelHelper.getModel(CardManager.class).createNew();
			Intent intent = new Intent(getActivity(), SDiyActivity.class);
			getContext().startActivity(intent);
		}

		
	}



	@Override
	public void onResume() {
		if(adapter!=null){
			adapter.reload();
		}
	}

	@Override
	public void onDestroy() {
		ECardJsonManager.getInstance().onDestroy(getActivity());
		adapter = null;
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id._title_right:
			if (ECardJsonManager.getInstance().isLogin()) {
				ActivityUtil.startActivity(getActivity(), SCartActivity.class);
			} else {
				DMAccount.callLoginActivity(getActivity(),null);
			}
			break;
		case R.id._title_left:
			getActivity().finish();
			break;
		default:
			break;
		}

	}

	
	

	@Override
	public void onTabPaneViewSwitch(View view, int index, boolean firstSwitch) {
		if (firstSwitch) {
			if (index == 0) {
				
				GridView localGridView = (GridView) view;
				adapter = new DiyAdapter(getContext());

				localGridView.setAdapter(adapter);
				localGridView.setSelector(getActivity().getResources().getDrawable(
						R.drawable.s_item_click_bg));
				localGridView.setOnItemClickListener(this);
				adapter.reload();
			} else {
				@SuppressWarnings("unchecked")
				StateListView<SDiyListVo> listView = (StateListView<SDiyListVo>) view;
				listView.setItemRes(R.layout.s_item_diy_online);
//				listView.setSelector(getActivity().getResources().getDrawable(
//						R.drawable.s_item_click_bg));
				listView.setTask(ModelHelper.getModel(ShopModel.class).getSDiyList(LibConfig.StartPosition));
				listView.setOnItemClickListener(new OnItemClickListenerOpenActivity<SDiyListVo>(
						SDiyDetailActivity.class));
				listView.setSelector(R.drawable.item_selector);
				
			}
		}
	}




	
}
