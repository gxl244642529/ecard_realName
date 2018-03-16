package com.citywithincity.ecard.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.citywithincity.ecard.R;
import com.citywithincity.interfaces.DialogListener;
import com.citywithincity.utils.Alert;
import com.damai.core.IdManager;
import com.damai.helper.IValue;
import com.damai.widget.FormElement;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;



public class BirthPickerView extends RelativeLayout implements FormElement,IValue{
	private TextView txtBirth;
	private Date birthBateDate;
	
	
	@SuppressLint("SimpleDateFormat") static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	public BirthPickerView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	@Override
	protected void onFinishInflate() {
		
		txtBirth = (TextView) findViewById(R.id._select_hint);
		if(txtBirth==null){
			throw new RuntimeException("FormSelectView must have a textview with id R.id._select_hint");
		}
		this.setOnClickListener(listener);
	}
	
	
	DatePicker datePicker;
	
	

	private OnClickListener listener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if(datePicker!=null){
				return;
			}
			datePicker = new DatePicker(getContext());
			
			Calendar calendar = Calendar.getInstance();
			if (birthBateDate != null) {
				calendar.setTime(birthBateDate);
			}else{
				try {
					calendar.setTime(dateFormat.parse(dateFormat.format(new Date())));
				} catch (ParseException e) {
				}
			}
			datePicker.init(calendar.get(Calendar.YEAR),
					calendar.get(Calendar.MONTH),
					calendar.get(Calendar.DAY_OF_MONTH),
					new OnDateChangedListener() {

						@Override
						public void onDateChanged(DatePicker view, int year,
								int monthOfYear, int dayOfMonth) {

						}
					});
			Alert.popup(getContext(), datePicker, "选择生日", Alert.OK_CANCEL,disDialogListener);
		}
	};
	
	private DialogListener disDialogListener = new DialogListener() {
		
		@Override
		public void onDialogButton(int id) {
			if (id == R.id._id_ok) {
				Calendar calendar = Calendar.getInstance();
				calendar.set(datePicker.getYear(),
						datePicker.getMonth(),
						datePicker.getDayOfMonth());
				birthBateDate = calendar.getTime();
				txtBirth.setText(dateFormat.format(calendar.getTime()));
				
			}
			datePicker = null;
		}
	};
	

	@Override
	public String validate(Map<String, Object> data) {
	
		if(birthBateDate==null){
			return "请选择生日";
		}
		data.put( IdManager.idToString(getContext(), getId()), txtBirth.getText().toString());
		return null;
	}

	@Override
	public void setValue(Object value) {
		//如果是text
		if(value==null){
			return;
		}
		if(value instanceof String){
			
			txtBirth.setText((String)value);
			try {
				birthBateDate = dateFormat.parse((String)value);
			} catch (ParseException e) {
				//解析错误
				
			}
		}
		
		
	}

	
	public Object getValue() {
		return txtBirth.getText().toString();
	}
}
