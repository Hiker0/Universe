package com.allen.test.tool.cookbook.data;

import android.net.Uri;
import android.provider.BaseColumns;

public class CookbookData {
    public static final String AUTHORITY = "com.allen.test.tool.cookbook.data";
    //数据库版本
    public final static int VERSON=2;
    public final static String DB_NAME="cookbook.db";
    //内容类型 MIME类型
    //文件夹类型
    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.user.mydata";
    //文件类型
    public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.user.mydata";
    
    
    private CookbookData(){};
    public static final class MenuColumns implements BaseColumns{
        public final static String TBL_NAME="MenuTbl";
    	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + TBL_NAME);
        //table
        //这是定义的列名，总共有3列，BaseColumns有一个隐含的_ID成员，不需要再定义
        public static final String 	NAME = "name"; 
        public static final String 	EFFICACY = "efficacy";
        public static final String 	USAGE = "usage"; 
        public static final String IMAGE = "image";
        public static final String SEASON = "season";
        public static final String CUSTOM = "custom";
        //#是一个通配符
        public final static String TBL_ITEM="MenuTbl/#";
        //默认排序常量
        public static final String DEFAULT_SORT_ORDER = null;

        public static final int MENU = 1001;
        public static final int MENU_ID = 1002;
        
    }
    
    public static final class RateColumns implements BaseColumns{
        public final static String TBL_NAME="RateTbl";
        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + TBL_NAME);
        //table
        //这是定义的列名，总共有3列，BaseColumns有一个隐含的_ID成员，不需要再定义
        public static final String  NAME = "name"; 
        public static final String  WEIGHT = "weight";
        public static final String  WATER = "water"; 
        public static final String  PARENT = "parent";
        //#是一个通配符
        public final static String TBL_ITEM="RateTbl/#";
        //默认排序常量
        public static final String DEFAULT_SORT_ORDER = null;

        public static final int RATE = 2001;
        public static final int RATE_ID = 2002;
    }
}
