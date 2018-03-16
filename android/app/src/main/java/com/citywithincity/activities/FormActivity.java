package com.citywithincity.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.citywithincity.auto.Form;
import com.citywithincity.auto.FormElement;
import com.citywithincity.auto.tools.FormError;
import com.citywithincity.ecard.R;
import com.citywithincity.interfaces.IWidget;
import com.citywithincity.libs.LibConfig;
import com.citywithincity.models.vos.Null;
import com.citywithincity.utils.ActivityUtil;
import com.citywithincity.utils.Alert;
import com.citywithincity.utils.ValidateUtil;
import com.citywithincity.utils.ViewUtil;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public abstract class FormActivity extends BaseActivity {
	
	public static final int REQUEST_EDIT = 200;
	public static final int REQUEST_ADD = 201;
	
	/**
	 * 编辑模式、增加模式
	 */
	
	private Object editValue;
	/**
	 * 修改
	 * @param contenxt
	 */
	public static void edit(Activity context,Class<? extends FormActivity> clazz, Serializable data){
		ActivityUtil.startActivityForResult(context, clazz, data, REQUEST_EDIT);
	}
	
	
	
	
	protected void setFormValue(Object data){
		ViewUtil.setBinddataViewValues(data, this);
	}
	
	protected void createForm(){
		
	}
	@Override
	protected void onSetContent(Bundle savedInstanceState) {
		Form form = getClass().getAnnotation(Form.class);
		if(LibConfig.DEBUG){
			if(form==null){
				throw new Error("请设置FormActivity的Form标注");
			}
		}
		
		setContentView(form.layout());
		createForm();
		Intent intent = getIntent();
		if(intent!=null){
			Bundle bundle = intent.getExtras();
			if(bundle!=null){
				Object data = bundle.get(LibConfig.DATA_NAME);
				if(data!=null){
					setFormValue(data);
					editValue = data;
				}
			}
		}
		
		
		int defaultButton = form.defaultButton();
		findViewById(defaultButton).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					Map<String, Object> formValues = onPreSubmit();
					onBeforeSubmit(formValues);
					onSubmit(formValues);
				} catch (NoSuchFieldException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					if(LibConfig.DEBUG){
						Alert.alert(FormActivity.this, e.getMessage());
					}
				} catch (InvocationTargetException e) {
					
					if(e.getCause() instanceof FormError){
						Alert.showShortToast(e.getCause().getMessage());
					}else{
						e.printStackTrace();
						if(LibConfig.DEBUG){
							Alert.alert(FormActivity.this, e.getMessage());
						}
					}
				}
			}
		});
	}

	protected abstract void onSubmit(Map<String, Object> formValues);

	/**
	 * 其他表单验证，如没有在注解中说明的
	 * 
	 * @throws FormError
	 */
	protected void onBeforeSubmit(Map<String, Object> formValues)
			throws InvocationTargetException {

	}

	/**
	 * 表单验证出错,给出用户提示
	 * 
	 * @param error
	 */
	protected void onFormError(FormError error){
		Alert.showShortToast(error.getMessage());
	}
	
	
	private InvocationTargetException getError(int errorRes,String name){
		return new InvocationTargetException( new FormError(String.format(getString(errorRes), name)) );
	}

	private Object getFormValue(View view, Class<?> type, FormElement element) throws InvocationTargetException{
		String typeName = type.getName();
		String result = null;
		if (view instanceof EditText) {
			result= ((EditText) view).getText().toString();
		} else if( view instanceof CheckBox){
			CheckBox c = (CheckBox)view;
			if(typeName.equals("int") || type.equals(Integer.class)){
				return c.isChecked() ? 1 : 0;
			}else{
				return c.isChecked();
			}
		}else if (view instanceof TextView) {
			result= ((TextView) view).getText().toString();
		} 
		// 验证
		int validate = element.validate();
		//格式校验
		if( type.equals(Integer.class) || typeName.equals("int")){
			int ret = Integer.parseInt(result);
			if ((validate & FormElement.REQUIRED) > 0) {
				if(ret==0){
					throw getError(R.string.validate_error_integer,element.name());
				}
			}
		}else if(type.equals(Float.class) || typeName.equals("float")){
			return Float.parseFloat(result);
		}else if(type.equals(Double.class) || typeName.equals("double")){
			return Double.parseDouble(result);
		}else {
			//string
			if ((validate & FormElement.REQUIRED) > 0) {
				if(TextUtils.isEmpty(result)){
					throw getError(R.string.validate_error_required,element.description());
				}
			}
			if((validate & FormElement.TEL) > 0){
				if(!ValidateUtil.isTel(result)){
					throw getError(R.string.validate_error_tel,element.description());
				}
			}
			
		}

		return result;
	}

	@SuppressWarnings("unchecked")
	private Map<String, Object> onPreSubmit() throws  NoSuchFieldException, InvocationTargetException {
		Map<String, Object> formValues = new HashMap<String, Object>();
		// 获取表单元素的值
		Form form = getClass().getAnnotation(Form.class);
		FormElement[] forms = form.forms();
		//首先校验参数
		Class<?> beanClass = form.beanClass();
		//类型校验
		boolean checkBean = true;
		if(beanClass.equals(Null.class)){
			checkBean = false;
		}
		
		for (FormElement element : forms) {
			View formElement = findViewById(element.id());
			if(formElement instanceof IWidget<?>){
				/**
				 * 一般来说，widget应该自己完成数据校验
				 */
				IWidget<?> widget = (IWidget<?>)formElement;
				Object result;
				try {
					result = widget.getValue();
					if(result instanceof Map<?, ?>){
						formValues.putAll((Map<String, Object>)result);
					}else{
						formValues.put(element.name(), result);
					}
				} catch (FormError e) {
					e.printStackTrace();
					throw new InvocationTargetException(e);
				}
				
			}else{
				Class<?> type;
				if(checkBean){
					Field field = beanClass.getField(element.name());
					type = field.getType();
				}else{
					type = element.clazz();
				}
				
				Object value = getFormValue(formElement,type,element);
				formValues.put(element.name(), value);
			}
			
		}
		return formValues;

	}
	
	@Override
	protected void onDestroy() {
		Alert.cancelWait();
		super.onDestroy();
	}
	
	/**
	 * 是否是编辑模式
	 * @return
	 */
	public boolean isEditMode(){
		return editValue!=null;
	}
	
	public Object getEditValue(){
		return editValue;
	}
}
