package com.allen.test.demon;

import java.util.Random;

import com.allen.test.R;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RemoteViews;

public class WidgetProvider extends AppWidgetProvider {
	static final String TAG = "allen";
	static final String CLICK_ACTION = "com.test.CLICK";
	RemoteViews mRemoteViews;
	AppWidgetManager mAppWidgetManager;

	// 没接收一次广播消息就调用一次，使用频繁
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		Log.d(TAG, "onReceive");
		super.onReceive(context, intent);
	    Uri data = intent.getData();  
	    int widgetID = -1;  
	    if(data != null){  
	    	widgetID = Integer.parseInt(data.getSchemeSpecificPart());  
	    	int color = 0;
			Random rd = new Random();
			int r = rd.nextInt(255);
			int g = rd.nextInt(255);
			int b = rd.nextInt(255);
			color = Color.rgb(r, g, b);

			RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
			remoteViews.setTextColor(R.id.id_text, color);
			AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
			if (widgetID > 0) {
				appWidgetManager.updateAppWidget(widgetID, remoteViews);
			}
	    }  
	    

	}

	// 每次更新都调用一次该方法，使用频繁
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		// TODO Auto-generated method stub
		Log.d(TAG, "update--->");
		super.onUpdate(context, appWidgetManager, appWidgetIds);

		mAppWidgetManager = appWidgetManager;

		for (int i = 0; i < appWidgetIds.length; i++) {
			Intent intent = new Intent();
			intent.setClass(context, WidgetProvider.class);
			intent.setData(Uri.parse("harvic:" + appWidgetIds[i]));
			//intent.putExtra("WIDGET_ID", appWidgetIds[i]);
			Log.d(TAG, "WIDGET_ID:"+appWidgetIds[i] );

			PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
			mRemoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
			mRemoteViews.setOnClickPendingIntent(R.id.id_btn, pendingIntent);

			appWidgetManager.updateAppWidget(appWidgetIds[i], mRemoteViews);
		}

	}

	// 没删除一个就调用一次
	public void onDeleted(Context context, int[] appWidgetIds) {
		// TODO Auto-generated method stub
		Log.d(TAG, "Deleted");
		for (int i = 0; i < appWidgetIds.length; i++) {
			Log.d(TAG, appWidgetIds[i] + ";");
		}
		super.onDeleted(context, appWidgetIds);
	}

	// 当该Widget第一次添加到桌面是调用该方法，可添加多次但只第一次调用
	public void onEnabled(Context context) {
		// TODO Auto-generated method stub
		Log.d(TAG, "OnEnable");
		super.onEnabled(context);
	}

	// 当最后一个该Widget删除是调用该方法，注意是最后一个
	public void onDisabled(Context context) {
		// TODO Auto-generated method stub
		Log.d(TAG, "onDisable");
		ImageView view = null;
		
		super.onDisabled(context);
	}

	@Override
	public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId,
			Bundle newOptions) {
		// TODO Auto-generated method stub
		Log.d(TAG, "onAppWidgetOptionsChanged");
		super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
	}

	@Override
	public void onRestored(Context context, int[] oldWidgetIds, int[] newWidgetIds) {
		// TODO Auto-generated method stub
		Log.d(TAG, "onRestored");
		super.onRestored(context, oldWidgetIds, newWidgetIds);
	}

}
