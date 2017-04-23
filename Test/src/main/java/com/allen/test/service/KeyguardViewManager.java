package com.allen.test.service;

import android.app.KeyguardManager;
import android.app.KeyguardManager.OnKeyguardExitResult;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.PixelFormat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;

import com.allen.test.R;
import com.allen.test.service.KeyguardUpdateMonitor.BatteryStatus;

public class KeyguardViewManager {
	final static String TAG = "CellKeyguard/KeyguardViewManager";

	private Context mContext = null;
	private KeyguardViewBase mGuardView = null;
	private KeyguardUpdateMonitor mUpdateMonitor = null;
	private WindowManager mWindowManager;
    private KeyguardManager mKeyguardManager;
    private KeyguardManager.KeyguardLock mKeyguardlock;
    
	boolean showing = false;
	boolean isScreenOn = false;
	private ViewManagerHost mKeyguardHost;

	private WindowManager.LayoutParams wlp;
	private LayoutParams params;
	
	KeyguardUpdateMonitorCallback mCallback = new KeyguardUpdateMonitorCallback() {

		@Override
		void onRefreshBatteryInfo(BatteryStatus status) {
			// TODO Auto-generated method stub
			if (isShowing() && status != null && mGuardView != null) {
			    mGuardView.onRefreshBatteryInfo(status);
			}
		}

		@Override
		void onTimeChanged() {
			if(isShowing() && mGuardView != null){
			    mGuardView.onTimeChanged();
			}
		}

		@Override
		public void onScreenTurnedOn() {
			// TODO Auto-generated method stub
		    isScreenOn = true;
		    showView();
		}

		@Override
		public void onScreenTurnedOff(int why) {
			// TODO Auto-generated method stub
		    isScreenOn =false;
		    
		}

	};
	
	public class KeyguardViewManagerCallback{
	    protected void unlock(){
	        //hideKeyguardView();
	        if(mKeyguardHost != null){
	            hideKeyguardView();
	            mWindowManager.removeView(mKeyguardHost);
	            mKeyguardHost = null;
	        }
	    }
	}

	KeyguardViewManager(Context context) {
		Log.d(TAG, "oncreate");

		mContext = context;
		mUpdateMonitor = KeyguardUpdateMonitor.getInstance(context);
		mWindowManager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
        mKeyguardManager = (KeyguardManager) mContext.getSystemService(Context.KEYGUARD_SERVICE);
        mKeyguardlock = mKeyguardManager.newKeyguardLock(TAG);
        
		mUpdateMonitor.registerCallback(mCallback);;

	}

	private void maybeCreateKeyguardHost() {
		Log.d(TAG, "maybeCreateKeyguardHost");
		if (mKeyguardHost == null) {
			mKeyguardHost = new ViewManagerHost(mContext);

			int flags =WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
			        | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
					| WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD;
			
			final int stretch = ViewGroup.LayoutParams.MATCH_PARENT;
			final int type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
			wlp = new WindowManager.LayoutParams(stretch, stretch, type, flags,
					PixelFormat.TRANSLUCENT);

			wlp.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE;
			wlp.windowAnimations = android.R.style.Animation_Toast;
			wlp.screenOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
			mWindowManager.addView(mKeyguardHost, wlp);
		}else{

            mWindowManager.updateViewLayout(mKeyguardHost, wlp);
		}
	}
    public void cleanUp(){

        if(mKeyguardHost != null){
            hideKeyguardView();
            mWindowManager.removeView(mKeyguardHost);
            mKeyguardHost = null;
        }
        mUpdateMonitor.removeCallback(mCallback);
    }
	private void hideNavigationBar(){
	    int uiOption = View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
	            |View.SYSTEM_UI_FLAG_HIDE_NAVIGATION|View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
	            int vis = mKeyguardHost.getSystemUiVisibility();
	            mKeyguardHost.setSystemUiVisibility(vis | uiOption);
	}

	public boolean isShowing() {
		return showing;
	}

	public void showView() {
		Log.d(TAG, "showView");
		mKeyguardlock.disableKeyguard();
		maybeCreateKeyguardHost();
		if (!showing) {
		    showing = true;
			inflateKeyguardView();
			mKeyguardHost.setVisibility(View.VISIBLE);
	        hideNavigationBar();
		}
	}

	private void inflateKeyguardView() {

		Log.d(TAG, "initKeyguardView");
		if(mGuardView == null){
    		final LayoutInflater inflater = LayoutInflater.from(mContext);
    		mGuardView = (KeyguardViewBase) inflater.inflate(getLayoutRes(), null);
    
    		params = new LayoutParams(LayoutParams.MATCH_PARENT,
    				LayoutParams.MATCH_PARENT);
    		mKeyguardHost.addView(mGuardView, params);
    		mGuardView.setParent(mKeyguardHost);
    		mGuardView.setViewMediatorCallback(new KeyguardViewManagerCallback());
		}

	}
    void verifyKeyguard() {
        mKeyguardManager.exitKeyguardSecurely(new OnKeyguardExitResult(){

            @Override
            public void onKeyguardExitResult(boolean success) {
                // TODO Auto-generated method stub
                if(success){
                    mKeyguardlock.reenableKeyguard();
                }
            }
            
        });
    }
	public void hideKeyguardView() {
		Log.d(TAG, "hideKeyguardView");

		mKeyguardHost.setVisibility(View.GONE);
		mKeyguardHost.removeView(mGuardView);
		mGuardView.setViewMediatorCallback(null);
		mGuardView = null;
		showing = false;
		verifyKeyguard();
	}



	private int getLayoutRes() {

		return R.layout.keyguard_slider_layout;
	}


	class ViewManagerHost extends FrameLayout {

		public ViewManagerHost(Context context) {
			this(context, null);
			// TODO Auto-generated constructor stub
		}

		public ViewManagerHost(Context context, AttributeSet attrs) {
			super(context, attrs);
			// TODO Auto-generated constructor stub
		}

	}



}// end class
