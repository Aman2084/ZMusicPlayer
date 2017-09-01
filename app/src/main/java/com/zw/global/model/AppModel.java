package com.zw.global.model;

import com.zw.global.AppInstance;
import com.zw.global.model.data.Album;
import com.zw.global.model.data.Folder;
import com.zw.global.model.data.Singer;
import com.zw.global.model.data.Song;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * ZMusicPlayer 1.0
 * Created on 2017/8/30 11:43
 *
 * @author Aman
 * @Email: 1390792438@qq.com
 * 数据模型总入库
 */

public class AppModel {

    private static AppModel _instance = null;
    private ArrayList<Song> _allSongs = new ArrayList<Song>();

    public SettingsModel settings;


    public AppModel() throws Exception{
        if(_instance!=null){
            throw new Exception("Singleton");
        }
        _instance = this;

        settings = new SettingsModel();
        _allSongs = AppInstance.songSQL.queryAll();
    }


    public static AppModel getInstance(){
        if(_instance ==null){
            try{
                _instance = new AppModel();
            }catch (Exception $e){}
        }
        return _instance;
    }



//getter and setter
    public void set_allSongs(ArrayList<Song> $allSongs) {
        _allSongs = $allSongs;
    }

    public ArrayList<Song> get_allSongs(){
        ArrayList<Song> a = new ArrayList<Song>();
        a.addAll(_allSongs);
        return a;
    }

    public ArrayList<Album> get_allAlbums(){
        ArrayList<Album> a = new ArrayList<Album>();
        HashMap<String , Album> m = new HashMap<String , Album>();
        int n = _allSongs.size();
        for (int i = 0; i < n; i++) {
            Song s = _allSongs.get(i);
            Album o = m.get(s.albumId);
            if(o==null){
                o = new Album(s.albumId , s.getDisplayAlbum() , 0);
                m.put(s.albumId , o);
                a.add(o);
            }
            o.numSong ++;
        }
        return a;
    }

    public ArrayList<Singer> get_allSingers(){
        ArrayList<Singer> a = new ArrayList<Singer>();
        HashMap<String , Singer> m = new HashMap<String , Singer>();
        int n = _allSongs.size();
        for (int i = 0; i < n; i++) {
            Song s = _allSongs.get(i);
            Singer o = m.get(s.singerId);
            if(o==null){
                o = new Singer(s.singerId , s.getDisplaySinger() , 0);
                m.put(s.singerId , o);
                a.add(o);
            }
            o.numSong ++;
        }
        return a;
    }

    public ArrayList<Folder> get_allFolders(){
        ArrayList<Folder> a = new ArrayList<Folder>();
        HashMap<String , Folder> m = new HashMap<String , Folder>();
        int n = _allSongs.size();
        for (int i = 0; i < n; i++) {
            Song s = _allSongs.get(i);
            Folder o = m.get(s.folderPath);
            if(o==null){
                o = new Folder(s.folderPath , "" , 0);

                String [] arr = s.folderPath.split("/");
                for (int j = arr.length-1; j>-1; j--) {
                    if(arr[j]!=null && arr[j]!=""){
                        o.name = arr[j];
                        break;
                    }
                }
                m.put(s.folderPath , o);
                a.add(o);
            }
            o.numSong ++;
        }
        return a;
    }



    public ArrayList<Song> getSongsBySinger(String $singerId) {
        ArrayList<Song> a = new ArrayList<Song>();
        for (int i = 0; i <_allSongs.size() ; i++) {
            Song s = _allSongs.get(i);
            if(s.singerId==$singerId){
                a.add(s);
            }
        }
        return a;
    }

    public ArrayList<Song> getSongsByAlumb(String $albumId) {
        ArrayList<Song> a = new ArrayList<Song>();
        for (int i = 0; i <_allSongs.size() ; i++) {
            Song s = _allSongs.get(i);
            if(s.albumId==$albumId){
                a.add(s);
            }
        }
        return a;
    }

    public ArrayList<Song> getSongsByFolder(String $path) {
        ArrayList<Song> a = new ArrayList<Song>();
        for (int i = 0; i <_allSongs.size() ; i++) {
            Song s = _allSongs.get(i);
            if(s.folderPath==$path){
                a.add(s);
            }
        }
        return a;
    }
}
