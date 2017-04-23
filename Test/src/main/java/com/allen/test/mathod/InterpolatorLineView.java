package com.allen.test.mathod;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.animation.Interpolator;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class InterpolatorLineView extends View {

	final static int LINE_COLOR = Color.BLUE;
	final static int ARROW_COLOR=Color.GRAY;
	final static int PADDING=30;
	Interpolator mInterpolator;
	Paint mPointPaint;
	Paint mArrowPaint;
	
	
	public InterpolatorLineView(Context context) {
		this(context, null);
		// TODO Auto-generated constructor stub
	}
	public InterpolatorLineView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		// TODO Auto-generated constructor stub
	}
	public InterpolatorLineView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		mInterpolator = new LeCubicBezierInterpolator(0.33f,0,0.15f,1.0f);
		
		mPointPaint = new Paint();
		mPointPaint.setColor(LINE_COLOR);
		mPointPaint.setStrokeWidth(1);
		
		mArrowPaint = new Paint();
		mArrowPaint.setColor(ARROW_COLOR);
		mArrowPaint.setStrokeWidth(2);
		// TODO Auto-generated constructor stub
	}

	public void setInterPolator(Interpolator interpolator){
		mInterpolator = interpolator;
		invalidate();
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);  
        int heightSize = MeasureSpec.getSize(heightMeasureSpec); 
        int size = Math.min(widthSize, heightSize);
        Log.d("allen","size="+size);
        setMeasuredDimension(size, size);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		float size = this.getWidth();

		canvas.drawLine(0, size-PADDING, size, size-PADDING, mArrowPaint);
		canvas.drawLine(size-PADDING, size , size, size-PADDING, mArrowPaint);
		
		canvas.drawLine(PADDING, 0, PADDING, size, mArrowPaint);
		canvas.drawLine(0, PADDING , PADDING, 0, mArrowPaint);
		
		float drawSize = size-PADDING*2;
		for(int i=0; i<drawSize;i++){
			
			float ratio =  1.0f*i/drawSize;
			float y =(1.0f - mInterpolator.getInterpolation(ratio)) * drawSize;
			canvas.drawPoint(PADDING+i, PADDING+y, mPointPaint);
		}
		
	}

}
