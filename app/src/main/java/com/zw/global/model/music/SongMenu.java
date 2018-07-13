package com.zw.global.model.music;

import com.zw.global.model.data.SongList;
import com.zw.global.model.data.SongListItem;

import java.util.ArrayList;

/**
 * 歌单模块
 * ZMusicPlayer 1.0
 * Created on 2018/5/2 11:46
 *
 * @author Aman
 * @Email 1390792438@qq.com
 */

public class SongMenu {

    /**单曲*/
    public static final String Single = "single";
    /**顺序*/
    public static final String Order = "order";
    /**乱序（每首歌播放一次）*/
    public static final String Disorder = "disorder";

    /**
     * 是否输出过：setData后是否被getNext过
     * private boolean _nexted = false;
     */
    private String _mode = Order;
    private boolean _isLoop = false;
    private int _index = -1;


    public SongListItem lastSong = null;
    private SongList _list = null;
    private ArrayList<SongListItem> _arr = new ArrayList<>();


    public void setData(SongList $l){
        setData($l , 0);
    }

    public void setData(SongList $l , int $index){
        _list = $l;
        _index = $index;
        lastSong = null;
        refuse();
    }

    public void setIndex(int $i){
        _index = $i;
    }

    /**
     * 强行下一曲（不考虑循环和模式）
     */
    public SongListItem next_super(){
        SongListItem item = next_order();
        return item;
    }
    public SongListItem prev_super(){
        SongListItem s = null;
        _index --;
        if(_index<0){
            _index = _arr.size() -1;
        }
        s = _arr.get(_index);
        return s;
    }



    public SongListItem next(){
        SongListItem item = null;

        if(_list==null || _list.items==null || _list.items.size()==0){
            return item;
        }

        lastSong = getCurrectSong();

        switch(_mode){
            case Single:
                item = next_single();
                break;
            case Order:
                item = next_order();
                break;
            case Disorder:
                item = next_disorder();
                break;
        }
        return item;
    }

    public boolean hasNext(){
        boolean b = _arr!=null &&_arr.size()>0;
        if(!b){
            return false;
        }else if(_isLoop){
            return true;
        }

        //非循环过程
        switch(_mode) {
            case Single:
                b = false;
                break;
            case Order:
            case Disorder:
                b = _index!=(_arr.size()-1);
                break;
        }

        return b;
    };

    private SongListItem next_disorder() {
        SongListItem item = null;
        if(_isLoop){
            _index = (int)Math.floor(Math.random()*_arr.size());
        }else{
            _index ++;
        }
        if(_index<_arr.size()){
            item = _arr.get(_index);
        }
        return item;
    }

    private SongListItem next_order() {
        _index ++;
        SongListItem s = null;

        if(_isLoop && _index>=_arr.size()){
            _index = 0;
        }
        if(_index<_arr.size()){
            s = _arr.get(_index);
        }
        return s;
    }
    private SongListItem next_single(){
        SongListItem s = getCurrectSong();
        if(!_isLoop && s==lastSong){
            s = null;
        }
        return s;
    }

    private void refuse(){
        _arr.clear();
        if(_list==null || _list.items==null || _list.items.size()==0){
            return;
        }
        _arr.addAll(_list.items);
        SongListItem s = getCurrectSong();
        _index = -1;

        //不循环随机播放时，首次时进行随机排列
        if(_mode.equals(Disorder) && !_isLoop){
            _index = 0;
            int l = _arr.size();
            for (int i = 0; i <l-1 ; i++) {
                int n = l-1-i;
                if(n>0){
                    double k = i + Math.random() * n;
                    k = Math.ceil(k);
                    int j = (int) k;
                    SongListItem s1 = _arr.get(i);
                    SongListItem s2 = _arr.get(j);
                    _arr.set(i , s2);
                    _arr.set(j , s1);
                }
            }
        }

        //顺序/单曲播放时，对相关变量进行调整
        if(!_mode.equals(Disorder)){
            _index = _arr.indexOf(s);
        }
    }

    public SongListItem getCurrectSong(){
        SongListItem s = null;
        if(_index>-1 && _arr.size()>_index){
            s = _arr.get(_index);
        }
        return s;
    }

    public int getCurrectIndex(){
        if (isEmpty()) {
            return -1;
        }

        int i = _index;
        if(_mode==Disorder && !_isLoop){
            SongListItem item = getCurrectSong();
            i = _list.items.indexOf(item);
        }
        return i;
    }


    public void setIsLoop(boolean $b){
        if($b==_isLoop){
            return;
        }
        _isLoop = $b;
        refuse();
    }

    public boolean getIsLoop(){
        return _isLoop;
    }

    public void setMode(String $m){
        if($m.equals(_mode)){
            return;
        }
        _mode = $m;
        refuse();
    }

    public String getMode(){
        return _mode;
    }

    public static String getNextModel(String $model) {
        String s = null;
        switch ($model){
            case Order:
                s = Single;
                break;
            case Single:
                s = Disorder;
                break;
            case Disorder:
                s = Order;
                break;
        }
        return s;
    }

    public SongListItem jump2RelationId(int $relationId) {
        if(_arr==null){
            return null;
        }

        SongListItem s = null;
        int index = -1;
        for (int i = 0; i <_arr.size() ; i++) {
            SongListItem o = _arr.get(i);
            if(o.relationId==$relationId){
                index = i;
                s = o;
                break;
            }
        }

        //没找到返回null
        if(index==-1){
            return s;
        }

        //找到进行Jump处理
        if(!_isLoop && _mode.equals(Disorder)){     //随机播放一次时改变歌曲顺序
            _arr.set(index , _arr.get(_index));
            _arr.set(_index , s);
        }else{              //其他情况直接跳转到此index
            _index = index;
        }
        return s;
    }

    public int getIndexByPath(String $p){
        int index = -1;
        for (int i = 0; i <_arr.size() ; i++) {
            SongListItem o = _arr.get(i);
            if(o.song.getPath().equals($p)){
                _index = i;
                break;
            }
        }
        return index;
    }

    public boolean isEmpty(){
        boolean b = _arr==null || _arr.size()==0;
        return b;
    }

    public void clear(){
        _arr.clear();
        _list = null;
        lastSong = null;
    }
}
