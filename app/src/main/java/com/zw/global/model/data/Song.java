package com.zw.global.model.data;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;

import com.zw.R;
import com.zw.global.AppInstance;
import com.zw.global.model.SettingsModel;
import com.zw.utils.AppUtils;

import java.lang.ref.SoftReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ZMusicPlayer 1.0
 * Created on 2017/8/22 14:57
 *
 * @author Aman
 * @Email 1390792438@qq.com
 */

public class Song {

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

    private boolean _hasBitmap = true;
    private SoftReference<Bitmap> _bmp = null;

    /**是否为收藏歌曲*/
    public boolean isFavorite = false;

    public Song(){
//        super(null , null);
    }


//getter and setter

    public void set_path(String $str){
        if($str==null || $str.equals("")){
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
        SettingsModel m = AppInstance.model.settings;
        if(!m.useFileName){
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

//    @Override
    public String getName() {
        return _name;
    }

//    @Override
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

    public Bitmap getBmp(Context $c){
        if(!_hasBitmap || _path==null){
            return null;
        }
        Bitmap b = null;

        if(_bmp !=null){
            b = _bmp.get();
            if(b !=null){
                return b;
            }
        }


        Uri selectedAudio = Uri.parse(_path);
        MediaMetadataRetriever myRetriever = new MediaMetadataRetriever();
        myRetriever.setDataSource($c , selectedAudio);

        byte[] artwork;
        artwork = myRetriever.getEmbeddedPicture();
        if (artwork != null) {
            b = BitmapFactory.decodeByteArray(artwork, 0, artwork.length);
        }
        if(b==null){
            _hasBitmap = false;
        }else{
            _bmp = new SoftReference(b);
        }
        return b;
    }
}
