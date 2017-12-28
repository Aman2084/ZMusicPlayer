package com.zw.my.ui;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.aman.ui.containers.ZRelativeLayout;
import com.aman.ui.containers.items.ZRelativeItem;
import com.aman.ui.containers.subPage.AnimationTypes;
import com.aman.ui.containers.subPage.ISubpage;
import com.aman.utils.UIUtils;
import com.aman.utils.observer.ZNotifcationNames;
import com.aman.utils.observer.ZNotification;
import com.aman.utils.observer.ZObserver;
import com.zw.R;
import com.zw.global.AppInstance;
import com.zw.global.AppNotificationNames;
import com.zw.global.model.data.Album;
import com.zw.global.model.data.Folder;
import com.zw.global.model.data.PlayList;
import com.zw.global.model.data.Singer;
import com.zw.global.model.data.Song;
import com.zw.global.model.data.SongGroup;
import com.zw.global.model.my.SongModel;
import com.zw.my.ui.item.MySongItem;
import com.zw.ui.containers.SubPageTitle;
import com.zw.utils.AppUtils;

import java.util.ArrayList;
import java.util.Observer;

/**
 * ZMusicPlayer 1.0
 * Created on 2017/8/20 10:35
 *
 * @author Aman
 * @Email: 1390792438@qq.com
 * 全部音乐
 */
public class MyAllMusic extends ZRelativeLayout implements ISubpage{

    private View[] _btns;

    private ListView _list;

    private Adapter_Song _adapter_song;
    private Adapter_Singer _adapter_singer;
    private Adapter_Album _adapter_album;
    private Adapter_Folder _adapter_folder;

    public MyAllMusic(Context $c, AttributeSet $a) {
        super($c, $a);
        LayoutInflater.from($c).inflate(R.layout.my_allmusic , this);

        _adapter_song = new Adapter_Song(null , onItem);
        _adapter_singer = new Adapter_Singer(null , onItem);
        _adapter_album = new Adapter_Album(null , onItem);
        _adapter_folder = new Adapter_Folder(null , onItem);

        int[] a = {R.id.btn_music , R.id.btn_singer , R.id.btn_album , R.id.btn_folder};
        _btns = UIUtils.setOnClickByIds(this , a , onRadioButton);
        _list = (ListView)findViewById(R.id.list);

        SubPageTitle t = (SubPageTitle)findViewById(R.id.title);
        t.set_observable(this);

        TextView txt = (TextView) findViewById(R.id.txt_scan);
        txt.setOnClickListener(onClick);
        txt = (TextView) findViewById(R.id.txt_manage);
        txt.setOnClickListener(onClick);

        refause();
        _list.setAdapter(_adapter_song);
    }

//interface
    public void refause() {
        SongModel m = AppInstance.model.song.song;
        _adapter_song.setData(m.get_allSongs());
        _adapter_album.setData(m.get_allAlbums());
        _adapter_singer.setData(m.get_allSingers());
        _adapter_folder.setData(m.get_allFolders());
    }

//Listener

    private  OnClickListener onClick = new OnClickListener() {
        @Override
        public void onClick(View $v) {
            switch ($v.getId()){
                case R.id.txt_scan:
                    sendNotification(AppNotificationNames.Scan);
                    break;
                case R.id.txt_manage:
                    sendNotification(AppNotificationNames.Manage);
                    break;
            }
        }
    };

    private OnClickListener onRadioButton = new OnClickListener() {
        @Override
        public void onClick(View $v) {
            for (View v :_btns) {
                int c = v==$v ? Color.RED : Color.BLACK;
                RadioButton b = (RadioButton)v;
                b.setTextColor(c);
            }
            switch ($v.getId()){
                case R.id.btn_music:
                    _list.setAdapter(_adapter_song);
                    break;
                case R.id.btn_singer:
                    _list.setAdapter(_adapter_singer);
                    break;
                case R.id.btn_album:
                    _list.setAdapter(_adapter_album);
                    break;
                case R.id.btn_folder:
                    _list.setAdapter(_adapter_folder);
                    break;
            }
        }
    };

