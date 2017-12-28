package com.zw.global.model.data;

import java.util.ArrayList;

/**
 * ZMusicPlayer 1.0
 * Created on 2017/8/31 23:38
 *
 * @author Aman
 * @Email: 1390792438@qq.com
 */

public class PlayList {
    public ArrayList<Song> list = null;
    public int index;

    public PlayList(ArrayList<Song> $a , int $index){
        list = $a;
        index = $index;
    }

}
