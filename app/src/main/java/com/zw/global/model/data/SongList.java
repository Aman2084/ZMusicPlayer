package com.zw.global.model.data;

import java.util.ArrayList;

/**
 * ZMusicPlayer 1.0
 * Created on 2017/8/31 23:33
 *
 * @author Aman
 * @Email: 1390792438@qq.com
 */

public class SongList {

    /**收藏*/
    public static final int Type_fav = 0;
    /**藏*/
    public static final int Type_play = 1;
    /**用户自定义歌单*/
    public static final int Type_user = 2;


//歌单数据部分
    public int id;
    public int type;
    public String title;

    public ArrayList<SongListItem> items = new ArrayList<>();

    public int getSongNum(){
        return items.size();
    }


//操作数据部分
    public boolean useBtn = true;


//操作接口
    public boolean deleteSongs(ArrayList<String> $a) {
        boolean b = false;
        for (int i = 0; i <items.size() ; i++) {
            SongListItem s  = items.get(i);
            if($a.contains(s.songId)){
                b = true;
                items.remove(i);
                i--;
            }
        }
        return b;
    }
}