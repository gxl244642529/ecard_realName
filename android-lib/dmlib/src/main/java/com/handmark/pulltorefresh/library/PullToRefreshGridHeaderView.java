package com.handmark.pulltorefresh.library;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.util.AttributeSet;
import android.view.View;
import android.widget.GridView;

import com.citywithincity.widget.HeaderGridView;
import com.damai.lib.R;
import com.handmark.pulltorefresh.library.internal.EmptyViewMethodAccessor;

public class PullToRefreshGridHeaderView extends PullToRefreshGridView {

	public PullToRefreshGridHeaderView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public PullToRefreshGridHeaderView(
			Context context,
			com.handmark.pulltorefresh.library.PullToRefreshBase.Mode mode,
			com.handmark.pulltorefresh.library.PullToRefreshBase.AnimationStyle style) {
		super(context, mode, style);
		// TODO Auto-generated constructor stub
	}

	public PullToRefreshGridHeaderView(Context context,
			com.handmark.pulltorefresh.library.PullToRefreshBase.Mode mode) {
		super(context, mode);
		// TODO Auto-generated constructor stub
	}

	public PullToRefreshGridHeaderView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected GridView createRefreshableView(Context context, AttributeSet attrs) {
		final GridView gv;
		if (VERSION.SDK_INT >= VERSION_CODES.GINGERBREAD) {
			gv = new InternalGridViewSDK9(context, attrs);
		} else {
			gv = new InternalGridView(context, attrs);
		}

		// Use Generated ID (from res/values/ids.xml)
		gv.setId(R.id._wrapped_grid_view);
		return gv;
	}
	

	class InternalGridView extends HeaderGridView implements EmptyViewMethodAccessor {

		public InternalGridView(Context context, AttributeSet attrs) {
			super(context, attrs);
		}

		@Override
		public void setEmptyView(View emptyView) {
			PullToRefreshGridHeaderView.this.setEmptyView(emptyView);
		}

		@Override
		public void setEmptyViewInternal(View emptyView) {
			super.setEmptyView(emptyView);
		}
	}

	@TargetApi(9)
	final class InternalGridViewSDK9 extends InternalGridView {

		public InternalGridViewSDK9(Context context, AttributeSet attrs) {
			super(context, attrs);
		}

		@Override
		protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX,
				int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {

			final boolean returnValue = super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX,
					scrollRangeY, maxOverScrollX, maxOverScrollY, isTouchEvent);

			// Does all of the hard work...
			OverscrollHelper.overScrollBy(PullToRefreshGridHeaderView.this, deltaX, scrollX, deltaY, scrollY, isTouchEvent);

			return returnValue;
		}
	}

	public void addHeaderView(View headerView) {
		HeaderGridView gridView = (HeaderGridView)getRefreshableView();
		gridView.addHeaderView(headerView);
	}
}
