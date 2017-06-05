package com.allen.games.five;

import java.util.ArrayList;

import com.allen.games.five.FiveMain.Dot;

import android.os.Handler;
import android.util.Log;

public class SetController {
    final static String TAG = "five";
    final static int SUCCESS_NONE = 0;
    final static int SUCCESS_WIN = 1;
    final static int SUCCESS_TIMEOUT = 2;
    final static int SUCCESS_FORBIDEN= 3;
    
    int mRow = 0;
    int mLine = 0;
    public Dot first;
    private Dot current;
    private ArrayList<Step> mSteps;
    private Step lastStep;
    private Dot[][] dots;
    private JudgeCallBack mCallback;
    private SetState mState = SetState.SET_STOP;
    private Result mResult;
    private SetInfoPanel mInfoPanel;
    private int TIMEOUT;
    private Timer mTimer;
    
    interface JudgeCallBack {
        void onStart();
        void refreshUI();
        void onDoStep(Step step);
        void onStepDone(Step step);
        void onGameWin(Result result);
        void onExit();
    }

    enum SetState {
        SET_STOP, SET_START, SET_PAUSE, SET_WIN,
    }
    class Timer{
        private long mMillsCount = 0;
        private Handler mHandler;
        private long startTime = 0;
        private final int check_time = 300;
        private Runnable timerRunable = new Runnable(){

            @Override
            public void run() {
                // TODO Auto-generated method stub
                mHandler.postDelayed(timerRunable, check_time);
                long left = TIMEOUT*1000-mMillsCount + startTime - System.currentTimeMillis();

                if(left <= 0 ){
                    
                    Dot winer;
                    if(current == Dot.DOT_BLACK){
                        winer = Dot.DOT_WHITE;
                    }else{
                        winer = Dot.DOT_BLACK;
                    }
                    mResult = new Result(winer);
                    mState = SetState.SET_WIN;
                    mResult.reason = SUCCESS_TIMEOUT;
                    mTimer.stop();
                    if (mCallback != null) {
                        mCallback.onGameWin(mResult);
                    }
                }else{
                    int lefttime = (int) (left/1000);
                    mInfoPanel.setTime(current, lefttime);
                }
            }
        };
        
        Timer(){
            mHandler= new Handler();
            mMillsCount = 0L;
        }
        
        void start(){
            startTime = System.currentTimeMillis();
            mHandler.postDelayed(timerRunable, check_time);
        }
        
        void reStart(){
            startTime = System.currentTimeMillis();
            mMillsCount = 0L;
            mInfoPanel.setTime(current, TIMEOUT);
            mHandler.postDelayed(timerRunable, check_time);
        }
        
        void stop(){
            mMillsCount = System.currentTimeMillis()-startTime;
            mHandler.removeCallbacks(timerRunable);
        }
        
    }
    
    static public class Step {
        int x;
        int y;
        Long time;
        Dot dot;

        Step() {
            x = 0;
            y = 0;
            time = 0L;
            dot = Dot.DOT_NULL;
        }

        Step(int x, int y, long time, Dot dot) {
            this.x = x;
            this.y = y;
            this.time = time;
            this.dot = dot;
        }

        void copy(Step copy) {
            x = copy.x;
            y = copy.y;
            time = copy.time;
            dot = copy.dot;
        }

        @Override
        public String toString() {
            // TODO Auto-generated method stub

            return "Step(" + x + "," + y + ") t:" + time + "Dot:" + dot;
        }

    }

    class Result {
        Dot winer = Dot.DOT_NULL;
        int reason = SUCCESS_NONE;
        ArrayList<Step> steps = new ArrayList<Step>();

        Result(Dot dot) {
            winer = dot;
        }

        void addStep(Step step) {
            steps.add(step);
        }

        void cleanStep() {
            steps.clear();
        }

        int size() {
            return steps.size();
        }

    }

