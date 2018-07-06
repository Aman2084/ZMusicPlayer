
package com.zw;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.FrameLayout;

import com.aman.ui.controls.Alert;
import com.aman.utils.Debuger;
import com.zw.global.AppInstance;
import com.zw.main.MainMediator;
import com.zw.music.MusicMediator;
import com.zw.music.ZMusicService;
import com.zw.my.MyMediator;
import com.zw.settings.SettingMediator;
import com.zw.time.TimeMediator;

public class MainActivity extends Activity {

    private MainMediator _mediator_main;
    private MyMediator _mediator_my;
    private MusicMediator _mediator_music;
    private SettingMediator _mediator_settings;
    private TimeMediator _mediator_time;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.

    private GoogleApiClient client;
     */
    @Override
    protected void onCreate(Bundle $s) {
        super.onCreate($s);

        //初始化基础UI
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        //初始化Service(播放数据在Service中初始化)
        Intent intent = new Intent(this, ZMusicService.class);
        startService(intent);

        Debuger.traceTime("onCreat");
    }


    @Override
    protected void onSaveInstanceState(Bundle $s) {
        $s.putString("activity" , "helloworld!");
        super.onSaveInstanceState($s);
        Debuger.traceTime("onSaveInstanceState");
    }

    @Override
    protected void onStart() {
        Debuger.traceTime("onStart");
        super.onStart();
    }

    @Override
    protected void onResume() {
        Debuger.traceTime("onResume");
        super.onResume();
        if(AppInstance.mainActivity!=this){
            AppInstance.mainActivity = this;
            AppInstance.layer_second = (FrameLayout) this.findViewById(R.id.secondLayer);
            AppInstance.layer_third = (FrameLayout) this.findViewById(R.id.thirdLayer);
            AppInstance.layer_popUp = (FrameLayout) this.findViewById(R.id.popUpLayer);
        }

        if(!AppInstance.initialized){
            AppInstance.init(getApplicationContext());
        }


        if(_mediator_main!=null && _mediator_main.getContext()!=this){
            _mediator_main.destroy();
            _mediator_my.destroy();
            _mediator_music.destroy();
            _mediator_settings.destroy();
            _mediator_time.destroy();
            _mediator_main = null;
            _mediator_my = null;
            _mediator_music = null;
            _mediator_settings = null;
            _mediator_time = null;
        }

        if(_mediator_main==null){
            _mediator_main = new MainMediator(this);
            _mediator_my = new MyMediator(this);
            _mediator_music = new MusicMediator(this);
            _mediator_time = new TimeMediator(this);
            _mediator_settings = new SettingMediator(this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Debuger.traceTime("onPause");
        _mediator_main.destroy();
        _mediator_my.destroy();
        _mediator_music.destroy();
        _mediator_settings.destroy();
        _mediator_time.destroy();
        _mediator_main = null;
        _mediator_my = null;
        _mediator_music = null;
        _mediator_settings = null;
        _mediator_time = null;
    }

    @Override
    public void onStop() {
        super.onStop();
        Alert.clear();

        Debuger.traceTime("onStop");
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
//        AppIndex.AppIndexApi.end(client, getIndexApiAction());
//        client.disconnect();
    }

    @Override
    protected void onDestroy() {
        Debuger.traceTime("onDestroy");
        super.onDestroy();
    }

}
