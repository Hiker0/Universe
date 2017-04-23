package com.allen.test.tool.cookbook;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.allen.test.R;
import com.allen.test.tool.cookbook.CookData.RateItem;
import com.allen.test.tool.cookbook.data.CookbookData;
import com.allen.test.view.CookImage;

public class CookMenu extends Activity implements OnPageChangeListener {
    final static String TAG = "allen.CookMenu";
    final static String TAG_COOKBOOK = "menu";
    final static String TAG_MENU = "menu";
    final static String TAG_RATES = "rates";
    final static String TAG_RATE = "rate";
    private Context mContext;
    private View mParent;
    private ViewPager mPager;
    private TextView mTitle;
    private TextView mEfficacy;
    private TextView mUsage;
    private TextView mRate;
    private ArrayList<CookData> mCooks;
    private ImageAdapter mAdapter;
    private int[] mColors = { Color.BLUE, Color.GREEN, Color.CYAN, Color.RED,
            Color.WHITE };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.cook_menu_activity);

        mParent = findViewById(R.id.parent);
        mPager = (ViewPager) findViewById(R.id.id_pager);
        mTitle = (TextView) findViewById(R.id.id_title);
        mEfficacy = (TextView) findViewById(R.id.id_efficacy);
        mUsage = (TextView) findViewById(R.id.id_usage);
        mRate = (TextView) findViewById(R.id.id_rate);
        mAdapter = new ImageAdapter();

    }

    private void initialCookView() {
        Log.d(TAG, "initialCookView");
        LayoutInflater inflater = LayoutInflater.from(mContext);
        for (CookData data : mCooks) {
            CookImage root = (CookImage) inflater.inflate(R.layout.cook_item,
                    null);
            data.setView(root);
        }
        Log.d(TAG, "initialCookData end");

    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        //initialCookData();
        loadDatabase();
        //loadItem(R.xml.default_cookitem);
        initialCookView();
        onPageSelected(0);
        mPager.setAdapter(mAdapter);
        mPager.setOnPageChangeListener(this);
        
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

    public class ImageAdapter extends PagerAdapter {

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            // TODO Auto-generated method stub
            Log.d(TAG, "destroyItem position=" + position);
            CookData data = mCooks.get(position);
            container.removeView(data.getView());
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            // TODO Auto-generated method stub
            Log.d(TAG, "instantiateItem position=" + position);
            CookData data = mCooks.get(position);
            //data.decodeImage();
            container.addView(data.getView());
            return data.getView();
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return mCooks.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            // TODO Auto-generated method stub
            return arg0 == arg1;
        }

    }

    @Override
    public void onPageScrollStateChanged(int arg0) {
        // TODO Auto-generated method stub
        Log.d(TAG, "onPageScrollStateChanged arg0=" + arg0);
    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onPageSelected(int arg0) {
        // TODO Auto-generated method stub
        CookData data = mCooks.get(arg0);
        Log.d(TAG, "onPageSelected arg0=" + arg0);
        mTitle.setText(data.getName());
        mEfficacy.setText(data.getEfficacy());
        mUsage.setText(data.getUsage());
        mRate.setText(data.getRate(0));
        mParent.setBackgroundColor(mColors[data.getSeason()]);
    }
    
    private void loadDatabase() {
        Log.d(TAG,"loadDatabase begin >>");
        mCooks = new ArrayList<CookData>();
        ContentResolver cr = this.getContentResolver();
        Uri uri = Uri.parse("content://com.allen.test.tool.cookbook.data/MenuTbl");
        Cursor cur = cr.query(uri, null, null, null, null);
        
        
        final int idIndex = cur.getColumnIndexOrThrow(CookbookData.MenuColumns._ID);
        final int nameIndex = cur.getColumnIndexOrThrow(CookbookData.MenuColumns.NAME);
        final int efficacyIndex = cur.getColumnIndexOrThrow(CookbookData.MenuColumns.EFFICACY);
        final int usageIndex = cur.getColumnIndexOrThrow(CookbookData.MenuColumns.USAGE);
        final int seasonIndex = cur.getColumnIndexOrThrow(CookbookData.MenuColumns.SEASON);
        final int imageIndex =cur.getColumnIndexOrThrow(CookbookData.MenuColumns.IMAGE);
        final int customIndex =cur.getColumnIndexOrThrow(CookbookData.MenuColumns.CUSTOM);
        while(cur.moveToNext()){
            Log.d(TAG,"loadDatabase load one menu");
            CookData data = new CookData();
            data.setId(cur.getInt(idIndex));
            data.setName(cur.getString(nameIndex));
            data.setEfficacy(cur.getString(efficacyIndex));
            data.setUsage(cur.getString(usageIndex));
            data.setImage(getIconFromCursor(cur,imageIndex));
            data.setSeason(cur.getInt(seasonIndex));
            data.setCustom(cur.getInt(customIndex) == 1); 
            mCooks.add(data);
        }
        cur.close();  
        Uri uri1 = Uri.parse("content://com.allen.test.tool.cookbook.data/RateTbl");
        String selection = CookbookData.RateColumns.PARENT+"=?";
        String[] where = new String[1];
        for( CookData data: mCooks){
            where[0] = new String(data.getId()+"");
            Cursor ratecur = cr.query(uri1, null, selection, where, null);
            while(ratecur.moveToNext()){
                Log.d(TAG,"loadDatabase load one rate");
                RateItem item = new RateItem();
                item.itemName =ratecur.getString(1);
                item.itemWeight =ratecur.getInt(2);
                item.isWater= (ratecur.getInt(4)==1);
                data.addRate(item);      
            }
            ratecur.close();
            
            Log.d(TAG, data.toString());
        }
        Log.d(TAG,"loadDatabase end <<");
    }

    Bitmap getIconFromCursor(Cursor c, int iconIndex) {

        byte[] data = c.getBlob(iconIndex);
        try {
            return BitmapFactory.decodeByteArray(data, 0, data.length);
        } catch (Exception e) {
            return null;
        }
    }
    
    private void loadItem(int workspaceResourceId) {
        mCooks = new ArrayList<CookData>();
        XmlResourceParser parser = mContext.getResources().getXml(
                workspaceResourceId);
        AttributeSet attrs = Xml.asAttributeSet(parser);
        int type;
        try {
            while ((type = parser.next()) != XmlPullParser.START_TAG
                    && type != XmlPullParser.END_DOCUMENT) {
                Log.d(TAG, "loop once");
                ;
            }

            if (type != XmlPullParser.START_TAG) {
                Log.e(TAG, "! START_TAG");
            }

            if (!parser.getName().equals(TAG_COOKBOOK)) {
                Log.e(TAG, "! START_TAG");
            }

            final int depth = parser.getDepth();
            Log.d(TAG, "depth=" + depth);

            while (((type = parser.next()) != XmlPullParser.END_TAG || parser
                    .getDepth() > depth) && type != XmlPullParser.END_DOCUMENT) {
                Log.d(TAG, "parser.getDepth()=" + parser.getDepth());
                final String name = parser.getName();
                Log.d(TAG, "parser.getName()=" + name);
                if (type != XmlPullParser.START_TAG) {
                    Log.d(TAG, "parser type = XmlPullParser.END_TAG");
                    continue;
                }
                Log.d(TAG, "parser type = XmlPullParser.START_TAG");
                
                if (TAG_MENU.equals(name)) {
                    TypedArray a = mContext.obtainStyledAttributes(attrs, R.styleable.menu);
                    CookData data = new CookData();
                    data.setName(a.getString(R.styleable.menu_name));
                    data.setEfficacy(a.getString(R.styleable.menu_efficacy));
                    data.setUsage(a.getString(R.styleable.menu_usage));
                    data.setImage(decodeImage(a.getString(R.styleable.menu_image)));
                    data.setSeason(a.getInt(R.styleable.menu_season, 0));
                    final int menudepth = parser.getDepth();
                    Log.d(TAG, "menudepth=" + menudepth);
                    ArrayList<RateItem> rates = new ArrayList<RateItem>();
                    while ((type = parser.next()) != XmlPullParser.END_TAG || parser.getDepth() > menudepth) {
                        if (type != XmlPullParser.START_TAG) {
                            continue;
                        }
                        final String ratename = parser.getName();
                        TypedArray ar = mContext.obtainStyledAttributes(attrs, R.styleable.rate);
                        if(TAG_RATE.equals(ratename)){
                           RateItem item = new RateItem(); 
                           item.itemName = (ar.getString(R.styleable.rate_rate_name));
                           item.itemWeight= (ar.getInt(R.styleable.rate_rate_weight, 100));
                           item.isWater= (ar.getBoolean(R.styleable.rate_rate_water, false));
                           rates.add(item);
                        }
                        ar.recycle();
                        
                    }
                    data.setRates(rates);
                   
                    
                    Log.d(TAG,data.toString());
                    a.recycle();
                    mCooks.add(data);
                }
                
            }
        } catch (XmlPullParserException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    public Bitmap decodeImage(String path) {
        InputStream in = null;
        try {
            in = this.getAssets().open(path);

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (in != null) {
            return BitmapFactory.decodeStream(in);
        } else {
            return null;
        }
    }
}
