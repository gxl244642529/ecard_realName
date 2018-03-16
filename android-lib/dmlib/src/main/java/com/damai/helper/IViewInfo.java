package com.damai.helper;

import android.view.View;

public interface IViewInfo {
	DataSetter createSetter(View view) ;
	String getName();
}
