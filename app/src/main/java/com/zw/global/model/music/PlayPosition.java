package com.zw.global.model.music;

/**
 * 播放定位
 *
 * ZMusicPlayer 1.0
 * Created on 2018/6/12 22:30
 *
 * @author Aman
 * @Email 1390792438@qq.com
 */

public class PlayPosition {

    /**
     * 歌单Id
     */
    public int songListId;
    /**
     * 当前播放的歌曲的关系Id
     * */
    public int relationId;
    /**
     * 当前播放到的毫秒数（ms）
     */
    public int position = 0;

    public PlayPosition(int $songListId , int $relationId , int $position){
        songListId = $songListId;
        relationId = $relationId;
        position = $position;
    }

    public PlayPosition(int $songListId , int $relationId) {
        songListId = $songListId;
        relationId = $relationId;
    }


}
