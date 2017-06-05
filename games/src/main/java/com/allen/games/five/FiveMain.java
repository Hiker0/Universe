package com.allen.games.five;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.LinearLayout;
import com.allen.games.five.GlobalSetting.Style;
import com.allen.games.five.SetController.*;
import com.allen.games.R;

public class FiveMain extends Activity {
    final static String TAG = "five";
    PanelView panelView;
    FrameLayout rootView,panelContainer;
    SetInfoPanel mInfoPanel;
    PlayControlPanel mPlayPanel;
    DisplayMetrics outMetrics;
    PanelInfo mPanelInfo;
    Theme mTheme;
    SetController mSetController;
    SoundPool mSound;
    GlobalSetting mSettings;
    int fallSoundId = 0;

    JudgeCallBack mCallback = new JudgeCallBack(){

        @Override
        public void onStart() {
            // TODO Auto-generated method stub
            panelView.invalidate();
        }
        @Override
        public void onGameWin(Result result) {
            // TODO Auto-generated method stub
            panelView.invalidate();
            StringBuilder sb= new StringBuilder();
            if(result.winer == Dot.DOT_BLACK){
                sb.append(FiveMain.this.getString(R.string.set_black_sucess)); 
            }else{
                sb.append(FiveMain.this.getString(R.string.set_white_sucess));
            }
            if(result.steps.size() < 5){
                sb.append(FiveMain.this.getString(R.string.set_timeout));
            }
            mPlayPanel.showSuccess(sb.toString());
        }

        @Override
        public void onDoStep(SetController.Step step) {
            // TODO Auto-generated method stub
            playSound(fallSoundId);
            panelView.invalidate();
        }

        @Override
        public void refreshUI() {
            // TODO Auto-generated method stub
            panelView.invalidate();
        }
        @Override
        public void onExit() {
            // TODO Auto-generated method stub
            FiveMain.this.finish();
        }
        @Override
        public void onStepDone(SetController.Step step) {
            // TODO Auto-generated method stub
            if(mSettings.getMode() == GlobalSetting.MODE_SINGLE
                    && mSetController.getState() == SetState.SET_START){
                
            } 
        }
       
    };
    enum Dot {
        DOT_NULL(0), 
        DOT_WHITE(1), 
        DOT_BLACK(-1);
        
        int value;
        static Dot[] valueToDot = new Dot[]{DOT_BLACK,DOT_WHITE,DOT_NULL};
        Dot(int v){
            value = v;
        }
        
        static Dot getOpponent(Dot dot){
            return valueToDot[1+dot.value* (-1)];   
        }
    }


    class Theme {
        Style style;
        float mSoundVolume = 0.1f;
        Bitmap backImage;
        Bitmap whiteImage;
        Bitmap blackImage;

        void recycle() {
            if (backImage != null) {
                backImage.recycle();
            }
            if (whiteImage != null) {
                whiteImage.recycle();
            }
            if (blackImage != null) {
                blackImage.recycle();
            }
        }
    }

    class PanelInfo {

        Style style;
        int padding = 20;
        int width = 0;
        int height = 0;
        int row = 0;
        int line = 0;
        float grid = 0;
        int topMargin = 0;
        int leftOffset = 0;
        float anchorX = 0.5f;
        float anchorY = 0.5f;
        float anchorX_screen = 0;
        float anchorY_screen = 0f;
        ArrayList<Point> points;
        LayoutParams LP;

        PanelInfo(GlobalSetting.Style style) {
            this.style = style;

            points = new ArrayList<Point>();

            if (style == Style.STYLE_FULL_SCREEN) {
                row = 17;
                line = 13;

                points.add(new Point(3, 4));
                points.add(new Point(3, 8));
                points.add(new Point(3, 12));

                points.add(new Point(6, 4));
                points.add(new Point(6, 8));
                points.add(new Point(6, 12));

                points.add(new Point(9, 4));
                points.add(new Point(9, 8));
                points.add(new Point(9, 12));
            } else {
                row = 15;
                line = 15;

                points.add(new Point(3, 3));
                points.add(new Point(3, 7));
                points.add(new Point(3, 11));

                points.add(new Point(7, 3));
                points.add(new Point(7, 7));
                points.add(new Point(7, 11));

                points.add(new Point(11, 3));
                points.add(new Point(11, 7));
                points.add(new Point(11, 11));
            }
        }

