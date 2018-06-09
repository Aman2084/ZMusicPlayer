
package com.zw;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Window;
import android.widget.FrameLayout;

import com.aman.utils.message.ZLocalBroadcast;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.zw.global.AppInstance;
import com.zw.global.IntentActions;
import com.zw.global.model.AppModel;
import com.zw.main.MainMediator;
import com.zw.music.MusicMediator;
import com.zw.music.ZMusicService;
import com.zw.my.MyMediator;
import com.zw.settings.SettingMediator;
import com.zw.time.TimeMediator;
import com.zw.utils.sql.RelationDBSQL;
import com.zw.utils.sql.SongDBSQL;
import com.zw.utils.sql.SongListDBSQL;

public class MainActivity extends Activity {

    private MainMediator _mediator_main;
    private MyMediator _mediator_my;
    private SettingMediator _mediator_settings;
    private TimeMediator _mediator_time;
    private MusicMediator _mediator_music;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //初始化基础UI
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        AppInstance.mainActivity = this;
        AppInstance.layer_second = (FrameLayout) this.findViewById(R.id.secondLayer);
        AppInstance.layer_third = (FrameLayout) this.findViewById(R.id.thirdLayer);
        AppInstance.layer_popUp = (FrameLayout) this.findViewById(R.id.popUpLayer);

        //初始化数据模型
        AppInstance.songSQL = SongDBSQL.getInstance(this, SongDBSQL.Version);
        AppInstance.songListSQL = SongListDBSQL.getInstance(this, SongListDBSQL.Version);
        AppInstance.relationDBSQL = RelationDBSQL.getInstance(this, SongListDBSQL.Version);
        AppInstance.model = AppModel.getInstance();

        //初始化Service(播放数据在Service中初始化)
        Intent intent = new Intent(this, ZMusicService.class);
        startService(intent);

        //初始化功能模块
        _mediator_main = new MainMediator(this);
        _mediator_my = new MyMediator(this);
        _mediator_music = new MusicMediator(this);
        _mediator_time = new TimeMediator(this);
        _mediator_settings = new SettingMediator(this);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    protected void onDestroy() {
        AppInstance.clear();
        ZLocalBroadcast.sendAppIntent(IntentActions.Stop);
        super.onDestroy();
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}
