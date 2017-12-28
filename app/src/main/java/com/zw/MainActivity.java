
package com.zw;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.FrameLayout;

import com.zw.global.AppInstance;
import com.zw.global.model.AppModel;
import com.zw.main.MainMediator;
import com.zw.music.MusicMediator;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);


        AppInstance.mainActivity = this;
        AppInstance.layer_second = (FrameLayout) this.findViewById(R.id.secondLayer);
        AppInstance.layer_third = (FrameLayout) this.findViewById(R.id.thirdLayer);
        AppInstance.layer_popUp = (FrameLayout) this.findViewById(R.id.popUpLayer);

        AppInstance.songSQL = SongDBSQL.getInstance(this , SongDBSQL.Version);
        AppInstance.songListSQL = SongListDBSQL.getInstance(this , SongListDBSQL.Version);
        AppInstance.relationDBSQL = RelationDBSQL.getInstance(this , SongListDBSQL.Version);
        AppInstance.model = AppModel.getInstance();

        _mediator_main = new MainMediator(this);
        _mediator_my = new MyMediator(this);
        _mediator_music = new MusicMediator(this);
        _mediator_time = new TimeMediator(this);
        _mediator_settings = new SettingMediator(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        AppInstance.clear();
    }
}
