package com.aman.media;

import com.aman.utils.observer.ZNotification;

/**
 * AmanQuick 1.0
 * Created on 2018/4/24 23:01
 *
 * @author Aman
 * @Email 1390792438@qq.com
 */

public class ZAudioNotification extends ZNotification {

    /**音乐准备就绪*/
    public static final String Prepared = "prepared";
    /**开始播放*/
    public static final String Play = "play";
    /**广播播放进度*/
    public static final String Progress = "progress";
    /**暂停*/
    public static final String Pause = "pause";
    /**跳播寻道成功*/
    public static final String Seek = "seek";
    /**播放完成*/
    public static final String Complete = "complete";
    /**播放停止*/
    public static final String Stop = "stop";
    /**播放报错*/
    public static final String Error = "error";

    /**乐曲时长（毫秒）*/
    public int duration = 0;
    /**
     * 当前播放进度（毫秒）
     * */
    public int position = 0;

    public ZAudioNotification(String $name , int $duration , int $position) {
        super($name);
        duration = $duration;
        position = $position;
    }

    public ZAudioNotification(String $name) {
        super($name);
    }

    public ZAudioNotification(String $name, Object $data) {
        super($name, $data);
    }

    public ZAudioNotification(String $name, Object $data, String $action) {
        super($name, $data, $action);
    }
}
