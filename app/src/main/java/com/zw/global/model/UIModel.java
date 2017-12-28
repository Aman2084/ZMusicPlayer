package com.zw.global.model;

import android.util.DisplayMetrics;

import com.zw.global.AppInstance;

/**
 * ZMusicPlayer 1.0
 * Created on 2017/10/20 18:20
 *
 * @author Aman
 * @Email: 1390792438@qq.com
 */

public class UIModel {

    public int width_px;
    public int height_px;

    public UIModel() {
        DisplayMetrics m = new DisplayMetrics();
        AppInstance.mainActivity.getWindowManager().getDefaultDisplay().getMetrics(m);
        width_px = m.widthPixels;
        height_px = m.heightPixels;
    }
}
