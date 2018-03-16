package com.damai.widget.proxy;

import com.damai.interfaces.ITab;
import com.damai.widget.OnTabChangeListener;

public interface ITabView extends IWidgetProxy, ITab {
	void setOnTabChangeListener(OnTabChangeListener listener);
}
