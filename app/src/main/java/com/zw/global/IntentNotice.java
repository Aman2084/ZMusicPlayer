package com.zw.global;

/**
 * 存储常量，用于标识某种程序行为已经或正在发生
 *
 * ZMusicPlayer 1.0
 * Created on 2018/5/18 22:04
 *
 * @author Aman
 * @Email 1392438@qq.com
 */

public class IntentNotice {

    public static final String Song_Import = "data_ImportSongs";
    public static final String Song_Delete = "data_DeleteSongs";
    public static final String SongList_Creat = "data_CreatSongList";
    public static final String SongList_Delete = "data_DeleteSongList";
    public static final String SongList_UpData = "data_UpDataSongList";

    public static final String SongList_UpDataFavorite = "data_UpDataFavorite";
    public static final String SongList_UpDataPlayList = "data_UpDataPlayList";
    public static final String PlayModelChanged = "playModelChanged";
    public static final String PlayLoopChanged = "playLoopChanged";

    public static final String MusicReady = "musicReady";
    public static final String MusicStart = "musicStart";
    public static final String MusicPause = "musicPause";
    public static final String MusicProgress = "musicProgress";
    public static final String MusicStop = "musicStop";
    public static final String MusicComplete = "musicComplete";
    public static final String MusicListComplete = "musicListComplete";
    public static final String MusicError = "musicError";
    public static final String MusicSeekComplete = "musicSeekComplete";

    public static final String ActivityResumed = "activityResumed";
//    public static final String PlayMusicResumed = "activityResumed";
}
