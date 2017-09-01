package com.aman.utils;

import android.view.View;
import android.view.ViewGroup;

/**
 * AmanQuick 1.0
 *
 *
 * author:Aman
 * Email: 1390792438@qq.com
 */
public class UIUtils {


    public static View[] findViewsByIds(ViewGroup $g , int[] $a){
        if($a==null || $a.length==0){
            return new View[]{};
        }

        View[] a = new View[$a.length];
        for (int i = 0; i < $a.length; i++) {
            a[i] = $g.findViewById($a[i]);
        }
        return a;
    }


    public static View[] setOnclickByIds(ViewGroup $g , int[] $a , View.OnClickListener $l){
        if($a==null || $a.length==0){
            return new View[]{};
        }

        View[] a = new View[$a.length];
        for (int i = 0; i < $a.length; i++) {
            a[i] = $g.findViewById($a[i]);
            a[i].setOnClickListener($l);
        }
        return a;
    }

}