        void offsetDx(float dx) {
            offset(leftOffset + dx);
        }

        void offset(float x) {
            leftOffset = (int) (x);
            if (width + leftOffset < outMetrics.widthPixels) {
                leftOffset = outMetrics.widthPixels - width;
            } else if (leftOffset > 0) {
                leftOffset = 0;
            }
        }
        void setAnchor(float x, float y){
            anchorX = 1.0f*x/width;
            anchorY = 1.0f*y/height;
            anchorX_screen = x + leftOffset;
            anchorY_screen = y+topMargin;
        }
        
        void zoom(float distance) {
            float value = distance;

            float intentWith =width + value;
            if (intentWith > outMetrics.heightPixels) {
                intentWith = outMetrics.heightPixels;
            } else if (intentWith < outMetrics.widthPixels) {
                intentWith = outMetrics.widthPixels;
            }

            // calculate offset
            width = (int) intentWith;
            
            
            float offset =anchorX_screen - width * anchorX;
            offset(offset);

            height = mPanelInfo.width;

            grid = (1.0f * width - 2 * padding)
                    / (line - 1);
            topMargin = (outMetrics.heightPixels - height)/2;
            LP = new LayoutParams(width, height);
            LP.topMargin = topMargin;
            LP.leftMargin = leftOffset;
            panelContainer.updateViewLayout(panelView, LP);
        }
    }

   
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setWindow();
        setContentView(R.layout.five_main);

        outMetrics = new DisplayMetrics();
        Display display = this.getWindowManager().getDefaultDisplay();
        display.getMetrics(outMetrics);
        mSettings = GlobalSetting.getInstance();
        iniThemes();
        initPanel(mTheme.style);

        rootView = (FrameLayout) findViewById(R.id.root);
        panelContainer = (FrameLayout) findViewById(R.id.panel_container); 
        panelView = new PanelView(this);
        panelContainer.addView(panelView, mPanelInfo.LP);
        
        LinearLayout panel = (LinearLayout) findViewById(R.id.set_info_panel);
        mInfoPanel = new SetInfoPanel(this,panel);
        LinearLayout playpanel = (LinearLayout) findViewById(R.id.play_ctr_panel);
        mSetController = new SetController(mPanelInfo.row, mPanelInfo.line, mCallback);
        mSetController.setTimeout(90);
        mSetController.setPanel(mInfoPanel);
        //mSetController.start(Dot.DOT_BLACK);
        mPlayPanel = new PlayControlPanel(mSetController,playpanel);
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        if(mSetController != null){
            mSetController.pause();
        }
        if(!mPlayPanel.isShowing()){
            if(mSetController.getState() == SetState.SET_STOP ){
                mPlayPanel.showStart();
            }else{
                mPlayPanel.show(false); 
            }
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        if(mSetController != null){
            mSetController.pause();
        }
        
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        if(mSetController != null){
            mSetController.reset(); 
        }
        mTheme.recycle();
        mSound.release();
        super.onDestroy();
    }
    void setWindow(){
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN|WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, 
                WindowManager.LayoutParams.FLAG_FULLSCREEN|WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }
    void initPanel(Style style) {
        mPanelInfo = new PanelInfo(style);
        switch (style) {
        case STYLE_IN_SCREEN:
            mPanelInfo.width = Math.min(outMetrics.heightPixels,
                    outMetrics.widthPixels);
            mPanelInfo.height = mPanelInfo.width;
            mPanelInfo.padding = 30;
            mPanelInfo.grid = (1.0f * mPanelInfo.width - 2 * mPanelInfo.padding)
                    / (mPanelInfo.row - 1);
            mPanelInfo.topMargin = (outMetrics.heightPixels - mPanelInfo.height)/2;
            mPanelInfo.LP = new LayoutParams(mPanelInfo.width, mPanelInfo.height);
            mPanelInfo.LP.topMargin = mPanelInfo.topMargin;
            break;
        case STYLE_FULL_SCREEN:
            mPanelInfo.width = Math.min(outMetrics.heightPixels,
                    outMetrics.widthPixels);
            mPanelInfo.padding = 30;
            mPanelInfo.grid = (1.0f * mPanelInfo.width - 2 * mPanelInfo.padding)
                    / (mPanelInfo.line - 1);
            mPanelInfo.height = (int) ((mPanelInfo.row - 1) * mPanelInfo.grid
                    + 2 * mPanelInfo.padding);
            mPanelInfo.topMargin = (outMetrics.heightPixels - mPanelInfo.height)/2;
            mPanelInfo.LP = new LayoutParams(mPanelInfo.width, mPanelInfo.height);
            mPanelInfo.LP.topMargin = mPanelInfo.topMargin;
            break;
        case STYLE_BIG_SCREEN:
            mPanelInfo.width = Math.max(outMetrics.heightPixels,
                    outMetrics.widthPixels);
            mPanelInfo.height = mPanelInfo.width;
            mPanelInfo.padding = 40;
            mPanelInfo.grid = (1.0f * mPanelInfo.width - 2 * mPanelInfo.padding)
                    / (mPanelInfo.row - 1);
            mPanelInfo.topMargin = (outMetrics.heightPixels - mPanelInfo.height)/2;
            mPanelInfo.LP = new LayoutParams(mPanelInfo.width, mPanelInfo.height);
            mPanelInfo.LP.topMargin = mPanelInfo.topMargin;
            break;
        }
    }

