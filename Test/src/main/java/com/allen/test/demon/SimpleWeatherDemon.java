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
    private WebView webView;   //����WebView����Ķ���
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_demon_activity);
        webView=(WebView)findViewById(R.id.webView1);   //��ȡWebView���
        webView.getSettings().setJavaScriptEnabled(true);   //����JavaScript����
        webView.setWebChromeClient(new WebChromeClient());  //����JavaScript�Ի���
        webView.setWebViewClient(new WebViewClient());  //��������֪ͨ�������¼��������ʹ�øþ���룬��ʹ�����������������ҳ
        webView.loadUrl("http://www.weather.com.cn"); //����Ĭ����ʾ������Ԥ����Ϣ
        webView.setInitialScale(57*4);  //����ҳ���ݷŴ�4��
        Button bj=(Button)findViewById(R.id.bj);    //��ȡ���ֹ����������ӵġ���������ť
        bj.setOnClickListener(this);
        Button sh=(Button)findViewById(R.id.sh);    //��ȡ���ֹ����������ӵġ��Ϻ�����ť
        sh.setOnClickListener(this);
        Button heb=(Button)findViewById(R.id.heb);  //��ȡ���ֹ����������ӵġ�����������ť
        heb.setOnClickListener(this);
        Button cc=(Button)findViewById(R.id.cc);    //��ȡ���ֹ����������ӵġ���������ť
        cc.setOnClickListener(this);
        Button sy=(Button)findViewById(R.id.sy);    //��ȡ���ֹ����������ӵġ���������ť
        sy.setOnClickListener(this);
        Button gz=(Button)findViewById(R.id.gz);    //��ȡ���ֹ����������ӵġ����ݡ���ť
        gz.setOnClickListener(this);
    }
    @Override
    public void onClick(View view){
        switch(view.getId()){
            case R.id.bj:   //�������ǡ���������ť
                openUrl("101010100");
                break;
            case R.id.sh:   //�������ǡ��Ϻ�����ť
                openUrl("101020100");
                break;
            case R.id.heb:  //�������ǡ�����������ť
                openUrl("101050101");
                break;
            case R.id.cc:   //�������ǡ���������ť
                openUrl("101060101");
                break;
            case R.id.sy:   //�������ǡ���������ť
                openUrl("101070101");
                break;
            case R.id.gz:   //�������ǡ����ݡ���ť
                openUrl("101280101");
                break;
        }
    }
    //����ҳ�ķ���
    private void openUrl(String id){
        //webView.loadUrl("http://www.weather.com.cn");
        webView.loadUrl("http://www.weather.com.cn/weather1d/"+id+".shtml");   //��ȡ����ʾ����Ԥ����Ϣ
    }
}