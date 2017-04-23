package com.allen.test.view;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Point;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;
import android.widget.FrameLayout;

import com.allen.test.R;
import com.allen.test.service.KeyguardUpdateMonitor.BatteryStatus;
import com.allen.test.service.KeyguardViewBase;

public class KeyguardSliderView extends KeyguardViewBase {
    final static String TAG = "CellKeyguard/KeyguardSliderView";
    final static int VER_UNLOCK_DISTANCE = 300;
    final static int HOR_UNLOCK_DISTANCE = 500;
    private BatteryView mBatteryView = null;
    private CellClockView mClockView = null;
    private DateView mDateView = null;
    private SecondView mSecondView = null;
    private HostViewListener mOnTouchListener = new HostViewListener();;
    private UnlockAnimationListener mUnlockAnimationListener;
    private ValueAnimator mUnlocAnimation;
    private final int WIDTH, HEIGHT;
    private Handler mHandler = new Handler();
    private Context mContext;
    private boolean isScreenOn = false;

    public KeyguardSliderView(Context context) {
        this(context, null);
        // TODO Auto-generated constructor stub
    }

    @SuppressLint("ClickableViewAccessibility")
    public KeyguardSliderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        mContext = context;
        WindowManager wm = (WindowManager) mContext
                .getSystemService(Context.WINDOW_SERVICE);
        Point outSize = new Point();
        wm.getDefaultDisplay().getSize(outSize);
        WIDTH = Math.max(outSize.x, outSize.y);
        HEIGHT = Math.min(outSize.x, outSize.y);
        setOnTouchListener(mOnTouchListener);
    }

    @Override
    protected void onFinishInflate() {
        // TODO Auto-generated method stub
        super.onFinishInflate();
        mBatteryView = (BatteryView) this.findViewById(R.id.battery_view);
        mClockView = (CellClockView) this.findViewById(R.id.clock_view);
        mDateView = (DateView) this.findViewById(R.id.date_view);
        mSecondView = (SecondView) this.findViewById(R.id.second_view);
    }

    @Override
    public void onScreenTurnedOff() {
        // TODO Auto-generated method stub
        isScreenOn = false;
    }

    @Override
    public void onScreenTurnedOn() {
        // TODO Auto-generated method stub
        isScreenOn = true;
    }

    @Override
    public void show() {
        // TODO Auto-generated method stub
        this.requestFocus();
        resetState();
    }

    @Override
    public void verifyUnlock() {
        // TODO Auto-generated method stub

    }

    @Override
    public void cleanUp() {
        // TODO Auto-generated method stub

    }

    @Override
    public long getUserActivityTimeout() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void onTimeChanged() {
        // TODO Auto-generated method stub
        mClockView.updateTime();
        mDateView.updateTime();
        mSecondView.updateTime();
    }

    @Override
    public void onRefreshBatteryInfo(BatteryStatus status) {
        // TODO Auto-generated method stub
        mBatteryView.updateBattery(status.level);
    }

    private void resetState() {
        mOnTouchListener.mMoveState = HostViewListener.MOVE_STATE_NONE;
    }

    private void updatePosition(int dx, int dy) {
        FrameLayout.LayoutParams params = (android.widget.FrameLayout.LayoutParams) this
                .getLayoutParams();
        Log.d(TAG, "updatePosition dx=" + dx + " dy=" + dy);
        params.rightMargin = -dx;
        params.leftMargin = dx;
        params.topMargin = dy;
        params.bottomMargin = -dy;
        parentView.updateViewLayout(this, params);
    }

    private void animateUnlock(int state) {

        if (mUnlocAnimation != null) {
            mUnlocAnimation.cancel();
        } else {
            mUnlocAnimation = ValueAnimator.ofFloat(0f, 1f);
            mUnlocAnimation.setDuration(300);
            mUnlocAnimation.setInterpolator(new LinearInterpolator());
        }

        switch (state) {
        case HostViewListener.MOVE_STATE_TOP:

            mUnlockAnimationListener = new UnlockAnimationListener(
                    -VER_UNLOCK_DISTANCE, -HEIGHT,
                    UnlockAnimationListener.UNLOCK_TYPE_TOP);
            break;
        case HostViewListener.MOVE_STATE_BOTTOM:
            mUnlockAnimationListener = new UnlockAnimationListener(
                    VER_UNLOCK_DISTANCE, HEIGHT,
                    UnlockAnimationListener.UNLOCK_TYPE_BOTTOM);
            break;
        case HostViewListener.MOVE_STATE_LEFT:
            mUnlockAnimationListener = new UnlockAnimationListener(
                    -HOR_UNLOCK_DISTANCE, -WIDTH,
                    UnlockAnimationListener.UNLOCK_TYPE_LEFT);
            break;
        case HostViewListener.MOVE_STATE_RIGHT:
            mUnlockAnimationListener = new UnlockAnimationListener(
                    HOR_UNLOCK_DISTANCE, WIDTH,
                    UnlockAnimationListener.UNLOCK_TYPE_RIGHT);
            break;
        default:
        }

        mUnlocAnimation.addUpdateListener(mUnlockAnimationListener);
        mUnlocAnimation.start();
    }

    private void doUnlock() {
        mHandler.post(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                mOnTouchListener.mMoveState = HostViewListener.MOVE_STATE_NONE;
                unlock();
            }

        });

    }

    class HostViewListener implements View.OnTouchListener {

        public final static int MOVE_STATE_NONE = -1;
        public final static int MOVE_STATE_DOWN = 0;
        public final static int MOVE_STATE_LEFT = 1;
        public final static int MOVE_STATE_RIGHT = 2;
        public final static int MOVE_STATE_TOP = 3;
        public final static int MOVE_STATE_BOTTOM = 4;
        public final static int MOVE_STATE_UNLOCK = 5;

        private Point mDownPoint = new Point();
        private int mMoveState = MOVE_STATE_NONE;

        private void resetPoint(int x, int y) {
            mDownPoint.x = x;
            mDownPoint.y = y;
        }

        private void handleMoveEvent(int dx, int dy) {
            switch (mMoveState) {
            case MOVE_STATE_DOWN:
                if (Math.abs(dx) > Math.abs(dy)) {
                    if (dx > MotionEvent.AXIS_TOUCH_MINOR) {
                        mMoveState = MOVE_STATE_RIGHT;
                    } else if (dx < -MotionEvent.AXIS_TOUCH_MINOR) {
                        mMoveState = MOVE_STATE_LEFT;
                    }
                } else {
                    if (dy > MotionEvent.AXIS_TOUCH_MINOR) {
                        mMoveState = MOVE_STATE_BOTTOM;
                    } else if (dy < -MotionEvent.AXIS_TOUCH_MINOR) {
                        mMoveState = MOVE_STATE_TOP;
                    }
                }
                break;

            case MOVE_STATE_LEFT:
                if (dx < 0) {
                    updatePosition(dx, 0);
                    if (Math.abs(dx) > HOR_UNLOCK_DISTANCE) {
                        mMoveState = MOVE_STATE_UNLOCK;
                        animateUnlock(MOVE_STATE_LEFT);
                    }
                } else {
                    updatePosition(0, 0);
                    resetPoint(mDownPoint.x + dx, mDownPoint.y);
                }
                break;
            case MOVE_STATE_RIGHT:
                if (dx > 0) {
                    updatePosition(dx, 0);
                    if (Math.abs(dx) > HOR_UNLOCK_DISTANCE) {
                        mMoveState = MOVE_STATE_UNLOCK;
                        animateUnlock(MOVE_STATE_RIGHT);
                    }
                } else {
                    updatePosition(0, 0);
                    resetPoint(mDownPoint.x + dx, mDownPoint.y);
                }
                break;
            case MOVE_STATE_TOP:
                if (dy < 0) {
                    updatePosition(0, dy);
                    if (Math.abs(dy) > VER_UNLOCK_DISTANCE) {
                        mMoveState = MOVE_STATE_UNLOCK;
                        animateUnlock(MOVE_STATE_TOP);
                    }
                } else {
                    updatePosition(0, 0);
                    resetPoint(mDownPoint.x, mDownPoint.y + dy);
                }
                break;
            case MOVE_STATE_BOTTOM:
                if (dy > 0) {
                    updatePosition(0, dy);
                    if (Math.abs(dy) > VER_UNLOCK_DISTANCE) {
                        mMoveState = MOVE_STATE_UNLOCK;
                        animateUnlock(MOVE_STATE_BOTTOM);
                    }
                } else {
                    updatePosition(0, 0);
                    resetPoint(mDownPoint.x, mDownPoint.y + dy);
                }
                break;
            default:
                break;
            }
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            // TODO Auto-generated method stub

            float x = event.getRawX();
            float y = event.getRawY();

            switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:

                mDownPoint.x = (int) x;
                mDownPoint.y = (int) y;
                if (mMoveState == MOVE_STATE_NONE) {
                    mMoveState = MOVE_STATE_DOWN;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                int dx = (int) x - mDownPoint.x;
                int dy = (int) y - mDownPoint.y;

                handleMoveEvent(dx, dy);

                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                if (mMoveState == MOVE_STATE_DOWN) {
                    mMoveState = MOVE_STATE_NONE;
                }
                if (mMoveState > MOVE_STATE_DOWN
                        && mMoveState < MOVE_STATE_UNLOCK) {
                    updatePosition(0, 0);
                    mMoveState = MOVE_STATE_NONE;
                }
            }

            return true;
        }

    }

    class UnlockAnimation extends Animation {

        int mFromXDelta, mToXDelta, mFromYDelta, mToYDelta;

        public UnlockAnimation(int fromx, int tox, int fromy, int toy) {
            mFromXDelta = fromx;
            mToXDelta = tox;
            mFromYDelta = fromy;
            mToYDelta = toy;
        }

        @Override
        public void initialize(int width, int height, int parentWidth,

        int parentHeight)

        {

            super.initialize(width, height, parentWidth, parentHeight);

            setDuration(300);

            setFillAfter(true);

            setInterpolator(new LinearInterpolator());

        }

        @Override
        protected void applyTransformation(float interpolatedTime,
                Transformation t) {
            float dx = mFromXDelta;
            float dy = mFromYDelta;
            if (mFromXDelta != mToXDelta) {
                dx = mFromXDelta
                        + ((mToXDelta - mFromXDelta) * interpolatedTime);
            }
            if (mFromYDelta != mToYDelta) {
                dy = mFromYDelta
                        + ((mToYDelta - mFromYDelta) * interpolatedTime);
            }

            updatePosition((int) dx, (int) dy);

        }

    }

    class UnlockAnimationListener implements AnimatorUpdateListener {
        final static int UNLOCK_TYPE_NONE = 0;
        final static int UNLOCK_TYPE_LEFT = 1;
        final static int UNLOCK_TYPE_RIGHT = 2;
        final static int UNLOCK_TYPE_TOP = 3;
        final static int UNLOCK_TYPE_BOTTOM = 4;
        int unlockType = UNLOCK_TYPE_NONE;
        int mFrom, mTo;

        UnlockAnimationListener(int from, int to, int type) {
            mFrom = from;
            mTo = to;
            unlockType = type;
        }

        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            // TODO Auto-generated method stub
            if (mFrom == mTo) {
                return;
            }
            float distance = mFrom;
            float value = (Float) animation.getAnimatedValue();
            distance = mFrom + ((mTo - mFrom) * value);

            switch (unlockType) {
            case UNLOCK_TYPE_LEFT:
                updatePosition((int) distance, 0);
                break;
            case UNLOCK_TYPE_RIGHT:
                updatePosition((int) distance, 0);
                break;
            case UNLOCK_TYPE_TOP:
                updatePosition(0, (int) distance);
                break;
            case UNLOCK_TYPE_BOTTOM:
                updatePosition(0, (int) distance);
                break;
            }// end switch

            if (Math.abs(distance) >= Math.abs(mTo)) {
                switch (unlockType) {
                case UNLOCK_TYPE_LEFT:
                    doUnlock();
                    break;
                case UNLOCK_TYPE_RIGHT:
                    doUnlock();
                    break;

                case UNLOCK_TYPE_TOP:
                    doUnlock();
                    break;
                case UNLOCK_TYPE_BOTTOM:
                    doUnlock();
                    break;
                }
            }

        }// end onAnimationUpdate
    }// end UnlockAnimationListener
}
