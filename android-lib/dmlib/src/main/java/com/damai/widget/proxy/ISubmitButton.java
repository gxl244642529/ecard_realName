package com.damai.widget.proxy;

import com.damai.core.ApiJob;
import com.damai.widget.FormSubmit;

public interface ISubmitButton extends FormSubmit,IWidgetProxy {
	ApiJob getJob();
}
