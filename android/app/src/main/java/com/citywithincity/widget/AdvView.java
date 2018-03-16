package com.citywithincity.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.citywithincity.ecard.R;
import com.citywithincity.interfaces.IDataProvider;
import com.citywithincity.interfaces.IDataProviderListener;
import com.citywithincity.interfaces.IDestroyable;
import com.citywithincity.interfaces.IListDataProviderListener;
import com.citywithincity.models.MyTimer;
import com.citywithincity.models.MyTimer.ITimerListener;
import com.citywithincity.utils.Alert;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressLint("UseSparseArrays")
public class AdvView<T> extends RelativeLayout implements IDestroyable, OnPageChangeListener,
		ITimerListener, OnClickListener, IDataProviderListener<List<T>> {
	
	private ViewPager viewPager;
	private List<T> imageList;
	private int _currentIndex;
	private MyTimer _timer;
	private Context context;
	private Map<Integer, View> _contentViews;
	private PagerAdapter pagerAdapter;
	private int itemResID;
	private OnClickAdvViewListener<T> listener;
	private IListDataProviderListener<T> listDataProviderListener;
	private ViewPagerIndicator indicator;
	
	public interface OnClickAdvViewListener<T>{
		void onClickAdvView(View view,int index,T data);
	}
	
	
	public AdvView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		this.context = context;
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable._group_view);
		
		itemResID=a.getResourceId(R.styleable._group_view_item_layout, R.layout._network_image);
		
		a.recycle();
	}
	
	@Override
	protected void onFinishInflate() {
		if (isInEditMode()) {
			return;
		}
		viewPager = (ViewPager) findViewById(R.id._adv_view_pager);
		viewPager.setEnabled(false);
		viewPager.setOnPageChangeListener(this);
		_contentViews = new HashMap<Integer, View>();
		pagerAdapter = new MyPageAdapter();
		if(itemResID>0){
			viewPager.setAdapter(pagerAdapter);
		}
		indicator = (ViewPagerIndicator) findViewById(R.id._indicator_view);
	}

	@Override
	public void destroy() {
		this.context = null;
		if (_contentViews != null) {
			_contentViews.clear();
			_contentViews = null;
		}

		if (imageList != null) {
			imageList.clear();
			imageList = null;
		}

		if (_timer != null) {
			_timer.destroy();
			_timer = null;
		}
		
		pagerAdapter = null;

	}

	public void setData(List<T> data) {
		if(data.size()>0){
			if (indicator != null) {
				indicator.setIndicatorCount(data.size());
			}
			imageList = data;
			pagerAdapter.notifyDataSetChanged();
			start();
		}
	}

	public void start() {
		if (_timer == null) {
			_timer = new MyTimer(0, 5000, 5000);
			_timer.setListener(this);
			_timer.start();
		}
	}

	public void stop() {
		if (_timer != null) {
			_timer.destroy();
			_timer = null;
		}

	}

	private class MyPageAdapter extends PagerAdapter {
		@Override
		public int getCount() {
			return imageList==null?0 : Integer.MAX_VALUE;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			View imageView = _contentViews.get(position);
			container.removeView(imageView);
			_contentViews.remove(position);
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			View imageView = LayoutInflater.from(context).inflate(itemResID,null);
			if(listDataProviderListener!=null){
				int index = position % imageList.size();
				listDataProviderListener.onInitializeView(imageView, imageList.get(index), index);
			}
			
			_contentViews.put(position, imageView);
			imageView.setClickable(true);
			imageView.setOnClickListener(AdvView.this);
			container.addView(imageView);
			return imageView;
		}

	}

	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

	@Override
	public void onPageSelected(int index) {
		_currentIndex = index;
		if (indicator != null) {
			indicator.setCurrent(index%imageList.size());
		}
	}

	@Override
	public void onTimer(int id) {
		viewPager.setCurrentItem(_currentIndex + 1, true);
	}

	@Override
	public void onClick(View v) {
		if(listener!=null && imageList!=null && imageList.size()>0){
			int index = _currentIndex % imageList.size();
			listener.onClickAdvView(v, index, this.imageList.get(index));
		}
	}
	private IDataProvider<List<T>> dataProvider;
	
	@SuppressWarnings("unchecked")
	public void setDataProvider(IDataProvider<List<T>> dataProvider){
		dataProvider.setListener(this);
		dataProvider.load();
		
		if(dataProvider instanceof IListDataProviderListener){
			setListDataProviderListener((IListDataProviderListener<T>) dataProvider);
		}
		
		if(dataProvider instanceof OnClickAdvViewListener){
			setOnClickAdvViewListener( (OnClickAdvViewListener<T>) dataProvider);
		}
		this.dataProvider = dataProvider;
	}

	public void setOnClickAdvViewListener(OnClickAdvViewListener<T> listener) {
		this.listener = listener;
	}

	public void setListDataProviderListener(IListDataProviderListener<T> listDataProviderListener) {
		this.listDataProviderListener = listDataProviderListener;
	}

	@Override
	public void onReceive(List<T> data) {
		setData(data);
	}

	@Override
	public void onError(String error, boolean isNetworkError) {
		Alert.showShortToast(error);
	}

}
