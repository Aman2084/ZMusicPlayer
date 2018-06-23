package com.zw.test;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * ZMusicPlayer 1.0
 * Created on 2018/2/25 23:59
 *
 * @author Aman
 * @Email: 1390792438@qq.com
 */

public class MyContantProvider extends ContentProvider {

    public static final int Records = 1;
    public static final String Authority = "com.zw.test.provider";
    public static final Uri ContentUri = Uri.parse("content://com.zw.test.provider");
    private static final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        matcher.addURI(Authority , "records" , Records);
    }


    @Override
    public boolean onCreate() {
        //TODO...初始化数据库
        return false;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor c = null;
        //TODO... 根据参数从数据库中查询相关数据，并将结果记录到变量c中
        //TODO... 获取到Cursor后执行下列命令
//        c.setNotificationUri(getContext().getContentResolver() , uri);
        return c;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        int t = matcher.match(uri);
        switch (t){
            case Records:
                return ContentResolver.CURSOR_ITEM_BASE_TYPE + "/records";
            default:
                throw new IllegalArgumentException("未知或非法URi : " + uri.toString());
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri $uri, ContentValues $values) {
        long id = 0;
        //TODO... 根据参数向数据库中插入数据，并将该条数据的id记录到变量id中
        Uri u = Uri.withAppendedPath($uri , "/"+id);
        return u;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        //根据参数，删除相关数据
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
