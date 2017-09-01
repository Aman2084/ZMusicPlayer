package com.zw.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.zw.global.AppInstance;
import com.zw.global.model.data.Song;

import java.util.ArrayList;
import java.util.List;

/**
 * ZMusicPlayer 1.0
 * Created on 2017/8/20 18:27
 *
 * @author Aman
 * @Email: 1390792438@qq.com
 * 工具函数类
 */

public class AppUtils {

    public static List<Song> getSongList(Context $c) {
        List<Song> a = new ArrayList<Song>();
        Cursor cursor = $c.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI ,
                null , null , null , MediaStore.Audio.AudioColumns.IS_MUSIC);

        if(cursor!=null){
            while (cursor.moveToNext()){
                Song s = new Song();
                s.id = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID));
                s.name = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME));
//                s.singer = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
                s.singerId = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST_ID));
//                s.album = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM));
                s.albumId = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID));
                s.set_path(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)));
                s.duration = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));
                s.size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE));
                if(s.size>800000 && s.name!=null && s.name.contains("-")){
                    String[] str = s.name.split("-");
                    s.singer = str[0];
                    s.name = str[1];
                    int i = s.name.lastIndexOf(".");
                    if(i>-1){
                        s.name = s.name.substring(0,i);
                    }
                }

                String mUriAlbums = "content://media/external/audio/albums";
                String[] projection = new String[] { "album_art" };
                Cursor cur = $c.getContentResolver().query(Uri.parse(mUriAlbums + "/" + s.albumId),  projection, null, null, null);
                String album_art = null;
                if (cur.getCount() > 0 && cur.getColumnCount() > 0){
                    cur.moveToNext();
                    album_art = cur.getString(0);
                }
                a.add(s);
            }
        }
        cursor.close();
        return a;
    }

    public static String id2String(int $id){
        String s = AppInstance.mainActivity.getString($id);
        return s;
    }
}