    @SuppressWarnings("deprecation")
    void iniThemes() {
        mTheme = new Theme();
        mTheme.style = Style.STYLE_BIG_SCREEN;
        InputStream is = null;
        try {
            is = getAssets().open("image/dot_white.png");
            mTheme.whiteImage = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {
            is.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {
            is = getAssets().open("image/dot_black.png");
            mTheme.blackImage = BitmapFactory.decodeStream(is);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {
            is.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {
            is = getAssets().open("image/five_back1.jpg");
            mTheme.backImage = BitmapFactory.decodeStream(is);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {
            is.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        mSound = new SoundPool(1, AudioManager.STREAM_SYSTEM, 0);
        try {
            fallSoundId = mSound.load(getAssets().openFd("audio/fall.wav"), 1);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        AudioManager mAudioManager = (AudioManager) getSystemService(
                Context.AUDIO_SERVICE);
        mTheme.mSoundVolume = 0.1f * mAudioManager
                .getStreamVolume(AudioManager.STREAM_NOTIFICATION);

    }

    void playSound(int soundId) {
        mSound.play(soundId, mTheme.mSoundVolume, mTheme.mSoundVolume, 1, 0, 1.0f);
    }

    void updateOffset() {
        mPanelInfo.LP.leftMargin = mPanelInfo.leftOffset;
        panelContainer.updateViewLayout(panelView, mPanelInfo.LP);
    }


    enum TouchState {
        STATE_IDLE, STATE_TOUCH, STATE_ZOONING, STATE_ZOOMED, STATE_MOVE_HOR, STATE_MOVE_VER
    }

    class PanelView extends View {

        float downX = 0;
        float downY = 0;
        float zoomX = 0;
        float zoomY = 0;
        TouchState state = TouchState.STATE_IDLE;
        float baseDistance = 0;
        final int MOVE_THRESHOLD = 15;
        final int MOVE_THRESHOLD_VER = 50;
        final int MILLS_THRESHOLD = 200;
        long downMills = 0;

        public PanelView(Context context) {
            super(context, null);
            // TODO Auto-generated constructor stub
        }

        public PanelView(Context context, AttributeSet attrs) {
            super(context, attrs, 0);
            // TODO Auto-generated constructor stub
        }

        Point getTouchPoint(float x, float y) {
            Point dot = new Point();
            int row = (int) ((y - mPanelInfo.padding) / mPanelInfo.grid);
            float drow = (y - mPanelInfo.padding) % mPanelInfo.grid;

            if (drow < mPanelInfo.grid / 2) {
                dot.y = row;
            } else {
                dot.y = row + 1;
            }

            if (dot.y < 0) {
                dot.y = 0;
            } else if (dot.y > mPanelInfo.row) {
                dot.y = mPanelInfo.row;
            }

            int line = (int) ((x - mPanelInfo.padding) / mPanelInfo.grid);
            float dline = (x - mPanelInfo.padding) % mPanelInfo.grid;
            if (dline < mPanelInfo.grid / 2) {
                dot.x = line;
            } else {
                dot.x = line + 1;
            }

            if (dot.x < 0) {
                dot.x = 0;
            } else if (dot.x > mPanelInfo.line) {
                dot.x = mPanelInfo.line;
            }
            return dot;
        }

        @SuppressLint("ClickableViewAccessibility")
        @Override
        public boolean onTouchEvent(MotionEvent event) {
            // TODO Auto-generated method stub
            float x = event.getX();
            float y = event.getY();
            switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                downX = x;
                downY = y;
                state = TouchState.STATE_TOUCH;
                baseDistance = 0;
                downMills = System.currentTimeMillis();
                break;
            case MotionEvent.ACTION_MOVE:
                if (state == TouchState.STATE_ZOONING) {
                    float dx = event.getX(0) - event.getX(1);
                    float dy = event.getY(0) - event.getY(1);
                    float distance = (float) Math.sqrt(dx * dx + dy * dy);
                    float dv = distance - baseDistance;
                    // if(Math.abs(dv) > MOVE_THRESHOLD){
                    mPanelInfo.zoom(dv);
                    // }
                    baseDistance = distance;
                    break;
                }

                if (state == TouchState.STATE_TOUCH) {
                    if (Math.abs(x - downX) > Math.abs(y - downY)
                            && Math.abs(x - downX) > MOVE_THRESHOLD) {
                        state = TouchState.STATE_MOVE_HOR;
                    } else if (Math.abs(x - downX) < Math.abs(y - downY)
                            && Math.abs(y - downY) > MOVE_THRESHOLD_VER) {
                        state = TouchState.STATE_MOVE_VER;
                    }
                }

                if (state == TouchState.STATE_MOVE_HOR) {
                    if (System.currentTimeMillis()
                            - downMills < MILLS_THRESHOLD) {
                        downX = x;
                        downY = y;
                    } else {
                        mPanelInfo.offsetDx((x - downX) / 2);
                        updateOffset();
                    }
                } else if (state == TouchState.STATE_MOVE_VER) {
                    if(y > downY){
                        if(mSetController.getState() == SetState.SET_START ){
                            if(mSetController.getLastStep().dot != Dot.DOT_NULL){
                                mPlayPanel.show(true);
                            }else{
                                mPlayPanel.show(false);
                            }
                            mSetController.pause();
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (state == TouchState.STATE_TOUCH) {
                    Point point = getTouchPoint(x, y);
                    if (point != null && mSetController.goodPos(point.x, point.y)) {
                        Step step = new Step(point.x, point.y,
                                System.currentTimeMillis(),
                                mSetController.getCurrent());
                        mSetController.doStep(step);
                    }
                }
                state = TouchState.STATE_IDLE;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:

                float dx = event.getX(0) - event.getX(1);
                float dy = event.getY(0) - event.getY(1);
                zoomX = (event.getX(0) + event.getX(1)) / 2;
                zoomY = (event.getY(0) + event.getY(1)) / 2;
                baseDistance = (float) Math.sqrt(dx * dx + dy * dy);
                mPanelInfo.setAnchor(zoomX,zoomY);
                state = TouchState.STATE_ZOONING;
                break;
            case MotionEvent.ACTION_POINTER_UP:
                state = TouchState.STATE_ZOOMED;
                break;

            }
            return true;
        }

        @Override
        protected void onDraw(Canvas canvas) {
            // TODO Auto-generated method stub
            canvas.drawColor(0xffff0000);
            Paint paint = new Paint();
            Rect dst = new Rect(0, 0, mPanelInfo.width, mPanelInfo.height);
            Rect src = new Rect(0, 0, mPanelInfo.width, mPanelInfo.height);
            canvas.drawBitmap(mTheme.backImage, src, dst, paint);

            Paint paintLine1 = new Paint();
            paintLine1.setColor(Color.BLACK);
            paintLine1.setStrokeWidth(6);

            int frameMargin = mPanelInfo.padding - 6;
            canvas.drawLine(frameMargin, frameMargin,
                    mPanelInfo.width - frameMargin, frameMargin, paintLine1);
            canvas.drawLine(frameMargin, frameMargin, frameMargin,
                    mPanelInfo.height - frameMargin, paintLine1);
            canvas.drawLine(frameMargin, mPanelInfo.height - frameMargin,
                    mPanelInfo.width - frameMargin,
                    mPanelInfo.height - frameMargin, paintLine1);
            canvas.drawLine(mPanelInfo.width - frameMargin, frameMargin,
                    mPanelInfo.width - frameMargin,
                    mPanelInfo.height - frameMargin, paintLine1);

            paintLine1.setStrokeWidth(2);
            for (int i = 0; i < mPanelInfo.line; i++) {
                canvas.drawLine(mPanelInfo.padding + i * mPanelInfo.grid,
                        mPanelInfo.padding,
                        mPanelInfo.padding + i * mPanelInfo.grid,
                        mPanelInfo.height - mPanelInfo.padding, paintLine1);
            }
            ;
            for (int i = 0; i < mPanelInfo.row; i++) {
                canvas.drawLine(mPanelInfo.padding,
                        mPanelInfo.padding + i * mPanelInfo.grid,
                        mPanelInfo.width - mPanelInfo.padding,
                        mPanelInfo.padding + i * mPanelInfo.grid, paintLine1);
            }

            paintLine1.setStrokeWidth(10);
            for (Point point : mPanelInfo.points) {
                canvas.drawPoint(mPanelInfo.padding + point.x * mPanelInfo.grid,
                        mPanelInfo.padding + point.y * mPanelInfo.grid,
                        paintLine1);
            }

            float dotSize = mPanelInfo.grid * 2 / 5;

            for (int i = 0; i < mPanelInfo.line; i++) {
                for (int j = 0; j < mPanelInfo.row; j++) {
                    Dot dot = mSetController.getDot(i, j);
                    Rect dotDst = null;

                    if (dot == Dot.DOT_WHITE) {
                        dotDst = new Rect(
                                (int) (mPanelInfo.padding + i * mPanelInfo.grid
                                        - dotSize),
                                (int) (mPanelInfo.padding + j * mPanelInfo.grid
                                        - dotSize),
                                (int) (mPanelInfo.padding + i * mPanelInfo.grid
                                        + dotSize),
                                (int) (mPanelInfo.padding + j * mPanelInfo.grid
                                        + dotSize));
                        canvas.drawBitmap(mTheme.whiteImage, src, dotDst,
                                paint);
                    } else if (dot == Dot.DOT_BLACK) {
                        dotDst = new Rect(
                                (int) (mPanelInfo.padding + i * mPanelInfo.grid
                                        - dotSize),
                                (int) (mPanelInfo.padding + j * mPanelInfo.grid
                                        - dotSize),
                                (int) (mPanelInfo.padding + i * mPanelInfo.grid
                                        + dotSize),
                                (int) (mPanelInfo.padding + j * mPanelInfo.grid
                                        + dotSize));
                        canvas.drawBitmap(mTheme.blackImage, src, dotDst,
                                paint);
                    }
                }
            }

            Step step = mSetController.getLastStep();
            paintLine1.setStrokeWidth(15);
            paintLine1.setColor(Color.RED);
            if (step != null && step.dot != Dot.DOT_NULL) {
                canvas.drawPoint(mPanelInfo.padding + step.x * mPanelInfo.grid,
                        mPanelInfo.padding + step.y * mPanelInfo.grid,
                        paintLine1);
            }
            
            if(mSetController.getState() == SetState.SET_WIN 
                    && mSetController.getResult()!=null){
                for(Step winstep : mSetController.getResult().steps ){
                    canvas.drawPoint(mPanelInfo.padding + winstep.x * mPanelInfo.grid,
                            mPanelInfo.padding + winstep.y * mPanelInfo.grid,
                            paintLine1);
                }
            }
        }

    }
}
