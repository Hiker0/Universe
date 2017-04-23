package com.allen.test.effect;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

public class XfermodeImageView extends ImageView {
	final static int TYPE_CIRCLE = 1;
	final static int TYPE_ROUND = 2;
	final static int TYPE_OVAL  = 3;
	int mWidth = 0;
	int mHeight =  0;
	private PorterDuffXfermode iMode= null;
	private Bitmap mSrc = null;
	private Bitmap mDst = null;
	public XfermodeImageView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		// TODO Auto-generated constructor stub
	}

	public XfermodeImageView(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
	}

	  @Override
	  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
	    // TODO Auto-generated method stub
	    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	    //如果类型是圆形，则强制设置view的宽高一致，取宽高的较小值 
	    mWidth = getMeasuredWidth();
	    mHeight = getMeasuredHeight();
	    if(mSrc == null){
	    	mSrc = getDrawBitmap(mWidth/2,mWidth/2,TYPE_CIRCLE,0xffb3e5fc);
	    }
	    if(mDst == null){
	    	mDst = getDrawBitmap(mWidth/2,mWidth/2,TYPE_ROUND,0xffa7ffeb);
	    }
	  }
	  
	@Override
	protected void onAttachedToWindow() {
		// TODO Auto-generated method stub
		super.onAttachedToWindow();
		
	}

	@Override
	protected void onDetachedFromWindow() {
		// TODO Auto-generated method stub
	    if(mSrc != null){
	    	mSrc.recycle();
	    }
	    if(mDst != null){
	    	mDst.recycle();
	    }
		super.onDetachedFromWindow();
	}

	@Override
	public void draw(Canvas canvas) {
		// TODO Auto-generated method stub
		canvas.drawColor(0xfffff176); 
		
		int sc = canvas.saveLayer(0, 0, mWidth, mHeight, null,  
                Canvas.MATRIX_SAVE_FLAG | Canvas.CLIP_SAVE_FLAG  
                        | Canvas.HAS_ALPHA_LAYER_SAVE_FLAG  
                        | Canvas.FULL_COLOR_LAYER_SAVE_FLAG  
                        | Canvas.CLIP_TO_LAYER_SAVE_FLAG);
		
		Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setColor(0xffb3e5fc);
		canvas.drawCircle(mWidth/4,mWidth/4, mWidth/4,
				paint);
		if(iMode != null){
			paint.setXfermode(iMode);
		};
		paint.setColor(0xffa7ffeb);
		//canvas.drawBitmap(mDst, mWidth/4, mWidth/4, paint);
		canvas.drawRoundRect(new RectF(mWidth/4,mWidth/4, mWidth*3/4, mWidth*3/4),
				20, 20, paint);
		//canvas.drawBitmap(mSrc, 0, 0, paint);
		paint.setXfermode(null);
		canvas.restoreToCount(sc);
		
	}

	@Override
	public void invalidate() {
		// TODO Auto-generated method stub

		super.invalidate();
	}

	public void setMode(PorterDuff.Mode mode) {
		iMode = new PorterDuffXfermode(mode);
		invalidate();

	}
	
	/**
	 * 绘制不同的图形Bitmap
	 */
	private Bitmap getDrawBitmap(int width, int height,int mType,int color) {
		Bitmap bitmap = null;
		if (mType == TYPE_CIRCLE) {// 绘制圆形
			bitmap = Bitmap.createBitmap(width, width,
					Bitmap.Config.ARGB_8888);
			Canvas canvas = new Canvas(bitmap);
			Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
			paint.setColor(color);
			canvas.drawCircle(width / 2, width / 2, width / 2,
					paint);
		} else if (mType == TYPE_ROUND) {// 绘制圆角矩形
			bitmap = Bitmap.createBitmap(width, height,
					Bitmap.Config.ARGB_8888);
			Canvas canvas = new Canvas(bitmap);
			Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
			paint.setColor(color);
			canvas.drawRoundRect(new RectF(0, 0, width, height),
					20, 20, paint);
		} else if (mType == TYPE_OVAL) {
			bitmap = Bitmap.createBitmap(width, height,
					Bitmap.Config.ARGB_8888);
			Canvas canvas = new Canvas(bitmap);
			Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
			paint.setColor(color);
			canvas.drawOval(new RectF(0, 0, width, height), paint);
		}
		return bitmap;
	}
}
