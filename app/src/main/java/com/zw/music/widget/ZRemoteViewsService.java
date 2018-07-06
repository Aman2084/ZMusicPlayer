package com.zw.music.widget;

import android.content.Intent;
import android.widget.RemoteViews;

/**
 * ZMusicPlayer 1.0
 * Created on 2018/6/26 23:33
 *
 * @author Aman
 * @Email 1390792438@qq.com
 */

public class ZRemoteViewsService extends android.widget.RemoteViewsService {
    class MyRemoteViewsFatory implements RemoteViewsFactory{
        @Override
        public int getCount() {
            return 0;
        }

        @Override
        public void onCreate() {

        }

        @Override
        public void onDataSetChanged() {

        }

        @Override
        public void onDestroy() {

        }

        @Override
        public RemoteViews getViewAt(int position) {
            return null;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 0;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }
    }


    @Override
    public RemoteViewsFactory onGetViewFactory(Intent $it) {
        //TODO...  626
        return null;
    }
}
