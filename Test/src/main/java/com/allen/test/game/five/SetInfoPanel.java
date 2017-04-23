package com.allen.test.game.five;

import com.allen.test.R;
import com.allen.test.game.five.FiveMain.Dot;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

public class SetInfoPanel {
    Context mContext;
    View Parent;
    TextView blackTimer ,whiteTimer;
    
    SetInfoPanel(Context context,View root){
        mContext = context;
        Parent = root;
        whiteTimer = (TextView)root.findViewById(R.id.white_timer);
        blackTimer = (TextView)root.findViewById(R.id.balck_timer);
    }
    
    void setTime(Dot dot,int time){
        if(dot == Dot.DOT_BLACK){
            setBlackTime( time);
        }else if(dot == Dot.DOT_WHITE){
            setWhiteTime( time);
        }
        
    }
    void setTimeForce(Dot dot,int time){
        if(dot == Dot.DOT_BLACK){
            setBlackTime( time);
            setWhiteTime(0);
        }else if(dot == Dot.DOT_WHITE){
            setWhiteTime( time);
            setBlackTime( 0);
        }
        
    }   

    void setBlackTime(int time){
        blackTimer.setText(formatTime(time));
    }
    
    void setWhiteTime(int time){
        whiteTimer.setText(formatTime(time));
    }
    
    void  formatNum(StringBuilder sb,int num){
        if(num < 10){
            sb.append("0");
            sb.append(num);
        }else{
            sb.append(num);
        }
    }
    
    String formatTime(int time){
        if(time < 0){
            time = 0;
        }
        
        StringBuilder sb = new StringBuilder();
        if(time > 60){
            int min=time/60;
            int sec = time%60;
            formatNum(sb,min);
            sb.append(":");
            formatNum(sb,sec);
        }else{
            sb.append("00:");
            formatNum(sb,time);
        }
        return sb.toString();
    }

}
