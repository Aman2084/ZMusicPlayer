package com.aman.utils;

import com.aman.utils.math.Cartesian;
import com.aman.utils.math.Polar;

/**
 * 数学工具类
 * AmanQuick 1.0
 * Created on 2018/6/3 5:33
 *
 * @author Aman
 * @Email 1390792438@qq.com
 */

public class MathUtils {


    /**
     * 标准化弧度，获取一个在-PI 到 PI之间的弧度值
     * @param $r    弧度
     * @return      标准化弧值
     */
    public static double commonRadian(double $r){
        while($r>Math.PI){
            $r -= Math.PI*2;
        }
        while ($r<-Math.PI){
            $r += Math.PI*2;
        }

        return $r;
    }

    /**
     * 标准化弧度，获取一个在0 到 2PI之间的弧度值
     * @param $r    弧度
     * @return      标准化弧度值
     */
    public static double commonRadian_2Pi(double $r){
        double n = Math.PI*2;
        while($r>n){
            $r -= n;
        }
        while ($r<0){
            $r += n;
        }
        return $r;
    }



    /**
     * 笛卡尔坐标转极坐标
     * @param $pos      坐标点
     * @param $center   极坐标圆心（与pos位于同一坐标系）
     * @return  极坐标
     */
    public static Polar Cartesian2Polar(Cartesian $pos , Cartesian $center){
        Cartesian c = $pos.clone();
        c.offset(-$center.x , -$center.y);
        return Cartesian2Polar(c);
    }

    /**
     * 笛卡尔坐标转极坐标
     * @param $pos    坐标点
     * @return  极坐标
     */
    public static Polar Cartesian2Polar(Cartesian $pos){
        double a = Math.atan2($pos.y , $pos.x);
        double r = Math.abs($pos.y / Math.sin(a));
        Polar p = new Polar(a , r);
        return p;
    }

    /**
     * 极坐标转笛卡尔坐标
     * @param $p    极坐标
     * @return      笛卡尔坐标
     */
    public static Cartesian Polar2Cartesian(Polar $p){
        return Polar2Cartesian($p.radius , $p.radian);
    }

    /**
     *  极坐标转笛卡尔坐标
     * @param $radian       弧度
     * @param $radius       半径
     * @return      笛卡尔坐标
     */
    public static Cartesian Polar2Cartesian(double $radian , double $radius){
        Cartesian c = new Cartesian();
        c.x = $radius * Math.cos($radian);
        c.y = $radius * Math.sin($radian);
        return c;
    }

    /**
     *  极坐标转笛卡尔坐标
     * @param $radian       弧度
     * @param $radius       半径
     * @param $center   极坐标圆心（与笛卡尔坐标位于同一坐标系）
     * @return      笛卡尔坐标
     */
    public static Cartesian Polar2Cartesian(double $radian , double $radius , Cartesian $center){
        Cartesian c = Polar2Cartesian($radian , $radius);
        c.offset($center);
        return c;
    }
}
