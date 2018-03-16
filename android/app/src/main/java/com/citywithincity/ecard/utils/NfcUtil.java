package com.citywithincity.ecard.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.nfc.NfcAdapter;
import android.os.Build;

import com.citywithincity.interfaces.IViewContainer;

public class NfcUtil {


	/**
	 * nfc是否可用
	 * @param context
	 * @return
	 */
	public static boolean isAvailable(Context context){
		if (android.os.Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD_MR1) {
			NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(context);
			return nfcAdapter != null;
		}
		return false;
	}
	
	
}
