package com.allen.test.demon;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

public class StatusbarColorActivity extends Activity {
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        mContext = this;
    }
    
    

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        
        
    }



    @Override
    public void onAttachedToWindow() {
        // TODO Auto-generated method stub
        super.onAttachedToWindow();
    }



    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }
    
    void setWindowFlag() {
        
    }

}
