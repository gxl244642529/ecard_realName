package com.citywithincity.ecard.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.citywithincity.activities.BaseActivity;
import com.citywithincity.ecard.R;
import com.citywithincity.ecard.models.ECardJsonManager;
import com.citywithincity.ecard.models.ECardModel;
import com.citywithincity.ecard.models.ListGridAdapter;
import com.citywithincity.ecard.models.vos.BusinessInfo;
import com.citywithincity.interfaces.IListDataProviderListener;
import com.citywithincity.interfaces.IListRequsetResult;
import com.citywithincity.interfaces.IOnItemClickListener;
import com.citywithincity.libs.LibConfig;
import com.citywithincity.utils.ActivityUtil;
import com.citywithincity.utils.MessageUtil;
import com.citywithincity.utils.MessageUtil.IMessageListener;
import com.citywithincity.utils.SizeUtil;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.List;

public class BusinessMainActivity extends BaseActivity implements
		IListDataProviderListener<BusinessInfo>, OnClickListener,
		OnRefreshListener2<ListView>, IListRequsetResult<List<BusinessInfo>>,
		IMessageListener, IOnItemClickListener<BusinessInfo> {

	private ListGridAdapter<BusinessInfo> adapter;
	private int currentPosition;
	PullToRefreshListView listView;
	private Mode mode;
	private Mode currentMode;

	private int position = 0;
	private String selectData = "";
	private View loadingView;

	@Override
	protected void onSetContent(Bundle savedInstanceState) {
		setContentView(R.layout.business_main);
		setTitle("联盟商家");
		listView = (PullToRefreshListView) findViewById(R.id._list_view);
		View header = getLayoutInflater().inflate(
				R.layout.business_main_header, null);

		header.findViewById(R.id.type_yi).setOnClickListener(this);
		header.findViewById(R.id.type_shi).setOnClickListener(this);
		header.findViewById(R.id.type_zhu).setOnClickListener(this);
		header.findViewById(R.id.type_xing).setOnClickListener(this);
		header.findViewById(R.id.type_gou).setOnClickListener(this);
		header.findViewById(R.id.type_yu).setOnClickListener(this);
		header.findViewById(R.id.type_all).setOnClickListener(this);
		header.findViewById(R.id.type_zhoubian).setOnClickListener(this);
		listView.getRefreshableView().addHeaderView(header);
		adapter = new ListGridAdapter<BusinessInfo>(3, getLayoutInflater(),
				R.layout.business_grid_item, this);
		listView.setAdapter(adapter);

		listView.setOnRefreshListener(this);
		mode = listView.getMode();
		loadData(LibConfig.StartPosition);
		adapter.setOnItemClickListener(this);
		
		mode = Mode.BOTH;
		
		loadingView = findViewById(R.id._load_state_loading);
		Point pt = SizeUtil.measureView(header);
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		lp.topMargin = pt.y;
		loadingView.setVisibility(View.VISIBLE);
		loadingView.setLayoutParams(lp);
		
		// ECardModel.getInstance().getBusinessList(position, type, distance,
		// listener)
	}

	private void changeMode(Mode mode) {
		currentMode = mode;
		MessageUtil.sendMessage(0, this);
	}

	protected void loadData(int position) {
		currentPosition = position;
		ECardModel.getInstance().getBusinessList(position, "", 0, this);
	}

	@Override
	public void onInitializeView(View view, BusinessInfo data, int position) {
		ImageView imageView = (ImageView) view
				.findViewById(R.id.business_image);
		ECardJsonManager.getInstance().setImageSrc(data.img, imageView);
		TextView titleTextView = (TextView) view
				.findViewById(R.id.business_title);
		titleTextView.setText(data.title);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.type_yi:
			position = 0;
			selectData = "10";
			break;
		case R.id.type_shi:
			position = 0;
			selectData = "11";
			break;
		case R.id.type_zhu:
			position = 0;
			selectData = "12";
			break;
		case R.id.type_xing:
			position = 0;
			selectData = "13";
			break;
		case R.id.type_gou:
			position = 0;
			selectData = "14";
			break;
		case R.id.type_yu:
			position = 0;
			selectData = "15";
			break;
		case R.id.type_all:
			position = 0;
			selectData = "";
			break;
		case R.id.type_zhoubian:
			position = 3000;
			selectData = "";
			break;
		default:
			break;
		}
		Intent intent = new Intent(this,BusinessActivity.class);
		intent.putExtra("distance", position);
		intent.putExtra("defaultData", selectData);
		startActivity(intent);
	}

	@Override
	public void onRequestSuccess(List<BusinessInfo> result, boolean isLastPage) {
		adapter.setData(result, currentPosition > LibConfig.StartPosition);
		if (isLastPage) {
			if (mode == Mode.BOTH && listView.getMode() != Mode.PULL_FROM_START) {
				changeMode(Mode.PULL_FROM_START);
			}
		} else {
			if (mode == Mode.BOTH && listView.getMode() != Mode.BOTH) {
				changeMode(Mode.BOTH);
			}
		}
		loadingView.setVisibility(View.GONE);
		listView.onRefreshComplete();
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		setLabel();
		loadData(LibConfig.StartPosition);
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		setLabel();
		loadData(adapter.getTotal() + LibConfig.StartPosition);
	}

	private void setLabel() {
		String label = DateUtils.formatDateTime(this,
				System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
						| DateUtils.FORMAT_SHOW_DATE
						| DateUtils.FORMAT_ABBREV_ALL);
		listView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
	}

	@Override
	public void onRequestError(String errMsg, boolean isNetworkError) {

	}

	@Override
	public void onMessage(int message) {
		listView.setMode(currentMode);
	}

	@Override
	protected void onDestroy() {
		adapter.destroy();
		adapter = null;
		super.onDestroy();
	}

	@Override
	public void onItemClick(Activity activity,BusinessInfo data, int position) {
		ActivityUtil.startActivity(this, BusinessDetailActivity.class, data);
	}

}
