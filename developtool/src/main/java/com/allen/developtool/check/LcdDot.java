package com.allen.developtool.check;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;

public class LcdDot extends Activity implements OnClickListener {
	
	private View contentView;
	private int[] COLOR={0xffff0000,0xff00ff00,0xff0000ff,0xffffff00,0xffff00ff
			
	};
	int curColor = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		contentView = new View(this);
		setContentView(contentView);
		setColor(curColor);
		contentView.setOnClickListener(this);
	}
	
	private void setColor(int index){
		contentView.setBackgroundColor(COLOR[index]);
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
	public void onClick(View v) {
		// TODO Auto-generated method stub
		curColor++;
		if(curColor > COLOR.length-1){
			curColor = 0;
		}
		setColor(curColor);
	}

	

}
