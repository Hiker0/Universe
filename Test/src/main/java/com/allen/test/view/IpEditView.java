package com.allen.test.view;

import com.allen.test.R;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class IpEditView extends LinearLayout {
    EditText[] mIp;
    
    public IpEditView(Context context) {
        this(context, null);
        // TODO Auto-generated constructor stub
    }

    public IpEditView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        // TODO Auto-generated constructor stub
    }

    public IpEditView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr, 0);
        // TODO Auto-generated constructor stub
        mIp = new EditText[4];
    }


    @Override
    protected void onFinishInflate() {
        // TODO Auto-generated method stub
        super.onFinishInflate();
        LayoutInflater mInflater = LayoutInflater.from(getContext());
        mInflater.inflate(R.layout.ip_editer_container, this);
        mIp[0] = (EditText) findViewById(R.id.local_gw_edit_1);
        mIp[1] = (EditText) findViewById(R.id.local_gw_edit_2);
        mIp[2] = (EditText) findViewById(R.id.local_gw_edit_3);
        mIp[3] = (EditText) findViewById(R.id.local_gw_edit_4);
        
        for(EditText et :mIp){
            et.addTextChangedListener(new MyTextWatcher(et));
            et.setOnEditorActionListener(new MyEditorListener());
        }
    };
    class MyEditorListener implements OnEditorActionListener{

        @Override
        public boolean onEditorAction(TextView v, int actionId,
                KeyEvent event) {
            // TODO Auto-generated method stub
            Log.d("allen", "onEditorAction:"+actionId + " "+event);
            return false;
        }
        
    }
    
    
    class MyTextWatcher implements TextWatcher {
        EditText myView;
        
        MyTextWatcher(EditText view){
            myView = view;
        }
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                int after) {
            // TODO Auto-generated method stub
            Log.d("allen", "beforeTextChanged:"+s + " " +start +" " +count +" " +  after);
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                int count) {
            // TODO Auto-generated method stub
            Log.d("allen", "onTextChanged:"+s + " " +start +" " +count +" " +  before);
        }

        @Override
        public void afterTextChanged(Editable s) {
            // TODO Auto-generated method stub
            String content = s.toString();
            if (content != null && !content.isEmpty()) {
                int ip = Integer.parseInt(content);
                if (ip > 255) {
                    ip = 255;
                    myView.setText("" + 255);
                }
            }
            
            if(s.length() == 3)  
            {  
                if(myView == mIp[0])  
                {  
                    mIp[1].requestFocus();  
                }  
                else if(myView == mIp[1])  
                {  
                    mIp[2].requestFocus();  
                }  
                else if(myView == mIp[2])  
                {  
                    mIp[3].requestFocus();  
                }  
            }  
            else if(s.length() == 0)  
            {  
                if(myView == mIp[3])  
                {  
                    mIp[2].requestFocus();  
                }  
                else if(myView == mIp[2])  
                {  
                    mIp[1].requestFocus();  
                }  
                else if(myView == mIp[1])  
                {  
                    mIp[0].requestFocus();  
                }  
            }  
        }
        
    }
    

}
