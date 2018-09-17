package com.huzhou.gjj.reciver;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.huzhou.gjj.utils.ToastUtils;

import org.xutils.x;

//主要监听网络
public class InternetReciver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = connectivityManager.getActiveNetworkInfo();
            if (info != null && info.isAvailable()) {
                Log.e("info", info.getTypeName());
//                String name = info.getTypeName();
            } else {
                ToastUtils.showShort(x.app(), "你的网络已断开！");
            }
        }
    }
}
