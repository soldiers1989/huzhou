package com.huzhou.gjj.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.huzhou.gjj.R;
import com.huzhou.gjj.acitivity.ChangeActivity;
import com.huzhou.gjj.acitivity.DetailActivity;
import com.huzhou.gjj.acitivity.LoanMessageActivity;
import com.huzhou.gjj.acitivity.LoginActivity;
import com.huzhou.gjj.acitivity.UserInfoActivity;
import com.huzhou.gjj.utils.Const;
import com.huzhou.gjj.utils.ToastUtils;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.adapter.StaticPagerAdapter;

import org.xutils.x;


public class HomeFragment extends Fragment implements View.OnClickListener {
    private View view;
    private int[] lunbo = {R.mipmap.view1, R.mipmap.view2, R.mipmap.view3};
//    private int[] lunbo = {R.mipmap.view1, R.mipmap.view2};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, null);
        setListener();
        setRoll();
        return view;
    }

    private void setRoll() {
        RollPagerView mRollViewPager = (RollPagerView) view.findViewById(R.id.roll);
        //设置播放时间间隔
        mRollViewPager.setPlayDelay(3000);
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
            return view;
        }

        @Override
        public int getCount() {
            return lunbo.length;
        }
    }

    private void setListener() {
        view.findViewById(R.id.zhmx).setOnClickListener(this);
        view.findViewById(R.id.bgmx).setOnClickListener(this);
        view.findViewById(R.id.daikuanxinxi).setOnClickListener(this);
        view.findViewById(R.id.person_info).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (!Const.IS_LOGIN) {
            ToastUtils.showShort(x.app(), "请先登录！");
            startActivity(new Intent(x.app(), LoginActivity.class));
            return;
        }
        switch (v.getId()) {
            case R.id.zhmx:
                startActivity(new Intent(getActivity(), DetailActivity.class));
                break;
            case R.id.bgmx:
                startActivity(new Intent(getActivity(), ChangeActivity.class));
                break;
            case R.id.daikuanxinxi:
                startActivity(new Intent(getActivity(), LoanMessageActivity.class));
                break;
            case R.id.person_info:
                startActivity(new Intent(getActivity(), UserInfoActivity.class));
                break;

        }
    }

}
