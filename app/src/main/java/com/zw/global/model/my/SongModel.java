package com.zw.global.model.my;

import com.zw.global.AppInstance;
import com.zw.global.IntentNotice;
import com.zw.global.model.data.Album;
import com.zw.global.model.data.Folder;
import com.zw.global.model.data.Singer;
import com.zw.global.model.data.Song;
import com.zw.utils.ZProgress;
import com.zw.utils.sql.RelationDBSQL;
import com.zw.utils.sql.SongDBSQL;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * ZMusicPlayer 1.0
 * Created on 2017/12/5 20:54
 *
 * @author Aman
 * @Email 1390792438@qq.com
 */

public class SongModel extends ZProgress {

    private ArrayList<Song> _allSongs = new ArrayList<>();

    public SongModel() {
        super(null, null);

        SongDBSQL sql_song = AppInstance.songSQL;
        sql_song.openReadLink();
        _allSongs = sql_song.queryAll();
        sql_song.close();
    }


//增
    public void set_allSongs(ArrayList<Song> $allSongs) {
        _allSongs = $allSongs;
        AppInstance.songSQL.resetAllSong($allSongs);
        sendIntent(IntentNotice.Song_Import, _allSongs);
    }

//删
    /**
     * <br/><br/>
     * 根据SongId把相关的Song和SongList中的相关数据从数据库和本地数据中删除
     *
     * @param  $a ArrayList<String>  要删除的SongId
     *
     * @return
     *      ArrayList<Song> 被删除的歌曲
     *
     * */
    public ArrayList<Song> deleteSongs(ArrayList<String> $a){
        ArrayList<Song> a = new ArrayList<>();
        if($a.size()==0){
            return a;
        }

        SongDBSQL sql_song = AppInstance.songSQL;
        RelationDBSQL sql_relation = AppInstance.relationDBSQL;

        sql_song.openWriteLink();
        sql_relation.openWriteLink();
        int num = 0;
        for (int i = 0; i <_allSongs.size(); i++) {
            Song s = _allSongs.get(i);
            if($a.indexOf(s.get_id())>-1){
                //删除本地数据
                _allSongs.remove(i);
                i--;
                //删除歌曲表数据
                sql_song.deleteById(s.get_id());
                //删除歌单中的表数据
                num += sql_relation.deleteBySongId(s.get_id());
                a.add(s);
            }
        }
        sql_song.close();
        sql_relation.close();
        sendIntent(IntentNotice.Song_Delete, a);
        return a;
    }

//查
    public ArrayList<Song> get_allSongs(){
        ArrayList<Song> a = new ArrayList<>(_allSongs);
        return a;
    }

    public ArrayList<Album> get_allAlbums(){
        ArrayList<Album> a = new ArrayList<>();
        HashMap<String , Album> m = new HashMap<>();
        int n = _allSongs.size();
        for (int i = 0; i < n; i++) {
            Song s = _allSongs.get(i);
            Album o = m.get(s.getAlbumId());
            if(o==null){
                o = new Album(s.getAlbumId() , s.getDisplayAlbum() , 0);
                m.put(s.getAlbumId() , o);
                a.add(o);
            }
            o.numSong ++;
        }
        return a;
    }

    public ArrayList<Singer> get_allSingers(){
        ArrayList<Singer> a = new ArrayList<>();
        HashMap<String , Singer> m = new HashMap<>();
        int n = _allSongs.size();
        for (int i = 0; i < n; i++) {
            Song s = _allSongs.get(i);
            Singer o = m.get(s.getSingerId());
            if(o==null){
                o = new Singer(s.getSingerId() , s.getDisplaySinger() , 0);
                m.put(s.getSingerId() , o);
                a.add(o);
            }
            o.numSong ++;
        }
        return a;
    }

    public ArrayList<Folder> get_allFolders(){
        ArrayList<Folder> a = new ArrayList<>();
        HashMap<String , Folder> m = new HashMap<String , Folder>();
        int n = _allSongs.size();
        for (int i = 0; i < n; i++) {
            Song s = _allSongs.get(i);
            Folder o = m.get(s.getFolderPath());
            if(o==null){
                o = new Folder(s.getFolderPath() , "" , 0);

                String [] arr = s.getFolderPath().split("/");
                for (int j = arr.length-1; j>-1; j--) {
                    if(arr[j]!=null && arr[j]!=""){
                        o.name = arr[j];
                        break;
                    }
                }
                m.put(s.getFolderPath() , o);
                a.add(o);
            }
            o.numSong ++;
        }
        return a;
    }

    public ArrayList<Song> getSongsBySinger(String $singerId) {
        ArrayList<Song> a = new ArrayList<>();
        for (int i = 0; i <_allSongs.size() ; i++) {
            Song s = _allSongs.get(i);
            if(s.getSingerId().equals($singerId)){
                a.add(s);
            }
        }
        return a;
    }

    public ArrayList<Song> getSongsByAlumb(String $albumId) {
        ArrayList<Song> a = new ArrayList<>();
        for (int i = 0; i <_allSongs.size() ; i++) {
            Song s = _allSongs.get(i);
            boolean b;
            if($albumId==null){
                b = s.getAlbumId()==null;
            }else{
                b = $albumId.equals(s.getAlbumId());
            }
            if(b){
                a.add(s);
            }
        }
        return a;
    }

    public ArrayList<Song> getSongsByFolder(String $path) {
        ArrayList<Song> a = new ArrayList<>();
        for (int i = 0; i <_allSongs.size() ; i++) {
            Song s = _allSongs.get(i);
            String p = s.getFolderPath();
            if(p.equals($path)){
                a.add(s);
            }
        }
        return a;
    }

}
