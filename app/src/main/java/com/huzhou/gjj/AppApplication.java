package com.huzhou.gjj;

import android.app.Application;

import org.xutils.DbManager;
import org.xutils.x;

import static com.huzhou.gjj.utils.Const.DB_NAME;


public class AppApplication extends Application {

    private DbManager.DaoConfig daoConfig;

    public DbManager.DaoConfig getDaoConfig() {
        return daoConfig;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
//        x.Ext.setDebug(BuildConfig.DEBUG);// 是否输出debug日志, 开启debug会影响性能.
        daoConfig = new DbManager.DaoConfig()
                .setDbName(DB_NAME)//创建数据库的名称
                .setDbVersion(1)//数据库版本号
                .setDbUpgradeListener(new DbManager.DbUpgradeListener() {
                    @Override
                    public void onUpgrade(DbManager db, int oldVersion, int newVersion) {
                    }
                });//数据库更新操作
    }
}
