/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.allen.test.service;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
//import android.os.ServiceManager;
import android.telephony.TelephonyManager;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.allen.test.service.KeyguardUpdateMonitor.BatteryStatus;
//import android.media.IAudioService;
import com.allen.test.service.KeyguardViewManager.KeyguardViewManagerCallback;

/**
 * Base class for keyguard view.  {@link #reset} is where you should
 * reset the state of your view.  Use the {@link KeyguardViewCallback} via
 * {@link #getCallback()} to send information back (such as poking the wake lock,
 * or finishing the keyguard).
 *
 * Handles intercepting of media keys that still work when the keyguard is
 * showing.
 */
public abstract class KeyguardViewBase extends RelativeLayout {

    private AudioManager mAudioManager;
    private TelephonyManager mTelephonyManager = null;
    protected ViewGroup parentView = null;
    protected KeyguardViewManager.KeyguardViewManagerCallback mKeyguardViewManagerCallback;

    Context mContext;
    public KeyguardViewBase(Context context) {
        this(context, null);
    }

    public KeyguardViewBase(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }
    public void setParent(ViewGroup group){
        parentView = group;
    }

    abstract public void onTimeChanged();
    abstract public void onRefreshBatteryInfo(BatteryStatus status);
    /**
     * Called when the screen turned off.
     */
    abstract public void onScreenTurnedOff();

    /**
     * Called when the screen turned on.
     */
    abstract public void onScreenTurnedOn();

    /**
     * Called when the view needs to be shown.
     */
    abstract public void show();

    /**
     * Verify that the user can get past the keyguard securely.  This is called,
     * for example, when the phone disables the keyguard but then wants to launch
     * something else that requires secure access.
     *
     * The result will be propogated back via {@link KeyguardViewCallback#keyguardDone(boolean)}
     */
    abstract public void verifyUnlock();

    /**
     * Called before this view is being removed.
     */
    abstract public void cleanUp();

    /**
     * Gets the desired user activity timeout in milliseconds, or -1 if the
     * default should be used.
     */
    abstract public long getUserActivityTimeout();

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (interceptMediaKey(event)) {
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    /**
     * Allows the media keys to work when the keyguard is showing.
     * The media keys should be of no interest to the actual keyguard view(s),
     * so intercepting them here should not be of any harm.
     * @param event The key event
     * @return whether the event was consumed as a media key.
     */
    private boolean interceptMediaKey(KeyEvent event) {
        final int keyCode = event.getKeyCode();
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_MEDIA_PLAY:
                case KeyEvent.KEYCODE_MEDIA_PAUSE:
                case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:
                    if (mTelephonyManager == null) {
                        mTelephonyManager = (TelephonyManager) getContext().getSystemService(
                                Context.TELEPHONY_SERVICE);
                    }
                    if (mTelephonyManager != null &&
                            mTelephonyManager.getCallState() != TelephonyManager.CALL_STATE_IDLE) {
                        return true;  // suppress key event
                    }
                case KeyEvent.KEYCODE_MUTE:
                case KeyEvent.KEYCODE_HEADSETHOOK:
                case KeyEvent.KEYCODE_MEDIA_STOP:
                case KeyEvent.KEYCODE_MEDIA_NEXT:
                case KeyEvent.KEYCODE_MEDIA_PREVIOUS:
                case KeyEvent.KEYCODE_MEDIA_REWIND:
                case KeyEvent.KEYCODE_MEDIA_RECORD:
                case KeyEvent.KEYCODE_MEDIA_FAST_FORWARD:
                case KeyEvent.KEYCODE_MEDIA_AUDIO_TRACK: {
                    
                    return true;
                }

                case KeyEvent.KEYCODE_VOLUME_UP:
                case KeyEvent.KEYCODE_VOLUME_DOWN:
                case KeyEvent.KEYCODE_VOLUME_MUTE: {
                        return false;
                }
            }
        } else if (event.getAction() == KeyEvent.ACTION_UP) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_MUTE:
                case KeyEvent.KEYCODE_HEADSETHOOK:
                case KeyEvent.KEYCODE_MEDIA_PLAY:
                case KeyEvent.KEYCODE_MEDIA_PAUSE:
                case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:
                case KeyEvent.KEYCODE_MEDIA_STOP:
                case KeyEvent.KEYCODE_MEDIA_NEXT:
                case KeyEvent.KEYCODE_MEDIA_PREVIOUS:
                case KeyEvent.KEYCODE_MEDIA_REWIND:
                case KeyEvent.KEYCODE_MEDIA_RECORD:
                case KeyEvent.KEYCODE_MEDIA_FAST_FORWARD:
                case KeyEvent.KEYCODE_MEDIA_AUDIO_TRACK: {
                    
                    return true;
                }
            }
        }
        return false;
    }

    protected void unlock(){
        mKeyguardViewManagerCallback.unlock();
    }

    @Override
    public void dispatchSystemUiVisibilityChanged(int visibility) {
        super.dispatchSystemUiVisibilityChanged(visibility);

    }

    public void setViewMediatorCallback(
            KeyguardViewManagerCallback keyguardViewManagerCallback) {
        mKeyguardViewManagerCallback = keyguardViewManagerCallback;
    }

}