    SetController(int row, int line, JudgeCallBack callback) {
        mRow = row;
        mLine = line;
        mCallback = callback;
        dots = new Dot[line][row];
        for (int i = 0; i < line; i++) {
            for (int j = 0; j < row; j++) {
                dots[i][j] = Dot.DOT_NULL;
            }
        }

        if (mSteps == null) {
            mSteps = new ArrayList<Step>();
        }

        TIMEOUT = 90;
        mTimer = new Timer();
        mState = SetState.SET_STOP;
        lastStep = new Step();
        first = Dot.DOT_BLACK;
        current = first;
        mResult = null;
    }
    
    public void setTimeout(int timeout){
        TIMEOUT = timeout;
    }
    
    public void setPanel(SetInfoPanel panel){
        mInfoPanel = panel;
        mInfoPanel.setBlackTime(0);
        mInfoPanel.setWhiteTime(0);
    }
    

    public void start(Dot dot) {
        first = dot;
        current = first;
        mState = SetState.SET_START;
        mTimer.reStart();
        if(TIMEOUT != 0){
            mInfoPanel.setTime(current,TIMEOUT);
        }
        mCallback.onStart();
    }

    public void pause() {
        if(mState == SetState.SET_START){
            mState = SetState.SET_PAUSE;
            mTimer.stop();
        }
    }

    public void resume() {
        if (mState == SetState.SET_PAUSE) {
            mState = SetState.SET_START;
            mTimer.start();
        }
    }
    public void exit(){
        mCallback.onExit();
    }
    
    void redo(){
        //requestor is current
        redoSteps(1);
        mInfoPanel.setTimeForce(current, TIMEOUT);
    }

    void redo(Dot dot){
        if(dot==current){
            redoSteps(2);
            
        }else{
            redoSteps(1);
        }
        mInfoPanel.setTimeForce(current, TIMEOUT);
    }
    private void redoSteps(int num){
        if(num > mSteps.size()){
            return; 
        }
        while(num>0){
            num --;
            Step step = (Step) getLastObject(mSteps);;
            if(step != null){
                mSteps.remove(step);
                dots[step.x][step.y] = Dot.DOT_NULL;
                
            }else{
                return;
            } 
            step = (Step) getLastObject(mSteps);
            
            if(step != null){
                lastStep.copy(step);
                current = Dot.getOpponent(step.dot);
            }else{
                current = first;
                lastStep.dot =  Dot.DOT_NULL;
            }
        }
        mCallback.refreshUI();
    }
    
    Object getLastObject(ArrayList array){
        int size = array.size();
        if(size > 0){
            return array.get(size-1);
        }
        return null;
    }
    
    public void reset() {
        mState = SetState.SET_STOP;
        if (mSteps == null) {
            mSteps = new ArrayList<Step>();
        } else {
            mSteps.clear();
        }

        
        for (int i = 0; i < mLine; i++) {
            for (int j = 0; j < mRow; j++) {
                dots[i][j] = Dot.DOT_NULL;
            }
        }
        
        first = Dot.DOT_BLACK;
        lastStep = new Step();
        if(mTimer!=null) mTimer.stop();
        mInfoPanel.setBlackTime(0);
        mInfoPanel.setWhiteTime(0);
        current = first;
        mResult = null;
    }
    protected  void next() {
        Dot next ;
        next = Dot.getOpponent(first);
        start(next);
    }
    
    protected Dot getDot(int x, int y) {
        return dots[x][y];
    }

    protected boolean goodPos(int x, int y) {

        return dots[x][y] == Dot.DOT_NULL;
    }

    protected Step getLastStep() {
        return lastStep;
    }

    protected Dot getCurrent() {
        return current;
    }

    protected SetState getState(){
        return mState;
    }
    
