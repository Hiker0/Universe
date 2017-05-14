package com.allen.test.tool.cookbook.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

public class CookbookProvider extends ContentProvider {
    private static final String TAG = "allen.CookbookProvider";
    
    CookbookDbOpenHelper mOpenHelper;
    private static final UriMatcher uriMatcher = new UriMatcher(
            UriMatcher.NO_MATCH);
    static {
        uriMatcher.addURI(CookbookData.AUTHORITY,
                CookbookData.MenuColumns.TBL_NAME,
                CookbookData.MenuColumns.MENU);
        uriMatcher.addURI(CookbookData.AUTHORITY,
                CookbookData.MenuColumns.TBL_NAME + "#",
                CookbookData.MenuColumns.MENU_ID);

        uriMatcher.addURI(CookbookData.AUTHORITY,
                CookbookData.RateColumns.TBL_NAME,
                CookbookData.RateColumns.RATE);
        uriMatcher.addURI(CookbookData.AUTHORITY,
                CookbookData.RateColumns.TBL_NAME + "#",
                CookbookData.RateColumns.RATE_ID);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // TODO Auto-generated method stub
        Log.d(TAG, "delete");
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int count = 0;
        switch (uriMatcher.match(uri)) {
        case CookbookData.MenuColumns.MENU:
            count = db.delete(CookbookData.MenuColumns.TBL_NAME, selection,
                    selectionArgs);
            break;
        case CookbookData.MenuColumns.MENU_ID:
            String textId = uri.getPathSegments().get(1);

            count = db.delete(
                    CookbookData.MenuColumns.TBL_NAME,
                    CookbookData.MenuColumns._ID
                            + "="
                            + textId
                            + (!TextUtils.isEmpty(selection) ? " AND ("
                                    + selection + ')' : ""), selectionArgs);
            break;
        case CookbookData.RateColumns.RATE:
            count = db.delete(CookbookData.RateColumns.TBL_NAME, selection,
                    selectionArgs);
            break;
        case CookbookData.RateColumns.RATE_ID:
            String text = uri.getPathSegments().get(1);
            //
            count = db.delete(
                    CookbookData.RateColumns.TBL_NAME,
                    CookbookData.RateColumns._ID
                            + "="
                            + text
                            + (!TextUtils.isEmpty(selection) ? " AND ("
                                    + selection + ')' : ""), selectionArgs);
            break;
        default:
            throw new IllegalArgumentException("Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public String getType(Uri uri) {
        // TODO Auto-generated method stub
        Log.d(TAG, "getType");
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Log.d(TAG, "insert");
        // TODO Auto-generated method stub
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        long row = 0;
        switch (uriMatcher.match(uri)) {
        case CookbookData.MenuColumns.MENU:
            row = db.insert(CookbookData.MenuColumns.TBL_NAME, null, values);
            break;
        case CookbookData.RateColumns.RATE:
            row = db.insert(CookbookData.RateColumns.TBL_NAME, null, values);
            break;
        default:
            throw new IllegalArgumentException("Unknown URI " + uri);
        }
        return ContentUris.withAppendedId(uri, row);
    }

    @Override
    public boolean onCreate() {
        Log.d(TAG, "onCreate");
        // TODO Auto-generated method stub
        mOpenHelper = new CookbookDbOpenHelper(getContext(),
                CookbookData.DB_NAME, CookbookData.VERSON);


        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
            String[] selectionArgs, String sortOrder) {
        // TODO Auto-generated method stub
        Log.d(TAG, "query");
        SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        switch (uriMatcher.match(uri)) {
        case CookbookData.MenuColumns.MENU:
            Log.d(TAG, "query MENU");
            qb.setTables(CookbookData.MenuColumns.TBL_NAME);
            break;
        case CookbookData.RateColumns.RATE:
            Log.d(TAG, "query RATE");
            qb.setTables(CookbookData.RateColumns.TBL_NAME);
            break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
                
        }
        
        Cursor result = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);


        result.setNotificationUri(getContext().getContentResolver(), uri);

        return result;

    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
            String[] selectionArgs) {
        // TODO Auto-generated method stub
        Log.d(TAG, "update");
        SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        int count = 0;
        switch (uriMatcher.match(uri)) {
        case CookbookData.MenuColumns.MENU:
            count = db.update(CookbookData.MenuColumns.TBL_NAME, values, selection, selectionArgs);
            break;
        case CookbookData.RateColumns.RATE:
            count = db.update(CookbookData.RateColumns.TBL_NAME, values, selection, selectionArgs);
            break;
        }
        return count;
    }
    
}
