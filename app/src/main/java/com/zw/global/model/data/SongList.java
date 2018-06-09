package com.zw.global.model.data;

import java.util.ArrayList;

/**
 * ZMusicPlayer 1.0
 * Created on 2017/8/31 23:33
 *
 * @author Aman
 * @Email 1390792438@qq.com
 */

public class SongList {

    /**收藏*/
    public static final int Type_fav = 0;
    /**当前播放列表*/
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


//getter and setter
    public SongListItem getItemByIndex(int $index){
        return items.get($index);
    }

    public Song getSongByIndex(int $index){
        Song s = null;
        SongListItem item = getItemByIndex($index);
        if(item!=null){
            s = item.song;
        }
        return s;
    }

    public SongListItem getItemByRelationId(int $relacation){
        SongListItem item = null;
        for (SongListItem o:items) {
            if(o.relationId==$relacation){
                item = o;
                break;
            }
        }
        return item;
    }

    public SongListItem getPlayingItem(){
        SongListItem item = null;
        for (SongListItem o:items) {
            if(!o.stause.equals(SongListItem.Stop)){
                item = o;
                break;
            }
        }
        return item;
    }

    public void setPlayByRelation(int $relationId){
        SongListItem o = getPlayingItem();
        if(o!=null){
            o.stause = SongListItem.Stop;
        }
        o = getItemByRelationId($relationId);
        if(o!=null){
            o.stause = SongListItem.Play;
        }
    }

    public void setPauseByRelation(int $relationId) {
        SongListItem o = getPlayingItem();
        if(o!=null){
            o.stause = SongListItem.Stop;
        }
        o = getItemByRelationId($relationId);
        if(o!=null){
            o.stause = SongListItem.Pause;
        }
    }

    public void setStopByRelation(int $relationId) {
        SongListItem o = getItemByRelationId($relationId);
        if(o!=null){
            o.stause = SongListItem.Stop;
        }
    }

    public void setPauseItemByIndex(int $index){
        SongListItem o = getPlayingItem();
        if(o!=null){
            o.stause = SongListItem.Stop;
        }
        o = items.get($index);
        if(o!=null){
            o.stause = SongListItem.Pause;
        }
    }

}
