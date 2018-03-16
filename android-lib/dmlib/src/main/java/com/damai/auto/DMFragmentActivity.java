package com.damai.auto;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.citywithincity.interfaces.IViewContainer;
import com.damai.core.DMLib;
import com.damai.core.ILife;
import com.damai.core.IdManager;
import com.damai.core.IdReflect;
import com.damai.helper.ActivityResult;
import com.damai.helper.ActivityResultContainer;
import com.damai.helper.DataHolder;
import com.damai.lib.R;
import com.umeng.analytics.MobclickAgent;

public abstract class DMFragmentActivity extends FragmentActivity implements
		ActivityResultContainer, IViewContainer {

	private Set<ILife> lifeSet;
	private TextView titleTextView;
	private IdReflect idReflect;
	
	public DMFragmentActivity(){
		idReflect = IdManager.getDefault();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DMLib.getJobManager().onCreate(this);
		onSetContent(savedInstanceState);
		View view = findViewById(R.id._title_left);
		if(view!=null){
			view.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					backToPrevious();
				}
			});
		}
       
	}
	protected void backToPrevious(){
		finish();
	}
	
	@Override
	public void setContentView(int layoutResID) {
		super.setContentView(layoutResID);
		DMLib.getJobManager().onViewCreate(this);
	}
	protected void setTitle(String title){
		titleTextView = (TextView) findViewById(R.id._title_text);
		if(titleTextView!=null){
			titleTextView.setText(title);
		}
	}

	protected abstract void onSetContent(Bundle savedInstanceState);
	@Override
	public void addLife(ILife life) {
		if (lifeSet == null) {
			lifeSet = new HashSet<ILife>();
		}
		lifeSet.add(life);
	}
	@Override
	protected void onDestroy() {
		if(lifeSet!=null){
			for (ILife life : lifeSet) {
				life.onDestroy(this);
			}
		}
		DMLib.getJobManager().onDestroy(this);
		lifeSet = null;
		result = null;
		DataHolder.set(this.getClass(),null);
        if(actiMap!=null){
            actiMap.clear();
            actiMap = null;
        }
		super.onDestroy();
	}
	protected boolean needsStatistics(){
		return true;
	}
	@Override
	protected void onPause() {
		if(lifeSet!=null){
			for (ILife life : lifeSet) {
				life.onPause(this);
			}
		}
		super.onPause();
		if(needsStatistics()){
			MobclickAgent.onPause(this);
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if(needsStatistics()){
			MobclickAgent.onResume(this);
		}
		if(lifeSet!=null){
			for (ILife life : lifeSet) {
				life.onResume(this);
			}
		}
		DMLib.getJobManager().onResume(this);
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		List<Fragment> fragments = getSupportFragmentManager().getFragments();
		if(fragments!=null){
			for (Fragment fragment : fragments) {
				DMFragment dmFragment = (DMFragment) fragment;
				dmFragment.onNewIntent(intent);
			}
		}
		if(lifeSet!=null){
			for (ILife life : lifeSet) {
				life.onNewIntent(intent,this);
			}
		}
	}

	private ActivityResult result;
	private Map<Integer, ActivityResult> actiMap;
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		/*if (result != null) {
			result.onActivityResult(data, resultCode, requestCode);
			result = null;
		}*/
		if(actiMap!=null){
			ActivityResult result = actiMap.get(requestCode);
			if (result != null) {
				result.onActivityResult(data, resultCode, requestCode);
				actiMap.remove(requestCode);
				if(actiMap.size()==0){
					actiMap = null;
				}
			}
		}
	}


	@SuppressLint("UseSparseArrays")
	public void startActivityForResult(ActivityResult result, Intent intent,
									   int requestCode) {
		if(actiMap==null){
			actiMap = new HashMap<Integer, ActivityResult>();
		}
		actiMap.put(requestCode, result);
		startActivityForResult(intent, requestCode);
	}
	
	public void startActivity(Class<? extends Activity> activityClass,Object data){
		DataHolder.set(activityClass,data);
		startActivity(new Intent(this,activityClass));
	}
	public void startActivityForResult(Class<? extends Activity> activityClass,Object data,int requestCode){
		DataHolder.set(activityClass,data);
		startActivityForResult(new Intent(this,activityClass),requestCode);
	}

	@Override
	public Activity getActivity() {
		return this;
	}

	@Override
	public View findViewByName(String name) {
		//查找
		Resources resources = getResources();
		int id = resources.getIdentifier(name, "id", getPackageName());
		return findViewById(id);
	}
	@Override
	public int getViewId(String name) {
		Resources resources = getResources();
		int id = resources.getIdentifier(name, "id", getPackageName());
		return id;
	}
	@Override
	public String idToString(int id) {
		return idReflect.idToString(id);
	}
	
}