    private ZObserver onItem = new ZObserver() {
        @Override
        public void onNotification(ZNotification $n) {
            SongModel m = AppInstance.model.song.song;

            Object o = $n.data;
            SongGroup l = new SongGroup();

            if(o instanceof Song){
                ArrayList<Song> a = m.get_allSongs();
                int i = a.indexOf(o);
                PlayList p = new PlayList(a , i);
                sendNotification(AppNotificationNames.PlaySongList , p);
            }else if(o instanceof Singer){
                l.name = ((Singer) o).name;
                l.songs = m.getSongsBySinger(((Singer) o).id);
            }else if(o instanceof Album){
                Album album = (Album) o;
                l.name = album.name;
                l.songs = m.getSongsByAlumb(album.id);
            }else if(o instanceof Folder){
                l.name =((Folder) o).name;
                l.songs = m.getSongsByFolder(((Folder) o).path);
            }
            if(l.name !=null){
                sendNotification(AppNotificationNames.ShowSongGroup, l);
            }
        }
    };


//getter and setter
    public AnimationTypes get_showMovieType(){
        return AnimationTypes.Right;
    }
    public AnimationTypes get_hideMovieType(){
        return AnimationTypes.Right;
    }
    public void set_showMovieType(AnimationTypes $type){}
    public void set_hideMovieType(AnimationTypes $type){}


//children class
private class Adapter_Song extends BaseAdapter{

    private ArrayList<Song> _data;
    private ZObserver _istener;

    public Adapter_Song(ArrayList<Song> $a , ZObserver $l) {
        super();
        _data = $a;
        _istener = $l;
    }

    @Override
    public int getCount() {
        return _data.size();
    }

    @Override
    public Object getItem(int $position) {
        return _data.get($position);
    }

    @Override
    public long getItemId(int $position) {
        return $position;
    }

    @Override
    public View getView(int $position, View $convertView, ViewGroup $parent) {
        MySongItem m = (MySongItem)$convertView;
        if(m==null){
            m = new MySongItem(AppInstance.mainActivity, _istener);
        }
        m.setData(_data.get($position));
        return m;
    }

    public void setData(ArrayList<Song> $a){
        _data = $a;
        notifyDataSetChanged();
    }
}

private class Adapter_Singer extends BaseAdapter{

    private ArrayList<Singer> _data;
    private ZObserver _istener;

    public Adapter_Singer(ArrayList<Singer> $a , ZObserver $l) {
        super();
        _data = $a;
        _istener = $l;
    }

    @Override
    public int getCount() {
        return _data.size();
    }

    @Override
    public Object getItem(int $position) {
        return _data.get($position);
    }

    @Override
    public long getItemId(int $position) {
        return $position;
    }

    @Override
    public View getView(int $position, View $convertView, ViewGroup $parent) {
        Item_Singer m = (Item_Singer)$convertView;
        if(m==null){
            m = new Item_Singer(AppInstance.mainActivity, _istener);
        }
        m.setData(_data.get($position));
        return m;
    }

    public void setData(ArrayList<Singer> $a){
        _data = $a;
        notifyDataSetChanged();
    }
}

private class Adapter_Album extends BaseAdapter{

    private ArrayList<Album> _data;
    private ZObserver _istener;

    public Adapter_Album(ArrayList<Album> $a , ZObserver $l) {
        super();
        _data = $a;
        _istener = $l;
    }

    @Override
    public int getCount() {
        return _data.size();
    }

    @Override
    public Object getItem(int $position) {
        return _data.get($position);
    }

    @Override
    public long getItemId(int $position) {
        return $position;
    }

    @Override
    public View getView(int $position, View $convertView, ViewGroup $parent) {
        Item_Album m = (Item_Album)$convertView;
        if(m==null){
            m = new Item_Album(AppInstance.mainActivity, _istener);
        }
        m.setData(_data.get($position));
        return m;
    }

    public void setData(ArrayList<Album> $a){
        _data = $a;
        notifyDataSetChanged();
    }
}

private class Adapter_Folder extends BaseAdapter{

    private ArrayList<Folder> _data;
    private ZObserver _istener;

    public Adapter_Folder(ArrayList<Folder> $a , ZObserver $l) {
        super();
        _data = $a;
        _istener = $l;
    }

