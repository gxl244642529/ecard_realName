package com.citywithincity.models;

import java.util.List;

import android.app.Activity;
import android.text.format.DateUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.citywithincity.interfaces.IDestroyable;
import com.citywithincity.interfaces.IOnItemClickListener;
import com.citywithincity.libs.LibConfig;
import com.citywithincity.utils.MessageUtil;
import com.citywithincity.utils.MessageUtil.IMessageListener;
import com.damai.lib.R;
import com.handmark.pulltorefresh.library.PullToRefreshAdapterViewBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;

public class PullToRefreshListGroup<T, E extends AbsListView> extends
		ListGroup<T> implements IDestroyable, OnRefreshListener2<E>,
		IMessageListener, OnItemClickListener {

	private PullToRefreshAdapterViewBase<E> pullToRefreshView;
	private Activity parent;
	private Mode mode;
	private Mode currentMode;
	private IOnItemClickListener<T> listener;
	
	/**
	 * 
	 * @param content
	 * @param parent
	 * @param pullToRefreshAdapterViewBase
	 * @param itemViewResID   子项目layout id
	 * @param listener		监听
	 */
	public PullToRefreshListGroup(Activity content,
			ViewGroup parent,
			PullToRefreshAdapterViewBase<E> pullToRefreshAdapterViewBase,
			int itemViewResID,
			IListGroupListener<T> listener){
		super(content, parent,itemViewResID, listener);
		this.parent = content;
		this.pullToRefreshView =pullToRefreshAdapterViewBase;// (PullToRefreshAdapterViewBase<E>) parent.findViewById(R.id.list_view);
		pullToRefreshView.setOnRefreshListener(this);
		pullToRefreshView.setAdapter(this);
		mode = pullToRefreshView.getMode();
	}
	
	public PullToRefreshListGroup(Activity activity, int itemViewResID,
			PullToRefreshAdapterViewBase<E> adapterViewBase,
			IListGroupListener<T> listener) {
		super(activity, itemViewResID, listener);
		this.parent = activity;
		this.pullToRefreshView = adapterViewBase;
		pullToRefreshView.setOnRefreshListener(this);
		pullToRefreshView.setAdapter(this);
		mode = pullToRefreshView.getMode();
	}
	
	
	
	public PullToRefreshListGroup<T,E> setOnItemClickListener(IOnItemClickListener<T> listener){
		AbsListView listView = (AbsListView) pullToRefreshView.getRefreshableView();
		listView.setOnItemClickListener(this);
		this.listener = listener;
		return this;
	}
	
	public E getAdapterView() {
		return pullToRefreshView.getRefreshableView();
	}
	
	public PullToRefreshAdapterViewBase<E> getPullToRefreshView(){
		return pullToRefreshView;
	}
	
	@SuppressWarnings("unchecked")
	public PullToRefreshListGroup(Activity activity, int itemViewResID,
			IListGroupListener<T> listener) {
		this(activity, itemViewResID, (PullToRefreshAdapterViewBase<E>) activity.findViewById(R.id._list_view), listener);
	}
	

	@Override
	public void destroy() {
		parent = null;
		pullToRefreshView = null;
		mode = null;
		currentMode = null;
		listener = null;
		super.destroy();
	}

	public void config(boolean header, boolean footer) {
		if (!header && !footer)
			pullToRefreshView.setMode(Mode.DISABLED);
		else if (!header && footer) {
			pullToRefreshView.setMode(Mode.PULL_FROM_END);
		} else if (header && !footer) {
			pullToRefreshView.setMode(Mode.PULL_FROM_START);
		} else {
			pullToRefreshView.setMode(Mode.BOTH);
		}
		mode = pullToRefreshView.getMode();
	}

	@Override
	public void onRequestSuccess(List<T> result, boolean isLastPage) {
		super.onRequestSuccess(result, isLastPage);
		if (isLastPage) {
			if (mode == Mode.BOTH && pullToRefreshView.getMode() != Mode.PULL_FROM_START) {
				changeMode(Mode.PULL_FROM_START);
			}
		} else {
			if (mode == Mode.BOTH && pullToRefreshView.getMode() != Mode.BOTH) {
				changeMode(Mode.BOTH);
			}
		}
		onComplete();
	}

	@Override
	public void onRequestError(String errMsg, boolean isNetworkError) {
		super.onRequestError(errMsg, isNetworkError);
		onComplete();
	}

	public void onComplete() {
		pullToRefreshView.onRefreshComplete();
	}

	private void changeMode(Mode mode) {
		currentMode = mode;
		MessageUtil.sendMessage(0, this);
	}

	@Override
	public void onMessage(int message) {
		pullToRefreshView.setMode(currentMode);
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<E> refreshView) {
		setLabel();
		onLoadData(LibConfig.StartPosition);
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<E> refreshView) {
		setLabel();
		onLoadData(getCount() + LibConfig.StartPosition);
	}

	private void setLabel() {
		String label = DateUtils.formatDateTime(parent,
				System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
						| DateUtils.FORMAT_SHOW_DATE
						| DateUtils.FORMAT_ABBREV_ALL);
		pullToRefreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		@SuppressWarnings("unchecked")
		T info = (T) parent.getAdapter().getItem(position);
		this.listener.onItemClick(this.parent,info,position);
	}

}
