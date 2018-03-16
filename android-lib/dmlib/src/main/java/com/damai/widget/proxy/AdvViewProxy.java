package com.damai.widget.proxy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.RelativeLayout;

import com.citywithincity.interfaces.IViewContainer;
import com.citywithincity.models.cache.CachePolicy;
import com.citywithincity.utils.MessageUtil;
import com.citywithincity.utils.MessageUtil.IMessageListener;
import com.damai.auto.DMWebActivity;
import com.damai.core.ApiJob;
import com.damai.core.ApiListener;
import com.damai.core.DMLib;
import com.damai.core.DMServers;
import com.damai.core.ILife;
import com.damai.lib.R;
import com.damai.widget.NetworkImage;
import com.damai.widget.ScrollIndicator;

public class AdvViewProxy extends WidgetProxy implements ILife, ApiListener, IMessageListener, OnPageChangeListener, OnClickListener {
	private Timer mTimer;
	private TimerTask mTimerTask;
	private List<AdvVo> dataList;
	private ViewPager viewPager;
	private ApiJob job;
	private int _currentIndex;
	private int delay;
	private ScrollIndicator indicator;
	@SuppressLint("UseSparseArrays") public AdvViewProxy(Context context, AttributeSet attrs, RelativeLayout view) {
		super(context, attrs, view);
		
		((IViewContainer)context).addLife(this);
		
		
		viewPager = new ViewPager(context);
		viewPager.setBackgroundColor(Color.parseColor("#e1e1e1"));
		
		view.addView(viewPager);
		
		indicator = new ScrollIndicator(context);
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
		lp.bottomMargin = (int) context.getResources().getDimension(R.dimen._indicator_bottom_margin);
		view.addView(indicator,lp);
		
		TypedArray a = context.obtainStyledAttributes(attrs,R.styleable._adv);
		
		String module = a.getString(R.styleable._adv_module);
		if(module==null){
			throw new RuntimeException("请在AdvView中定义参数module");
		}
		delay=a.getInteger(R.styleable._adv_delay, 5000);
		a.recycle();
		
		job = DMLib.getJobManager().createArrayApi("adv/list");
		job.setApiListener(this);
		job.setCachePolicy(CachePolicy.CachePolity_NoCache);
		job.setServer(1);
		job.put("id", module);
		job.setEntity(AdvVo.class);
		job.execute();
	}
	
	private PagerAdapter adapter = new PagerAdapter() {
		@SuppressLint("UseSparseArrays") private Map<Integer, View> contentViews= new HashMap<Integer, View>();
		
		
		@Override
		public boolean isViewFromObject(View view, Object obj) {
			return view == obj;
		}
		
		@Override
		public int getCount() {
			return dataList.size() > 1 ? Integer.MAX_VALUE : 1;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			NetworkImage view = new NetworkImage(context);
			contentViews.put(position, view);
			view.setOnClickListener(AdvViewProxy.this);
			AdvVo advVo = dataList.get(position % dataList.size());
			if(!TextUtils.isEmpty(advVo.getImgBig())){
				view.load(DMServers.getImageUrl(0, advVo.getImgBig()));
			}else{
				view.load(DMServers.getImageUrl(0, advVo.getImg()));
			}
			container.addView(view);
			return view;
		}
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			View view = contentViews.get(position);
			container.removeView(view);
			contentViews.remove(position);
		}
	};
	
	
	

	@Override
	public void onFinishInflate() {
		
	}

	@Override
	public void destroy() {
		viewPager= null;
		adapter = null;
		super.destroy();
	}

	
	@Override
	public void onResume(IViewContainer container) {
		if(dataList!=null){
			start();
		}
	}

	

	@Override
	public void onPause(IViewContainer container) {
		if(dataList!=null){
			stop();
		}
	}


	@Override
	public void onNewIntent(Intent intent, IViewContainer container) {
		
	}

	

	private void stop() {
		if (mTimer != null) {
			mTimer.cancel();
			mTimer = null;
		}
		if (mTimerTask != null) {
			mTimerTask.cancel();
			mTimerTask = null;
		}
	}
	/**
	 * 启动
	 */
	private void start(){
		if(dataList.size()<=1)return;
		mTimer = new Timer();

		mTimerTask = new TimerTask() {
			@Override
			public void run() {
				MessageUtil.sendMessage(AdvViewProxy.this);
			}
		};

		mTimer.schedule(mTimerTask, delay, delay);
	}

	@Override
	public void onJobSuccess(ApiJob job) {
		List<AdvVo> list = job.getData();
		List<AdvVo> result = new ArrayList<AdvVo>();
		for (AdvVo advVo : list) {
			if(advVo.getPlat()==AdvVo.PLAT_IOS){
				continue;
			}
			result.add(advVo);
		}
		if(result.size() > 0 ){
			dataList = result;
			indicator.setTabCount(result.size());
			if(result.size()>1){
				viewPager.setOnPageChangeListener(this);
			}else{
				viewPager.setOnPageChangeListener(null);
			}
			viewPager.setAdapter(adapter);
			start();
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
	public void onMessage(int message) {
		if(viewPager!=null){
			viewPager.setCurrentItem(_currentIndex + 1, true);
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
			indicator.setCurrentIndex(index%dataList.size(),true);
		}
	}

	/**
	 * 点击以后进入
	 */
	@Override
	public void onClick(View v) {
		AdvVo advVo = dataList.get(_currentIndex % dataList.size());
		//进入
		if(advVo.getEnable()==1){
			DMWebActivity.openUrl((Activity) getContext(), advVo.getUrl(), advVo.getTitle());
		}
	}

	@Override
	public void onDestroy(IViewContainer container) {
		// TODO Auto-generated method stub
		
	}
}
