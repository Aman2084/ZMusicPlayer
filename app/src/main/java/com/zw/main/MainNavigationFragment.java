package com.zw.main;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aman.utils.UIUtils;
import com.aman.utils.message.ZLocalBroadcast;
import com.zw.R;
import com.zw.global.IntentActions;
import com.zw.ui.controls.MyRadioButton;

/**
 * A simple {@link Fragment} subclass.
 * 上方导航Bar
 */
public class MainNavigationFragment extends Fragment {

    private View[] _btns;

    public MainNavigationFragment() {
        // Required empty public constructor
    }


//init
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_main_navigation, container, false);
        ViewGroup vg = (ViewGroup) v;
        int[] a = {R.id.btn_mySpace , R.id.btn_time , R.id.btn_setting};
        UIUtils.setOnClickByIds(vg , a , onItemClick);
        _btns = UIUtils.findViewsByIds(vg , a);
        return v;
    }


//Listener
    private View.OnClickListener onItemClick = new View.OnClickListener() {
        public void onClick(View $v) {
            String s = null;
            String[] a = {IntentActions.ShowMyMain , IntentActions.ShowMusicMain ,
                    IntentActions.ShowTimeMain , IntentActions.ShowSettingMain};

            switch ($v.getId()){
                case R.id.btn_mySpace:
                    s = IntentActions.ShowMyMain;
                    break;
                case R.id.btn_time:
                    s = IntentActions.ShowTimeMain;
                    break;
                case R.id.btn_setting:
                    s = IntentActions.ShowSettingMain;
                    break;
            }

            for (View v:_btns) {
                MyRadioButton b = (MyRadioButton)v;
                b.refuseTop();
            }

            /*
            ViewGroup g = (ViewGroup)getView();
            MyRadioButton b = (MyRadioButton)g.findViewById(R.id.btn_mySpace);
            b.refuseTop();
            b = (MyRadioButton)g.findViewById(R.id.btn_time);
            b.refuseTop();
            b = (MyRadioButton)g.findViewById(R.id.btn_setting);
            b.refuseTop();
            */
            if(s!=null){
                ZLocalBroadcast.sendAppIntent(s);
            }
        }
    };
//interface
    public void setSelectedIndex(int $posion){
        for (int i = 0; i <_btns.length ; i++) {
            MyRadioButton b = (MyRadioButton)_btns[i];
            b.setChecked(i==$posion);
            b.refuseTop();
        }
    }


}
