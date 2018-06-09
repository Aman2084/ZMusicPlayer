package com.zw.global.model;

import com.zw.global.model.data.Song;
import com.zw.global.model.data.SongListItem;
import com.zw.global.model.my.SongListModel;
import com.zw.global.model.my.SongModel;

import java.util.ArrayList;

/**
 * ZMusicPlayer 1.0
 * Created on 2017/10/26 14:10
 *
 * @author Aman
 * @Email 1390792438@qq.com
 */

public class MySongModel {

    public SongModel song;
    public SongListModel songList;


    public MySongModel() {
        song = new SongModel();
        songList = new SongListModel();
        songList.init(song.get_allSongs());
    }


    /**
     * <br/><br/>
     * 根据SongId把相关的Song和SongList中的相关数据从数据库和本地数据中删除
     *
     * @param  $a ArrayList<String>  要删除的SongId
     *
     * */
    public void deleteSongs(ArrayList<String> $a){
        song.deleteSongs($a);
        songList.deleteSongs($a);
    };


    public static ArrayList<SongListItem> CloneListSong(ArrayList<SongListItem> $a){
        ArrayList<SongListItem> a = new ArrayList<>();
        for (int i = 0; i <$a.size() ; i++) {
            SongListItem s = $a.get(i);
            a.add(s.clone());
        }
        return a;
    }

    public static ArrayList<Song> SongListItems2Songs(ArrayList<SongListItem> $a){
        ArrayList<Song> a = new ArrayList<>();
        for (int i = 0; i <$a.size() ; i++) {
            SongListItem s = $a.get(i);
            a.add(s.song);
        }
        return a;
    }

    public static ArrayList<SongListItem> Songs2SongListItems(ArrayList<Song> $a){
        ArrayList<SongListItem> a = new ArrayList<>();
        for (int i = 0; i <$a.size() ; i++) {
            Song s = $a.get(i);
            SongListItem item = new SongListItem();
            item.songId = s.get_id();
            item.song = s;
            a.add(item);
        }
        return a;
    }

//Song





}