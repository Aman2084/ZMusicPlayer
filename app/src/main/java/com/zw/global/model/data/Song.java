package com.zw.global.model.data;

import com.aman.utils.observer.ZObservable;
import com.zw.R;
import com.zw.global.AppInstance;
import com.zw.utils.AppUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ZMusicPlayer 1.0
 * Created on 2017/8/22 14:57
 *
 * @author Aman
 * @Email: 1390792438@qq.com
 */

public class Song extends ZObservable{

    private String _id = null;

    private String _name = null;
    /**文件名（不含扩展名）*/
    private String _fileName = null;

    private String _singer = null;

    private String _singerId = null;

    private String _album = null;

    private String _albumId = null;

    private String _path = null;

    private String _folderPath = null;

    /**时长（毫秒）*/
    private int _duration = 0;
    /**文件大小（字节）*/
    private long _size = 0;

    public Song(){
        super(null , null);
    }

//getter and setter

    public void set_path(String $str){
        if($str==null || $str==""){
            return;
        }

        _path = $str;

        String reg = "(.*/)|(\\..*)";
        _fileName = $str.replaceAll(reg, "");

        Pattern p= Pattern.compile(".*/");
        Matcher m = p.matcher($str);
        if(m.find()){
            _folderPath = m.group();
        }
    }

    public String getDisplayName(){
        String s = _fileName;
        if(!AppInstance.model.settings.useFileName){
            s = _name==null ? s : _name;
        }
        return s;
    }

    public String getDisplaySinger(){
        String s = _singer==null ? AppUtils.id2String(R.string.global_unknown) : _singer;
        return s;
    }

    public String getDisplayAlbum(){
        String s = _album==null ? AppUtils.id2String(R.string.global_unknown) : _album;
        return s;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    @Override
    public String getName() {
        return _name;
    }

    @Override
    public void setName(String name) {
        this._name = name;
    }

    public String getFileName() {
        return _fileName;
    }

    public void setFileName(String fileName) {
        this._fileName = fileName;
    }

    public String getSinger() {
        return _singer;
    }

    public void setSinger(String singer) {
        this._singer = singer;
    }

    public String getSingerId() {
        return _singerId;
    }

    public void setSingerId(String singerId) {
        this._singerId = singerId;
    }

    public String getAlbum() {
        return _album;
    }

    public void setAlbum(String album) {
        this._album = album;
    }

    public String getAlbumId() {
        return _albumId;
    }

    public void setAlbumId(String albumId) {
        this._albumId = albumId;
    }

    public String getPath() {
        return _path;
    }

    public void setPath(String path) {
        this._path = path;
    }

    public String getFolderPath() {
        return _folderPath;
    }

    public void setFolderPath(String folderPath) {
        this._folderPath = folderPath;
    }

    public int getDuration() {
        return _duration;
    }

    public void setDuration(int duration) {
        this._duration = duration;
    }

    public long getSize() {
        return _size;
    }

    public void setSize(long size) {
        this._size = size;
    }

}
