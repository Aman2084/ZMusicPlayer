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
    public String id = null;

    public String name = null;
    /**文件名（不含扩展名）*/
    public String fileName = null;

    public String singer = null;

    public String singerId = null;

    public String album = null;

    public String albumId = null;

    public String path = null;

    public String folderPath = null;

    /**时长（毫秒）*/
    public int duration = 0;
    /**文件大小（字节）*/
    public long size = 0;

    public Song(){
        super(null , null);
    }

//getter and setter

    public void set_path(String $str){
        if($str==null || $str==""){
            return;
        }

        path = $str;

        String reg = "(.*/)|(\\..*)";
        fileName = $str.replaceAll(reg, "");

        Pattern p= Pattern.compile(".*/");
        Matcher m = p.matcher($str);
        if(m.find()){
            folderPath = m.group();
        }
    }

    public String getDisplayName(){
        String s = fileName;
        if(!AppInstance.model.settings.useFileName){
            s = name==null ? s : name;
        }
        return s;
    }

    public String getDisplaySinger(){
        String s = singer==null ? AppUtils.id2String(R.string.global_unknown) : singer;
        return s;
    }

    public String getDisplayAlbum(){
        String s = album==null ? AppUtils.id2String(R.string.global_unknown) : album;
        return s;
    }
}
