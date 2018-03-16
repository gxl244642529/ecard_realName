package com.damai.widget.proxy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.citywithincity.interfaces.IViewContainer;
import com.citywithincity.models.cache.CachePolicy;
import com.citywithincity.utils.Alert;
import com.citywithincity.utils.KeyboardUtil;
import com.citywithincity.utils.MessageUtil;
import com.citywithincity.utils.MessageUtil.IMessageListener;
import com.damai.core.ApiJob;
import com.damai.core.ApiTypes;
import com.damai.core.Crypt;
import com.damai.core.DMLib;
import com.damai.lib.R;
import com.damai.widget.Form;
import com.damai.widget.FormElement;
import com.damai.widget.FormSubmit;
import com.damai.widget.OnSubmitListener;

public class SubmitButtonProxy extends WidgetProxy implements FormSubmit,
		ISubmitButton, IMessageListener {

	public static final String TAG = "SubmitButton";

	private Form form;
	// private String[] submitFields;
	private String submitFields;
	private List<FormElement> elements;
	private ApiJob task;
	private String api;
	private int crypt;
	private String waiting;
	private int server;
	private String entity;

	public SubmitButtonProxy(Context context, AttributeSet attrs, View target) {
		super(context, attrs, target);
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.api);
		/**
		 * 如果有表单提交的对象，则可以不使用formView
		 */
		submitFields = a.getString(R.styleable.api_submit_fields);

		crypt = a.getInt(R.styleable.api_crypt, Crypt.NONE);
		api = a.getString(R.styleable.api_api);
		waiting = a.getString(R.styleable.api_waiting_message);
		server = a.getInt(R.styleable.api_server, 0);
		entity = a.getString(R.styleable.api_entity);
		if (api != null) {
			target.setOnClickListener(listener);
			int type = a.getInt(R.styleable.api_api_type, ApiTypes.object);
			switch (type) {
			case ApiTypes.object:
				task = DMLib.getJobManager().createObjectApi(api);
				break;
			case ApiTypes.array:
				task = DMLib.getJobManager().createArrayApi(api);
				break;
			case ApiTypes.page:
				task = DMLib.getJobManager().createPageApi(api);
				break;
			default:
				break;
			}

			if (entity != null) {
				try {
					task.setEntity(Class.forName(entity, false, getContext()
							.getClassLoader()));
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
			task.setCachePolicy(CachePolicy.CachePolity_NoCache);
			task.setCrypt(crypt);
			task.setServer(server);
			task.setWaitingMessage(waiting != null ? waiting : "请稍等...");
			task.setButton(target);
		}

		a.recycle();
	}

	public ApiJob getJob() {
		return task;
	}

	private OnSubmitListener onSubmitListener;

	public void setOnSubmitListener(OnSubmitListener onSubmitListener) {
		this.onSubmitListener = onSubmitListener;
	}

	@Override
	public void destroy() {
		super.destroy();
		form = null;
		onSubmitListener = null;
		elements = null;
	}

	@Override
	public void onFinishInflate() {
		// TODO Auto-generated method stub

	}

	protected boolean doValidate(Map<String, Object> data, int submitPolicy) {
		// 首先进行验证表单

		for (FormElement element : elements) {
			if (submitPolicy == SubmitPolicy_IgnoreNotShown
					&& !element.isShown()) {
				continue;
			}
			String error = element.validate(data);
			if (error != null) {
				if (element instanceof EditText) {
					((EditText) element).setError(error);
				} else {
					Alert.alert(getContext(), error);
				}

				return false;
			}
		}
		return true;
	}

	protected void doSubmit(Map<String, Object> data) {
		// 这里进行提交
		task.putAll(data);
		task.execute();
	}

	@Override
	public void submit(int submitPolicy) {
		KeyboardUtil.hideSoftKeyboard(getContext(), view);
		Map<String, Object> data = new HashMap<String, Object>();
		if (!doValidate(data, submitPolicy)) {
			return;
		}

		if (onSubmitListener != null
				&& !onSubmitListener.shouldSubmit(form, data)) {
			return;
		}

		if (form.isPersistent()) {
			// 保存起来
			form.persistent();
		}

		doSubmit(data);
	}

	@Override
	public void submit() {
		submit(SubmitPolicy_IgnoreNotShown);

	}

	private OnClickListener listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			submit(SubmitPolicy_IgnoreNotShown);
		}
	};

	@Override
	public void setForm(Form form) {

		this.form = form;
		if (submitFields != null) {
			MessageUtil.sendMessage(this);
		} else {
			elements = form.getElements();
		}
	}

	@Override
	public void onMessage(int message) {
		// 查找
		IViewContainer parent = (IViewContainer) context;
		elements = new ArrayList<FormElement>();
		for (String key : submitFields.split(",")) {
			View view = parent.findViewByName(key);
			if (view instanceof FormElement) {
				// 如果是表单对象
				elements.add((FormElement) view);
			} else {
				Log.e(TAG,
						"View R.id."
								+ key
								+ " is not a form element,the class must implements FormElement");
			}
		}
	}

}