    @Override
    public int getCount() {
        return _data.size();
    }

    @Override
    public Object getItem(int $position) {
        return _data.get($position);
    }

    @Override
    public long getItemId(int $position) {
        return $position;
    }

    @Override
    public View getView(int $position, View $convertView, ViewGroup $parent) {
        Item_Folder m = (Item_Folder)$convertView;
        if(m==null){
            m = new Item_Folder(AppInstance.mainActivity, _istener);
        }
        m.setData(_data.get($position));
        return m;
    }

    public void setData(ArrayList<Folder> $a){
        _data = $a;
        notifyDataSetChanged();
    }
}
/**
private class Item_Song extends ZRelativeItem{
    private TextView _txt_name;
    private TextView _txt_singer;

    public Item_Song(Context $c, @Nullable ZNotificationListener $l) {
        super($c, R.layout.my_song_item, $l);
        _txt_name = (TextView)findViewById(R._id.txt_name);
        _txt_singer = (TextView)findViewById(R._id.txt_singer);
        setOnClickListener(new OnClickListener() {
            public void onClick(View $v) {
                itemListener.sendNotification(ZNotifcationNames.Click , data);
            }
        });
    }

    @Override
    public void setData(Object $o) {
        super.setData($o);
        if(data!=null){
            Song s  = (Song)$o;
            _txt_name.setText(s.getDisplayName());
            _txt_singer.setText(s.getDisplaySinger());
        }
    }
}
*/

private class Item_Singer extends ZRelativeItem{

    private TextView _txt_name;
    private TextView _txt_num;

    public Item_Singer(Context $c, @Nullable Observer $l) {
        super($c, R.layout.my_singer_item, $l);

        _txt_name = (TextView)findViewById(R.id.txt_name);
        _txt_num = (TextView)findViewById(R.id.txt_num);
        setOnClickListener(new OnClickListener() {
            public void onClick(View $v) {
                sendNotification(ZNotifcationNames.Click , data);
            }
        });
    }

    @Override
    public void setData(Object $o) {
        super.setData($o);
        if(data!=null){
            Singer s  = (Singer)$o;
            _txt_name.setText(s.name);
            String str = AppUtils.id2String(R.string.global_songNum);
            str = String.format(str , s.numSong);
            _txt_num.setText(str);
        }
    }
}

private class Item_Album extends ZRelativeItem{

    private TextView _txt_name;
    private TextView _txt_num;

    public Item_Album(Context $c, @Nullable Observer $l) {
        super($c, R.layout.my_singer_item, $l);

        _txt_name = (TextView)findViewById(R.id.txt_name);
        _txt_num = (TextView)findViewById(R.id.txt_num);
        setOnClickListener(new OnClickListener() {
            public void onClick(View $v) {
                sendNotification(ZNotifcationNames.Click , data);
            }
        });
    }

    @Override
    public void setData(Object $o) {
        super.setData($o);
        if(data!=null){
            Album s  = (Album)$o;
            _txt_name.setText(s.name);
            String str = AppUtils.id2String(R.string.global_songNum);
            str = String.format(str , s.numSong);
            _txt_num.setText(str);
        }
    }
}

private class Item_Folder extends ZRelativeItem{

    private TextView _txt_path;
    private TextView _txt_name;
    private TextView _txt_num;

    public Item_Folder(Context $c, @Nullable ZObserver $l) {
        super($c, R.layout.my_folder_item, $l);

        _txt_path = (TextView)findViewById(R.id.txt_path);
        _txt_name = (TextView)findViewById(R.id.txt_name);
        _txt_num = (TextView)findViewById(R.id.txt_num);
        setOnClickListener(new OnClickListener() {
            public void onClick(View $v) {
                sendNotification(ZNotifcationNames.Click , data);
            }
        });
    }

    @Override
    public void setData(Object $o) {
        super.setData($o);
        if(data!=null){
            Folder s  = (Folder)$o;
            _txt_name.setText(s.name);
            _txt_path.setText(s.path);
            String str = AppUtils.id2String(R.string.global_songNum);
            str = String.format(str , s.numSong);
            _txt_num.setText(str);
        }
    }
}
}

