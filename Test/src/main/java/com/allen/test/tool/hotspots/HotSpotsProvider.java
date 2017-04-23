package com.allen.test.tool.hotspots;

import java.io.IOException;

import com.allen.test.R;
import com.allen.test.tool.hotspots.WeiboUtil.ParamsBuilder;
import com.allen.test.tool.hotspots.WeiboUtil.Spot;
import com.allen.test.tool.hotspots.WeiboUtil.Spots;
import com.google.gson.Gson;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class HotSpotsProvider extends AppWidgetProvider {
	final int[] mSpotsItemIDs = { R.id.id_0_0, R.id.id_0_1, R.id.id_1_0, R.id.id_1_1, R.id.id_2_0, R.id.id_2_1,
			R.id.id_3_0, R.id.id_3_1, R.id.id_4_0, R.id.id_4_1, };

	RemoteViews mRemoteViews;
	OkHttpClient mOkHttpClient;
	Spots mSpots = null;
	AppWidgetManager mAppWidgetManager;
	Context mContext;

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		super.onReceive(context, intent);
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		// TODO Auto-generated method stub
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		Log.i("allen","onUpdate");
		if(mOkHttpClient == null){
			mOkHttpClient = new OkHttpClient();
		}
		mAppWidgetManager = appWidgetManager;
		mContext = context;
		mRemoteViews = new RemoteViews(context.getPackageName(), R.layout.hotspots_widget_layout);
		requestData();
		//appWidgetManager.updateAppWidget(appWidgetIds, mRemoteViews);
	}

	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		// TODO Auto-generated method stub
		super.onDeleted(context, appWidgetIds);
		Log.i("allen","onDeleted");
	}

	@Override
	public void onEnabled(Context context) {
		// TODO Auto-generated method stub
		super.onEnabled(context);
		Log.i("allen","onEnabled");
		mOkHttpClient = new OkHttpClient();
	}

	@Override
	public void onDisabled(Context context) {
		// TODO Auto-generated method stub
		super.onDisabled(context);
		Log.i("allen","onDisabled");
	}

	void requestData() {
		int count = mSpotsItemIDs.length;
		ParamsBuilder builder = new ParamsBuilder(WeiboUtil.APP_KEY, WeiboUtil.SID, null, count);
		Log.i("allen","builder:"+builder.toString());

		Request request = new Request.Builder().addHeader("charset", "utf-8").url(builder.toString()).build();

		mOkHttpClient.newCall(request).enqueue(new Callback() {

			@Override
			public void onFailure(Call arg0, IOException arg1) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onResponse(Call arg0, Response arg1) throws IOException {
				// TODO Auto-generated method stub
				ResponseBody body =  arg1.body();
				String data = body.string();
				updateUI(data);
				Log.i("allen",data);
			}
		});
	}
	
	void updateUI(String json){
		Gson gson = new Gson();
		Spots spots= gson.fromJson(json, Spots.class);
		spots.format();
		
		for (int i = 0; i < mSpotsItemIDs.length; i++) {
			Spot spot = spots.data.get(i);
			Intent intent = new Intent();
			intent.setAction(Intent.ACTION_VIEW);
			intent.setData(WeiboUtil.getWeiboUri(spot.title));
			
			PendingIntent pi = PendingIntent.getActivity(mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
			mRemoteViews.setTextViewText(mSpotsItemIDs[i], spot.title);
			mRemoteViews.setOnClickPendingIntent(mSpotsItemIDs[i], pi);
		}
		
		mAppWidgetManager.updateAppWidget(new ComponentName(mContext, HotSpotsProvider.class), mRemoteViews);
		
	}

	void checkData(){
		
	}
}
