package com.allen.test.demon;

import com.allen.test.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;

public class VectorDrawableActivity extends Activity {
	Context mContext;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mContext = this;
		this.setContentView(R.layout.vector_activity);
		ImageView androidImageView = (ImageView) findViewById(R.id.image2);
        Drawable drawable = androidImageView.getDrawable();
        if (drawable instanceof Animatable) {
            ((Animatable) drawable).start();
        }
        
		ImageView starImageView = (ImageView) findViewById(R.id.image3);
        Drawable starDrawable = starImageView.getDrawable();
        if (starDrawable instanceof Animatable) {
            ((Animatable) starDrawable).start();
        }
        
		ImageView squareImageView = (ImageView) findViewById(R.id.image4);
        Drawable squareDrawable = squareImageView.getDrawable();
        if (squareDrawable instanceof Animatable) {
            ((Animatable) squareDrawable).start();
        }
		
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
