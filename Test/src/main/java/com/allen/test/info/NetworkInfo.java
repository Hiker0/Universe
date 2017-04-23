package com.allen.test.info;


import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import com.allen.test.R;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class NetworkInfo extends Activity {
    
    TextView radioIp, wifiIp;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network_info);
        
        radioIp = (TextView) this.findViewById(R.id.ip_radio);
        wifiIp = (TextView) this.findViewById(R.id.ip_wifi);

        getWifiIP();
        
        String radioip = getLocalIpAddress();
        radioIp.setText("Local Ip: "+radioip);
    }

    
    void getWifiIP(){
        
        WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);  
        
        if (!wifiManager.isWifiEnabled()) {  
            wifiManager.setWifiEnabled(true);    
        }else{
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();       
            int ipAddress = wifiInfo.getIpAddress();   
            String ip = intToIp(ipAddress);   
      
            wifiIp.setText("Wifi Ip: "+ip);  
        }
    }
    
    public String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf
                        .getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {
            Log.e("WifiPreference IpAddress", ex.toString());
        }
        return null;
    }
    
    private String intToIp(int i) {       
        
        return (i & 0xFF ) + "." +       
      ((i >> 8 ) & 0xFF) + "." +       
      ((i >> 16 ) & 0xFF) + "." +       
      ( i >> 24 & 0xFF) ;  
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
