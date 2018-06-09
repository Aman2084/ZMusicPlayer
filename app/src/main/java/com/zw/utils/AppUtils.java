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
 * @Email 1390792438@qq.com
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
                s.set_id(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)));
                s.setName(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)));
//              s.singer = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
                s.setSingerId(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST_ID)));
//              s.album = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM));
                s.setAlbumId(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID)));
                s.set_path(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)));
                s.setDuration(cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)));
                s.setSize(cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE)));
                if(s.getSize()>800000 && s.getName()!=null && s.getName().contains("-")){
                    String[] str = s.getName().split("-");
                    s.setSinger(str[0]);
                    s.setName(str[1]);
                    int i = s.getName().lastIndexOf(".");
                    if(i>-1){
                        s.setName(s.getName().substring(0,i));
                    }
                }

                String mUriAlbums = "content://media/external/audio/albums";
                String[] projection = new String[] { "album_art" };
                Cursor cur = $c.getContentResolver().query(Uri.parse(mUriAlbums + "/" + s.getAlbumId()),  projection, null, null, null);
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
