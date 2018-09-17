package com.huzhou.gjj.acitivity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.huzhou.gjj.R;
import com.huzhou.gjj.fragment.ZhixunFragment1;
import com.huzhou.gjj.fragment.ZhixunFragment2;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

@ContentView(R.layout.activity_zhixun)
public class ZhiXunActivity extends BaseAppCompatActivity {

    @ViewInject(R.id.main_viewPager)
    private ViewPager viewPager;

    @ViewInject(R.id.main_tab)
    private TabLayout tabLayout;

    private String tabTitles[] = new String[]{"可贷额度计算器", "月还款计划计算器"};
    private List<Fragment> fragmentList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        SetView();
    }

    private void initData() {
        fragmentList.add(new ZhixunFragment1());
        fragmentList.add(new ZhixunFragment2());
    }

    private void SetView() {
        MyFragmentPagerAdapter pagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
    }

    class MyFragmentPagerAdapter extends FragmentPagerAdapter {


        MyFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }
    }
}
