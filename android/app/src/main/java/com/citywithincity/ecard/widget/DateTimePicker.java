package com.citywithincity.ecard.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.citywithincity.ecard.R;
import com.citywithincity.interfaces.DialogListener;
import com.citywithincity.interfaces.IDestroyable;
import com.citywithincity.utils.Alert;

public class DateTimePicker implements IDestroyable {

	public interface IDateTimePickerListener {
		void onDateTimeSelected(DateTimePicker picker);
	}

	private TimePicker timePicker;
	private DatePicker datePicker;

	public DateTimePicker(Context context, String title,
			final IDateTimePickerListener listener) {

		View contentView = LayoutInflater.from(context).inflate(
				R.layout.date_time_picker, null);
		Alert.popup(context, contentView, title, Alert.OK_CANCEL,
				new DialogListener() {

					@Override
					public void onDialogButton(int id) {
						if (id == R.id._id_ok) {
							listener.onDateTimeSelected(DateTimePicker.this);
						}
					}
				});

		datePicker = (DatePicker) contentView.findViewById(R.id.date_picker);
		timePicker = (TimePicker) contentView.findViewById(R.id.time_picker);

	}

	@Override
	public void destroy() {
		timePicker = null;
		datePicker = null;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(datePicker.getYear()).append('-')
				.append(datePicker.getMonth()).append('-')
				.append(datePicker.getDayOfMonth()).append(' ')
				.append(timePicker.getCurrentHour()).append(':')
				.append(timePicker.getCurrentMinute());
		return sb.toString();
	}

}
