package com.huzhou.gjj.reciver;


import android.app.DownloadManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.huzhou.gjj.utils.SharedPreferencesUtil;

import static com.huzhou.gjj.utils.Const.DOWNLOAD_ID;


//主要监听网络
public class DownloadService extends Service {
    private BroadcastReceiver receiver;
    private String url, name;
    private String file_path;
    private DownloadManager dm;
    private long id;
    private SharedPreferencesUtil utils;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        utils = new SharedPreferencesUtil(this);
        url = intent.getStringExtra("url");
        file_path = intent.getStringExtra("file_path");
        name = intent.getStringExtra("name");
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
//                int status = DownloadManagerUtil.queryStatus(id, dm);
//                switch (status) {
//                    case DownloadManager.STATUS_SUCCESSFUL:
//                    PhoneUtils.installApp(file_path);
//                        break;
//                    default:
//                        dm.remove(id);
//                        if (PhoneUtils.isFileExists(file_path))
//                            PhoneUtils.deleteFile(file_path);
//                        break;
//                }
                stopSelf();
            }
        };
        registerReceiver(receiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        startDownload();
        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();
    }

    private void startDownload() {
        dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(
                Uri.parse(url));
        request.setMimeType("application/vnd.android.package-archive");
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, name);
        id = dm.enqueue(request);
        utils.save_long(DOWNLOAD_ID, id);
    }
}