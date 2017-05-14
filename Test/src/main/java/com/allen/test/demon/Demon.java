package com.allen.test.demon;

import java.util.ArrayList;

import com.allen.test.R;
import com.allen.test.demon.Rxjava.RxJavaActivity;
import com.allen.test.effect.XferModeActivity;
import com.allen.test.effect.XferModeTest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Demon extends Activity {

	ListView listView = null;
	ArrayList<String> list = new ArrayList<String>();;
	
	Class[] classes = {
			GreenDaoActivity.class,
	        KeyguardTestActivity.class,
			NotificationTestActivity.class,
			WeatherDemon.class,
			VectorDrawableActivity.class,
	        FaceDetect.class,
			ImageEffectActivity.class,
			XferModeActivity.class,
			XferModeTest.class,
			ShortCutTest.class,
			RxJavaActivity.class,
	};
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setWindowFlag();
		this.setContentView(R.layout.main_activity);
	
		
		for(Class cls:classes){
			list.add(cls.getSimpleName());
		}
		listView = (ListView)this.findViewById(R.id.list);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1 , list);
		
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(clicklistener);

	}
	AdapterView.OnItemClickListener clicklistener = new AdapterView.OnItemClickListener(){

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			Intent intent = new Intent();
			intent.setClass(getApplication(), classes[arg2]);
			startActivity(intent);
		}
		
	};
	
    void setWindowFlag() {
        Window win = this.getWindow();
        int flags = LayoutParams.FLAG_DISMISS_KEYGUARD
         | LayoutParams.FLAG_SHOW_WHEN_LOCKED;
        win.addFlags(flags);
        
    }
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}
	
}
