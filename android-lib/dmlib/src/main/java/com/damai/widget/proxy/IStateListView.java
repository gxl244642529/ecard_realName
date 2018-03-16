package com.damai.widget.proxy;

import android.view.View;
import android.widget.AbsListView;

import com.citywithincity.interfaces.IOnItemClickListener;
import com.damai.core.ApiJob;

public interface IStateListView<T> extends IWidgetProxy {
	void refreshWithState();
	void layout();
	View getHeaderView();
	ApiJob getJob();
	void setOnItemClickListener(IOnItemClickListener<T> listener);
	@SuppressWarnings("hiding")
	<T extends AbsListView> T getAdapterView();
}
