package com.zw.main;


import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.aman.utils.message.ZIntent;
import com.zw.R;
import com.zw.global.IntentActions;
import com.zw.my.MyMainContent;

import java.util.ArrayList;

public class MainContainerFragment extends Fragment {


    private ArrayList<View> viewList = new ArrayList<View>();

    private ViewPager _pageView;

    private PagerAdapter _apter = new PagerAdapter() {
        @Override
        public int getCount() {
            return viewList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return view==o;
        }
    };


    public MainContainerFragment() {

    }

//init
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main_container, container, false);


        initViewPager(v , inflater);
        LocalBroadcastManager m = LocalBroadcastManager.getInstance(v.getContext());
        initLocalBroadcast(m);
        return v;
    }

    private void initViewPager(View $v , LayoutInflater $inflater) {
        _pageView = (ViewPager) $v.findViewById(R.id.viewpager);
        ArrayList<View> a = new ArrayList<View>();
        View v0 = new MyMainContent($v.getContext() , null);
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                , ViewGroup.LayoutParams.MATCH_PARENT);
        v0.setLayoutParams(p);

        View v1 = $inflater.inflate(R.layout.time_main , null);
        View v2 = $inflater.inflate(R.layout.settings_main , null);
        a.add(v0);
        a.add(v1);
        a.add(v2);
        MyPageAdapter pa = new MyPageAdapter(a);
        _pageView.setAdapter(pa);
        _pageView.setCurrentItem(0);
    }


//Broadcast
    private void initLocalBroadcast(LocalBroadcastManager $m) {
        IntentFilter f = new IntentFilter();
        f.addAction(IntentActions.ShowMyMain);
        f.addAction(IntentActions.ShowSettingMain);
        f.addAction(IntentActions.ShowTimeMain);
        $m.registerReceiver(onBroadcast , f);
    }


    private BroadcastReceiver onBroadcast = new BroadcastReceiver() {
        @Override
        public void onReceive(Context $c, Intent $i) {
            Object o = ((ZIntent)$i).data;
            int n = _pageView.getCurrentItem();
            switch ($i.getAction()){
                case IntentActions.ShowMyMain:
                    n = 0;
                    break;
                case IntentActions.ShowTimeMain:
                    n = 1;
                    break;
                case IntentActions.ShowSettingMain:
                    n = 2;
                    break;
            }

            if(n !=_pageView.getCurrentItem()){
                _pageView.setCurrentItem(n);
            }
        }
    };




}

class MyPageAdapter extends PagerAdapter {

    private ArrayList<View> _views;

    public MyPageAdapter(ArrayList<View> $a) {
        _views = $a;
    }

    public int getCount(){
        return _views.size();
    }

    public boolean isViewFromObject(View view, Object o){
        return view == o;
    }

    public void destroyItem(ViewGroup $container, int $position, Object $object){
        $container.removeView(_views.get($position));//删除页卡
    }

    public Object instantiateItem(ViewGroup container, int position){
        View v = _views.get(position);
        container.addView(v , 0);
        return v;
    }
}