    protected Result getResult(){
        return mResult;
    }
    
    
    protected void doStep(Step step) {
        Log.d(TAG, "doStep:" + step);

        if (!goodPos(step.x, step.y) && step.dot != Dot.DOT_NULL) {
            Log.e(TAG, "bad position or no dot");
            return;
        }
        if (mState != SetState.SET_START) {
            return;
        }
        
        dots[step.x][step.y] = step.dot;

        lastStep.copy(step);
        if (step.dot == Dot.DOT_BLACK && current == Dot.DOT_BLACK) {
            mSteps.add(step);
            current = Dot.DOT_WHITE;
        } else if (step.dot == Dot.DOT_WHITE && current == Dot.DOT_WHITE) {
            mSteps.add(step);
            current = Dot.DOT_BLACK;
        }
        if (mCallback != null) {
            mCallback.onDoStep(step);
        }
        if( !judge(step)){
            mInfoPanel.setTimeForce(current, TIMEOUT);
            mTimer.reStart(); 
        }else{
            mTimer.stop();
        }
        
        if (mCallback != null) {
            mCallback.onStepDone(step);
        }

    }
    private boolean judgeForbiden(Step step){
        int rule = GlobalSetting.getInstance().getRule();
        if( rule == GlobalSetting.RULE_NONE){
            return false;
        }
        
        
        
        return false;
    }
    private boolean judge(Step step) {

        int bline = step.x - 4;
        int brow = step.y - 4;
        int fline = step.x + 5;
        int frow = step.y + 5;

        if (bline < 0) {
            bline = 0;
        }

        if (brow < 0) {
            brow = 0;
        }

        if (fline > mLine) {
            fline = mLine;
        }

        if (frow > mRow) {
            frow = mRow;
        }

        Dot dot = step.dot;
        Result result = new Result(dot);

        for (int i = bline; i < fline; i++) {
            if (dots[i][step.y] == dot) {
                result.addStep(new Step(i, step.y, 0L, dot));
                if (result.size() >= 5) {
                    mResult = result;
                    mResult.reason = SUCCESS_WIN;
                    mState = SetState.SET_WIN;
                    if (mCallback != null) {
                        mCallback.onGameWin(result);
                    }
                    Log.d(TAG, "win");
                    return true;
                }
            } else {
                result.cleanStep();
            }
        }

        for (int i = brow; i < frow; i++) {
            if (dots[step.x][i] == dot) {
                result.addStep(new Step(step.x, i, 0L, dot));
                if (result.size() >= 5) {
                    mResult = result;
                    mResult.reason = SUCCESS_WIN;
                    mState = SetState.SET_WIN;
                    Log.d(TAG, "win");
                    if (mCallback != null) {
                        mCallback.onGameWin(result);
                    }
                    return true;
                }
            } else {
                result.cleanStep();
            }
        }

        bline = step.x - 4;
        brow = step.y - 4;
        fline = step.x + 5;
        frow = step.y + 5;
        result.cleanStep();
        while (bline < fline) {
            if (bline < 0 || brow < 0) {
                bline += 1;
                brow += 1;
                continue;
            }

            if (bline >= mLine || brow >= mRow) {
                break;
            }

            if (dots[bline][brow] == dot) {
                result.addStep(new Step(bline, brow, 0L, dot));
                if (result.size() >= 5) {
                    mResult = result;
                    mResult.reason = SUCCESS_WIN;
                    mState = SetState.SET_WIN;
                    if (mCallback != null) {
                        mCallback.onGameWin(result);
                    }
                    Log.e(TAG, "win");
                    return true;
                }
            } else {
                result.cleanStep();
            }
            bline += 1;
            brow += 1;
        }

        bline = step.x - 4;
        brow = step.y - 4;
        fline = step.x + 5;
        frow = step.y + 4;
        result.cleanStep();
        while (bline < fline) {
            if (bline < 0 || frow >= mRow) {
                bline += 1;
                frow -= 1;
                continue;
            }

            if (bline >= mLine || frow < 0) {
                break;
            }

            if (dots[bline][frow] == dot) {
                result.addStep(new Step(bline, frow, 0L, dot));
                if (result.size() >= 5) {
                    mResult = result;
                    mResult.reason = SUCCESS_WIN;
                    mState = SetState.SET_WIN;
                    if (mCallback != null) {
                        mCallback.onGameWin(result);
                    }
                    Log.e(TAG, "win");
                    return true;
                }
            } else {
                result.cleanStep();
            }
            bline += 1;
            frow -= 1;
        }

        return false;
    }

}
