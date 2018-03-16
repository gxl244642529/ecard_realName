package com.damai.note.event;

import java.lang.reflect.Method;

import android.view.View;
import android.view.View.OnClickListener;

import com.citywithincity.interfaces.DialogListener;
import com.citywithincity.interfaces.IViewContainer;
import com.citywithincity.utils.Alert;
import com.damai.core.DMAccount;
import com.damai.core.LoginListener;
import com.damai.lib.R;
import com.damai.note.MethodInfo;

public class OnClickEvent extends MethodInfo implements OnClickListener, LoginListener, DialogListener {

	private int id;
	private String confirm;
	private boolean requireLogin;
	
	public OnClickEvent(Method method,int id,String confirm,boolean requireLogin){
		super(method);
		this.id = id;
		this.requireLogin = requireLogin;
		this.confirm = confirm.equals("") ? null : confirm;
	}
	
	
	@Override
	public void setTarget(IViewContainer observer) {
		super.setTarget(observer);
		
		View view = observer.findViewById(id);
		if(view==null){
			throw new RuntimeException("找不到视图:" + observer.idToString(id));
		}
		view.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		if(requireLogin){
			if(!DMAccount.isLogin()){
				DMAccount.callLoginActivity(this);
				return;
			}
		}
		if(confirm!=null){
			Alert.confirm(observer.getActivity(), confirm, this);
			return;
		}
		invoke();
	}

	@Override
	public void clearObserver() {
	
	}


	@Override
	public void onLoginSuccess() {
		invoke();
	}


	@Override
	public void onLoginCancel() {
		
	}


	@Override
	public void onDialogButton(int id) {
		if(id==R.id._id_ok){
			invoke();
		}
	}

}
