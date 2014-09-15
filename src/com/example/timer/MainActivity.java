package com.example.timer;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.app.ActionBar;
import android.app.AlarmManager;
import android.app.Fragment;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ParseException;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RemoteViews;
import android.os.Build;

public class MainActivity extends AppWidgetProvider {

    private boolean isTimeComplete = false;
    private int days, hours, minutes, seconds;
    private PendingIntent pendingIntent;
    
    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);

        Log.v("Testing", "onEnabled called");
        
        AlarmManager alarmManager = (AlarmManager) context
                .getSystemService(Context.ALARM_SERVICE);

        ComponentName thisWidget = new ComponentName(context,
                MainActivity.class);
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        manager.updateAppWidget(thisWidget,
                new RemoteViews(context.getPackageName(),
                        R.layout.activity_main));
        int appWidgetIds[] = manager.getAppWidgetIds(thisWidget);
        manager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.text_value);

        Intent alarm = new Intent(context, MainActivity.class);
        alarm.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        alarm.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
        pendingIntent = PendingIntent.getBroadcast(context, 0, alarm,
                    PendingIntent.FLAG_CANCEL_CURRENT);
        alarmManager.setRepeating(AlarmManager.RTC, System.currentTimeMillis(),
                1000, pendingIntent);
        
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
            int[] appWidgetIds) {
    	Log.v("Testing", "onUpdate called");
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                R.layout.activity_main);
        ComponentName timerWidget = new ComponentName(context,
                MainActivity.class);

        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss",
                Locale.US);
        Date d2 = null;
        try {
            d2 = format.parse("2014/10/04 10:00:00");
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (java.text.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        Calendar cal = Calendar.getInstance();

        long diff = d2.getTime() - cal.getTime().getTime();
        seconds = (int) (diff / 1000) % 60;
        minutes = (int) ((diff / (1000 * 60)) % 60);
        hours = (int) ((diff / (1000 * 60 * 60)) % 24);
        days = (int) (diff / (1000 * 60 * 60 * 24));
        remoteViews.setTextViewText(R.id.text_remaining,
                "Remaining text");
        remoteViews.setTextViewText(
        R.id.text_value,
        String.format("%d days",
                days)
        + " "
        + String.format("%d hh",
                hours)
        + " "
        + String.format("%d mm",
                minutes)
        + " "
        + String.format("%d ss",
                seconds));
        appWidgetManager.updateAppWidget(timerWidget, remoteViews);
    }
}