package com.aman.utils.math;

/**
 * 笛卡尔坐标
 *
 * AmanQuick 1.0
 * Created on 2018/6/3 6:10
 *
 * @author Aman
 * @Email 1390792438@qq.com
 */

public class Cartesian {

    public double x;
    public double y;

    /**
     * 极坐标构造函数
     * @param $x       x坐标
     * @param $y       y坐标
     */
    public Cartesian(double $x , double $y){
        x = $x;
        y = $y;
    }

    public Cartesian(){
        x = 0;
        y = 0;
    }

    public boolean equals(Cartesian $c) {
        boolean b = $c.x==x && $c.y==y;
        return b;
    }

    public void offset(Cartesian $c){
        offset($c.x , $c.y);
    }

    public void offset(double $x , double $y){
        x += $x;
        y += $y;
    }

    public Cartesian clone(){
        Cartesian c = new Cartesian(x , y);
        return c;
    }
}
