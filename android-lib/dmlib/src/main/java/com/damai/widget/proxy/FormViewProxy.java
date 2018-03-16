package com.damai.widget.proxy;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.citywithincity.interfaces.IViewContainer;
import com.citywithincity.models.LocalData;
import com.citywithincity.utils.MessageUtil;
import com.citywithincity.utils.MessageUtil.IMessageListener;
import com.citywithincity.utils.ReflectUtil;
import com.damai.helper.DataHolder;
import com.damai.helper.DetailDataSetter;
import com.damai.helper.IMutilValue;
import com.damai.helper.IPersistent;
import com.damai.helper.IValue;
import com.damai.lib.R;
import com.damai.widget.Form;
import com.damai.widget.FormElement;
import com.damai.widget.FormSubmit;
public class FormViewProxy extends WidgetProxy implements  Form, IMessageListener  {
	private String name;
	private List<FormElement> elements;
	private boolean save;
	private boolean initSetData;
	public FormViewProxy(Context context, AttributeSet attrs, View view) {
		super(context, attrs, view);
		elements = new LinkedList<FormElement>();

		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.api);
		save = a.getBoolean(R.styleable.api_save, false);
		initSetData = a.getBoolean(R.styleable.api_init_set_data, false);
		name = a.getString(R.styleable.api_name);
		if (name == null) {
			// 如果没有名称，则使用activity的类名称
			name = context.getClass().getSimpleName();
		}
		a.recycle();
		
	}
	
	@Override
	public void destroy() {
		elements.clear();
		elements = null;
		super.destroy();
	}

	@Override
	public void onFinishInflate() {
		findAllViews((ViewGroup)view);
		if (initSetData) {
			MessageUtil.sendMessage(0, this);
		} else {
			if (save) {
				MessageUtil.sendMessage(1, this);
			}
		}

	}

	protected void findAllViews(ViewGroup view) {
		// 查找所有表单元素
		for (int i = 0, count = view.getChildCount(); i < count; ++i) {
			View child = view.getChildAt(i);
			if (child instanceof FormElement) {
				elements.add((FormElement) child);
				continue;
			}
			if (child instanceof FormSubmit) {
				((FormSubmit) child).setForm(this);
			}
			if (child instanceof ViewGroup) {
				findAllViews((ViewGroup) child);
			}
		}
	}

	@Override
	public List<FormElement> getElements() {
		return elements;
	}

	@Override
	public FormElement getElement(String name) {
		// 根据id来
		int id = getContainer().getViewId(name);
		for (FormElement e : elements) {
			if (e.getId() == id) {
				return e;
			}
		}
		return null;
	}

	@Override
	public boolean isPersistent() {
		return save;
	}

	private DetailDataSetter dataSetter;

	@Override
	public void onMessage(int message) {
		if (message == 0) {
			Activity activity = (Activity) getContext();
			Object data = DataHolder.get(activity.getClass());
			if (data == null) {
				System.err.println("表单数据无,并没有设置表单");
				// Alert.alert(getContext(), "表单数据无,并没有设置表单");
				return;
			}
			dataSetter = new DetailDataSetter(getContext(), (ViewGroup)view, name);
			if (initSetData) {
				// 设置当前视图
				dataSetter.setInitData(data);
			}
		} else {
			// 加载数据
			Map<String, Object> data = LocalData.read().getMap(
					"form" + getContext().getClass().getName());
			if (data != null) {
				// 恢复
				for (Entry<String, Object> entry : data.entrySet()) {
					FormElement element = getElement(entry.getKey());
					if (element instanceof IPersistent) {
						((IPersistent) element).setValue(entry.getValue());
					}
				}
			}
		}

	}

	@Override
	public void persistent() {
		Map<String, Object> data = new HashMap<String, Object>();
		for (FormElement e : elements) {
			if ((e instanceof IPersistent)) {
				Object value = ((IPersistent) e).getValue();
				String name = getContainer().idToString(e.getId());
				data.put(name, value);
			}
		}
		LocalData.write()
				.putMap("form" + getContext().getClass().getName(), data)
				.save();
	}

	protected IViewContainer getContainer() {
		return (IViewContainer) context;
	}
	/**
	 * 这里需要对view进行复制
	 * 
	 * @param value
	 */
	public void setValue(Object value) {
		Map<String, Object> map = ReflectUtil.beanToMap(value);
		for (FormElement e : elements) {
			if(e instanceof IMutilValue){
				((IMutilValue) e).setValue(value);
			}else{
				String name = getContainer().idToString(e.getId());
				if (map.containsKey(name)) {
					if(e instanceof IValue){
						((IValue) e).setValue(map.get(name));
					}
				} else {
					// 查找其他的，看有没有能设置的
				}
			}
			
		}
	}

}
