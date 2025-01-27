package io.github.aidenkoog.testdriver.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.PowerManager;
import android.os.SystemClock;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.util.Log;

import io.github.aidenkoog.testdriver.operator.OperatorManager;
import io.github.aidenkoog.testdriver.operator.OperatorManager.Status;
import io.github.aidenkoog.testdriver.R;
import io.github.aidenkoog.testdriver.service.ObserverService;
import io.github.aidenkoog.testdriver.view.BleIotActivity;

import static io.github.aidenkoog.testdriver.operator.OperatorManager.Status.IDLE_STATUS;
import static io.github.aidenkoog.testdriver.operator.OperatorManager.Status.TRIGGER_STATUS;
import static io.github.aidenkoog.testdriver.operator.OperatorManager.Status.ACK_STATUS;
import static io.github.aidenkoog.testdriver.operator.OperatorManager.Status.TAKE_STATUS;

import java.util.Observable;
import java.util.Observer;

public class BleIoTService extends Service
        implements Observer, Handler.Callback {

    private static final String TAG = BleIoTService.class.getSimpleName();

    private static final int MSG_LED_RED    = 1;
    private static final int MSG_LED_YELLOW = 2;
    private static final int MSG_LED_GREEN  = 3;
    private static final int MSG_LED_OFF    = 4;

    private static final int LONG_CLICK_DURATION = 3000;

    private OperatorManager mOperatorManager;
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mParams;
    private View mView;
    private ImageView mImageView;
    
    private int mLastResourceId;
    private float START_X, START_Y;
    private int PREV_X, PREV_Y;
    private int MAX_X = -1, MAX_Y = -1;
    private boolean isSOSButtonPress = false;

    private Thread mThread;
    private Status mStatus = IDLE_STATUS;
    private Handler mHandler = new Handler(this);

    private PowerManager.WakeLock mWakeLock;

    private LedControlThread mControlThread = new LedControlThread();

    class LedControlThread implements Runnable {

        private static final int LED_BLINK_DURATION = 500;

        private boolean mIsOn = false;

        private void ledOff() {
            if (mHandler != null) {
                mHandler.sendEmptyMessage(MSG_LED_OFF);
            }
        }

        private void ledReverse() {
            mIsOn = !mIsOn;
            if (mStatus == IDLE_STATUS) {
                if (mHandler != null) {
                    mHandler.sendEmptyMessage(MSG_LED_OFF);
                }
            } else if (mStatus == TAKE_STATUS) {
                if (mHandler != null) {
                    mHandler.sendEmptyMessage(MSG_LED_GREEN);
                }
            } else {
                if (mIsOn == true) {
                    if (mStatus == ACK_STATUS) {
                        if (mHandler != null) {
                            mHandler.sendEmptyMessage(MSG_LED_YELLOW);
                        }
                    } else {
                        if (mHandler != null) {
                            mHandler.sendEmptyMessage(MSG_LED_RED);
                        }
                    }
                } else {
                    if (mHandler != null) {
                        mHandler.sendEmptyMessage(MSG_LED_OFF);
                    }
                }
            }
        }

        @Override
        public void run() {
            mIsOn = false;
            try {
                while (!Thread.currentThread().isInterrupted()
                                            && mStatus != IDLE_STATUS) {
                    ledReverse();
                    Thread.sleep(LED_BLINK_DURATION);
                }
            } catch (InterruptedException e) {
                Log.e(TAG, "LedControlThread is finished");
            } finally {
                ledOff();
                mIsOn = false;
            }
        }
    }

    private OnTouchListener mViewTouchListener = new OnTouchListener() {
        @Override public boolean onTouch(View v, MotionEvent event) {
            switch(event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if(MAX_X == -1)
                        setMaxPosition();
                    START_X = event.getRawX();
                    START_Y = event.getRawY();
                    PREV_X = mParams.x;
                    PREV_Y = mParams.y;
                    break;
                case MotionEvent.ACTION_MOVE:
                    int x = (int)(event.getRawX() - START_X);
                    int y = (int)(event.getRawY() - START_Y);

                    mParams.x = PREV_X + x;
                    mParams.y = PREV_Y + y;

                    optimizePosition();
                    mWindowManager.updateViewLayout(mView, mParams);
                    break;
            }
            return true;
        }
    };

    private OnTouchListener mViewSOSListener = new OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch(event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    isSOSButtonPress = true;
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (isSOSButtonPress) {
                                Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                                vibrator.vibrate(1000);
                                mOperatorManager.eventSos();
                            }
                        }
                    }, LONG_CLICK_DURATION);
                    break;
                case MotionEvent.ACTION_UP:
                    isSOSButtonPress = false;
                    break;
            }
            return true;
        }
    };

    @Override
    public IBinder onBind(Intent arg0) { return null; }
    
    @Override
    public void onCreate() {
        super.onCreate();

        LayoutInflater inflate = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mParams = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT);
        } else {
            mParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        }

        mParams.gravity = Gravity.LEFT | Gravity.TOP;
        mParams.x = 100;
        mParams.y = 600;

        mView = inflate.inflate(R.layout.view_in_service, null);
        mImageView = mView.findViewById(R.id.image_sos);
        mView.setOnTouchListener(mViewTouchListener);
        mView.findViewById(R.id.btn_sos).setOnTouchListener(mViewSOSListener);
        mLastResourceId = R.drawable.item_round_grey;
        mImageView.setImageResource(mLastResourceId);

        mWindowManager.addView(mView, mParams);

        mOperatorManager = OperatorManager.getInstance();
        ObserverService.getInstance().addObserver(OperatorManager.class.getSimpleName(), this);
        mOperatorManager.eventPowerOn();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand:" + intent);
        super.onStartCommand(intent, flags, startId);

        Intent in = new Intent(this, BleIotActivity.class);
        in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent =
                PendingIntent.getActivity(this, 0, in, PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder builder;

        if (Build.VERSION.SDK_INT >= 26) {
            String CHANNEL_ID = "snwodeer_service_channel";
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                "SnowDeer Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT);

            ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE))
                .createNotificationChannel(channel);
            builder = new NotificationCompat.Builder(this, CHANNEL_ID);
        } else {
            builder = new NotificationCompat.Builder(this);
        }

        builder.setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent);

        startForeground(1, builder.build());

        PowerManager powerManager = (PowerManager)getSystemService(Context.POWER_SERVICE);

        if (powerManager != null) {
            mWakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, TAG);
            mWakeLock.acquire();
        }

        return START_NOT_STICKY;
    }

    private void setMaxPosition() {
        DisplayMetrics matrix = new DisplayMetrics();
        mWindowManager.getDefaultDisplay().getMetrics(matrix);
        
        MAX_X = matrix.widthPixels - mView.getWidth();
        MAX_Y = matrix.heightPixels - mView.getHeight();
    }
    
    private void optimizePosition() {
        if(mParams.x > MAX_X) mParams.x = MAX_X;
        if(mParams.y > MAX_Y) mParams.y = MAX_Y;
        if(mParams.x < 0) mParams.x = 0;
        if(mParams.y < 0) mParams.y = 0;
    }

    @Override
    public void update(Observable observed, Object arg) {
        mStatus = (Status)arg;
        switch (mStatus) {
            //  Stop LED Service
            case IDLE_STATUS:
                if (mThread != null && mThread.isAlive()) {
                    mThread.interrupt(); 
                }
                mThread = null;
                break;
            //  Start LED Service
            case TRIGGER_STATUS:
            case ACK_STATUS:
            case TAKE_STATUS:
                if (mThread == null) {
                    mThread = new Thread(mControlThread);
                    mThread.start();
                }
                break;
        }
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case MSG_LED_RED:
                mLastResourceId = R.drawable.item_round_red;
                break;
            case MSG_LED_YELLOW:
                mLastResourceId = R.drawable.item_round_yellow;
                break;
            case MSG_LED_GREEN:
                mLastResourceId = R.drawable.item_round_green;
                break;
            case MSG_LED_OFF:
                mLastResourceId = R.drawable.item_round_white;
                break;
        }
        mImageView.setImageResource(mLastResourceId);
        return false;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        setMaxPosition();
        optimizePosition();
    }
    
    @Override
    public void onDestroy() {
        mOperatorManager.clear();

        if (mHandler != null) {
            mHandler = null;
        }
        if (mWakeLock.isHeld()) {
            mWakeLock.release();
        }
        if(mWindowManager != null) {
            if(mView != null) mWindowManager.removeView(mView);
        }

        super.onDestroy();
    }
}
