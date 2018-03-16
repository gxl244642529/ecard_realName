package com.citywithincity.ecard.ui.views;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.FrameLayout;

import com.citywithincity.ecard.R;
import com.citywithincity.ecard.widget.PaneContainerView;
import com.damai.util.ViewUtil;

public class MainView extends FrameLayout implements OnGlobalLayoutListener {

	public enum MainPaneDir {
		MainPaneDir_Top, MainPaneDir_Left, MainPaneDir_Right
	}

	public interface IMainViewEvent {

		void onMainButtonClick(int index);

		void onSubButtonClick(int index);

		void onPaneButtonClick(MainPaneDir mainPaneDir);

	}

	private IMainViewEvent eventListener;
	
	

	public MainView(Context context, AttributeSet attrs) {
		super(context, attrs);
		if(isInEditMode())return;
		ViewUtil.initParam((Activity)context);
		this.getViewTreeObserver().addOnGlobalLayoutListener(this);
	}

	
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		ViewGroup viewGroup = (ViewGroup) findViewById(R.id.mainContainer);
		for(int i = 0; i< 4; ++i){
			View view = viewGroup.getChildAt(i);
			view.setTag(i);
			view.setOnClickListener(onMainButtonClick);
		}
		viewGroup = (ViewGroup) findViewById(R.id.subContainer);
		int c = (viewGroup.getChildCount()+1) / 2;
		for(int i = 0 ; i< c; ++i){
			View view = viewGroup.getChildAt(i*2);
			view.setTag(i);
			view.setOnClickListener(onSubButtonClick);
		}
		
		
	}

	private OnClickListener onMainButtonClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			eventListener.onMainButtonClick((Integer)v.getTag());
		}
		
	};
	private OnClickListener onSubButtonClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			eventListener.onSubButtonClick((Integer)v.getTag());
		}
		
	};
	private OnClickListener onMainPaneClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			int id = v.getId();
			if(id== R.id.main_pane_top){
				eventListener.onPaneButtonClick(MainPaneDir.MainPaneDir_Top);
			}else if(id == R.id.main_pane_left){
				eventListener.onPaneButtonClick(MainPaneDir.MainPaneDir_Left);
			}else{
				eventListener.onPaneButtonClick(MainPaneDir.MainPaneDir_Right);
			}
		}
	};

	@SuppressWarnings("deprecation")
	@Override
	public void onGlobalLayout() {
		ViewGroup viewGroup = (ViewGroup) findViewById(R.id.mainContainer);

		View view = viewGroup.getChildAt(0);
		int yuanwidth = view.getWidth();
		for (int i = 0; i < 4; ++i) {
			View child = viewGroup.getChildAt(i);
			android.widget.LinearLayout.LayoutParams param1 = (android.widget.LinearLayout.LayoutParams) child
					.getLayoutParams();
			param1.height = yuanwidth;
			child.setLayoutParams(param1);

		}
		PaneContainerView containerView = (PaneContainerView) findViewById(R.id.pane_container);
		containerView.setOnClickListener(onMainPaneClick);
		int y = findViewById(R.id.mainContainer).getHeight()
				+findViewById(R.id.header_container).getHeight()
				- findViewById(R.id.main_account).getHeight() / 2;
		int x = ViewUtil.screenWidth / 2;
		containerView.movoTo(x, y);

		getViewTreeObserver()
				.removeGlobalOnLayoutListener(this);
	}


	public IMainViewEvent getEventListener() {
		return eventListener;
	}


	public void setEventListener(IMainViewEvent eventListener) {
		this.eventListener = eventListener;
	}
}
