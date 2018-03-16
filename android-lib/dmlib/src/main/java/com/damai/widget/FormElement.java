package com.damai.widget;

import java.util.Map;

public interface FormElement  {
	String validate(Map<String, Object> data);
	boolean isShown();
	int getId();
}
