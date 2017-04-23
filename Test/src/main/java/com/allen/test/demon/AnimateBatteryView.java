package com.allen.test.demon;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

public class AnimateBatteryView extends ImageView {
    final static String TAG = "AnimateBatteryView";
    final static int BATTERY_LOWPOWER_WARNING_LEVEL = 5;
    final static int BATTERY_LOWPOWER_WARNING_COLOR = Color.RED;
    final static int ANIMATIOR_DURATION = 2000;
    final static float DEFAULT_LEFT_BOUND = 6.0F;
    final static float DEFAULT_RIGHT_BOUND = 5.0F;
    private boolean needAnimate = true;
    private int mLevel = 95;
    private int width, height;
    final private Rect mFrame = new Rect();
    private ValueAnimator mAnimator = null;
    private float sugestPos = 0;

    public AnimateBatteryView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        // TODO Auto-generated constructor stub
    }

    public AnimateBatteryView(Context context, AttributeSet attrs,
            int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub
        Drawable drawable = this.getDrawable();
        int srcWith = drawable.getIntrinsicWidth();
        int srcHeight = drawable.getIntrinsicHeight();
        width = srcWith;
        height = srcHeight;
        mFrame.top = 0;
        mFrame.left = 0;
        mFrame.right = width;
        mFrame.bottom = height;
        this.setMeasuredDimension(width, height);
        updateAnimator();
    }

    public void setNeedAnimate(boolean need) {
        if (needAnimate != need) {
            needAnimate = need;
            updateAnimator();
        }
    }

    public void setLevel(int level) {
        mLevel = level;
        updateAnimator();
    }

    @Override
    public void setVisibility(int visibility) {
        // TODO Auto-generated method stub
        super.setVisibility(visibility);
        updateAnimator();
    }

    
    @Override
    protected void onAttachedToWindow() {
        // TODO Auto-generated method stub
        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        // TODO Auto-generated method stub
        if (mAnimator != null) {
            mAnimator.cancel();
            mAnimator = null;
        }
        super.onDetachedFromWindow();
    }
    private int getPosition() {
        Log.d(TAG,"getPosition");
        int pos = 0;
        if (needAnimate) {
            pos = (int) sugestPos;
        } else {
            pos = (int) getRightFromLevel(mLevel);
        }

        return pos;
    }

    private void updateAnimator() {
        if (mAnimator != null) {
            mAnimator.cancel();
            mAnimator = null;
        }
        int vis = this.getVisibility();
        Log.d(TAG,"updateAnimator needAnimate ="+needAnimate + " vis" +vis);
        if (needAnimate && vis == View.VISIBLE) {
            mAnimator = ValueAnimator.ofFloat(getRightFromLevel(mLevel),
                    (width - DEFAULT_RIGHT_BOUND));
            mAnimator.setDuration(ANIMATIOR_DURATION);
            mAnimator.setRepeatCount(ValueAnimator.INFINITE);
            mAnimator
                    .addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            // TODO Auto-generated method stub
                            sugestPos = (Float) animation.getAnimatedValue();
                            Log.d(TAG,"onAnimationUpdate sugestPos="+sugestPos);
                            invalidate();
                        }
                    });
            mAnimator.start();
        }else{
            invalidate();
        }
    }

    private float getRightFromLevel(int level) {
        float right = 0;
        float validwidth = width - DEFAULT_LEFT_BOUND - DEFAULT_LEFT_BOUND;
        right = (int) (DEFAULT_LEFT_BOUND + validwidth * level / 100);
        return right;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        Drawable drawable = this.getDrawable();
        drawable.setAlpha(0xaa);
        drawable.setBounds(0, 0, width, height);
        drawable.draw(canvas);
        int sc = canvas.saveLayer(0, 0, width, height, null,
                Canvas.MATRIX_SAVE_FLAG | Canvas.CLIP_SAVE_FLAG
                        | Canvas.HAS_ALPHA_LAYER_SAVE_FLAG
                        | Canvas.FULL_COLOR_LAYER_SAVE_FLAG
                        | Canvas.CLIP_TO_LAYER_SAVE_FLAG);
        drawable.setAlpha(0xff);
        mFrame.right = getPosition();
        canvas.clipRect(mFrame);
        drawable.draw(canvas);
        if (!needAnimate && mLevel < BATTERY_LOWPOWER_WARNING_LEVEL) {
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setColor(BATTERY_LOWPOWER_WARNING_COLOR);
            PorterDuffXfermode iMode = new PorterDuffXfermode(
                    PorterDuff.Mode.SRC_IN);
            paint.setXfermode(iMode);
            canvas.drawRect(mFrame, paint);
            paint.setXfermode(null);
        }
        canvas.restoreToCount(sc);
    }

}
