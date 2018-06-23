package com.zw.global.model.data;

import com.aman.utils.observer.ZObservable;

/**
 * ZMusicPlayer 1.0
 * Created on 2017/11/24 1:10
 *
 * @author Aman
 * @Email 1390792438@qq.com
 *
 * 歌曲列表中的每一项
 */

public class SongListItem extends ZObservable {

    public static final String Stop = "Stop";
    public static final String Play = "play";
    public static final String Pause = "pause";

    public String songId = null;
    public int relationId = -1;
    /**是否为收藏歌曲*/
    public boolean isFavorite = false;

    /**
     * 在歌单中的序列
     */
    public int index = -1;
    public boolean selected = false;

    public Song song = null;

    /**播放参数*/
    public String stause = Stop;
    public int position ;

    public SongListItem clone(){
        SongListItem s = new SongListItem();
        s.index = index;
        s.songId = songId;
        s.relationId = relationId;
        s.selected = selected;
        s.isFavorite = isFavorite;
        s.song = song;
        s.stause = stause;
        s.position = position;
        return s;
    }
}
