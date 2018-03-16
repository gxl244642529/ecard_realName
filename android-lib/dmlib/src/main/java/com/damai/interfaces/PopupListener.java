package com.damai.interfaces;

import android.view.View;

public interface PopupListener<T extends View>{
	void onPopup(int id,T contentView);
}
