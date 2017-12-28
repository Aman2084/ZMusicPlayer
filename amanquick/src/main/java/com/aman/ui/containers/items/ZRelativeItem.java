package com.aman.ui.containers.items;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;

import com.aman.ui.containers.ZRelativeLayout;

import java.util.Observer;

/**
 * AmanQuick 1.0
 * Created on 2017/8/31 8:33
 *
 * @author Aman
 * @Email: 1390792438@qq.com
 */

public class ZRelativeItem extends ZRelativeLayout {

    public ZRelativeItem(Context $c , int $layout , @Nullable Observer $b){
        super($c , null);
        LayoutInflater.from($c).inflate($layout , this);
        if($b!=null){
            addObserver($b);
        }
    }
}
