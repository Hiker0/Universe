package com.allen.test.demon;

import com.allen.test.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

public class SimpleWeatherDemon extends Activity implements OnClickListener {
    private WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_demon_activity);
        webView=(WebView)findViewById(R.id.webView1);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("http://www.weather.com.cn");
        webView.setInitialScale(57*4);
        Button bj=(Button)findViewById(R.id.bj);
        bj.setOnClickListener(this);
        Button sh=(Button)findViewById(R.id.sh);
        sh.setOnClickListener(this);
        Button heb=(Button)findViewById(R.id.heb);
        heb.setOnClickListener(this);
        Button cc=(Button)findViewById(R.id.cc);
        cc.setOnClickListener(this);
        Button sy=(Button)findViewById(R.id.sy);
        sy.setOnClickListener(this);
        Button gz=(Button)findViewById(R.id.gz);
        gz.setOnClickListener(this);
    }
    @Override
    public void onClick(View view){
        switch(view.getId()){
            case R.id.bj:
                openUrl("101010100");
                break;
            case R.id.sh:
                openUrl("101020100");
                break;
            case R.id.heb:
                openUrl("101050101");
                break;
            case R.id.cc:
                openUrl("101060101");
                break;
            case R.id.sy:
                openUrl("101070101");
                break;
            case R.id.gz:
                openUrl("101280101");
                break;
        }
    }

    private void openUrl(String id){
        //webView.loadUrl("http://www.weather.com.cn");
        webView.loadUrl("http://www.weather.com.cn/weather1d/"+id+".shtml");   //��ȡ����ʾ����Ԥ����Ϣ
    }
}
