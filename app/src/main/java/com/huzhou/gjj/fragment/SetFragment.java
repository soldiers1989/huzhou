package com.huzhou.gjj.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.huzhou.gjj.R;
import com.huzhou.gjj.acitivity.AuthorizationActivity;
import com.huzhou.gjj.acitivity.BindingActivity;
import com.huzhou.gjj.acitivity.LoginActivity;
import com.huzhou.gjj.acitivity.PayActivity;
import com.huzhou.gjj.acitivity.PhoneChangeActivity;
import com.huzhou.gjj.utils.Const;
import com.huzhou.gjj.utils.ToastUtils;

import org.xutils.x;


public class SetFragment extends Fragment implements View.OnClickListener {

    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_set, null);
        setListener();
        return view;
    }


    private void setListener() {
//        view.findViewById(R.id.tiqianhuankuan).setOnClickListener(this);
        view.findViewById(R.id.weituotiqu).setOnClickListener(this);
        view.findViewById(R.id.shoufukuanshouquan).setOnClickListener(this);
        view.findViewById(R.id.shoujihaoxiugai).setOnClickListener(this);
        view.findViewById(R.id.yinghangkabangding).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if (!Const.IS_LOGIN) {
            ToastUtils.showShort(x.app(), "请先登录！");
            startActivity(new Intent(x.app(), LoginActivity.class));
            return;
        }
        switch (v.getId()) {
//            case R.id.tiqianhuankuan:
//                ToastUtils.showShort(x.app(),"暂未开放");
//                startActivity(new Intent(getActivity(), AdvanceActivity.class));
//                break;
            case R.id.weituotiqu:
                startActivity(new Intent(getActivity(), AuthorizationActivity.class));
                break;
            case R.id.shoufukuanshouquan:
                startActivity(new Intent(getActivity(), PayActivity.class));
                break;
            case R.id.shoujihaoxiugai:
                startActivity(new Intent(getActivity(), PhoneChangeActivity.class));
                break;
            case R.id.yinghangkabangding:
                startActivity(new Intent(getActivity(), BindingActivity.class));
                break;
        }
    }
}
