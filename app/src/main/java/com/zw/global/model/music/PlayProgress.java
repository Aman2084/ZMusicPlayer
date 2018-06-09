package com.zw.global.model.music;

/**
 * ZMusicPlayer 1.0
 * Created on 2018/5/18 23:17
 *
 * @author Aman
 * @Email 1390792438@qq.com
 */

public class PlayProgress {


    /**乐曲时长（毫秒）*/
    public int duration = 0;
    /**
     * 当前播放进度（毫秒）
     * */
    public int position = 0;

    public PlayProgress(int $duration , int $position){
        setData($duration , $position);
    }


    public void setData(int $duration , int $position){
        duration = $duration;
        position = $position;
    }

    public void setData(PlayProgress $p){
        duration = $p.duration;
        position = $p.position;
    }

    public void reset() {
        duration = 0;
        position = 0;
    }

    public PlayProgress clone(){
        PlayProgress p = new PlayProgress(duration , position);
        return p;
    }
}
