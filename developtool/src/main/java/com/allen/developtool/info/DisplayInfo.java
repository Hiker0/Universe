package com.allen.developtool.info;


import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.allen.developtool.R;
import com.allen.z.library.utils.PxUitls;

public class DisplayInfo extends Activity {
    final static String TAG="DisplayInfo";
    
	EditText dpEdit;
	EditText pxEdit;
	TextWatcher pxWatcher;
	TextWatcher dpWatcher ;
	
    TextView widthView, heightView, widthrange, heightrange, densityView, resourthView;
    DisplayMetrics dm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_info);
        
        widthView = (TextView) this.findViewById(R.id.width);
        heightView = (TextView) this.findViewById(R.id.height);
        widthrange= (TextView) this.findViewById(R.id.width_range);
        heightrange= (TextView) this.findViewById(R.id.height_range);
        densityView = (TextView) this.findViewById(R.id.density);
        resourthView = (TextView) this.findViewById(R.id.resourth);
      
        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
         dm= new DisplayMetrics();
        display.getMetrics(dm);

        Point size = new Point();
        Point smallestSize = new Point();
        Point largestSize = new Point();
        display.getSize(size);
        display.getCurrentSizeRange(smallestSize, largestSize);
        
           
        float density  = dm.density;
        int densityDPI = dm.densityDpi;
        float xdpi = dm.xdpi;            
        float ydpi = dm.ydpi;
        
        widthView.setText("��Ļ��ȣ�"+xdpi+" ("+size.x+"px)");
        heightView.setText("��Ļ���ȣ�"+ydpi+" ("+size.y+"px)");
        widthView.setText("��Ļ��ȣ�"+xdpi+" ("+size.x+"px)");
        heightView.setText("��Ļ���ȣ�"+ydpi+" ("+size.y+"px)");
        
        widthrange.setText("��ȷ�Χ��px��:" +smallestSize.x+" - "+ largestSize.x);
        heightrange.setText("�߶ȷ�Χ��px��:" +smallestSize.y+" - "+ largestSize.y);
        densityView.setText("��Ļ�ܶȣ�"+density+" ("+densityDPI+")");
        initEditView();
    }
    
    void initEditView(){

    	dpEdit = (EditText) this.findViewById(R.id.id_dp);
    	pxEdit = (EditText) this.findViewById(R.id.id_pix);
    	
    	dpWatcher = new TextWatcher(){

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				Log.d(TAG, "dpWatcher afterTextChanged");
				String content = s.toString();
				if(content != null && content.length()>0){
					float size = Float.parseFloat(content);
					int px = PxUitls.pxFromDp(size, dm);
					pxEdit.removeTextChangedListener(pxWatcher);
					pxEdit.setText(px+"");
					pxEdit.addTextChangedListener(pxWatcher);
				}
			}
    		
    	};
    	
    	pxWatcher = new TextWatcher(){

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				Log.d(TAG, "pxWatcher afterTextChanged");
				String content = s.toString();
				if(content != null && content.length()>0){
					int px = Integer.parseInt(content);
					float dp = PxUitls.dpiFromPx(px, dm);
					dpEdit.removeTextChangedListener(dpWatcher);
					dpEdit.setText(dp+"");
					dpEdit.addTextChangedListener(dpWatcher);
				}
			}
    		
    	};
    	
    	dpEdit.addTextChangedListener(dpWatcher);
    	pxEdit.addTextChangedListener(pxWatcher);
    	
    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

}
