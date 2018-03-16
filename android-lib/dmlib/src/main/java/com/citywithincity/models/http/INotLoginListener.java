package com.citywithincity.models.http;






interface INotLoginListener {
	boolean onNotLogin(AbsJsonTask<?> task);
	void onRequireAccessToken(AbsJsonTask<?> task);
}
