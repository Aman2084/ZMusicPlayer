package com.aman.utils.math;

/**
 * 极坐标，采用制弧度
 *
 * AmanQuick 1.0
 * Created on 2018/6/3 5:24
 *
 * @author Aman
 * @Email 1390792438@qq.com
 */

public class Polar {

    /**
     * 弧度
     */
    public double radian;
    /**
     * 半径
     * */
    public double radius;

    /**
     * 极坐标构造函数
     * @param $radian       弧度
     * @param $radius       半径
     */
    public Polar(double $radian , double $radius){
        radian = $radian;
        radius = $radius;
    }

    public Polar(){
        radian = 0;
        radius = 1;
    }



//inteface

    /**
     * 坐标系转动（参考角度（0°）参考角度转动）
     * @param $radius       转动的弧度值
     */
    public void systemRotation(double $radius){
        radian +=  $radius;
    }
}
