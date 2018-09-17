package com.huzhou.gjj.utils;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.xutils.x;


/**
 * Toast统一管理类
 */
public class IntentUtils {
    private static ConnectivityManager connectivityManager;
    private static NetworkInfo info;

    public static boolean checkIntent() {
        connectivityManager = (ConnectivityManager) x.app()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        info = connectivityManager.getActiveNetworkInfo();

        return info != null && info.isAvailable();
    }

}
