package com.allen.games.five;

import com.allen.games.R;
import com.allen.games.five.FiveMain.Dot;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class PlayControlPanel implements OnClickListener {
    SetController mController;
    View Parent;
    View menu;
    Button backButton, redoButton, nextButton, exitButton;
    TextView infoView;

    PlayControlPanel(SetController controller,View root){
        mController = controller;
        Parent = root;
        menu=root.findViewById(R.id.play_ctr_menu);
        backButton = (Button)root.findViewById(R.id.play_ctr_contine);
        redoButton = (Button)root.findViewById(R.id.play_ctr_redo);
        nextButton = (Button)root.findViewById(R.id.play_ctr_next);
        exitButton = (Button)root.findViewById(R.id.play_ctr_exit);
        infoView = (TextView) root.findViewById(R.id.play_ctr_info);
        
        backButton.setOnClickListener(this);
        redoButton.setOnClickListener(this);
        nextButton.setOnClickListener(this);
        exitButton.setOnClickListener(this);
    }

    boolean isShowing(){
        if(Parent.getVisibility()==View.VISIBLE){
            return true;
        }
        return false;
    }

    void show(boolean allowRedo){
        backButton.setVisibility(View.VISIBLE);
        if(allowRedo){
            redoButton.setVisibility(View.VISIBLE);
        }else{
            redoButton.setVisibility(View.GONE);
        }
        nextButton.setVisibility(View.VISIBLE);
        exitButton.setVisibility(View.VISIBLE);
        infoView.setVisibility(View.GONE); 
        Parent.setVisibility(View.VISIBLE);
    }

    void showSuccess(String info){
        backButton.setVisibility(View.GONE);
        redoButton.setVisibility(View.GONE);
        nextButton.setVisibility(View.VISIBLE);
        exitButton.setVisibility(View.VISIBLE);
        infoView.setVisibility(View.VISIBLE);
        infoView.setText(info);
        Parent.setVisibility(View.VISIBLE);
    } 

    void showStart(){
        backButton.setVisibility(View.GONE);
        redoButton.setVisibility(View.GONE);
        nextButton.setVisibility(View.VISIBLE);
        exitButton.setVisibility(View.VISIBLE);
        infoView.setText(R.string.set_welcome);
        infoView.setVisibility(View.VISIBLE);
        
        Parent.setVisibility(View.VISIBLE);
    } 

    void hide(){
        Parent.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch(v.getId()){
        case R.id.play_ctr_contine:
            hide();
            mController.resume();
            break;
        case R.id.play_ctr_redo:
            hide();
            mController.redo();
            mController.resume();
            break;
        case R.id.play_ctr_next:
            hide();
            mController.reset();
            mController.start(Dot.DOT_BLACK);
            break;  
        case R.id.play_ctr_exit:
            hide();
            mController.exit();
            break;
        }
    }
}
