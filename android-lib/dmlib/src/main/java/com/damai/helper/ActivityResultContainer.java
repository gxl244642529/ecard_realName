package com.damai.helper;

import android.content.Intent;

public interface ActivityResultContainer {
	void startActivityForResult(ActivityResult result,Intent intent,int requestCode);
}
