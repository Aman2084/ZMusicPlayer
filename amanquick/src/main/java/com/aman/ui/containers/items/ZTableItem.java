package com.aman.ui.containers.items;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;

import com.aman.ui.ZItemListener;
import com.aman.ui.containers.ZTableLayout;

/**
 * AmanQuick 1.0
 * Created on 2017/8/31 8:35
 *
 * @author Aman
 * @Email: 1390792438@qq.com
 */

public class ZTableItem extends ZTableLayout {

    protected ZItemListener itemListener = null;


    public ZTableItem(Context $c , int $layout , @Nullable ZItemListener $l){
        super($c , null);
        LayoutInflater.from($c).inflate($layout , this);

        itemListener = $l;
    }

    public void setItemListener(ZItemListener $l){
        itemListener = $l;
    }
}
