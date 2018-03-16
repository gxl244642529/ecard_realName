package com.citywithincity.widget;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.text.format.DateUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;

import com.citywithincity.interfaces.IArrayJsonTask;
import com.citywithincity.interfaces.IListDataProviderListener;
import com.citywithincity.interfaces.IListRequsetResult;
import com.citywithincity.interfaces.IListTask;
import com.citywithincity.interfaces.IOnItemClickListener;
import com.citywithincity.libs.LibConfig;
import com.citywithincity.models.ListDataProvider;
import com.citywithincity.models.OnItemClickListenerOpenActivity;
import com.citywithincity.utils.MessageUtil;
import com.citywithincity.utils.MessageUtil.IMessageListener;
import com.damai.lib.R;
import com.handmark.pulltorefresh.library.PullToRefreshAdapterViewBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshGridHeaderView;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

/**
 * 支持gridview、listview，上拉下拉、state等
 * @author Randy
 *
 */
public class StateListView<T> extends RelativeLayout implements OnItemClickListener, OnRefreshListener2, IListRequsetResult<List<T>>, IMessageListener {

	public static final int TYPE_LIST = 1;
	public static final int TYPE_GRID = 2;
	public static final int TYPE_GRID_WITH_HEADER = 3;
	
	/**
	 * 点击事件
	 */
	private IOnItemClickListener<T> listener;
	
	/**
	 * listview
	 */
	private PullToRefreshAdapterViewBase<?> listView;
	
	/**
	 * 状态支持
	 */
	private StateHandler handler;
	
	/**
	 * adapter
	 */
	private ListDataProvider<T> dataProvider;
	
	/**
	 * 任务
	 */
	private IListTask task;
	
