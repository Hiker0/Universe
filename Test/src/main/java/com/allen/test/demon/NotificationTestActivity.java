package com.allen.test.demon;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;

import com.allen.test.R;

public class NotificationTestActivity extends Activity implements
        OnClickListener {
    static final String TAG = "NotificationTestActivity";
    static final int NOTIFICATION_NORMAL_BEGIN = 10;
    static final int NOTIFICATION_ONGOING_BEGIN = 20;
    static final int NOTIFICATION_WITH_INTENT_BEGIN = 30;
    static final int NOTIFICATION_WHEN_BEGIN = 40;
    int normal_count = 0;
    int ongoing_count = 0;
    int with_intent_count = 0;
    int when_count = 0;
    private Context mContext;
    NotificationManager NM = null;
    private Handler mHandler = new Handler();
    int ids[] = { R.id.bt_normal, R.id.bt_normal_cancel, R.id.bt_ongo,
            R.id.bt_ongol_cancel, R.id.bt_with_intent, R.id.bt_intent_cancel,
            R.id.bt_when, R.id.bt_when_cancel, };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setWindowFlag();
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.notification_test);
        NM = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        for (int id : ids) {
            Button button = (Button) findViewById(id);
            button.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
        case R.id.bt_normal:
            sendNormalNotification();
            break;
        case R.id.bt_normal_cancel:
            cancelNormalNotification();
            break;
        case R.id.bt_ongo:
            sendOngoing();
            break;
        case R.id.bt_ongol_cancel:
            cancelOngoing();
            break;
        case R.id.bt_with_intent:
            sendWithIntent();
            break;
        case R.id.bt_intent_cancel:
            cancelWithIntent();
            break;
        case R.id.bt_when:
            sendTimeNotification();
            break;
        case R.id.bt_when_cancel:
            cancelTimeNotification();
            break;
        default:

        }
    }

    void setWindowFlag() {
        Window win = this.getWindow();
        int flags = LayoutParams.FLAG_DISMISS_KEYGUARD
        // | LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | LayoutParams.FLAG_TURN_SCREEN_ON;
        win.addFlags(flags);
    }

    void sendNormalNotification() {

        Intent saver = new Intent();
        saver.setClass(this, Demon.class);

        saver.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);

        if (normal_count < 10 && NM != null) {
            normal_count++;
            Notification.Builder builder = new Notification.Builder(this);
            builder.setTicker("Normal Notification(" + normal_count + ")");
            builder.setSmallIcon(R.drawable.ic_launcher);
            builder.setContentText("Normal Notification(" + normal_count + ")");
            builder.setWhen(System.currentTimeMillis());
            builder.setNumber(3);
            builder.setContentIntent(PendingIntent.getActivity(mContext, 0,
                    saver, 0, null));
            NM.notify(NOTIFICATION_NORMAL_BEGIN + normal_count, builder.build());

            Log.d(TAG, "Notify id="
                    + (NOTIFICATION_NORMAL_BEGIN + normal_count));
        }
    }

    void cancelNormalNotification() {
        for (int id = normal_count; id > 0; id--) {
            NM.cancel(id + NOTIFICATION_NORMAL_BEGIN);
            Log.d(TAG, "Cancel id=" + (NOTIFICATION_NORMAL_BEGIN + id));
        }
        normal_count = 0;
    }

    void sendOngoing() {
        if (ongoing_count < 10 && NM != null) {
            ongoing_count++;
            Notification.Builder builder = new Notification.Builder(this);
            builder.setTicker("Ongoing Notification(" + ongoing_count + ")");
            builder.setSmallIcon(R.drawable.ic_launcher);
            builder.setContentText("Ongoing Notification(" + ongoing_count
                    + ")");
            builder.setWhen(System.currentTimeMillis());
            builder.setOngoing(true);
            NM.notify(NOTIFICATION_ONGOING_BEGIN + ongoing_count,
                    builder.build());

            Log.d(TAG, "Notify id="
                    + (NOTIFICATION_ONGOING_BEGIN + ongoing_count));
        }
    }

    void cancelOngoing() {
        for (int id = ongoing_count; id > 0; id--) {
            NM.cancel(id + NOTIFICATION_ONGOING_BEGIN);
            Log.d(TAG, "Cancel id=" + (NOTIFICATION_ONGOING_BEGIN + id));
        }
        ongoing_count = 0;
    }

    void sendWithIntent() {
        if (with_intent_count < 10 && NM != null) {
            with_intent_count++;
            Intent intent = new Intent(this, NotificationTestActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_NEW_TASK);
            PendingIntent pintent = PendingIntent.getActivity(this, 1, intent,
                    PendingIntent.FLAG_ONE_SHOT);
            Notification.Builder builder = new Notification.Builder(this);
            builder.setTicker("Intent Notification(" + with_intent_count + ")");
            builder.setSmallIcon(R.drawable.ic_launcher);
            builder.setContentText("Intent Notification(" + with_intent_count
                    + ")");
            builder.setWhen(System.currentTimeMillis());
            builder.setContentIntent(pintent);

            Intent demon = new Intent();
            demon.setClass(this, Demon.class);

            demon.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            builder.addAction(R.drawable.ic_launcher, "To Demon",
                    PendingIntent.getActivity(mContext, 0, demon, 0, null));
            
            Intent saver = new Intent();
            saver.setClassName("com.phicomm.security", "com.phicomm.security.savepower.LongWaitlActivity");
            saver.putExtra("enter_saver", true);
            saver.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            builder.addAction(R.drawable.ic_launcher, "To power saver",
                    PendingIntent.getActivity(mContext, 0, saver, 0, null));
            NM.notify(NOTIFICATION_WITH_INTENT_BEGIN + with_intent_count,
                    builder.build());

            Log.d(TAG, "Notify id="
                    + (NOTIFICATION_WITH_INTENT_BEGIN + with_intent_count));
        }
    }

    void cancelWithIntent() {
        for (int id = with_intent_count; id > 0; id--) {
            NM.cancel(id + NOTIFICATION_WITH_INTENT_BEGIN);
            Log.d(TAG, "Cancel id=" + (NOTIFICATION_WITH_INTENT_BEGIN + id));
        }
        with_intent_count = 0;
    }

    void sendTimeNotification() {
        long now = System.currentTimeMillis();

        if (when_count < 10 && NM != null) {
            when_count++;
            long when = when_count * 5000;
            mHandler.postDelayed(new Runnable() {

                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    Notification.Builder builder = new Notification.Builder(
                            NotificationTestActivity.this);
                    builder.setTicker("When Notification(" + when_count + ")");
                    builder.setSmallIcon(R.drawable.ic_launcher);
                    builder.setContentText("When Notification(" + when_count
                            + ")");
                    builder.setNumber(3);
                    builder.setDefaults(Notification.DEFAULT_SOUND
                            | Notification.DEFAULT_VIBRATE);
                    builder.setShowWhen(true);
                    NM.notify(NOTIFICATION_WHEN_BEGIN + when_count,
                            builder.build());
                    Log.d(TAG, "Notify id="
                            + (NOTIFICATION_WHEN_BEGIN + when_count));
                }

            }, when);
        }
    }

    void cancelTimeNotification() {
        for (int id = when_count; id > 0; id--) {
            NM.cancel(id + NOTIFICATION_WHEN_BEGIN);
            Log.d(TAG, "Cancel id=" + (NOTIFICATION_WHEN_BEGIN + id));
        }
        when_count = 0;
    }
}
