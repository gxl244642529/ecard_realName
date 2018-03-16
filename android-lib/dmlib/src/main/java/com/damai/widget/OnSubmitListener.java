package com.damai.widget;

import java.util.Map;

/**
 * 当表单提交的时候
 * @author Randy
 *
 */
public interface OnSubmitListener {
	/**
	 * 是否应该提交表单
	 * @param formView
	 * @param data
	 * @return
	 */
	boolean shouldSubmit(Form formView,Map<String, Object> data);
}