	/**
	 * 
	 */
	private Mode mode;
	private Mode currentMode;
	private int currentPosition;
	
	
	public StateListView(Context context,int type){
		super(context);
		handler = new StateHandler(context,this);
		dataProvider = new ListDataProvider<T>(context);
		currentPosition = LibConfig.StartPosition;
		switch (type) {
		case TYPE_LIST:
		{
			PullToRefreshListView list = new PullToRefreshListView(context);
			listView = list;
		}
			break;
		case TYPE_GRID:
		{
			listView = new PullToRefreshGridView(context);
			
		}
			break;
		case TYPE_GRID_WITH_HEADER:
		{
			PullToRefreshGridHeaderView headerGridView = new PullToRefreshGridHeaderView(context);
			listView = headerGridView;
		}
		default:
			break;
		}
		
		addView(listView,new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

	}
	
	public ListDataProvider<T> getAdapter() {
		return dataProvider;
	}
	
	@SuppressWarnings("unchecked")
	public StateListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		if(isInEditMode())return;
		handler = new StateHandler(context,this);
		dataProvider = new ListDataProvider<T>(context);
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable._PullToRefresh);
		currentPosition = LibConfig.StartPosition;
		//default
		int res = a.getResourceId(R.styleable._PullToRefresh_list_type, R.integer._list_view);
		int resHeader = a.getResourceId(R.styleable._PullToRefresh_header_view, 0);
		
		
		if(res==R.integer._list_view){
			PullToRefreshListView list = new PullToRefreshListView(context,attrs);
			if(resHeader>0){
				View headerView = LayoutInflater.from(context).inflate(resHeader, null);
				list.getRefreshableView().addHeaderView(headerView);
			}
			listView = list;
		}else{
			if(resHeader>0){
				View headerView = LayoutInflater.from(context).inflate(resHeader, null);
				PullToRefreshGridHeaderView headerGridView = new PullToRefreshGridHeaderView(context,attrs);
				headerGridView.addHeaderView(headerView);
				listView = headerGridView;
			}else{
				listView = new PullToRefreshGridView(context,attrs);
			}
			
		}
		listView.setAdapter(dataProvider);
		addView(listView,new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

		/**
		 * 如果有headerview
		 */
		handler.setIds(a.getResourceId(R.styleable._PullToRefresh_loading, R.layout._loading_layout),
				a.getResourceId(R.styleable._PullToRefresh_error, R.layout._network_error),
				a.getResourceId(R.styleable._PullToRefresh_no_result, R.layout._no_result));
		
		handler.setListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				reloadWithState();
			}
		});
		mode = listView.getMode();
		listView.setOnRefreshListener(this);
		
		a.recycle();
	}
	
	
	public void setMode(Mode mode){
		this.mode = mode;
		listView.setMode(mode);
	}
	
	
	/**
	 * 
	 * @param adapter
	 */
	public void setAdapter(ListAdapter adapter){
		this.listView.setAdapter(adapter);
	}
	
	
	/**
	 * item res
	 * @param res
	 */
	public void setItemRes(int res){
		dataProvider.setItemRes(res);
	}
	
	/**
	 * 
	 * @param view
	 */
	public void addHeaderView(View view){
		if(listView instanceof PullToRefreshListView){
			((PullToRefreshListView)listView).getRefreshableView().addHeaderView(view);
		}else{
			HeaderGridView headerGridView =  (HeaderGridView)listView.getRefreshableView();
			headerGridView.addHeaderView(view);
		}
	}
	
	public void setTask(IArrayJsonTask<T> task){
		this.task = task;
		task.setListener(this);
	}
	
	public void setTask(IListTask task){
		this.task = task;
	}
	
	@SuppressWarnings("unchecked")
	public IArrayJsonTask<T> getTask(){
		return (IArrayJsonTask<T>)this.task;
	}
	
	public void setDataListener(IListDataProviderListener<T> listener){
		this.dataProvider.setListener(listener);
	}
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void setDetailActivity(Class<? extends Activity> clazz){
		setOnItemClickListener(new OnItemClickListenerOpenActivity(clazz));
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void setDetailActivity(Class<? extends Activity> clazz,int requestCode){
		setOnItemClickListener(new OnItemClickListenerOpenActivity(clazz,requestCode));
	}
	
	
	public void setOnItemClickListener(IOnItemClickListener<T> listener){
		this.listener = listener;
		listView.setOnItemClickListener(this);
	}

	@Override
	public void onRequestError(String errMsg, boolean isNetworkError) {
		handler.onError(errMsg, isNetworkError);
		onComplete();
	}

	@Override
	public void onRequestSuccess(List<T> result, boolean isLastPage) {
		dataProvider.setData(result, currentPosition <= LibConfig.StartPosition ? false : true);
		handler.onSuccess(result.size()>0);
		if (isLastPage) {
			if (mode == Mode.BOTH && listView.getMode() != Mode.PULL_FROM_START) {
				changeMode(Mode.PULL_FROM_START);
			}
		} else {
			if (mode == Mode.BOTH && listView.getMode() != Mode.BOTH) {
				changeMode(Mode.BOTH);
			}
		}
		onComplete();
	}
	public void onComplete() {
		listView.onRefreshComplete();
		setLabel();
	}
	private void changeMode(Mode mode) {
		currentMode = mode;
		MessageUtil.sendMessage(0, this);
	}
	@Override
	public void onPullDownToRefresh(@SuppressWarnings("rawtypes") PullToRefreshBase refreshView) {
		currentPosition = LibConfig.StartPosition;
		if (task != null) {
			task.reload();
		}
	}

	@Override
	public void onPullUpToRefresh(@SuppressWarnings("rawtypes") PullToRefreshBase refreshView) {
		currentPosition =dataProvider.getCount() + LibConfig.StartPosition;
		task.loadMore(currentPosition);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		@SuppressWarnings("unchecked")
		T info = (T) parent.getAdapter().getItem(position);
		this.listener.onItemClick((Activity)getContext(),info,position);
	}
	
	private void setLabel() {
		String label = DateUtils.formatDateTime(getContext(),
				System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
						| DateUtils.FORMAT_SHOW_DATE
						| DateUtils.FORMAT_ABBREV_ALL);
		listView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
	}

	@Override
	public void onMessage(int message) {
		listView.setMode(currentMode);
	}
	
	public void reloadWithState(){
		handler.setStateLoading();
		if (task != null) {
			task.reload();
		}
	}
	
	public void setStateLoading(){
		handler.setStateLoading();
	}
	
	public ListDataProvider<T> getDataProvider() {
		return dataProvider;
	}
	
	public void setSelector(int resID) {
		listView.getRefreshableView().setSelector(resID);
	}
	
	public PullToRefreshAdapterViewBase<?> getListView(){
		return listView;
	}
	
}
