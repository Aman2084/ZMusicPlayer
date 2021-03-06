package com.zw.test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.zw.R;
import com.zw.global.AppInstance;
import com.zw.global.model.data.Song;
import com.zw.global.model.data.SongList;
import com.zw.utils.sql.SongListDBSQL;

import java.util.ArrayList;

public class TestActivity extends Activity implements View.OnClickListener{


    public TestActivity() {
        super();
        AppInstance.mainActivity = this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

//        AppInstance.songSQL = SongDBSQL.getInstance(this, SongDBSQL.Version);
//        AppInstance.songListSQL = SongListDBSQL.getInstance(this , SongListDBSQL.Version);
//        AppInstance.relationDBSQL = RelationDBSQL.getInstance(this , RelationDBSQL.Version);
//        AppInstance.model = AppModel.getInstance();

        Intent intent = new Intent(this, TestService.class);
        startService(intent);

    }

    @Override
    public void onClick(View $v) {

    }

    private ArrayList<Song> test_song() {
        ArrayList<Song> a = new ArrayList<>();
        for (int i=0; i<10; i++){
            Song s = new Song();
            String str = "" + i;
            s.set_id(str);
            s.setAlbumId(str);
            s.setSingerId(str);
            s.setName("歌曲" + i);
            s.setSinger("歌手" + i);
            s.setAlbum("专辑" + i);
            s.set_path("sd0/mysong/歌曲名"+i + ".mp3");
        }
        return a;
    }

    private void test_songlist() {
        SongList data = new SongList();
        data.title = "测试List名";
        data.type = SongList.Type_user;
        SongListDBSQL l = AppInstance.songListSQL;
        l.openWriteLink();
        long i = l.insert(data);
        l.close();
        Log.d("write====" , Long.toString(i));
    }

    private void test_read(){
        SongListDBSQL l = AppInstance.songListSQL;
        l.openReadLink();
        ArrayList<SongList> s = l.queryAll();
        l.close();
        Log.d("read====" , Integer.toString(s.size()));
    }

    private void test_delete() {
        SongListDBSQL l = AppInstance.songListSQL;
        String where = "_id=?";
        String[] value = {"2"};
        l.openWriteLink();
        int i = l.delete(where , value);
        l.close();
        Log.d("delete====" , Integer.toString(i));
    }
}
