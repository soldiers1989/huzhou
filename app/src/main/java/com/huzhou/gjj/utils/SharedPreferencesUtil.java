package com.huzhou.gjj.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SharedPreferencesUtil {
    private SharedPreferences sp;
    private Editor editor;
    Context context;
    private final static String SP_NAME = "mydata";
    @SuppressWarnings("deprecation")
    private final static int MODE = Context.MODE_WORLD_READABLE
            + Context.MODE_WORLD_WRITEABLE;

    @SuppressLint("CommitPrefEdits")
    public SharedPreferencesUtil(Context context) {
        this.context = context;
        sp = context.getSharedPreferences(SP_NAME, MODE);
        editor = sp.edit();
    }

    public boolean save(String key, String value) {
        editor.putString(key, value);
        return editor.commit();
    }

    public String read(String key) {
        String str = null;
        str = sp.getString(key, null);
        return str;
    }


    public boolean save_long(String key, Long value) {
        editor.putLong(key, value);
        return editor.commit();
    }

    public Long read_long(String key) {
        Long str;
        str = sp.getLong(key, 0);
        return str;
    }
}
