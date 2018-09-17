package com.huzhou.gjj.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.huzhou.gjj.R;
import com.huzhou.gjj.acitivity.DotDistributionActivity;
import com.huzhou.gjj.acitivity.DotDistributionActivity2;
import com.huzhou.gjj.acitivity.DotQueueActivity;
import com.huzhou.gjj.acitivity.LoginActivity;
import com.huzhou.gjj.acitivity.ScheduleActivity;
import com.huzhou.gjj.acitivity.ZhiXunActivity;
import com.huzhou.gjj.acitivity.ZhiXunActivity2;
import com.huzhou.gjj.utils.Const;
import com.huzhou.gjj.utils.ToastUtils;

import org.xutils.x;


public class FindFragment extends Fragment implements View.OnClickListener {
    private View view;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_find, null);
        setListener();
        return view;
    }

    private void setListener() {
        view.findViewById(R.id.daiqianzhixun).setOnClickListener(this);
        view.findViewById(R.id.daikuanjindu).setOnClickListener(this);
        view.findViewById(R.id.wdcx).setOnClickListener(this);
        view.findViewById(R.id.wdyy).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.daiqianzhixun:
//                startActivity(new Intent(getActivity(), ZhiXunActivity.class));
                if (!Const.IS_LOGIN) {
                    ToastUtils.showShort(x.app(), "请先登录！");
                    startActivity(new Intent(x.app(), LoginActivity.class));
                    return;
                }
                startActivity(new Intent(getActivity(), ZhiXunActivity2.class));
                break;
            case R.id.daikuanjindu:
                if (!Const.IS_LOGIN) {
                    ToastUtils.showShort(x.app(), "请先登录！");
                    startActivity(new Intent(x.app(), LoginActivity.class));
                    return;
                }
                startActivity(new Intent(getActivity(), ScheduleActivity.class));
                break;
            case R.id.wdcx:
                startActivity(new Intent(getActivity(), DotDistributionActivity2.class));
//                startActivity(new Intent(getActivity(), DotDistributionActivity.class));
                break;
            case R.id.wdyy:
                if (!Const.IS_LOGIN) {
                    ToastUtils.showShort(x.app(), "请先登录！");
                    startActivity(new Intent(x.app(), LoginActivity.class));
                    return;
                }
                startActivity(new Intent(getActivity(), DotQueueActivity.class));
                break;
        }
    }
}
