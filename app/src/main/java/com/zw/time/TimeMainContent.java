package com.zw.time;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.zw.R;

/**
 * Created by Administrator on 2017/8/9.
 */

public class TimeMainContent extends LinearLayout {

    public TimeMainContent(Context $c , @Nullable AttributeSet $a){
        super($c , $a);
        LayoutInflater.from($c).inflate(R.layout.settings_main , this , true);
    }

}
