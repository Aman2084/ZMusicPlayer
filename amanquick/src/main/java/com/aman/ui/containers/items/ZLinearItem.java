package com.aman.ui.containers.items;

import android.content.Context;
import android.view.LayoutInflater;

import com.aman.ui.containers.ZLinearLayout;

/**
 * AmanQuick 1.0
 * Created on 2017/8/31 8:33
 *
 * @author Aman
 * @Email: 1390792438@qq.com
 */

public class ZLinearItem extends ZLinearLayout {


    public ZLinearItem(Context $c , int $layout){
        super($c , null);

        LayoutInflater.from($c).inflate($layout , this);
    }

}
