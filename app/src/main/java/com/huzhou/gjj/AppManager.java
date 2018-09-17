package com.huzhou.gjj;

import android.app.Activity;

import java.util.Stack;

//主要用于管理Activity
public class AppManager {
    private static AppManager appManager;
    // stack(栈)：后进先出，先进入的数据被压入栈底，最后的数据在栈顶,需要读数据的时候从栈顶开始弹出数据
    // activities 管理所有进入的activity
    private Stack<Activity> activities;

    private AppManager() {
    }

    // 单例类（双重检查）：获取AppManager实例
    public static AppManager getAppManager() {
        if (appManager == null) {
            synchronized (AppManager.class) {
                if (appManager == null) {
                    appManager = new AppManager();
                }
            }
        }
        return appManager;
    }

    // 添加activity
    public void addActivity(Activity activity) {
        if (activities == null) {
            activities = new Stack<>();
        }
        activities.add(activity);
    }

    // 删除activity
    public void deleteActivity(Activity activity) {
        if (activities != null) {
            activities.remove(activity);
            activity.finish();
        }

    }

    // 删除所有activity
    public void deleteAllActivity() {
        for (int i = 0; i < activities.size(); i++) {
            if (null != activities.get(i)) {
                activities.get(i).finish();
            }
        }
        activities.clear();
    }
}
