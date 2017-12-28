package com.zw.global.model.data;

/**
 * AmanQuick 1.0
 * Created on 2017/8/31 11:42
 *
 * @author Aman
 * @Email: 1390792438@qq.com
 * 专辑信息
 */

public class Album {

    public String id = null;
    public String name = null;
    public int numSong = 0;

    public Album(String $id , String $name , int $num){
        id = $id;
        name = $name;
        numSong = $num;
    }
}
