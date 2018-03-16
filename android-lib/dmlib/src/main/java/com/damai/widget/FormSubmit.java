package com.damai.widget;

/**
 * 提交按钮
 * @author Randy
 *
 */
public interface FormSubmit {
	
	/**
	 * 默认情况下忽略不可见元素
	 */
	public static final int SubmitPolicy_IgnoreNotShown = 0;
	
	/**
	 * 全部提交
	 */
	public static final int SubmitPolicy_All = 1;
	/**
	 * 提交
	 */
	void submit(int submitPolicy);
	
	void submit();

	void setForm(Form form);
	
	
	void setOnSubmitListener(OnSubmitListener listener);
}
