package com.aman.ui.containers.items;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

/**
 * AmanQuick 1.0
 * Created on 2017/8/31 8:24
 *
 * @author Aman
 * @Email 1390792438@qq.com
 */

public class ZFrameItem extends FrameLayout {

    public ZFrameItem(Context $c , int $layout){
        super($c , null);
        LayoutInflater.from($c).inflate($layout , this);
    }

}
