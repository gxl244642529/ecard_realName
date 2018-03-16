package com.damai.react.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.AbstractWheelTextAdapter;

/**
 * Created by renxueliang on 16/11/9.
 */

public class MonthPicker extends LinearLayout implements OnWheelChangedListener {


    private WheelView monthView;
    private WheelView dayView;
    int startYear;
    int endYear;
    int currentYear;
    int currentMonth;

    public MonthPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private class MonthAdapter extends AbstractWheelTextAdapter {

        public MonthAdapter(Context context) {
            super(context);
        }

        @Override
        public int getItemsCount() {
            return 12;
        }

        @Override
        protected CharSequence getItemText(int index) {
            return String.format("%d月",index+1);
        }
    }
    private class YearAdapter extends AbstractWheelTextAdapter {

        public YearAdapter(Context context) {
            super(context);
        }

        @Override
        public int getItemsCount() {
            return endYear - startYear +1 ;
        }


        @Override
        protected CharSequence getItemText(int index) {
            return String.valueOf(startYear + index);
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        monthView = (WheelView) getChildAt(0);
        dayView = (WheelView)getChildAt(1);


        dayView.addChangingListener(this);
        monthView.addChangingListener(this);
    }

    public void init(int year,int month){
        currentMonth = month;
        currentYear = year;
        List<String> list = new ArrayList<String>();
        //2000年到现在
        startYear = year < 2000 ? year : 2000;
        endYear = year > 2099 ? year : 2099;
        int index= year - startYear;
        monthView.setViewAdapter(new YearAdapter(getContext()));
        monthView.setCurrentItem(index, WheelView.UserMode);
    }


    public void setMinDate(long date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(date));
        //
        
    }

    public void setMaxDate(long date){

    }

    public int getYear(){
        return monthView.getCurrentItem() + startYear;
    }
    public int getMonth(){
        return dayView.getCurrentItem();
    }

    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue, int mode) {
        if(wheel==monthView){
            currentYear = startYear + oldValue;
            dayView.setViewAdapter(new MonthAdapter(getContext()));
            dayView.setCurrentItem(currentMonth, WheelView.UserMode);
        }
    }
}
