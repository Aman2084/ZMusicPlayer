package com.zw.global.model.my;

import com.zw.global.AppInstance;
import com.zw.global.IntentNotice;
import com.zw.global.model.data.Song;
import com.zw.global.model.data.SongList;
import com.zw.global.model.data.SongListItem;
import com.zw.utils.ZProgress;
import com.zw.utils.sql.RelationDBSQL;
import com.zw.utils.sql.SongListDBSQL;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * ZMusicPlayer 1.0
 * Created on 2017/12/5 20:54
 *
 * @author Aman
 * @Email 1390792438@qq.com
 */

public class SongListModel extends ZProgress{

    private ArrayList<SongList> _allSongLists = new ArrayList<>();

    public SongList list_fav = null;
    public SongList list_play = null;

    public SongListModel() {
        super(null, null);
    }

    public void init(ArrayList<Song> $a){
        SongListDBSQL sql_list = AppInstance.songListSQL;
        sql_list.openReadLink();
        _allSongLists = sql_list.queryAll();
        sql_list.close();

        //首次启动创建默认列表
        if(_allSongLists.size()==0){
            int type = SongList.Type_fav;
            list_fav = creatSongList(String.valueOf(type) , type);
            type = SongList.Type_play;
            list_play = creatSongList(String.valueOf(type) , type);
            AppInstance.relationDBSQL.openWriteLink();
            return;
        }

        //为歌单添加歌曲列表
        RelationDBSQL sql_relation = AppInstance.relationDBSQL;
        sql_relation.openReadLink();
        HashMap<Integer , ArrayList<SongListItem>> o1 = sql_relation.queryAll();
        sql_relation.close();

        for (int i = 0; i <_allSongLists.size() ; i++) {
            SongList l = _allSongLists.get(i);
            ArrayList<SongListItem> a = o1.get(l.id);
            if(a!=null && a.size()>0){
                findSongsForList(a , $a);
                l.items = a;
            }

            //对收藏和正在播放的列表进行特殊处理
            boolean isSpecial = false;
            switch(l.type){
                case SongList.Type_fav:
                    list_fav = l;
                    isSpecial = true;
                    break;
                case SongList.Type_play:
                    list_play = l;
                    isSpecial = true;
                    break;
            }
            if(isSpecial){
                _allSongLists.remove(i);
                i--;
            }
        }

        for (SongListItem s:list_fav.items) {
            s.song.isFavorite = true;
        }
    }


//增

    public SongList creatSongList(String $title){
        return creatSongList($title , SongList.Type_user);
    }

    public SongList creatSongList(String $title , Integer $type){
        if($title==null || $title==""){
            return null;
        }

        SongList l = new SongList();
        l.title = $title;
        l.type = $type;
        SongListDBSQL sql = AppInstance.songListSQL;
        sql.openWriteLink();
        l.id = (int)sql.insert(l);
        sql.close();
        if(l.type!=SongList.Type_play && l.type!=SongList.Type_fav){
            _allSongLists.add(l);
        }
        sendIntent(IntentNotice.SongList_Creat , l);
        return l;
    }

//删
    public void deleteSongList(int $id) {
        SongList list = null;
        for (SongList l: _allSongLists) {
            if(l.id==$id){
                list = l;
                _allSongLists.remove(l);
                break;
            }
        }
        if(list==null){
            return;
        }

        SongListDBSQL sql_list = AppInstance.songListSQL;
        sql_list.openWriteLink();
        sql_list.delete($id);
        sql_list.close();
        RelationDBSQL sql_relation = AppInstance.relationDBSQL;
        sql_relation.openWriteLink();
        sql_relation.delete(list);
        sql_relation.close();
        sendIntent(IntentNotice.SongList_Delete , list);
    }


//改
    public ArrayList<SongListItem> addSong2List(int $id, ArrayList<Song> $a) {
        SongList l = getSongListById($id);
        if(l==null){
            return null;
        }
        return addSong2List(l , $a , true);
    }

