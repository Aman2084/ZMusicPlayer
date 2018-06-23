package com.zw.test;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;

/**
 * ZMusicPlayer 1.0
 * Created on 2018/2/25 23:39
 *
 * @author Aman
 * @Email: 1390792438@qq.com
 */

public class ContantUser {

    public static final int ReqSign = 666;


    public void sendIntent(Activity $a){
        Uri uri = ContactsContract.Contacts.CONTENT_URI;
        Log.d("" ,  uri.toString());
        Intent o = new Intent(Intent.ACTION_PICK , uri);
        $a.startActivityForResult(o , ReqSign);
    }

}
