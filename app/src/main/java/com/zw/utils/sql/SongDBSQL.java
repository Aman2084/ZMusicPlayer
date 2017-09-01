package com.zw.utils.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.aman.utils.SQLUtils;
import com.aman.utils.ZUtils;
import com.zw.global.model.data.Song;

import java.util.ArrayList;

/**
 * ZMusicPlayer 1.0
 * Created on 2017/8/28 19:15
 *
 * @author Aman
 * @Email: 1390792438@qq.com
 */

public class SongDBSQL extends SQLiteOpenHelper {

    public static final String Key_id = "sid";
    public static final String Key_name = "name";
    public static final String Key_singer = "singer";
    public static final String Key_fileName = "filename";
    public static final String Key_singerId = "singerid";
    public static final String Key_album = "album";
    public static final String Key_albumId = "albumId";
    public static final String Key_path = "path";
    public static final String Key_folderPath = "folderPath";
    public static final String Key_duration = "duration";
    public static final String Key_size = "size";


    public static final String Name = "SongDB";
    public static final String Url = "song.db";
    public static final int Version = 1;

    private static SongDBSQL _instance = null;

    private SQLiteDatabase _db;

    public SongDBSQL(Context $context) {
        super($context, Url, null, Version);
    }

    public SongDBSQL(Context $context , int $version) {
        super($context, Url, null, $version);
    }


//listeners
    @Override
    public void onCreate(SQLiteDatabase $db) {
        String s = SQLUtils.dropTable(Name);
        $db.execSQL(s);
        s = SQLUtils.CreateTable + Name;
        String primaryKey = SQLUtils.createInt("_id", false) + SQLUtils.PrimaryKey +  SQLUtils.Autoincrement + SQLUtils.NotNull;
        ArrayList<String> a = new ArrayList<String>();
        a.add(primaryKey);
        a.add(SQLUtils.createVarchar(Key_id, true));
        a.add(SQLUtils.createVarchar(Key_name , false));
        a.add(SQLUtils.createVarchar(Key_singer , false));
        a.add(SQLUtils.createVarchar(Key_singerId , false));
        a.add(SQLUtils.createVarchar(Key_album , false));
        a.add(SQLUtils.createVarchar(Key_albumId , false));
        a.add(SQLUtils.createVarchar(Key_path , true));
        a.add(SQLUtils.createInt(Key_duration , true));
        a.add(SQLUtils.createLong(Key_size , true));

        s = SQLUtils.format(s , a);
        $db.execSQL(s);
    }

    @Override
    public void onUpgrade(SQLiteDatabase $db, int oldVersion, int $newVersion) {
        if($newVersion>1){
            String s = SQLUtils.AlertTable + Name + SQLUtils.AddColumn +
                    SQLUtils.createVarchar("phone" , false) + ";";
            Log.d(Name , "exe：" + s);
            $db.execSQL(s);
        }
    }

    public SQLiteDatabase openReadLink(){
        if(_db==null || !_db.isOpen()){
            _db = _instance.getReadableDatabase();
        }
        return _db;
    }

    public SQLiteDatabase openWriteLink(){
        if(_db==null || !_db.isOpen()){
            _db = _instance.getWritableDatabase();
        }
        return _db;
    }

    public void closeLink(){
        if(_db!=null && _db.isOpen()){
            _db.close();;
            _db = null;
        }
    }

//增删改查：
    public long insert(Song $s){
        ArrayList<Song> a = new ArrayList<Song>();
        a.add($s);
        long l = insert(a);
        return l;
    };


    public long insert(ArrayList<Song> $a){
        long l = -1;
        for (Song s:$a) {
            ContentValues c = new ContentValues();
            c.put(Key_id, s.id);
            c.put(Key_name , s.name);
            c.put(Key_singer , s.singer);
            c.put(Key_singerId , s.singerId);
            c.put(Key_album , s.album);
            c.put(Key_albumId , s.albumId);
            c.put(Key_path , s.path);
            c.put(Key_duration , s.duration);
            c.put(Key_size , s.size);
            l = _db.insert(Name , "" ,c);
            if(l==-1){
                return l;
            }
        }
        return l;
    }

    public int delete(String $condition){
        int i = _db.delete(Name , $condition , null);
        return i;
    }

    public int deleteAll(){
        int i = _db.delete(Name , "1=1" , null);
        return i;
    }

    public int update(Song $s , String $condition){
        ContentValues c = new ContentValues();
        c.put(Key_id, $s.id);
        c.put(Key_name , $s.name);
        c.put(Key_singer , $s.singer);
        c.put(Key_singerId , $s.singerId);
        c.put(Key_album , $s.album);
        c.put(Key_albumId , $s.albumId);
        c.put(Key_path , $s.path);
        c.put(Key_duration , $s.duration);
        c.put(Key_size , $s.size);
        int i = _db.update(Name , c , $condition , null);
        return i;
    }

    public int update(Song $s){
        String c = Key_id + "=" + $s.id;
        return update($s , c);
    }

    public ArrayList<Song> query(String $condition){
        String[] a = {Key_id,Key_name,Key_singer,Key_singerId,Key_album,Key_albumId,Key_path,Key_duration,Key_size};

        String sql = String.format("select %s from %s" , ZUtils.join(a,",") , Name);

        if($condition!=null && $condition!=""){
            sql += String.format(" where %s" , $condition);
        }
        sql += ";";

        Cursor c = _db.rawQuery(sql , null);
        ArrayList<Song> arr = new ArrayList<Song>();
        if(c.moveToFirst()){
            do{
                Song s = new Song();
                s.id = c.getString(0);
                s.name = c.getString(1);
                s.singer = c.getString(2);
                s.singerId = c.getString(3);
                s.album = c.getString(4);
                s.albumId = c.getString(5);
                s.set_path(c.getString(6));
                s.duration = c.getInt(7);
                s.size = c.getLong(8);
                arr.add(s);
            }while (c.moveToNext());
        }
        return arr;
    }

    public ArrayList<Song> queryAll(){
        return query("");
    }

//listeners
    public void resetAllSong(ArrayList<Song> $a){
        openWriteLink();
        deleteAll();
        insert($a);
    }

//getter and setter
    public String getDBName(){
        String s = _instance==null ? Name : _instance.getDBName();
        return s;
    }

    public static SongDBSQL getInstance(Context $content , int $version){
        if(_instance==null){
            if($version>0){
                _instance = new SongDBSQL($content , $version);
            }else{
                _instance = new SongDBSQL($content , Version);
            }
        }
        return _instance;
    }
}
