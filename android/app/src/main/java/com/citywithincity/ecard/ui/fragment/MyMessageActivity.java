package com.citywithincity.ecard.ui.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.citywithincity.activities.BaseActivity;
import com.citywithincity.ecard.PushHandler;
import com.citywithincity.ecard.R;
import com.citywithincity.ecard.models.PushMessageDataProvider;
import com.citywithincity.ecard.models.PushMessageDataProvider.PushInfo;
import com.citywithincity.interfaces.IListDataProviderListener;
import com.citywithincity.interfaces.IListRequsetResult;
import com.citywithincity.models.ListDataProvider;
import com.citywithincity.models.LoadingState;

import java.util.List;

public class MyMessageActivity extends BaseActivity implements IListRequsetResult<List<PushInfo>>, IListDataProviderListener<PushInfo>, OnItemClickListener {

	private PushMessageDataProvider dataProvider;
	private ListDataProvider<PushInfo> listDataProvider;
	private LoadingState loadingState;
	
	
	
	@Override
	protected void onDestroy() {
		dataProvider.destroy();
		dataProvider=null;
		listDataProvider.destroy();
		listDataProvider=null;
		loadingState.destroy();
		loadingState = null;
		super.onDestroy();
	}

	@Override
	public void onRequestSuccess(List<PushInfo> result, boolean isLastPage) {
		loadingState.onSuccess(result.size()>0);
		listDataProvider.setData(result, false);
		
	}

	@Override
	public void onRequestError(String errMsg, boolean isNetworkError) {
	
	}

	@Override
	public void onInitializeView(View view, PushInfo data, int position) {
		((TextView)view.findViewById(R.id.push_title)).setText(data.title);
		((TextView)view.findViewById(R.id.push_description)).setText(data.description);
		((TextView)view.findViewById(R.id.push_time)).setText(data.time);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		PushInfo pushInfo = (PushInfo)arg0.getAdapter().getItem(arg2);
		PushHandler.onClickCustomContent(this, pushInfo.time, pushInfo.description, pushInfo.customContent);
	}

	@Override
	protected void onSetContent(Bundle savedInstanceState) {
		setContentView(R.layout.my_message_layout);
		setTitle("我的消息");
		dataProvider = new PushMessageDataProvider(this,this);
		listDataProvider = new ListDataProvider<PushInfo>(this, R.layout.item_push_message, this);
		loadingState = new LoadingState(this);
		ListView listView = (ListView)findViewById(R.id._list_view);
		listView.setAdapter(listDataProvider);
		listView.setOnItemClickListener(this);
		dataProvider.getMyPushList();
	}
}
