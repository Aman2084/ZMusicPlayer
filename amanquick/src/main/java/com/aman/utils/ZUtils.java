package com.aman.utils;

import java.util.ArrayList;

/**
 * AmanQucick 1.0
 * Created on 2017/8/29 21:59
 *
 * @author Aman
 * @Email: 1390792438@qq.com
 *
 * 数据结构和基类有关的工具函数
 */

public class ZUtils {

    public static String join(String[] $a , String $sep){
        String s = "";

        if($a!=null && $a.length>0){
            s = $a[0];
            for (int i = 1; i <$a.length ; i++) {
                s += $sep + $a[i];
            }
        }
        return s;
    }

    public static String join(ArrayList<String> $a , String $sep){
        String s = "";
        if($a!=null){
            int n = $a.size();
            if(n>0){
                s = $a.get(0);
                for (int i = 1; i <n ; i++) {
                    s += $sep + $a.get(i);
                }
            }
        }
        return s;
    }

}
