package com.huzhou.gjj.viewUtils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import com.huzhou.gjj.AppManager;

public class MyProgressDialog extends Dialog {
    public MyProgressDialog(Context context, int theme) {
        super(context, theme);
    }

    Activity acitivty;

    public void setActivity(Activity acitivty) {
        this.acitivty = acitivty;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onBackPressed() {
//        AppManager.getAppManager().deleteActivity(acitivty);
        super.onBackPressed();
    }

    @Override
    public void onStart() {
        setCanceledOnTouchOutside(false);
//        setCancelable(false);
        super.onStart();
    }
}
