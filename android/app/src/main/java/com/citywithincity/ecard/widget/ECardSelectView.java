package com.citywithincity.ecard.widget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.TypedArray;
import android.nfc.tech.TagTechnology;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.citywithincity.ecard.R;
import com.citywithincity.ecard.nfc.ECardNfcModel;
import com.citywithincity.ecard.utils.MyECardUtil;
import com.citywithincity.ecard.utils.NfcUtil;
import com.damai.auto.LifeManager;
import com.damai.helper.ActivityResult;
import com.damai.helper.ActivityResultContainer;
import com.jzoom.nfc.NfcException;
import com.jzoom.nfc.NfcListener;

import java.io.IOException;

public class ECardSelectView extends RelativeLayout implements ActivityResult,  com.jzoom.nfc.NfcListener {

	@Override
	public void onNfcEvent(TagTechnology tag) {
        try {
            onCardIdChanged(model.getCardId());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NfcException e) {
            e.printStackTrace();
        }
    }

	public interface ECardSelectViewListener{
		void onSelectECard(String cardId);
	}

	private ECardNfcModel model;
	
	private EditText editText;
	public ECardSelectView(Context context, AttributeSet attrs) {
		super(context, attrs);
		if(isInEditMode())return;
		TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.ecardSelect);

		
		a.recycle();
		model = new ECardNfcModel(LifeManager.getActivity());
		model.setListener(this);

		//NfcUtil.createNfcModel(LifeManager.getCurrent(),mode, this);
	}
	ECardSelectViewListener selectListener;
	
	public void setListener(ECardSelectViewListener selectListener){
		this.selectListener = selectListener;
	}

	@Override
	protected void onFinishInflate() {
		if(isInEditMode())return;
		View selEcard = findViewById(R.id._select);
		if(selEcard!=null){
			selEcard.setOnClickListener(listener);
		}
		for(int i=0 , c = getChildCount(); i < c; ++i){
			View view = getChildAt(i);
			if(view instanceof EditText){
				editText = (EditText) view;
				break;
			}
		}
		if(editText == null){
			throw new RuntimeException("找不到EditText");
		}
	}
	@Override
	protected void onDetachedFromWindow() {
		selectListener = null;
		super.onDetachedFromWindow();
	}
	
	private OnClickListener listener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			MyECardUtil.selectECard(ECardSelectView.this);
		}
	};
	
	public ActivityResultContainer getActivity(){
		return (ActivityResultContainer)getContext();
	}


	@Override
	public void onActivityResult(Intent intent, int resultCode, int requestCode) {
		if(resultCode==Activity.RESULT_OK ){
			String cardId = MyECardUtil.getCardId(intent);
			
			onCardIdChanged(cardId);
		}
	}
	
	private void onCardIdChanged(String cardId){
		if(!editText.getText().toString().equals(cardId)){
			editText.setText(cardId);
			editText.setError(null);
			if(selectListener!=null){
				selectListener.onSelectECard(cardId);
			}
		}
		
	}



}
