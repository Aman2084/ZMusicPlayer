package com.zw.utils.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.aman.utils.SQLUtils;
import com.aman.utils.ZUtils;
import com.zw.global.model.data.Song;
import com.zw.global.model.data.SongList;
import com.zw.global.model.data.SongListItem;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * ZMusicPlayer 1.0
 * Created on 2017/10/24 5:07
 *
 * @author Aman
 * @Email 1390792438@qq.com
 */

public class RelationDBSQL extends SQLiteOpenHelper {


    private static RelationDBSQL _instance = null;

    private SQLiteDatabase _db;

    public static final String Key_id = "_id";
    public static final String Key_songListId = "songListId";
    public static final String Key_songId = "songId";


    public static final String Name = "RelationDB";
    public static final String Url = "relationDB.db";
    public static final int Version = 1;

    public RelationDBSQL(Context $c) {
        super($c, Name, null, Version);
    }

    public RelationDBSQL(Context $context , int $version) {
        super($context, Url, null, $version);
    }

//listeners
    @Override
    public void onCreate(SQLiteDatabase $db) {
        String s = SQLUtils.dropTable(Name);
        $db.execSQL(s);
        s = SQLUtils.CreateTable + Name;
        String primaryKey = SQLUtils.createInt(Key_id, false) + SQLUtils.PrimaryKey +  SQLUtils.Autoincrement + SQLUtils.NotNull;
        ArrayList<String> a = new ArrayList<>();
        a.add(primaryKey);
        a.add(SQLUtils.createVarchar(Key_songId , true));
        a.add(SQLUtils.createInt(Key_songListId , true));
        s = SQLUtils.format(s , a);
        $db.execSQL(s);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

//增删改查
    public ArrayList<SongListItem> insert(int $listId , ArrayList<Song> $a){
        ArrayList<SongListItem> a = new ArrayList<>();
        for (int j = 0; j <$a.size() ; j++) {
            Song s = $a.get(j);
            ContentValues c = new ContentValues();
            c.put(Key_songId , s.get_id());
            c.put(Key_songListId , $listId);
            long i = _db.insert(Name , "" , c);
            if(i!=-1){
                SongListItem item = new SongListItem();
                item.songId = s.get_id();
                item.relationId = (int)i;
                a.add(item);
            }else{
                break;
            }
        }
        return a;
    }

    public int deleteBySongId(String $id) {
        String s = Key_songId + "=" + $id;
        int i = _db.delete(Name , s , null);
        return i;
    }

    public int delete(SongList $l){
        String s = Key_songListId + "=" + $l.id;
        int i = _db.delete(Name , s , null);
        return i;
    }

    public int deleteAll(){
        int i = _db.delete(Name , "1=1" , null);
        return i;
    }

    public void deleteRelations(ArrayList<Integer> $a) {
        for (int i = 0; i <$a.size() ; i++) {
            String where = Key_id + "=" + $a.get(i);
            _db.delete(Name, where , null);
        }
    }

    public void deleteBySongIds(ArrayList<String> $a) {
        for (int i = 0; i <$a.size() ; i++) {
            String where = Key_songId + "=" + $a.get(i);
            _db.delete(Name, where , null);
        }
    }

    public ArrayList<SongListItem> update(SongList $l , ArrayList<Song> $a){
        delete($l);
        return insert($l.id , $a);
    }

    public HashMap<Integer , ArrayList<SongListItem>> query(String $condition){
        String[] a = {Key_songListId , Key_songId , Key_id};
        String sql = String.format("select %s from %s" , ZUtils.join(a,",") , Name);

        if($condition!=null && $condition!=""){
            sql += String.format(" where %s" , $condition);
        }
        sql += ";";

        Cursor c = _db.rawQuery(sql , null);
        HashMap<Integer , ArrayList<SongListItem>> map = new HashMap<>();
        if(c.moveToFirst()){
            do{
                int songListId = c.getInt(0);
                SongListItem s = new SongListItem();
                s.songId = c.getString(1);
                s.relationId = c.getInt(2);

                ArrayList<SongListItem> arr = map.get(songListId);
                if(arr==null){
                    arr = new ArrayList<>();
                    map.put(songListId , arr);
                }
                arr.add(s);
            }while (c.moveToNext());
        }
        return map;
    }

    public HashMap<Integer , ArrayList<SongListItem>> query(int $id){
        String s = Key_songListId + "=" + $id;
        return query(s);
    }

    public HashMap<Integer , ArrayList<SongListItem>> query(SongList $l){
        return query($l);
    }

    public HashMap<Integer , ArrayList<SongListItem>> queryAll(){
        return query("");
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


//getter and setter
    public String getDBName(){
        String s = _instance==null ? Name : _instance.getDBName();
        return s;
    }

    public static RelationDBSQL getInstance(Context $content , int $version){
        if(_instance==null){
            if($version>0){
                _instance = new RelationDBSQL($content , $version);
            }else{
                _instance = new RelationDBSQL($content , Version);
            }
        }
        return _instance;
    }
}
