package com.huzhou.gjj.acitivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.huzhou.gjj.R;
import com.huzhou.gjj.utils.Const;
import com.huzhou.gjj.utils.SharedPreferencesUtil;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.adapter.StaticPagerAdapter;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;


//用户引导页面
@ContentView(R.layout.activity_user_guide)
public class UserGuideActivity extends BaseAppCompatActivity {


    @ViewInject(R.id.enter_btn)
    private Button enter_btn;

    @ViewInject(R.id.roll)
    private RollPagerView roll;


    private SharedPreferencesUtil utils;
    private int[] lunbo = {R.mipmap.user_guide1, R.mipmap.user_guide2, R.mipmap.user_guide3};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        initView();
        setRoll();
    }

    private void setRoll() {
        RollPagerView mRollViewPager = (RollPagerView) findViewById(R.id.roll);
        //设置播放时间间隔
        assert mRollViewPager != null;
        mRollViewPager.setPlayDelay(300000);
        //设置透明度
        mRollViewPager.setAnimationDurtion(500);
        mRollViewPager.setAdapter(new TestNomalAdapter());
    }

    private class TestNomalAdapter extends StaticPagerAdapter {

        @Override
        public View getView(ViewGroup container, int position) {
            ImageView view = new ImageView(container.getContext());
            view.setBackgroundResource(lunbo[position]);
            view.setScaleType(ImageView.ScaleType.CENTER_CROP);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            if (position == 2) {
                enter_btn.setVisibility(View.VISIBLE);
            }
            return view;
        }

        @Override
        public int getCount() {
            return lunbo.length;
        }
    }

    @Event(R.id.enter_btn)
    private void enter_btn(View view) {
        //设置用户是否第一次打开app
        utils.save(Const.IS_FIRST_KEY, "false");
        startActivity(new Intent(UserGuideActivity.this, MainActivity.class));
        finish();
    }

    private void initView() {
        utils = new SharedPreferencesUtil(this);
    }

}
