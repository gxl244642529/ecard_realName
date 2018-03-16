package com.damai.widget.proxy;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.citywithincity.interfaces.IOnItemClickListener;
import com.citywithincity.interfaces.IViewContainer;
import com.citywithincity.models.cache.CachePolicy;
import com.citywithincity.utils.MessageUtil;
import com.citywithincity.utils.MessageUtil.IMessageListener;
import com.damai.core.ApiJob;
import com.damai.core.ApiListener;
import com.damai.core.DMLib;
import com.damai.dmlib.LibBuildConfig;
import com.damai.helper.AdapterFactory;
import com.damai.helper.CellDataSetter;
import com.damai.helper.DMAdapter;
import com.damai.helper.OnItemClickActivity;
import com.damai.helper.StateHandler;
import com.damai.interfaces.IStateData;
import com.damai.interfaces.ITab;
import com.damai.interfaces.ITabGroup;
import com.damai.lib.R;
import com.damai.pulltorefresh.PullToRefreshBase;
import com.damai.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import com.damai.pulltorefresh.PullToRefreshGridView;
import com.damai.pulltorefresh.PullToRefreshListView;

@SuppressWarnings("rawtypes")
public class TabListViewProxy<T extends IStateData> extends WidgetProxy implements
		OnItemClickListener, ApiListener, ITab, IMessageListener, OnPageChangeListener,IStateListView<T>,OnRefreshListener {

	private ViewPager viewPager;
	int loadingLayout;
	int errorLayout;
	int notResultLayout;
	int cols;
	int itemRes;
	private CellDataSetter<T> dataSetter;
	private OnItemClickActivity targetActivity;
	private ApiJob task;

	private List<TabListViewGroup> views;
	boolean auto;
	private ITabGroup tabGroup;
	@Override
	public void destroy() {
		tabGroup = null;
		targetActivity= null;
		if(dataSetter!=null){
			dataSetter.destroy();
			dataSetter=null;
		}
		
		super.destroy();
		viewPager = null;
	}
	public TabListViewProxy(Context context, AttributeSet attrs,
			RelativeLayout view) {
		super(context, attrs, view);
		init(context, attrs, view);

	}

	private class TabListViewGroup extends FrameLayout {
		StateHandler handler;
		DMAdapter<T> adapter;
		
		PullToRefreshBase<? extends AbsListView> pullToRefreshBase;
		public TabListViewGroup(Context context) {
			super(context);
		}

		public void refreshWithState() {
			handler.setStateLoading();
		}

		@SuppressWarnings("unchecked")
		public void init() {
			Context context = getContext();
			DMAdapter<T> adapter = new DMAdapter<T>(context, itemRes,
					dataSetter);
			this.adapter = adapter;
			
			
			if (cols > 1) {
				pullToRefreshBase = new PullToRefreshGridView(context);
				GridView listView = (GridView) pullToRefreshBase
						.getRefreshableView();
				listView.setAdapter(adapter);
			} else {
				// 默认创建listView
				pullToRefreshBase = new PullToRefreshListView(context);
				ListView listView = (ListView) pullToRefreshBase
						.getRefreshableView();
				listView.setAdapter(adapter);
			}
			FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			addView(pullToRefreshBase, lp);

			StateHandler handler = new StateHandler(context, this);
			handler.setListener(refreshListener);
			handler.setStateLoading();
			/**
			 * 如果有headerview
			 */
			handler.setIds(loadingLayout, errorLayout, notResultLayout);

			this.handler = handler;

			if (targetActivity != null) {
				pullToRefreshBase.getRefreshableView().setOnItemClickListener(
						targetActivity);
			} else {
				pullToRefreshBase.getRefreshableView().setOnItemClickListener(
						TabListViewProxy.this);
			}
			pullToRefreshBase.setOnRefreshListener(TabListViewProxy.this);

		}

		private OnClickListener refreshListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				TabListViewProxy.this.refreshWithState();
			}

		};

		public void setData(List<T> list) {
			adapter.setData(list);
			pullToRefreshBase.onPullDownRefreshComplete();
			pullToRefreshBase.onPullUpRefreshComplete();
			handler.onSuccess(list.size()>0);
		}
	}

	public void refreshWithState() {
		for (int i = 0, c = views.size(); i < c; ++i) {
			views.get(i).refreshWithState();
		}
		task.reload();
	}

	private IOnItemClickListener<T> listener;
	private int tabCount;

	public void setOnItemClickListener(IOnItemClickListener<T> listener) {
		this.listener = listener;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (listener != null) {
			T data = (T) parent.getAdapter().getItem(position);
			listener.onItemClick((Activity) getContext(), data, position);
		}
	}

	
	@SuppressWarnings("unchecked")
	private void init(Context context, AttributeSet attrs, ViewGroup view) {

		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable._state_list);
		cols = a.getInt(R.styleable._state_list_cols, 0);
		itemRes = a.getResourceId(R.styleable._state_list_item_view, 0);
		
		dataSetter = AdapterFactory.createCellDataSetter((IViewContainer)context, itemRes);
		loadingLayout = a.getResourceId(R.styleable._state_list_state_loading,
				R.layout._loading_layout);
		errorLayout = a.getResourceId(R.styleable._state_list_state_error,
				R.layout._network_error);
		notResultLayout = a.getResourceId(
				R.styleable._state_list_state_no_result, R.layout._no_result);

		a.recycle();
		a = context.obtainStyledAttributes(attrs, R.styleable.api);
		String api = a.getString(R.styleable.api_api);
		String target = a.getString(R.styleable.api_target);
		if (target != null) {
			while (true) {
				try {
					Class<?> clazz = Class.forName(target, false, getContext()
							.getClassLoader());
					if (!Activity.class.isAssignableFrom(clazz)) {
						if (LibBuildConfig.DEBUG) {
							throw new RuntimeException("Class "+ clazz.getName()+ " is not a activity class!");
						}
						break;
					}
					targetActivity = new OnItemClickActivity(
							(Class<? extends Activity>) clazz);
				} catch (ClassNotFoundException e) {
					throw new RuntimeException("Cannot find class " + target);
				}
				break;
			}
		}
		if (api != null) {
			task = DMLib.getJobManager().createArrayApi(api);
			String clazzString = a.getString(R.styleable.api_entity);
			if (clazzString != null) {
				try {
					Class<?> clazz = Class.forName(clazzString,false,getContext().getClassLoader());
					task.setEntity(clazz);
				} catch (ClassNotFoundException e) {
					throw new RuntimeException("Cannot find class "+ clazzString);
				}
			}
			task.setCachePolicy(CachePolicy.get(a.getInt(R.styleable.api_cachePolicy, 0)));
			task.setApiListener(this);
			int server = a.getInt(R.styleable.api_server, 0);
			task.setServer(server);
			auto = a.getBoolean(R.styleable.api_auto, false);
			
		}
		a.recycle();
		
		
		a = context.obtainStyledAttributes(attrs, R.styleable._group_view);
		tabCount = a.getInt(R.styleable._group_view_group_count, 0);
		if(tabCount==0){
			throw new RuntimeException("请在layout中定义group_count,确定视图数量");
		}
		a.recycle();
	}
	
	private void initView(int tabCount) {
		ViewGroup view = (ViewGroup) this.view;
		views = new ArrayList<TabListViewGroup>(tabCount);
		for (int i = 0; i < tabCount; ++i) {
			TabListViewGroup group = new TabListViewGroup(context);
			group.init();
			views.add(group);
		}
		// 初始化
		viewPager = new ViewPager(context);
		view.addView(viewPager);
		viewPager.setAdapter(new MyPagerAdapter(tabCount));
		viewPager.setOnPageChangeListener(this);
	}

	private class MyPagerAdapter extends PagerAdapter {
		private int count;

		public MyPagerAdapter(int count) {
			this.count = count;
		}

		@Override
		public int getCount() {
			return count;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			View view = views.get(position);
			container.addView(view);
			return view;
		}
		
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View)object);
		}

	}

	@Override
	public void onFinishInflate() {
		initView(tabCount);
		MessageUtil.sendMessage(this);
	}

	
	private List<T>[] dataGroup;
	@SuppressWarnings("unchecked")
	@Override
	public void onJobSuccess(ApiJob job) {
		List<T> list = job.getData();
		int c = views.size();
		if(dataGroup==null){
			List<T>[] dataGroup = new List[c];
			for(int i=0; i < c; ++i){
				dataGroup[i] = new ArrayList<T>();
			}
			this.dataGroup = dataGroup;
		}else{
			for(int i=0; i < c; ++i){
				dataGroup[i].clear();
			}
		}
		//分开检索
		for (T data :list) {
			List<T> d = dataGroup[data.getState()];
			d.add(data);
		}
		
		
		for(int i=0 ; i< c; ++i){
			views.get(i).setData(dataGroup[i]);
		}
		
	}

	@Override
	public boolean onJobError(ApiJob job) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onApiMessage(ApiJob job) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setCurrentIndex(int index, boolean animated) {
		viewPager.setOnPageChangeListener(null);
		viewPager.setCurrentItem(index, animated);
		viewPager.setOnPageChangeListener(this);
	}

	
	@Override
	public void onMessage(int message) {
		// 查找tabGroup
		ViewGroup viewGroup = (ViewGroup) view.getParent();
		for (int i = 0; i < 3; ++i) {
			if (viewGroup == null) {
				break;
			}
			ITabGroup tabGroup = (ITabGroup) viewGroup.findViewById(R.id._tab_group);
			if (tabGroup != null) {
				this.tabGroup = tabGroup;
				if (auto) {
					// 自动执行
					task.execute();
				}
				break;
			}
			viewGroup = (ViewGroup) viewGroup.getParent();
		}
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPageSelected(int index) {
		if(tabGroup!=null){
			tabGroup.setCurrentIndex(index, false);
		}
		
	}
	
	@Override
	public void layout() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public View getHeaderView() {
		return null;
	}
	@Override
	public ApiJob getJob() {
	
		return task;
	}
	@Override
	public void onPullDownToRefresh(PullToRefreshBase refreshView) {
		refreshWithState();
	}
	@Override
	public void onPullUpToRefresh(PullToRefreshBase refreshView) {
		
	}
	@Override
	public <T extends AbsListView> T getAdapterView() {
		// TODO Auto-generated method stub
		return null;
	}

}
