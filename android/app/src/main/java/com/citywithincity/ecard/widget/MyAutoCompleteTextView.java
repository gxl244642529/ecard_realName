package com.citywithincity.ecard.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.citywithincity.ecard.R;
import com.citywithincity.models.LocalData;
import com.damai.widget.FormElement;

import java.util.Map;

public class MyAutoCompleteTextView extends AutoCompleteTextView implements FormElement{
	
	public static final String TEXT_RECORD = "history_record_";
	private Context mContext;
	private String mKey;

	public MyAutoCompleteTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
	}
	
	public void setKey(String key) {
		mKey = TEXT_RECORD + key;
	}
	
	/**
	 * 保存数据时选择一个固定值做　“ｋｅｙ“ 这样再读取时才知道通过什么ｋｅｙ来取值。
	 * 
	 * @param field
	 */
	public void saveHistory(String field) {
		String addText = getText().toString();
		String history = LocalData.read().getString(mKey, "");

		if (!history.contains(addText + ",")) {
			StringBuilder sb = new StringBuilder(history);
			sb.insert(0, addText + ",");
			LocalData.write().putString(mKey, sb.toString()).destroy();
		}
	}
	
	public void onInit() {
		String history = LocalData.read().getString(mKey, "");

		String[] hisArrays = history.split(",");

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext,
				R.layout.search_item, R.id.text, hisArrays);
		// 只保留最近的50条的记录
		if (hisArrays.length > 50) {
			String[] newArrays = new String[50];
			System.arraycopy(hisArrays, 0, newArrays, 0, 50);
			adapter = new ArrayAdapter<String>(mContext, R.layout.search_item,
					R.id.text, newArrays);
		}
		this.setAdapter(adapter);
		this.setDropDownBackgroundDrawable(null);
		this.setDropDownBackgroundResource(android.R.color.white);
		this.setDropDownHeight(400);
		this.setThreshold(1);
		// auto.setCompletionHint("最近的5条记录");
		this.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				AutoCompleteTextView view = (AutoCompleteTextView) v;
				if (hasFocus) {
					view.showDropDown();
				}
				String history = LocalData.read().getString(mKey, "");
				
				String[] hisArrays = history.split(",");

				@SuppressWarnings("unused")
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(
						mContext, R.layout.search_item,
						R.id.text, hisArrays);
			}
		});
	}

	@Override
	public String validate(Map<String, Object> data) {
		String cardId = getText().toString().trim();
		if(TextUtils.isEmpty(cardId)){
			return "请输入或选择e通卡卡号";
		}
		data.put("cardId", cardId);
		return null;
	}

}
