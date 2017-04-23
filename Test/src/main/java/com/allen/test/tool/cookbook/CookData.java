package com.allen.test.tool.cookbook;

import java.util.ArrayList;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.allen.test.R;
import com.allen.test.view.CookImage;

public class CookData {

    final String TAG = "CookData";
    final static int UNKOWN = 0;
    final static int SPRING = 1;
    final static int SUMMER = 2;
    final static int AUTURM = 3;
    final static int WINTER = 4;

    private int mId;
    private String mName;
    private String mEfficacy;
    private String mUsage;
    private int mSeason;
    private boolean isCustom = false;
    private ArrayList<RateItem> mRates;
    private RateItem mWater = null;
    private String imagePath = null;
    private CookImage mView = null;
    private Bitmap mImage = null;

    public static class RateItem {
        public String itemName;
        public int itemWeight;
        public boolean isWater;

        public RateItem() {
            // TODO Auto-generated constructor stub
        }
        RateItem(String itemname, int itemweight, boolean iswater) {
            itemName = itemname;
            itemWeight = itemweight;
            isWater = iswater;
        }
    }

    public CookData() {
        mRates = new ArrayList<RateItem>();
        mSeason = 0;
    }

    CookData( int id, String name,boolean custom) {
        this();
        mName = name;
        mId = id;
        isCustom = custom;
    }
    
    public void setView(CookImage view){
        mView = view;

      if(mView != null && mImage != null){
          ImageView thumb = (ImageView) mView.findViewById(R.id.id_thumb);
          thumb.setImageBitmap(mImage);
      }
    }
    public void addRate(String itemname, int itemweight, boolean iswater) {
        RateItem rate = new RateItem(itemname, itemweight, iswater);
        if (rate.isWater) {
            if (mWater != null) {
                mRates.remove(mWater);
            }
            mWater = rate;
        }
        mRates.add(rate);
    }

    public void  setId(int id){
        mId = id;
    }
    
    public void addRate(RateItem rate) {
        if (rate.isWater) {
            if (mWater != null) {
                mRates.remove(mWater);
            }
            mWater = rate;
        }
        mRates.add(rate);
    }

    public void setRates(ArrayList<RateItem> rates) {
        mRates.clear();
        for (RateItem rate : rates) {
            addRate(rate);
        }
    }

    public void setUsage(String usage) {
        mUsage = usage;
    }

    public void setEfficacy(String efficacy) {
        mEfficacy = efficacy;
    }

    public void setName(String name) {
        mName = name;
    }

    public void setSeason(int season) {
        mSeason = season;
    }

    public void setCustom(boolean custom){
        isCustom = custom;
    }
    
    public void setImagePath(String path) {
        imagePath = path;
    }
    

    public void setImage(Bitmap bitmap) {
        mImage = bitmap;
    }
    
    public int getId(){
        return mId;
    }
    
    public String getName() {
       return  mName;
    }
    public String getImagePath(){
        return imagePath;
    }
            
    public final ArrayList<RateItem> getRates() {
        return mRates;
    }

    public String getUsage() {
        return mUsage;
    }

    public String getEfficacy() {
        return mEfficacy;
    }


    public int getSeason() {
        return mSeason;
    }
    public CookImage getView(){
       return  mView;
    }
    public Bitmap getImage() {
       return mImage;
    }
    
    public boolean getCustom(){
        return isCustom;
    }

//    public void  decodeImage(){
//        InputStream  in = null;
//        try {
//            in = mRes.getAssets().open(imagePath);
//            
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        if(in != null){
//            mBitmap = BitmapFactory.decodeStream(in);
//        }else{
//            return;
//        }
//        
//        if(mView != null){
//            ImageView thumb = (ImageView) mView.findViewById(R.id.id_thumb);
//            thumb.setImageBitmap(mBitmap);
//        }
//        
//        try {
//            in.close();
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//    }
//    
//    public void  recycleImage(){
//        if(mView != null){
//            ImageView thumb = (ImageView) mView.findViewById(R.id.id_thumb);
//            thumb.setImageBitmap(null);
//        }
//
//        if(mBitmap != null){
//            mBitmap.recycle();
//            mBitmap = null;
//        }
//    }
    
    public String getRate(int water){
       StringBuilder sb = new StringBuilder();
       StringBuilder sb2 = new StringBuilder();
       if(mRates==null || mRates.size() < 1){
           sb.append("None");
           return sb.toString();
       }
       
       if(mWater == null){
           
           for(int i=0; i< mRates.size(); i++){
               RateItem rt = mRates.get(i);
               sb.append(rt.itemName);
               sb2.append(rt.itemWeight);
               if(i != mRates.size() -1 ){
                   sb.append(":");
                   sb2.append(":");
               }
           }
           
           sb.append(" = ");
           sb.append(sb2);
          
           return sb.toString();
       }
       
       if(water == 0){
           water = mWater.itemWeight;
       }
       
       for(RateItem rt:mRates){
           if(rt != mWater){
               sb.append(rt.itemName);
               sb.append(":");
               sb2.append((int)(1.0f*rt.itemWeight/mWater.itemWeight*water));
               sb2.append(":");  
           }

       }
       
       sb.append(mWater.itemName);
       sb2.append(water);
       
       sb.append(" = ");
       sb.append(sb2);
       return sb.toString();
    }
    
    @Override
    public String toString() {
        // TODO Auto-generated method stub
        StringBuilder sb = new StringBuilder();
        sb.append("mName:"+mName+"\n");
        sb.append("mEfficacy:"+mEfficacy+"\n");
        sb.append("mUsage:"+mUsage+"\n");
        sb.append("mSeason:"+mSeason+"\n");
        sb.append("imagePath:"+imagePath+"\n");
        sb.append("rates:"+getRate(0)+"\n");
        return sb.toString();
    }
    
}
