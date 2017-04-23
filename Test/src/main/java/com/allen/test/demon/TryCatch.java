package com.allen.test.demon;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class TryCatch extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        
        StringBuilder sb =new StringBuilder();
        float f = 1.2f;
        Float F = new Float(1.2);
        F = new Float(1.2f);
        Double D = new Double(1.2);
        
        sb.append("float f = 1.2f");
        sb.append("\n");
        
        sb.append("Float F = new Float(1.2)");
        sb.append("\n");
        
        sb.append("F = new Float(1.2f)");
        sb.append("\n");
        
        sb.append("Double D = new Double(1.2)");
        sb.append("\n");
        
        if(f == F){
            Log.d("allen", "f == F");
            sb.append("f == F");
        }else{
            sb.append("f != F");
        }
        sb.append("\n");
        if(F.equals(f)){
            Log.d("allen", "F.equals(f)");
            sb.append("F.equals(f)");
        }else{
            sb.append("! F.equals(f)");
        }
        sb.append("\n");
        if(F.equals(D)){
            Log.d("allen", "F.equals(D)");
            sb.append("F.equals(D)");
        }else{
            sb.append("! F.equals(D)");
        }
        sb.append("\n");
        if(f == D){
            Log.d("allen", "f == D");
            sb.append("f == D");
        }else{
            sb.append("f != D");
        }
        sb.append("\n");
        
        

        TextView v = new TextView(this);
        this.setContentView(v);
        
        try{
            Log.d("allen", "return");
            sb.append("return");
            sb.append("\n");
            v.setText(sb);
            return;
        }finally{
            Log.d("allen", "finally");
            sb.append("finally");
            sb.append("\n");
            v.setText(sb);
        }
        
        //Log.d("allen", "end");
    }

}
