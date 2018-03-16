package com.citywithincity.interfaces;

import android.app.Activity;
import android.view.View;

import com.damai.core.ILife;
import com.damai.helper.ActivityResultContainer;

/**
 * 此接口用于实体类的数据绑定
 * @author randy
 *
 */
public interface IViewContainer extends ActivityResultContainer {
	View findViewById(int id);
	Activity getActivity();
	void addLife(ILife life);
	View findViewByName(String name);
	int getViewId(String name);
	void finish();
	String idToString(int id);
}
