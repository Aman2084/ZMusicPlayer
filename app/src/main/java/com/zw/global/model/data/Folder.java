package com.zw.global.model.data;

/**
 * AmanQuick 1.0
 * Created on 2017/8/31 11:45
 *
 * @author Aman
 * @Email: 1390792438@qq.com
 */

public class Folder {

    public String path = null;
    public String name = null;
    public int numSong = 0;

    public Folder(String $path , String $name , int $num){
        path = $path;
        name = $name;
        numSong = $num;
    }
}
