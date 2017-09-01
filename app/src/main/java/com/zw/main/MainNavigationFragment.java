package com.zw.main;


import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import com.aman.utils.UIUtils;
import com.aman.utils.message.ZIntent;
import com.zw.R;
import com.zw.global.IntentActions;

/**
 * A simple {@link Fragment} subclass.
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
        int[] a1 = {R.id.btn_mySpace , R.id.btn_music , R.id.btn_time , R.id.btn_setting};
        View[] a2 = UIUtils.findViewsByIds(vg , a1);
        for (int i = 0; i <a2.length ; i++) {
            RadioButton b = (RadioButton)a2[i];
            b.setOnClickListener(onItemClick);
        }
        _btns = a2;
        return v;
    }


//Listener
    private View.OnClickListener onItemClick = new View.OnClickListener() {
        public void onClick(View v) {
            String s = null;
            String[] a = {IntentActions.ShowMyMain , IntentActions.ShowMusicMain ,
                    IntentActions.ShowTimeMain , IntentActions.ShowSettingMain};
            for (int i = 0; i < _btns.length; i++) {
                RadioButton b = (RadioButton) _btns[i];
                boolean bool = b == v;
                if(bool){
                    s = a[i];
                }
                int color = (b == v ? R.color.red : R.color.black);
                int c  = ContextCompat.getColor(b.getContext() , color);
                b.setTextColor(c);
            }

            if(s!=null){
                LocalBroadcastManager m = LocalBroadcastManager.getInstance(v.getContext());
                ZIntent intent = new ZIntent(s);
                m.sendBroadcast(intent);
            }
        }
    };



}
