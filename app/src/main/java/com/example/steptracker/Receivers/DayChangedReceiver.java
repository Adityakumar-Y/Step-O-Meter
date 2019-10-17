package com.example.steptracker.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.example.steptracker.Interface.DayChangedListner;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DayChangedReceiver extends BroadcastReceiver {

    private Date date = new Date();
    private SimpleDateFormat df = new SimpleDateFormat("yyMMdd", Locale.getDefault());
    private DayChangedListner listner;


    public DayChangedReceiver(DayChangedListner listner) {
        this.listner = listner;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        Date currentDate = new Date();

        if(!(isSameDay(currentDate))){
            listner.onDayChanged();
            date = currentDate;
        }
    }

    private boolean isSameDay(Date currentDate) {
        return (df.format(currentDate).equals(df.format(date)));
    }

}
