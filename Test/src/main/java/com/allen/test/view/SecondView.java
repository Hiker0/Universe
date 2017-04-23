package com.allen.test.view;

import java.util.Calendar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.allen.test.R;


public class SecondView extends ImageView {
	final static String TAG =  "CellKeyguard/SecondView";
	
	Context mContext;
	Calendar mCalendar;
	int srcWidth = 0,srcHeight=0;
	int value = 0;
	Paint rectPaint = null;
	Rect rect = null;
	
	public SecondView(Context context) {
		this(context, null);
		// TODO Auto-generated constructor stub
	}
	public SecondView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		// TODO Auto-generated constructor stub
	}
	public SecondView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		
		mContext = context;
		mCalendar = Calendar.getInstance();
		

		Drawable drawable = this.getDrawable();
		srcWidth = drawable.getIntrinsicWidth();
		srcHeight = drawable.getIntrinsicHeight();
		
		rect = new Rect();
		rect.top=0;
		rect.bottom=srcHeight;
		rect.left=srcWidth;
		rect.right=srcWidth;
		
		rectPaint = new Paint();
		rectPaint.setAntiAlias(false);
		rectPaint.setStyle(Paint.Style.FILL_AND_STROKE);
		
		int color = this.getResources().getColor(R.color.SecondsColor);
		rectPaint.setColor(color);
		
	}	
	@Override
	protected void onFinishInflate() {
		// TODO Auto-generated method stub
		super.onFinishInflate();
		
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub

		this.setMeasuredDimension(srcWidth, srcHeight);
	}	
	
	@Override
	protected void onDraw(Canvas canvas) {
		
		int valueWidth = value*srcWidth/60;

		rect.left=srcWidth-valueWidth;

		canvas.drawRect(rect, rectPaint);
		
		this.getDrawable().draw(canvas);

		canvas.save();
	}

	@Override
	protected void onAttachedToWindow() {
		// TODO Auto-generated method stub
		super.onAttachedToWindow();
		
		updateTime();
	}
	@Override
	protected void onDetachedFromWindow() {
		// TODO Auto-generated method stub
		super.onDetachedFromWindow();
		removeCallbacks(mClockTick);
	}
	
	Runnable mClockTick = new Runnable(){

		@Override
		public void run() {
			// TODO Auto-generated method stub
			refresh();
			invalidate();
			postDelayed(mClockTick,1000);
		}   
		
	};
	
	private void refresh(){
		
		mCalendar.setTimeInMillis(System.currentTimeMillis());
        
        value = mCalendar.get(Calendar.SECOND);
	}
	
	public  void updateTime() {
		refresh();
		invalidate();
        removeCallbacks(mClockTick);
        post(mClockTick);
        
	}

	
	
}
