package com.damai.react;

import android.view.LayoutInflater;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.damai.interfaces.PopupListener;
import com.damai.react.views.MonthPicker;
import com.damai.widget.Dialogs;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by renxueliang on 16/10/29.
 */

public class DatePickerModule extends ReactContextBaseJavaModule {


    public DatePickerModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return "DatePickerModule";
    }


    private String getString(int time){
        if(time<10){
            return "0" + time;
        }
        return String.valueOf(time);
    }

    /**
     * 日期选择器
     * @param date
     * @param title
     * @param type
     * @param callback
     */
    @ReactMethod
    public void select( String date, String title, String type, ReadableMap opts, final Callback callback) throws ParseException {
        final Calendar calendar = Calendar.getInstance();
        if(type.equals("date")){

            DatePicker datePicker = new DatePicker(getCurrentActivity());

            final SimpleDateFormat  dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            if(date!=null){
                calendar.setTime(dateFormat.parse(date));
            }

            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

            datePicker.init( year, month,dayOfMonth, new DatePicker.OnDateChangedListener() {
                @Override
                public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    calendar.set(Calendar.YEAR,year);
                    calendar.set(Calendar.MONTH,monthOfYear);
                    calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                }
            });


            //现在的maxDate
            if(opts.hasKey("min")){
                String min = opts.getString("min");
                datePicker.setMinDate(dateFormat.parse(min).getTime());
            }

            if(opts.hasKey("max")){
                String max = opts.getString("max");
                datePicker.setMaxDate(dateFormat.parse(max).getTime());
            }

            Dialogs.createBottomPopup(getCurrentActivity(), datePicker, title, new PopupListener<DatePicker>() {
                @Override
                public void onPopup(int id, DatePicker contentView) {
                    if(id== R.id._id_ok){
                        callback.invoke(dateFormat.format(calendar.getTime()));
                    }
                }
            });
        }else if(type.equals("time")){
            TimePicker datePicker = new TimePicker(getCurrentActivity());
            final SimpleDateFormat  dateFormat = new SimpleDateFormat("HH:mm");
            if(date!=null){
                calendar.setTime(dateFormat.parse(date));
            }
            datePicker.setIs24HourView(true);
            datePicker.setCurrentHour(calendar.get(Calendar.HOUR_OF_DAY));
            datePicker.setCurrentMinute(calendar.get(Calendar.MINUTE));

            datePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
                @Override
                public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {

                }
            });

            Dialogs.createBottomPopup(getCurrentActivity(), datePicker, title, new PopupListener<TimePicker>() {
                @Override
                public void onPopup(int id, TimePicker contentView) {
                    if(id== R.id._id_ok){
                        callback.invoke(getString(contentView.getCurrentHour()) + ":" + getString(contentView.getCurrentMinute()));
                    }
                }
            });

        }else if(type.equals("month")){
            MonthPicker picker = (MonthPicker) LayoutInflater.from(getCurrentActivity()).inflate(R.layout.month_picker,null);
            final SimpleDateFormat  dateFormat = new SimpleDateFormat("yyyy-MM");
            if(date!=null){
                calendar.setTime(dateFormat.parse(date));
            }


            if(opts.hasKey("min")){
                String min = opts.getString("min");
                picker.setMinDate(dateFormat.parse(min).getTime());
            }

            if(opts.hasKey("max")){
                String max = opts.getString("max");
                picker.setMaxDate(dateFormat.parse(max).getTime());
            }


            picker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH));
            Dialogs.createBottomPopup(getCurrentActivity(), picker, title, new PopupListener<MonthPicker>() {
                @Override
                public void onPopup(int id, MonthPicker contentView) {
                    if(id== R.id._id_ok){
                        calendar.set(Calendar.YEAR,contentView.getYear());
                        calendar.set(Calendar.MONTH,contentView.getMonth());


                        callback.invoke(dateFormat.format(calendar.getTime()));
                    }
                }
            });
        }

    }

}
