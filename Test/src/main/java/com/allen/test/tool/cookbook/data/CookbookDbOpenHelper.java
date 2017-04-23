package com.allen.test.tool.cookbook.data;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Xml;

import com.allen.test.R;

public class CookbookDbOpenHelper extends SQLiteOpenHelper {
    private static final String TAG = "allen.CookbookDbOpenHelper";
    final static String TAG_COOKBOOK = "menu";
    final static String TAG_MENU = "menu";
    final static String TAG_RATES = "rates";
    final static String TAG_RATE = "rate";

    Context mContext = null;
    private static final HashSet<String> mValidTables = new HashSet<String>();

    static {
        mValidTables.add(CookbookData.MenuColumns.TBL_NAME);
        mValidTables.add(CookbookData.RateColumns.TBL_NAME);
    }

    public CookbookDbOpenHelper(Context context, String name, int version) {
        super(context, name, null, version);
        // TODO Auto-generated constructor stub
        mContext = context;
    }

    public static boolean isValidTable(String name) {
        return mValidTables.contains(name);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "onCreate");
        // TODO Auto-generated method stub
        db.execSQL("CREATE TABLE " + CookbookData.MenuColumns.TBL_NAME + "("
                + CookbookData.MenuColumns._ID + " INTEGER PRIMARY KEY,"
                + CookbookData.MenuColumns.NAME + " TEXT ,"
                + CookbookData.MenuColumns.EFFICACY + " TEXT,"
                + CookbookData.MenuColumns.USAGE + " TEXT,"
                + CookbookData.MenuColumns.IMAGE + " BLOB DEFAULT NULL,"
                + CookbookData.MenuColumns.CUSTOM + " INTEGER,"  
                + CookbookData.MenuColumns.SEASON + " INTEGER"
                + ");");

        db.execSQL("CREATE TABLE " + CookbookData.RateColumns.TBL_NAME + "("
                + CookbookData.RateColumns._ID + " INTEGER PRIMARY KEY,"
                + CookbookData.RateColumns.NAME + " TEXT,"
                + CookbookData.RateColumns.WEIGHT + " INTEGER,"
                + CookbookData.RateColumns.PARENT + " TEXT,"
                + CookbookData.RateColumns.WATER + " INTEGER" + ");");

        boolean loadSuccess = false;
        if (!loadSuccess) {
            loadItem(db, R.xml.default_cookitem);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS " + CookbookData.MenuColumns.TBL_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + CookbookData.RateColumns.TBL_NAME);
        // Recreates the database with a new version
        onCreate(db);
    }

    private void loadItem(SQLiteDatabase db, int workspaceResourceId) {
        Log.d(TAG, "loadItem begin>>");
        AssetManager asset = mContext.getAssets();
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

            while (((type = parser.next()) != XmlPullParser.END_TAG || parser
                    .getDepth() > depth) && type != XmlPullParser.END_DOCUMENT) {
                final String name = parser.getName();
                if (type != XmlPullParser.START_TAG) {
                    Log.d(TAG, "parser type = XmlPullParser.END_TAG");
                    continue;
                }

                if (TAG_MENU.equals(name)) {
                    TypedArray a = mContext.obtainStyledAttributes(attrs,
                            R.styleable.menu);

                    ContentValues values = new ContentValues();
                    values.put(CookbookData.MenuColumns.NAME,
                            a.getString(R.styleable.menu_name));
                    values.put(CookbookData.MenuColumns.EFFICACY,
                            a.getString(R.styleable.menu_efficacy));
                    values.put(CookbookData.MenuColumns.USAGE,
                            a.getString(R.styleable.menu_usage));
                    values.put(CookbookData.MenuColumns.SEASON,
                            a.getInt(R.styleable.menu_season, 0));
                    values.put(CookbookData.MenuColumns.CUSTOM, false);
                    writeImageToDb(asset, a.getString(R.styleable.menu_image),
                            values);
                    long row = db.insert(CookbookData.MenuColumns.TBL_NAME, null,
                            values);
                    Log.d(TAG, "insert MenuColumns row = "+ row);
                    a.recycle();

                    final int menudepth = parser.getDepth();
                    while ((type = parser.next()) != XmlPullParser.END_TAG
                            || parser.getDepth() > menudepth) {
                        if (type != XmlPullParser.START_TAG) {
                            continue;
                        }
                        final String ratename = parser.getName();

                        if (TAG_RATE.equals(ratename)) {
                            TypedArray ar = mContext.obtainStyledAttributes(
                                    attrs, R.styleable.rate);
                            ContentValues values2 = new ContentValues();
                            values2.put(CookbookData.RateColumns.NAME,
                                    ar.getString(R.styleable.rate_rate_name));
                            values2.put(CookbookData.RateColumns.WEIGHT, ar
                                    .getInt(R.styleable.rate_rate_weight, 100));
                            values2.put(CookbookData.RateColumns.WATER, ar
                                    .getBoolean(R.styleable.rate_rate_water,
                                            false));
                            values2.put(CookbookData.RateColumns.PARENT, row);
                            long row1 = db.insert(CookbookData.RateColumns.TBL_NAME, null,
                                    values2);
                            Log.d(TAG, "insert RateColumns row = "+ row1);
                            ar.recycle();
                        }

                    }
                }

            }
        } catch (XmlPullParserException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Log.d(TAG, "loadItem end <<");
    }

    public void writeImageToDb(AssetManager asset, String imagePath,
            ContentValues values) {
        InputStream in = null;
        Bitmap mBitmap = null;
        try {
            in = asset.open(imagePath);

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (in != null) {
            mBitmap = BitmapFactory.decodeStream(in);
        } else {
            return;
        }

        if (mBitmap != null && mBitmap.getHeight() > 0
                && mBitmap.getWidth() > 0) {
            int size = mBitmap.getWidth() * mBitmap.getHeight() * 4;
            ByteArrayOutputStream out = new ByteArrayOutputStream(size);
            try {
                mBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                out.flush();
                out.close();
                byte[] data = out.toByteArray();
                values.put(CookbookData.MenuColumns.IMAGE, data);
                mBitmap.recycle();
            } catch (IOException e) {
                Log.w("Favorite", "Could not write icon");
            }
        }

        try {
            in.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    // public void recycleImage() {
    // if (mView != null) {
    // ImageView thumb = (ImageView) mView.findViewById(R.id.id_thumb);
    // thumb.setImageBitmap(null);
    // }
    //
    // if (mBitmap != null) {
    // mBitmap.recycle();
    // mBitmap = null;
    // }
    // }
}
