package com.citywithincity.ecard.widget;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.citywithincity.ecard.R;
import com.citywithincity.ecard.user.activities.UserInfoActivity;
import com.citywithincity.interfaces.IDestroyable;
import com.citywithincity.interfaces.IViewContainer;
import com.damai.core.DMAccount;
import com.damai.core.ILife;


public class UserInfoView extends RelativeLayout implements IDestroyable,ILife {

	private View loginView, addrView;
	private TextView name;

	public UserInfoView(Context context, AttributeSet attrs) {
		super(context, attrs);
		if(isInEditMode())return;
		((IViewContainer)context).addLife(this);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		if (isInEditMode()) {
			return;
		}


		name = (TextView) findViewById(R.id.name);

		loginView = findViewById(R.id.login_container);
		addrView = findViewById(R.id.id_address);
		loginView.setClickable(true);
		addrView.setClickable(true);
		loginView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				DMAccount.callLoginActivity(null);
			}
		});
		addrView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				getContext().startActivity(new Intent(getContext(),UserInfoActivity.class));
			}
		});

		
		
		
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		destroy();
	}
	

	private void init(boolean isLogin) {
		if (isLogin) {
			loginView.setVisibility(View.GONE);
			addrView.setVisibility(View.VISIBLE);
			name.setText(DMAccount.get().getAccount());
		} else {
			loginView.setVisibility(View.VISIBLE);
			addrView.setVisibility(View.GONE);
		}
	}


	@Override
	public void destroy() {
		loginView = null;
		addrView = null;
		name = null;
	}


	@Override
	public void onResume(IViewContainer container) {
		init(DMAccount.isLogin());
	}

	

	@Override
	public void onPause(IViewContainer container) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onNewIntent(Intent intent, IViewContainer container) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDestroy(IViewContainer container) {
		// TODO Auto-generated method stub
		
	}


}
