package com.citywithincity.models;

import android.app.Activity;
import android.widget.ScrollView;

import com.citywithincity.utils.MessageUtil;
import com.citywithincity.utils.MessageUtil.IMessageListener;
import com.damai.lib.R;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;

public class PullToRefreshDetailGroup<T,ID> extends DetailGroup<T> implements OnRefreshListener<ScrollView>, IMessageListener {
	private PullToRefreshScrollView scrollView;
	
	public PullToRefreshDetailGroup(Activity activity,IDetailGroupListener<T> listener) {
		super(activity,listener);
		/**
		 * id必须是
		 */
		scrollView = (PullToRefreshScrollView)activity.findViewById(R.id._scroll_view);
		if(scrollView!=null)
			scrollView.setOnRefreshListener(this);
	}

	@Override
	public void destroy() {
		scrollView = null;
		super.destroy();
	}

	@Override
	public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
		//重新加载页面
		refreshData();
	}

	@Override
	public void onRequestSuccess(T result)
	{
		MessageUtil.sendMessage(0, this);
		super.onRequestSuccess(result);
	}

	@Override
	public void onMessage(int message) {
		scrollView.onRefreshComplete();
	}
	
	

}