    public ArrayList<SongListItem> addSong2List(SongList $l, ArrayList<Song> $a , boolean $sendIntent) {
        RelationDBSQL sql = AppInstance.relationDBSQL;
        sql.openWriteLink();
        ArrayList<SongListItem> arr = sql.insert($l.id , $a);
        sql.close();
        findSongsForList(arr , $a);
        $l.items.addAll(arr);
        if($sendIntent){
            sendUpdateIntent($l);
        }
        return arr;
    }


    /**
     * 更新歌单中的歌曲
     * @param $id   歌单id
     * @param $a    歌曲列表
     */
    public void updateSongList_song(int $id , ArrayList<Song> $a){
        SongList l = null;
        if($id==list_play.id){
            l = list_play;
        }else if($id==list_fav.id){
            l = list_fav;
        }else{
            l = getSongListById($id);
        }
        updateSongList_song(l , $a);
    }

    /**
     * 更新歌单中的歌曲
     * @param $l     歌单
     * @param $a    歌曲列表
     */
    private void updateSongList_song(SongList $l , ArrayList<Song> $a){
        if($l==null){
            return;
        }

        RelationDBSQL sql = AppInstance.relationDBSQL;
        sql.openWriteLink();
        ArrayList<SongListItem> a = sql.update($l , $a);
        sql.close();

        for (int i = 0; i <a.size() ; i++) {
            SongListItem item = a.get(i);
            for (Song s:$a) {
                if(item.songId.equals(s.get_id())){
                    item.song = s;
                    break;
                }
            }
        }
        signFavorite(a);

        $l.items = a;
    }



    public void updateSongList_title(int $id , String $title){
        SongList l = getSongListById($id);
        if(l==null){
            return;
        }
        l.title = $title;
        SongListDBSQL sql = AppInstance.songListSQL;
        sql.openWriteLink();
        sql.update(l);
        sendUpdateIntent(l);
        sql.close();
    }

    public void deleteSongsFromeList(int $id , ArrayList<SongListItem> $items){
        SongList l = getSongListById($id);
        if(l==null){
            return;
        }
        deleteSongsFromeList(l , $items , true);
    }

    public void deleteSongsFromeList(SongList $l , ArrayList<SongListItem> $items , boolean $sendIntent){
        ArrayList<Integer> relations = new ArrayList<>();
        for (int i = 0; i <$items.size() ; i++) {
            relations.add($items.get(i).relationId);
        }

        boolean change = false;
        for (int i = 0; i <$l.items.size() ; i++) {
            SongListItem item = $l.items.get(i);
            int n = relations.indexOf(item.relationId);
            if(n>-1){
                $l.items.remove(i);
                i--;
                change = true;
            }
        }

        RelationDBSQL sql = AppInstance.relationDBSQL;
        sql.openWriteLink();
        sql.deleteRelations(relations);
        sql.close();

        if(change && $sendIntent){
            sendUpdateIntent($l);
        }
    }

    /**
     * <br/><br/>
     * 根据SongId把相关的Song和SongList中的相关数据从数据库和本地数据中删除
     *
     * @param  $a ArrayList<String>  要删除的SongId
     *
     * @return    ArrayList<int> 有改动的歌单Id
     *
     * */
    public ArrayList<SongList> deleteSongs(ArrayList<String> $a){
        ArrayList<SongList> a = new ArrayList<>();

        for (SongList l:_allSongLists) {
            boolean b = l.deleteSongs($a);
            if(b){
                a.add(l);
            }
        }

        RelationDBSQL sql = AppInstance.relationDBSQL;
        sql.openWriteLink();
        sql.deleteBySongIds($a);
        sql.close();

        if(a.size()>0){
            sendUpdateIntent(a);
        }
        if(list_fav.deleteSongs($a)){
            sendIntent(IntentNotice.SongList_UpDataFavorite , null);
        }
        if(list_play.deleteSongs($a)){
            sendIntent(IntentNotice.SongList_UpDataPlayList , null);
        }
        return a;
    }

