package com.allen.test.data;

import android.net.Uri;
import android.provider.BaseColumns;

public class SelectorData {
    public static final String AUTHORITY = "com.allen.test.data.SelectorData";
    public final static int VERSON=2;
    public final static String DB_NAME="selectordata.db";

    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.user.mydata";

    public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.user.mydata";
    
    
    private SelectorData(){};
    public static final class DataColumns implements BaseColumns{
        public final static String TBL_NAME="DataTbl";
    	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + TBL_NAME);
        //table
        public static final String 	TYPE = "type"; 
        public static final String 	GROUP = "group";
        public static final String 	NAME = "type"; 
        public static final String PICTURE = "picture";
        public static final String DETAIL = "detail";

        public final static String TBL_ITEM="DataTbl/#";

        public static final String DEFAULT_SORT_ORDER = null;

       
    }
}
