package com.allen.test.service;

import com.allen.test.R;

import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.service.dreams.DreamService;
import android.widget.ImageView;

public class StarDeam extends DreamService {


	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
	}

	@Override
	public void onAttachedToWindow() {
		// TODO Auto-generated method stub
		super.onAttachedToWindow();
		this.setContentView(R.layout.star_dream_view);
	}

	@Override
	public void onDetachedFromWindow() {
		// TODO Auto-generated method stub
		super.onDetachedFromWindow();
	}

	@Override
	public void onDreamingStarted() {
		// TODO Auto-generated method stub
		super.onDreamingStarted();
		ImageView starImageView = (ImageView) findViewById(R.id.image3);
		Drawable starDrawable = starImageView.getDrawable();
		if (starDrawable instanceof Animatable) {
			((Animatable) starDrawable).start();
		}
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

}
