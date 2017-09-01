package com.zw.global;

import android.widget.FrameLayout;

import com.zw.MainActivity;
import com.zw.global.model.AppModel;
import com.zw.utils.sql.SongDBSQL;

/**
 * ZMusicPlayer 1.0
 * Created on 2017/8/18 21:31
 * <p>
 * @author Aman
 * @Email: 1390792438@qq.com
 */

public class AppInstance {

    public static SongDBSQL songSQL;
    public static AppModel model;

    public static MainActivity mainActivity;
    public static FrameLayout layer_second;
    public static FrameLayout layer_third;
    public static FrameLayout layer_popUp;


    public static void clear() {
        mainActivity = null;
        layer_second = null;
        layer_third = null;
        layer_popUp = null;
    }
}
