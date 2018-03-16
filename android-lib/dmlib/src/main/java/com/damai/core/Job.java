package com.damai.core;

import com.citywithincity.interfaces.IDestroyable;

public interface Job extends IDestroyable {
	void setId(String id);

	String getId();

	void cancel();
	boolean isDestroied();
	void setCanceled(boolean isCanceled);

	boolean isCanceled();
}
