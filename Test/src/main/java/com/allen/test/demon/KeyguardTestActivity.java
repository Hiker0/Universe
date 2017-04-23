package com.allen.test.demon;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.KeyguardManager;
import android.app.KeyguardManager.OnKeyguardExitResult;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;

import com.android.internal.policy.IKeyguardShowCallback;
import com.android.internal.policy.IKeyguardExitCallback;
import com.android.internal.policy.IKeyguardService;
import com.allen.test.R;

public class KeyguardTestActivity extends Activity implements OnClickListener {
    static final String TAG = "KeyguardTestActivity";
    static final String KEYGUARD_PACKAGE = "com.android.systemui";
    static final String KEYGUARD_CLASS = "com.android.systemui.keyguard.KeyguardService";

    private Context mContext;
    private IKeyguardService mKeyguardService;
    private KeyguardManager mKeyguardManager;
    private KeyguardManager.KeyguardLock mKeyguardlock;
    private RemoteServiceConnection mConnection;

    int ids[] = { R.id.bt_disable_keyguard, R.id.bt_enable_keyguard,
            R.id.bt_toggle_black, R.id.bt_toggle_write, R.id.bt_verify_keyguard,
            R.id.start_keyguard_service, R.id.stop_keyguard_service };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setWindowFlag();
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.keyguard_test);
        for (int id : ids) {
            Button button = (Button) findViewById(id);
            button.setOnClickListener(this);
        }
        mConnection = new RemoteServiceConnection();
        //checkService();
        mKeyguardManager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
        mKeyguardlock = mKeyguardManager.newKeyguardLock(TAG);
    }

    private boolean checkService() {
        if (mKeyguardService == null) {
            Log.v(TAG, "KeyguardService is null ! Reconnect");
            Intent service = new Intent();
            service.setClassName(KEYGUARD_PACKAGE, KEYGUARD_CLASS);
            try {
                if (!bindService(service, mConnection, Context.BIND_AUTO_CREATE)) {
                    Log.v(TAG, "FAILED TO BIND TO KEYGUARD!");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
        case R.id.bt_disable_keyguard:
            disableKeyguard();
            break;
        case R.id.bt_enable_keyguard:
            enableKeyguard();
            break;
        case R.id.bt_toggle_black:

            break;
        case R.id.bt_toggle_write:
            break;
        case R.id.bt_verify_keyguard:
           verifyKeyguard();
            break;
        case R.id.start_keyguard_service:
            startKeyguardService();
            break;
        case R.id.stop_keyguard_service:
            stopKeyguardService();
            break;
        default:

        }
    }
    Handler mHandler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
        }
        
    };
    
    private final static String PACKAGE_NAME = "com.allen.test";
    private final static String CLASS_NAME = "com.allen.test.service.KeyguardService";
    
    private void startKeyguardService(){
        Intent intent = new Intent();
        intent.setClassName(PACKAGE_NAME, CLASS_NAME);
        mContext.startService(intent);
    }
    private void stopKeyguardService(){
        Intent intent = new Intent();
        intent.setClassName(PACKAGE_NAME, CLASS_NAME);
        mContext.stopService(intent);
    }
    private class RemoteServiceConnection implements ServiceConnection {

        public void onServiceDisconnected(ComponentName className) {
            Log.v(TAG, "onServiceDisconnected()");
            mKeyguardService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // TODO Auto-generated method stub
            Log.v(TAG, "onServiceConnected()");
            mKeyguardService = IKeyguardService.Stub.asInterface(service);
            try {
                mKeyguardService.asBinder().linkToDeath(
                        new IBinder.DeathRecipient() {
                            @Override
                            public void binderDied() {
                                new AlertDialog.Builder(
                                        KeyguardTestActivity.this)
                                        .setMessage("Oops! Keygued died")
                                        .setPositiveButton("OK", null).show();
                            }
                        }, 0);
            } catch (RemoteException e) {
                Log.w(TAG, "Couldn't linkToDeath");
                e.printStackTrace();
            }
        }
    };

    void setWindowFlag() {
         Window win = this.getWindow();
         int flags = LayoutParams.FLAG_DISMISS_KEYGUARD
         | LayoutParams.FLAG_SHOW_WHEN_LOCKED
         | LayoutParams.FLAG_TURN_SCREEN_ON;
         win.addFlags(flags);
    }

    @SuppressWarnings("deprecation")
    void disableKeyguard() {
            mKeyguardlock.disableKeyguard();

    }

    @SuppressWarnings("deprecation")
    void enableKeyguard() {
            mKeyguardlock.reenableKeyguard();
    }

    @SuppressWarnings("deprecation")
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

    void startStatusbarColorActivity(int color) {
    }

    void sendTimeNotification() {
    }

    void cancelTimeNotification() {
    }
}
