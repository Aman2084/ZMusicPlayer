package com.aman.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Rect;
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

    public static View[] setOnClickByIds(ViewGroup $g , int[] $a , View.OnClickListener $l){
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

    public static AlertDialog alert(Context $c , String $title , String $msg, String $ok,
                                    DialogInterface.OnClickListener $onOK){
        AlertDialog.Builder builder = new AlertDialog.Builder($c);
        builder.setTitle($title);
        builder.setMessage($msg);
        builder.setNegativeButton($ok , $onOK);
        AlertDialog dialog = builder.create();
        dialog.show();
        return dialog;
    };


    public static AlertDialog alert(Context $c ,
            String $title , String $msg, String $ok, String $cancel,
            DialogInterface.OnClickListener $onOK , DialogInterface.OnClickListener $onCancle){

        AlertDialog.Builder builder = new AlertDialog.Builder($c);
        builder.setTitle($title);
        builder.setMessage($msg);
        builder.setNegativeButton($cancel, $onCancle);
        builder.setPositiveButton($ok , $onOK);
        AlertDialog dialog = builder.create();
        dialog.show();
        return dialog;
    };

    public static void setPosAndSize(View $v , Rect $r){
        $v.setX($r.left);
        $v.setY($r.top);

        ViewGroup.LayoutParams p = $v.getLayoutParams();
        p.width = $r.width();
        p.height = $r.height();
        $v.setLayoutParams(p);
    }


    public static int getRandomColor(){
        int n = (int) (Math.random()*16777216);
        return n;
    }
}
