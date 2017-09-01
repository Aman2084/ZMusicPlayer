package com.zw.test;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;

import com.aman.ui.containers.ZRelativeLayout;
import com.zw.R;

/**
 * ZMusicPlayer 1.0
 * Created on 2017/8/26 12:55
 *
 * @author Aman
 * @Email: 1390792438@qq.com
 */

public class UITest extends ZRelativeLayout {



    public UITest(Context $c, AttributeSet $a) {
        super($c, $a);

        LayoutInflater.from($c).inflate(R.layout.my_musicfloders , this);
    }
}
