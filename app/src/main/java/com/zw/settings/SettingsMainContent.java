package com.zw.settings;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.zw.R;

/**
 * Created by Administrator on 2017/8/9.
 */

public class SettingsMainContent extends LinearLayout {

    public SettingsMainContent(Context $c , @Nullable AttributeSet $a){
        super($c , $a);
        LayoutInflater.from($c).inflate(R.layout.settings_main, this , true);



    }

}
