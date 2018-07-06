package com.zw.global.model.music;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.zw.global.enums.SharedPreferencesKeys;

/**
 * ZMusicPlayer 1.0
 * Created on 2018/5/24 23:38
 *
 * @author Aman
 * @Email 1390792438@qq.com
 */

public class PlayModel {


    public String playModel = SongMenu.Order;
    public boolean isLoop = true;
    public boolean isPlaying = false;

    /**当前播放到的歌曲在列表中的位置，非数据库中的Index或RealacationId*/
    public int index = 0;

    public PlayProgress progress = new PlayProgress(0 , 0);


    public PlayModel(Context $c) {
        SharedPreferences p1 = PreferenceManager.getDefaultSharedPreferences($c);
        index = p1.getInt(SharedPreferencesKeys.CurrectMusicIndex , 0);
        progress.position = p1.getInt(SharedPreferencesKeys.CurrectMusicPosition , 0);
        playModel = p1.getString(SharedPreferencesKeys.PlayModel , SongMenu.Order);
        isLoop = p1.getBoolean(SharedPreferencesKeys.PlayLoop , true);
    }

    public void reset(){
        isPlaying = false;
        index = 0;
        progress.reset();
    }
}
