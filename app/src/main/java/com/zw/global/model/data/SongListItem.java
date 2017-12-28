package com.zw.global.model.data;

/**
 * ZMusicPlayer 1.0
 * Created on 2017/11/24 1:10
 *
 * @author Aman
 * @Email: 1390792438@qq.com
 *
 * 歌曲列表中的每一项
 */

public class SongListItem {

    public String songId = null;

    public int relationId = -1;

    public boolean selected = false;
    /**是否为收藏歌曲*/
    public boolean isFavorite = false;

    public Song song = null;



    public SongListItem clone(){
        SongListItem s = new SongListItem();
        s.songId = songId;
        s.relationId = relationId;
        s.selected = selected;
        s.song = song;
        s.isFavorite = isFavorite;
        return s;
    }
}
