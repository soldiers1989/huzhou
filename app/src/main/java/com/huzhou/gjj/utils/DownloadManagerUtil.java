package com.huzhou.gjj.utils;

import android.app.DownloadManager;
import android.database.Cursor;


/**
 * Created by Administrator on 2017/5/5.
 */

public class DownloadManagerUtil {
    // 根据DownloadManager下载的Id，查询DownloadManager某个Id的下载任务状态。
    public static int queryStatus(long id, DownloadManager dm) {
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(id);
        Cursor cursor = dm.query(query);
        if (cursor.moveToFirst()) {
            return cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
        }
        return 0;
    }
}
