package com.damai.widget;

import java.util.List;

public interface Form {
	List<FormElement> getElements();

	FormElement getElement(String name);
	
	/**
	 * 是否持久化
	 * @return
	 */
	boolean isPersistent();

	void persistent();
	
}
