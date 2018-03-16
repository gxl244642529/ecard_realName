package com.damai.widget;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;

import com.citywithincity.interfaces.IViewContainer;
import com.damai.helper.DMAdapter;
import com.damai.helper.DMAdapterFactory;
import com.damai.helper.IValue;
import com.damai.lib.R;

public class ListView extends android.widget.ListView implements IValue {
	@SuppressWarnings("rawtypes")
	private DMAdapter adapter;

	public ListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		if (isInEditMode())
			return;
		init(context, attrs);
	}

	public ListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		if (isInEditMode())
			return;
		init(context, attrs);
	}

	private void init(Context context, AttributeSet attrs) {

		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable._state_list);
		int itemRes = a.getResourceId(R.styleable._state_list_item_view, 0);
		int headerView = a.getResourceId(R.styleable._state_list_state_header_view, 0);
		if(headerView>0){
			View view = ((Activity)context).getLayoutInflater().inflate(headerView, null);
			addHeaderView(view);
		}
		// 这里如果不能确定是否在activity中,还是在fragment里面，还是在子view里面
		// 如果需要处理item中的事件，则应该将item事件处理程序移动到activity中

		adapter = DMAdapterFactory.create((IViewContainer) context, itemRes);
		setAdapter(adapter);

		a.recycle();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void setValue(Object data) {
		adapter.setData((List) data);
	}

}
