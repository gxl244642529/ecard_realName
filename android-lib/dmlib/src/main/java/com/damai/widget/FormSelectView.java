package com.damai.widget;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.citywithincity.interfaces.IViewContainer;
import com.citywithincity.utils.Alert;
import com.citywithincity.utils.Alert.IOnSelectData;
import com.damai.helper.IPersistent;
import com.damai.lib.R;
import com.damai.models.KVData;
public class FormSelectView extends RelativeLayout implements FormElement,IPersistent,IOnSelectData<KVData> {

	private List<KVData> listData = new ArrayList<KVData>();
	// 加载值

	private Object value;
	private TextView text;

	public FormSelectView(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable._select);
		String optionsString = a.getString(R.styleable._select_options);
		//格式为  int|K:V,K:V      string|K:V,K:V
		try{
			String[] arr = optionsString.split("\\|");
			String type = arr[0];
			boolean isInt = type.equals("int");
			String[] contents = arr[1].split(",");
			for (String string : contents) {
				String[] ar = string.split(":");
				listData.add(new KVData(isInt ? Integer.parseInt(ar[1]):ar[1], ar[0]));
			}
		}catch(Throwable e){
			throw new RuntimeException("RuntimeException when get options ,the format is  int|K:V,K:V or string|K:V,K:V",e);
		}
		
		
		a.recycle();
	}
	
	@Override
	protected void onFinishInflate() {
		
		text = (TextView) findViewById(R.id._select_hint);
		if(text==null){
			for(int i=0 , c = getChildCount(); i < c; ++i){
				View view = getChildAt(i);
				if(view instanceof TextView){
					text =(TextView) view;
					break;
				}
			}
		}
		
		if(text==null){
			throw new RuntimeException("FormSelectView must have a textview with id R.id._select_hint");
		}
		this.setOnClickListener(listener);
	}

	
	/**
	 * 
	 */
	private OnClickListener listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			int index = 0;
			for (KVData data : listData) {
				if(data.getData().equals(value)){
					break;
				}
				++index;
			}
			Alert.select(getContext(), text.getHint().toString(), listData, index, FormSelectView.this);
		}
	};

	@Override
	public String validate(Map<String, Object> data) {
		if (value == null) {
			return text.getHint().toString();
		}
		data.put(((IViewContainer)getContext()).idToString(getId()), value);
		return null;
	}

	@Override
	public void onSelectData(int position, KVData data) {
		value = data.getData();
		text.setText(data.getLabel());
	}
	@Override
	public String getLabel(KVData data) {
		return data.getLabel();
	}
	@Override
	public Object getValue() {
		return value;
	}

	@Override
	public void setValue(Object value) {
		for (KVData data : listData) {
			if(data.getData().equals(value)){
				this.value = value;
				text.setText(data.getLabel());
				break;
			}
		}
	}


	

}
