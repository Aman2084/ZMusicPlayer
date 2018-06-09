
package com.zw.global.model.data;

import java.util.ArrayList;

/**
 * ZMusicPlayer 1.0
 * Created on 2017/12/5 20:15
 *
 * @author Aman
 * @Email 1390792438@qq.com
 */

public class SongGroup {

    public String name = null;

    public ArrayList<Song> songs = new ArrayList<>();

    /**从哪首曲子开始播放*/
    public int index = 0;
    /**从哪个时间点开始播放*/
    public int position = 0;

    public SongGroup(){}

    public SongGroup(ArrayList<Song> $a , int $index){
        songs = $a;
        index = $index;
    }
}
