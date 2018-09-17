package com.huzhou.gjj;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.WindowManager;

import com.huzhou.gjj.acitivity.BaseAppCompatActivity;
import com.huzhou.gjj.acitivity.MainActivity;
import com.huzhou.gjj.acitivity.UserGuideActivity;
import com.huzhou.gjj.utils.Const;
import com.huzhou.gjj.utils.SharedPreferencesUtil;

import org.xutils.view.annotation.ContentView;


//欢迎页面
@ContentView(R.layout.activity_welcome)
public class WelcomeActivity extends BaseAppCompatActivity {
    private SharedPreferencesUtil utils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        utils = new SharedPreferencesUtil(this);

        //用户是否登陆
        CheckIsLogin();

        //检查第一次登陆
        Is_First();


    }


    private void CheckIsLogin() {
        if ("true".equals(utils.read(Const.LOGIN)))
            Const.IS_LOGIN = true;
    }


    private void Is_First() {
        String is_first = utils.read(Const.IS_FIRST_KEY);
        if (TextUtils.isEmpty(is_first)) {
            startActivity(new Intent(WelcomeActivity.this, UserGuideActivity.class));
            finish();
        } else
            setTime();
    }

    //设置2s后跳转
    private void setTime() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
                finish();
            }
        }, 2000);

    }

}
