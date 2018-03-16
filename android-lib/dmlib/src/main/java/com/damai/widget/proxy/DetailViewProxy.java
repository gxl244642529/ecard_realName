package com.damai.widget.proxy;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.ViewGroup;

import com.citywithincity.models.cache.CachePolicy;
import com.citywithincity.utils.Alert;
import com.damai.core.ApiJob;
import com.damai.core.ApiListener;
import com.damai.core.DMLib;
import com.damai.core.EntityUtil;
import com.damai.helper.DataHolder;
import com.damai.helper.DetailDataSetter;
import com.damai.lib.R;
public class DetailViewProxy extends WidgetProxy implements  ApiListener {
	private boolean initSetData;
	private String api;
	private String entityClass;
	private String name;
	private ApiJob job;
	private DetailDataSetter dataSetter;
	private boolean autoExecute;
	private String dataKey;
	private String idKey;
	public DetailViewProxy(Context context, AttributeSet attrs,ViewGroup view) {
		super(context, attrs,view);
		
		
		TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.api);
		api = a.getString(R.styleable.api_api);
		entityClass = a.getString(R.styleable.api_entity);
		
		autoExecute = a.getBoolean(R.styleable.api_auto, false);
		
		initSetData = a.getBoolean(R.styleable.api_init_set_data, false);
		name = a.getString(R.styleable.api_name);
		if(name==null){
			//如果没有名称，则使用activity的类名称
			name = context.getClass().getSimpleName();
		}
		
		
		if(api!=null){
			dataKey = a.getString(R.styleable.api_dataKey);
			idKey = a.getString(R.styleable.api_idKey);
			if(idKey==null){
				idKey = dataKey;
			}
			job = DMLib.getJobManager().createObjectApi(api);
			job.setCachePolicy(CachePolicy.get(a.getInt(R.styleable.api_cachePolicy,0)));
			if(entityClass!=null){
				try {
					job.setEntity(Class.forName(entityClass,false,getContext().getClassLoader()));
				} catch (ClassNotFoundException e) {
					throw new RuntimeException("Cannot find class "+ entityClass);
				}
			}
			job.setApiListener(this);
			job.setServer(a.getInt(R.styleable.api_server, 0));
			//这里需要设置数据
			if(dataKey!=null){
				Activity activity = (Activity) getContext();
				Object listData = DataHolder.get(activity.getClass());
                if(listData==null){
                    Alert.showShortToast("数据为空，请重新进");
                    return;
                }
				Object value = EntityUtil.get(listData, dataKey);
				if(value instanceof String){
					job.put(idKey, (String)value);
				}else if(value instanceof Integer){
					job.put(idKey, (Integer)value);
				}else if(value instanceof Long){
					job.put(idKey, (Long)value);
				}
			}
			if(autoExecute){
				job.execute();
			}
		}
		
		a.recycle();
	}



	@Override
	public void onFinishInflate() {
		Activity activity = (Activity) getContext();
		Object data = DataHolder.get(activity.getClass());
		dataSetter = new DetailDataSetter(getContext(), (ViewGroup)view,name);
		if(initSetData){
			//设置当前视图
			dataSetter.setInitData(data);
		}
	}


	@Override
	public void onJobSuccess(ApiJob job) {
		dataSetter.setLoadedData(job.getData());
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

}