    /**
     * 充新导入歌曲后校验所有歌单
     * @param $a
     */
    public void importSongs(ArrayList<Song> $a) {
        ArrayList<SongList> arr = getAll();
        for (int i = 0; i <arr.size() ; i++) {
            SongList l = arr.get(i);
            ArrayList<Song> a = compareSongs(l , $a);
            updateSongList_song(l , a);
        }
    }

//查
    private void findSongsForList(ArrayList<SongListItem> $a , ArrayList<Song> $s) {
        HashMap<String , Song> o = getAllSongsMap($s);
        for (int i = 0; i <$a.size() ; i++) {
            SongListItem s = $a.get(i);
            s.song = o.get(s.songId);
        }
    }

    private HashMap<String , Song> getAllSongsMap(ArrayList<Song> $a){
        HashMap<String , Song> o  = new HashMap<>();
        for (Song s:$a) {
            o.put(s.get_id(), s);
        }
        return o;
    }

    public SongList getSongListById(int $id){
        SongList list = null;
        for (SongList l: _allSongLists) {
            if(l.id==$id){
                list = l;
                break;
            }
        }
        return list;
    }

    public ArrayList<SongList> get_allSongList(){
        return _allSongLists;
    }

//tools
    /**
     * 查询并标记一组SongListItem的收藏（isFavorite）属性
     *
     * @param $a ArrayList<SongListItem>  待查询的SongListItem
     *
     * */
    public void signFavorite(ArrayList<SongListItem> $a){
        ArrayList<String> ids  = new ArrayList<>();
        for (SongListItem s: list_fav.items) {
            ids.add(s.songId);
        }

        for (SongListItem s:$a) {
            s.song.isFavorite = ids.contains(s.songId);
        }
    }

    /**
     * 设置单曲收藏属性
     * */
    public void setFavorite(SongListItem $o){
        boolean b = isFavorite($o.songId);
        if(b==$o.song.isFavorite){
            return;
        }
        if(!$o.song.isFavorite){
            ArrayList<SongListItem> a = new ArrayList<>();
            $o = list_fav.getItemBySongId($o.songId);
            a.add($o);
            deleteSongsFromeList(list_fav , a , false);
        }else{
            ArrayList<Song> a = new ArrayList<>();
            a.add($o.song);
            ArrayList<SongListItem> arr = addSong2List(list_fav , a , false);
            for (SongListItem item:arr){
                item.song.isFavorite = true;
            }
        }
        sendIntent(IntentNotice.SongList_UpDataFavorite , null);
    }

    private void sendUpdateIntent(SongList $l){
        ArrayList<SongList> a = new ArrayList<>();
        a.add($l);
        sendUpdateIntent(a);
    }

    private void sendUpdateIntent(ArrayList<SongList> $a){
        sendIntent(IntentNotice.SongList_UpData , $a);
    }

    private ArrayList<SongList> getAll(){
        ArrayList<SongList> a = new ArrayList<>(_allSongLists);
        a.add(0 , list_play);
        a.add(0 , list_fav);
        return a;
    }

    public boolean isFavorite(String $songId){
        boolean b = false;
        for (SongListItem s: list_fav.items) {
            if(s.songId==$songId){
                b = true;
                break;
            }
        }
        return b;
    }

    /**
     * 计算歌曲列表
     * @param $l    目标SongList
     * @param $a    目标Song列表
     * @return      所需歌曲列表
     */
    private ArrayList<Song> compareSongs(SongList $l , ArrayList<Song> $a){
        ArrayList<Song> a = new ArrayList<>();
        for (SongListItem item:$l.items) {
            String path = item.song.getPath();
            for (int i=0;i<$a.size();i++){
                Song s = $a.get(i);
                if(path.equals(s.getPath())){
                    a.add(s);
                    break;
                }
            }
        }
        return a;
    }
}
