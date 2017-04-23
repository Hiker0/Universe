package com.allen.test.effect;

import java.util.ArrayList;
import java.util.List;

import com.allen.test.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

public class XferModeActivity extends Activity {
	Context mContext;
	private List<String> list = new ArrayList<String>();
	private ArrayAdapter<String> adapter; 
	private XfermodeImageView  image = null;
	PorterDuff.Mode[] modes = {
			PorterDuff.Mode.CLEAR,
			PorterDuff.Mode.SRC,
			PorterDuff.Mode.DST,
			PorterDuff.Mode.SRC_OVER,
			PorterDuff.Mode.DST_OVER,
			PorterDuff.Mode.SRC_IN,
			PorterDuff.Mode.DST_IN,
			PorterDuff.Mode.SRC_OUT,
			PorterDuff.Mode.DST_OUT,
			PorterDuff.Mode.SRC_ATOP,
			PorterDuff.Mode.DST_ATOP,
			PorterDuff.Mode.XOR,
			PorterDuff.Mode.DARKEN,
			PorterDuff.Mode.LIGHTEN,
			PorterDuff.Mode.MULTIPLY,
			PorterDuff.Mode.SCREEN
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mContext = this;
		this.setContentView(R.layout.xfermode_activity);
		image = (XfermodeImageView) this.findViewById(R.id.image);
		Spinner  sp = (Spinner) this.findViewById(R.id.spinner1);
		list.add("PorterDuff.Mode.CLEAR");
		list.add("PorterDuff.Mode.SRC");
		list.add("PorterDuff.Mode.DST");
		list.add("PorterDuff.Mode.SRC_OVER");
		list.add("PorterDuff.Mode.DST_OVER");
		list.add("PorterDuff.Mode.SRC_IN");
		list.add("PorterDuff.Mode.DST_IN");
		list.add("PorterDuff.Mode.SRC_OUT");
		list.add("PorterDuff.Mode.DST_OUT");
		list.add("PorterDuff.Mode.SRC_ATOP");
		list.add("PorterDuff.Mode.DST_ATOP");
		list.add("PorterDuff.Mode.XOR");
		list.add("PorterDuff.Mode.DARKEN");
		list.add("PorterDuff.Mode.LIGHTEN");
		list.add("PorterDuff.Mode.MULTIPLY");
		list.add("PorterDuff.Mode.SCREEN");
		adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, list);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sp.setAdapter(adapter); 
		sp.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				image.setMode(modes[position]);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			} 
			
		});
	
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

}
