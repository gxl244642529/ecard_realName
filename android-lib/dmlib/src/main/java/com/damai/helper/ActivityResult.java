package com.damai.helper;

import android.content.Intent;

import java.io.IOException;

public interface ActivityResult {
	void onActivityResult(Intent intent,int resultCode,int requestCode);
}
