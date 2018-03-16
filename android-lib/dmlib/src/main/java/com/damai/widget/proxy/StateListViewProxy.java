package com.damai.widget.proxy;

import java.util.List;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.GridView;
import android.widget.ListView;

import com.citywithincity.interfaces.IOnItemClickListener;
import com.citywithincity.interfaces.IViewContainer;
import com.citywithincity.models.cache.CachePolicy;
import com.citywithincity.widget.HorizontalListView;
import com.damai.core.ApiJob;
import com.damai.core.ApiListener;
import com.damai.core.DMLib;
import com.damai.core.DMPage;
import com.damai.helper.AdapterFactory;
import com.damai.helper.CellDataSetter;
import com.damai.helper.DMAdapter;
import com.damai.helper.OnItemClickActivity;
import com.damai.helper.StateHandler;
import com.damai.lib.R;
import com.damai.pulltorefresh.PullToRefreshBase;
import com.damai.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import com.damai.pulltorefresh.PullToRefreshGridView;
import com.damai.pulltorefresh.PullToRefreshListView;

@SuppressWarnings("rawtypes")
public class StateListViewProxy<T> extends WidgetProxy implements
		IStateListView<T>, OnRefreshListener, OnItemClickListener, ApiListener {
	private static final String TAG = "StateListView";

	public static final int none = 0;
	public static final int single = 1;
	public static final int multi = 2;
	public static final int modal = 3;

	private CellDataSetter<T> dataSetter;
	private DMAdapter<T> adapter;
	private ApiJob task;
	private PullToRefreshBase<? extends AbsListView> pullToRefreshBase;
	private AdapterView refreshView;
	private boolean isPaged;
	private View headerView;
	/**
	 * 状态支持
	 */
	private StateHandler handler;
	private IOnItemClickListener<T> listener;

	
	public StateListViewProxy(Context context,FrameLayout targetView,int itemRes,int cols) {
		super(context, null, targetView);
		adapter = AdapterFactory.create((IViewContainer) context, itemRes);
	}
	

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@SuppressWarnings("unchecked")
	public StateListViewProxy(Context context, AttributeSet attrs,
			FrameLayout targetView) {
		super(context, attrs, targetView);

		TypedArray a = context.obtainStyledAttributes(attrs,R.styleable._state_list);
		int cols = a.getInt(R.styleable._state_list_cols, 0);
		int header = a.getResourceId(R.styleable._state_list_state_header_view,
				0);
		int itemRes = a.getResourceId(R.styleable._state_list_item_view, 0);
		// 这里如果不能确定是否在activity中,还是在fragment里面，还是在子view里面
		// 如果需要处理item中的事件，则应该将item事件处理程序移动到activity中

		adapter = AdapterFactory.create((IViewContainer) context, itemRes);

		if (cols > 1) {
			pullToRefreshBase = new PullToRefreshGridView(context);
			GridView listView = (GridView) pullToRefreshBase
					.getRefreshableView();
			listView.setAdapter(adapter);
			listView.setNumColumns(cols);
			refreshView = pullToRefreshBase.getRefreshableView();
		} else {
			boolean isHorizontal = a.getBoolean(
					R.styleable._state_list_is_horizontal, false);
			if (isHorizontal) {
				refreshView = new HorizontalListView(context, attrs);
				refreshView.setAdapter(adapter);
			} else {
				// 默认创建listView
				pullToRefreshBase = new PullToRefreshListView(context);
				if (header > 0) {
					ListView listView = (ListView) pullToRefreshBase
							.getRefreshableView();
					headerView = LayoutInflater.from(context).inflate(header,
							null);
					listView.addHeaderView(headerView, null, false);
				}
				ListView listView = (ListView) pullToRefreshBase
						.getRefreshableView();
				listView.setAdapter(adapter);

				int choiceMode = a.getInt(R.styleable._state_list_choice_mode,
						none);
				switch (choiceMode) {
				case none:
					listView.setChoiceMode(ListView.CHOICE_MODE_NONE);
					break;
				case single:
					listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
					break;
				case multi:
					listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
					break;
				case modal:
					listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
					break;
				default:
					break;
				}

				refreshView = pullToRefreshBase.getRefreshableView();
			}

		}
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		if (pullToRefreshBase != null) {
			targetView.addView(pullToRefreshBase, lp);
			pullToRefreshBase.setOnRefreshListener(this);
		} else {
			targetView.addView(refreshView);
		}

		StateHandler handler = new StateHandler(context, targetView);
		handler.setListener(refreshListener);
		handler.setStateLoading();
		/**
		 * 如果有headerview
		 */
		handler.setIds(a.getResourceId(R.styleable._state_list_state_loading,
				R.layout._loading_layout), a.getResourceId(
				R.styleable._state_list_state_error, R.layout._network_error),
				a.getResourceId(R.styleable._state_list_state_no_result,
						R.layout._no_result));

		handler.setHeaderView(headerView);
		this.handler = handler;
		a.recycle();

		/**
		 * 
		 */
		a = context.obtainStyledAttributes(attrs, R.styleable.api);
		String api = a.getString(R.styleable.api_api);
		String target = a.getString(R.styleable.api_target);
		if (target != null) {
			while (true) {
				try {
					Class<?> clazz = Class.forName(target, false, getContext()
							.getClassLoader());
					refreshView.setOnItemClickListener(new OnItemClickActivity(
							clazz));
				} catch (ClassNotFoundException e) {
					Log.e(TAG, "Cannot find class " + target);
				}
				break;
			}

		} else {

			refreshView.setOnItemClickListener(this);
		}
		if (api != null) {
			isPaged = a.getBoolean(R.styleable.api_paged, false);
			if (isPaged) {
				task = DMLib.getJobManager().createPageApi(api);
				pullToRefreshBase.setPullLoadEnabled(false);
				pullToRefreshBase.setScrollLoadEnabled(true);
			} else {
				task = DMLib.getJobManager().createArrayApi(api);
			}
			String clazzString = a.getString(R.styleable.api_entity);
			if (clazzString != null) {
				try {
					task.setEntity(Class.forName(clazzString, false,
							getContext().getClassLoader()));
				} catch (ClassNotFoundException e) {
					Log.e(TAG, "Cannot find class " + clazzString);
					throw new RuntimeException(e);
				}
			}
			task.setCachePolicy(CachePolicy.get(a.getInt(
					R.styleable.api_cachePolicy, 0)));
			task.setPosition(0);
			task.setApiListener(this);
			int server = a.getInt(R.styleable.api_server, 0);
			task.setServer(server);
			boolean auto = a.getBoolean(R.styleable.api_auto, false);
			if (auto) {
				// 自动执行
				task.execute();
			}
		}

		a.recycle();

	}

	@Override
	public void destroy() {
		super.destroy();
		if (adapter != null) {
			adapter.destroy();
			adapter = null;
		}
		headerView = null;
		listener = null;
	}

	private OnClickListener refreshListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			refreshWithState();
		}
	};

	public void refreshWithState() {
		handler.setStateLoading();
		task.reload();
	}

	public View getHeaderView() {
		view.findViewById(0);
		return headerView;
	}

	public ApiJob getJob() {
		return task;
	}

	@Override
	public void onJobSuccess(ApiJob job) {
		final PullToRefreshBase<?> pullToRefreshBase = this.pullToRefreshBase;
		final DMAdapter<T> adapter = this.adapter;
		final boolean isPaged = this.isPaged;

		if (pullToRefreshBase != null) {
			pullToRefreshBase.onPullDownRefreshComplete();
			pullToRefreshBase.onPullUpRefreshComplete();
		}

		if (isPaged) {
			DMPage<T> page = job.getData();
			// 如果是第一页
			if (page.isFirst()) {
				adapter.setData(page.getList());
			} else {
				adapter.appendData(page.getList());
			}
			if (pullToRefreshBase != null) {
				pullToRefreshBase.setHasMoreData(!page.isLast());
			}
			handler.onSuccess(!page.isFirst() || page.getList().size() > 0);
		} else {
			List<T> list = job.getData();
			adapter.setData(list);
			handler.onSuccess(list.size() > 0);
		}

	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase refreshView) {
		task.setPosition(0);
		task.reload();
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase refreshView) {
		task.setPosition(adapter.getCount());
		task.execute();
	}

	public void layout() {
		handler.layout();
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

	public void setOnItemClickListener(IOnItemClickListener<T> listener) {
		this.listener = listener;
	}

	@Override
	public boolean onJobError(ApiJob job) {
		return false;
	}

	@Override
	public boolean onApiMessage(ApiJob job) {
		return false;
	}

	@Override
	public void onFinishInflate() {

	}

	@SuppressWarnings({ "unchecked", "hiding" })
	@Override
	public <T extends AbsListView> T getAdapterView() {

		return (T) refreshView;
	}

}
