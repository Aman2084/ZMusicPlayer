package com.zw.utils.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.aman.utils.SQLUtils;
import com.aman.utils.ZUtils;
import com.zw.global.model.data.SongList;

import java.util.ArrayList;

/**
 * ZMusicPlayer 1.0
 * Created on 2017/10/24 2:01
 *
 * @author Aman
 * @Email 1390792438@qq.com
 */

public class SongListDBSQL extends SQLiteOpenHelper {

    private static SongListDBSQL _instance = null;

    private SQLiteDatabase _db;

    public static final String Key_id = "_id";
    public static final String Key_name = "name";
    public static final String Key_type = "type";


    public static final String Name = "SongListDB";
    public static final String Url = "list.db";
    public static final int Version = 1;

    public SongListDBSQL(Context $c) {
        super($c, Name, null, Version);
    }

    public SongListDBSQL(Context $context , int $version) {
        super($context, Url, null, $version);
    }


//listeners
    @Override
    public void onCreate(SQLiteDatabase $db) {
        Log.d("initDB===" , "songlist");
        String s = SQLUtils.dropTable(Name);
        $db.execSQL(s);
        s = SQLUtils.CreateTable + Name;
        String primaryKey = SQLUtils.createInt(Key_id, false) + SQLUtils.PrimaryKey +  SQLUtils.Autoincrement + SQLUtils.NotNull;

        ArrayList<String> a = new ArrayList<String>();
        a.add(primaryKey);
        a.add(SQLUtils.createVarchar(Key_name , false));
        a.add(SQLUtils.createInt(Key_type , true));
        s = SQLUtils.format(s , a);
        $db.execSQL(s);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

//open and close
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

//增删改查
    public long insert(SongList $l){
        ArrayList<SongList> a = new ArrayList<>();
        a.add($l);
        long n = insert(a);
        return n;
    }

    public long insert(ArrayList<SongList> $a){
        long i = -1;
        for(SongList l:$a){
            ContentValues c = new ContentValues();
            c.put(Key_name , l.title);
            c.put(Key_type , l.type);
            i = _db.insert(Name , "" , c);
            if(i==-1){
                return i;
            }
        }
        return i;
    }

    public int delete(int $id){
        String s = Key_id + "=" + $id;
        return delete(s , null);
    }

    public int delete(String $where , String[] $value){
        int i = _db.delete(Name , $where , $value);
        return i;
    }

    public int deleteAll(){
        int i = _db.delete(Name , "1=1" , null);
        return i;
    }

    public int update(SongList $l , String $condition){
        ContentValues c = new ContentValues();
        c.put(Key_name , $l.title);
        c.put(Key_type , $l.type);
        int i = _db.update(Name , c , $condition , null);
        return i;
    }

    public int update(SongList $l){
        String c = Key_id + "=" + $l.id;
        return update($l , c);
    }

    public ArrayList<SongList> query(String $condition){
        String[] a = {Key_id,Key_name,Key_type};

        String sql = String.format("select %s from %s" , ZUtils.join(a,",") , Name);

        if($condition!=null && $condition!=""){
            sql += String.format(" where %s" , $condition);
        }
        sql += ";";

        Cursor c = _db.rawQuery(sql , null);
        ArrayList<SongList> arr = new ArrayList<>();
        if(c.moveToFirst()){
            do{
                SongList l = new SongList();
                l.id = c.getInt(0);
                l.title = c.getString(1);
                l.type = c.getInt(2);
                arr.add(l);
            }while (c.moveToNext());
        }
        return arr;
    }

    public ArrayList<SongList> queryAll(){
        return query("");
    }


//getter and setter
    public String getDBName(){
        String s = _instance==null ? Name : _instance.getDBName();
        return s;
    }

    public static SongListDBSQL getInstance(Context $content , int $version){
        if(_instance==null){
            if($version>0){
                _instance = new SongListDBSQL($content , $version);
            }else{
                _instance = new SongListDBSQL($content , Version);
            }
        }
        return _instance;
    }
}
