package com.zw.global.model.music;

import com.zw.music.SongMenu;

/**
 * ZMusicPlayer 1.0
 * Created on 2018/5/24 23:38
 *
 * @author Aman
 * @Email 1390792438@qq.com
 */

public class PlayModel {

    public SongMenu menu = null;

    public String playModel = SongMenu.Order;
    public boolean isLoop = true;
    public boolean isPlaying = false;


    /**当前播放到的歌曲在列表中的位置，非数据库中的Index或RealacationId*/
    public int index = 0;

    public PlayProgress progress = new PlayProgress(0 , 0);
}
