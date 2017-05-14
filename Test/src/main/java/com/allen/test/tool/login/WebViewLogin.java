package com.allen.test.tool.login;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.allen.test.R;

@SuppressLint("SetJavaScriptEnabled")
public class WebViewLogin extends Activity implements OnClickListener,
        OnTouchListener, OnCheckedChangeListener {
    private final static String TAG = "WebViewLogin";
    final static String FILE_SELECTOR_ACTION =  "android.intent.action.FILE_SELECTOR";
    final static String INFO_FILE =  "xnw.info";
    final static int REQUEST_CODE = 100;
    private WebView mPreview; //
    private ArrayList<Loginfo> mLists;
    private int mCurrent = 0;
    private View infoContainer = null;
    private View infoTitle = null;
    private ListView mListView = null;
    private Button mPre = null;
    private Button mNext = null;
    private TextView mInfo = null;
    private AcountAdapter mAdapter = null;
    private FrameLayout mRoot;
    private EditText mRateEdit;
    private CheckBox mCheckBox;
    private int maxMargin = 0;
    private float downY = 0;
    private int downMargin = 0;
    private int mRate = 0;
    private boolean isAuto = true;
    private ImageView mImage = null;

    class Loginfo {
        String acount = null;
        String pwd = null;
        boolean done = false;

        Loginfo(String acount, String pwd) {
            this.acount = acount;
            this.pwd = pwd;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview_login_activity);
        mRoot = (FrameLayout) findViewById(R.id.root);

        mPreview = (WebView) findViewById(R.id.preview); //
        mPreview.getSettings().setJavaScriptEnabled(true); //
        mPreview.setWebChromeClient(new WebChromeClient()); //
        mPreview.setWebViewClient(new WebViewClient()); //
        mPreview.setInitialScale(57 * 4);
        mPreview.setWebViewClient(mWebViewClient);

        infoContainer = findViewById(R.id.info_container);
        infoContainer.setVisibility(View.VISIBLE);
        mListView = (ListView) findViewById(R.id.login_list);

        infoTitle = findViewById(R.id.info_title);
        infoTitle.setOnTouchListener(this);
        
        mPre = (Button) findViewById(R.id.bt_pre);
        mPre.setOnClickListener(this);
        mNext = (Button) findViewById(R.id.bt_next);
        mInfo = (TextView) findViewById(R.id.info);
        mNext.setOnClickListener(this);
        infoContainer.setFocusable(true);
        infoContainer.setOnTouchListener(this);

        mCheckBox = (CheckBox) findViewById(R.id.auto);
        mCheckBox.setChecked(isAuto);
        mCheckBox.setOnCheckedChangeListener(this);
        
        mRateEdit = (EditText) findViewById(R.id.input_rate);
        mImage = (ImageView) findViewById(R.id.initview);
        Drawable drawable = mImage.getDrawable();
        if (drawable instanceof Animatable) {
            ((Animatable) drawable).start();
        }
        
        infoContainer.post(new Runnable() {
            @Override
            public void run() {
                maxMargin =  mRoot.getHeight() - infoTitle.getHeight();
                updateInfoPosition(maxMargin);
            }
        });

    }

    WebViewClient mWebViewClient = new WebViewClient(){

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // TODO Auto-generated method stub
            super.onPageStarted(view, url, favicon);
            
            Log.d(TAG,"onPageStarted:"+url);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            // TODO Auto-generated method stub
            super.onPageFinished(view, url);
            Log.d(TAG,"onPageFinished:"+url);
            mImage.setVisibility(View.GONE);
        }

        @Override
        public void onLoadResource(WebView view, String url) {
            // TODO Auto-generated method stub
            super.onLoadResource(view, url);
            
            Log.d(TAG,"onLoadResource:"+url);
        }
        
    };
    private void loadData() {
          mLists = new ArrayList<Loginfo>();
//        addInfo("wangzhenjun@xnw.com", "123456");
//        addInfo("15536501082", "gyf1082");
//        addInfo("13834130722", "czw0722");
//        addInfo("13934654560", "gyf4560");
//        addInfo("18635637962", "hyn7962");
//        addInfo("15935129700", "kyh9700");
        
          File  root = Environment.getExternalStorageDirectory();
          File file = new File(root,INFO_FILE);
          if(file.exists()){
              try {
                  InputStream instream = new FileInputStream(file); 
                  if (instream != null) 
                  {
                      InputStreamReader inputreader = new InputStreamReader(instream);
                      BufferedReader buffreader = new BufferedReader(inputreader);
                      String line;
                      //
                      while (( line = buffreader.readLine()) != null) {
                          Log.d(TAG,"line:"+line);
                          String[] ss = line.split("\\s{1,}");
                          if(ss.length>=2  ){
                              if(ss[0].isEmpty() || ss[1].isEmpty()){
                                  continue;
                              }
                              addInfo(ss[0], ss[1]);
                          }
                      }                
                      instream.close();
                  }
              }
              catch (java.io.FileNotFoundException e) 
              {
                  Log.d(TAG, "The File doesn't not exist.");
              } 
              catch (IOException e) 
              {
                   Log.d(TAG, e.getMessage());
              }
          }

    }

    private void addInfo(String acount, String pwd) {
        Loginfo info = new Loginfo(acount, pwd);
        mLists.add(info);
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        // TODO Auto-generated method stub
        if(isChecked){
            String s = mRateEdit.getText().toString();
            int range =Integer.parseInt(s);
            Random random =new Random();
            int next = random.nextInt(range);
            Log.d(TAG,"next:"+next);
            mHandler.postDelayed(mAutoUrl, next*1000);
        }else{
            mHandler.removeCallbacks(mAutoUrl); 
        }
        
        isAuto = isChecked;
    }
    
    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        if (v == mPre && mCurrent > 0) {
            mCurrent = mCurrent - 1;
            updateAcount(mCurrent);
        } else if (v == mNext && mCurrent < mLists.size() - 1) {
            mCurrent = mCurrent + 1;
            updateAcount(mCurrent);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        // TODO Auto-generated method stub
        if(infoContainer == v){
            return true;
        }
        int nextp = 0;
        switch (event.getAction()) {
        case MotionEvent.ACTION_DOWN:
            downY = event.getRawY();
            LayoutParams params = (LayoutParams) infoContainer
                    .getLayoutParams();
            downMargin = params.topMargin;
            mHandler.postDelayed(mLongClickRunnable, 1000);
            break;
        case MotionEvent.ACTION_MOVE:
            float dy = event.getRawY() - downY;
            if(dy > 15){
                mHandler.removeCallbacks(mLongClickRunnable);
            }
            nextp = (int) (dy + downMargin);
            updateInfoPosition(nextp);
            break;
        case MotionEvent.ACTION_UP:
        case MotionEvent.ACTION_CANCEL:
            nextp = (int) (event.getRawY() - downY + downMargin);
            updateInfoPosition(nextp);
            downY = 0;
            downMargin = 0;
            break;
        }
        return true;
    }

    Handler mHandler = new Handler();
    Runnable mLongClickRunnable = new Runnable(){

        @Override
        public void run() {
            // TODO Auto-generated method stub
            //startFileSelector();
        }
        
    };
    
    Runnable mAutoUrl = new Runnable(){

        @Override
        public void run() {
            // TODO Auto-generated method stub
            if(mCurrent < mLists.size() - 1){
                mCurrent = mCurrent + 1;
            }else{
                mCurrent = 0;
            }
            
            updateAcount(mCurrent);
            String s = mRateEdit.getText().toString();
            int range =Integer.parseInt(s);
            Random random =new Random();
            int next = random.nextInt(range);
            Log.d(TAG,"next:"+next);
            mHandler.postDelayed(mAutoUrl, next*1000);
        }
    };
    
    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        loadData();
        mAdapter = new AcountAdapter();
        mListView.setAdapter(mAdapter);
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        Log.d(TAG, "onResume");

        updateAcount(mCurrent);
        if(isAuto){
            String s = mRateEdit.getText().toString();
            int range =Integer.parseInt(s);
            Random random =new Random();
            int next = random.nextInt(range);
            Log.d(TAG,"next:"+next);
            mHandler.postDelayed(mAutoUrl, next*1000);
        }
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        mHandler.removeCallbacks(mAutoUrl);
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
       if(requestCode == REQUEST_CODE && resultCode == RESULT_OK){
           
       }
    }
    
    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }
    private void startFileSelector(){
        Intent intent = new Intent();
        intent.setAction(FILE_SELECTOR_ACTION);
        this.startActivityForResult(intent, REQUEST_CODE);
    }
    private void updateInfoPosition(int topMargin) {
        if (topMargin < 0) {
            topMargin = 0;
        }

        if (topMargin > maxMargin) {
            topMargin = maxMargin;
        }
        LayoutParams params = (LayoutParams) infoContainer.getLayoutParams();
        params.topMargin = topMargin;
        mRoot.updateViewLayout(infoContainer, params);
    }


    private void updateAcount(int index) {
        if(mLists == null || mLists.size() == 0){
            mInfo.setText("Can't find info !");
            return;
        }
        
        Loginfo info = mLists.get(index);
        openUrl(info.acount, info.pwd);
        int size = mLists.size();
        String text = info.acount + " (" + (index + 1) + "/" + size + ")";
        mInfo.setText(text);

        if (index == 0) {
            mPre.setEnabled(false);
        } else {
            mPre.setEnabled(true);
        }

        if (index == (size - 1)) {
            mNext.setEnabled(false);
        } else {
            mNext.setEnabled(true);
        }
        info.done = true;
        mListView.setSelection(index);
        mAdapter.notifyDataSetInvalidated();
    }

    // ����ҳ�ķ���
    private void openUrl(String acount, String pwd) {
        mPreview.loadUrl("http://www.xnw.com/user/log_in.php?act=do_login&password="
                + pwd + "&account=" + acount + "&type=login_info&remember_me=1"); 
    }

    class AcountAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mLists.size();
        }

        @Override
        public Loginfo getItem(int position) {
            return mLists.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(getApplicationContext(),
                        R.layout.item_list_login, null);
                new ViewHolder(convertView);
            }
            ViewHolder holder = (ViewHolder) convertView.getTag();
            Loginfo item = getItem(position);
            holder.tv_account.setText(item.acount);
            holder.tv_password.setText(item.pwd);
            if (position == mCurrent) {
                holder.tv_account.setTextColor(Color.RED);
                holder.tv_password.setTextColor(Color.RED);
            } else {
                holder.tv_account.setTextColor(Color.BLACK);
                holder.tv_password.setTextColor(Color.BLACK);
            }
            if (item.done) {
                holder.iv_done.setVisibility(View.VISIBLE);
            } else {
                holder.iv_done.setVisibility(View.INVISIBLE);
            }
            return convertView;
        }

        class ViewHolder {
            TextView tv_account;
            TextView tv_password;
            ImageView iv_done;

            public ViewHolder(View view) {
                tv_account = (TextView) view.findViewById(R.id.tv_account);
                tv_password = (TextView) view.findViewById(R.id.tv_password);
                iv_done = (ImageView) view.findViewById(R.id.iv_done);
                view.setTag(this);
            }
        }
    }




}
