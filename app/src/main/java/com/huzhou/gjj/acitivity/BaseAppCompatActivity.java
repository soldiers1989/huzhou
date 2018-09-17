package com.huzhou.gjj.acitivity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.huzhou.gjj.AppManager;
import com.huzhou.gjj.R;

import org.xutils.x;


public class BaseAppCompatActivity extends AppCompatActivity {

    public AppManager mAppManager = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //完成视图注解框架的初始化
        x.view().inject(this);

        //添加返回按钮
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
//            actionBar.setHomeAsUpIndicator(R.mipmap.back2);
            actionBar.setCustomView(R.layout.actionbar_title);
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            actionBar.getCustomView().findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
            ((TextView) actionBar.getCustomView().findViewById(R.id.title)).setText(getTitle());
        }
        mAppManager = AppManager.getAppManager();
        mAppManager.addActivity(this);
    }

    @Override
    protected void onDestroy() {
        mAppManager.deleteActivity(this);
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
