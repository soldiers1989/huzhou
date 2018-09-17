package com.huzhou.gjj.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.huzhou.gjj.R;
import com.huzhou.gjj.adapter.EditChangedListener;
import com.huzhou.gjj.utils.JsonAnalysis;
import com.huzhou.gjj.utils.PhoneUtils;
import com.huzhou.gjj.utils.ToastUtils;
import com.huzhou.gjj.viewUtils.ClearWriteEditText;
import com.huzhou.gjj.viewUtils.LoadingDialog;
import com.huzhou.gjj.viewUtils.MyProgressDialog;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;


import static com.huzhou.gjj.utils.Const.NOINTENT_STR;
import static com.huzhou.gjj.utils.Const.SERVICE_REQUEST_URL;

public class ZhixunFragment1 extends Fragment {
    private View view;
    private MyProgressDialog pro_dialog;
    private String sfyj_str, fwmj_str, xb_str, tudi_xz_str, xz_str, gf_address_str, str1;

    private ClearWriteEditText yc_money, fangling, ycce, total_money;
    private Spinner sfyj, fwmj, xb, xz, year, tudi_xz, gf_address;
    private LinearLayout ll0, ll1, ll2, ll3, jieguo;
    private Button detail_find;
    private LinearLayout scroll;
    private TextView ly, ed;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_zhixun1, null);
        initView();
        return view;
    }


    private void initView() {
        pro_dialog = LoadingDialog.createLoadingDialog(getActivity(), "加载中...");
        pro_dialog.setActivity(getActivity());

        yc_money = (ClearWriteEditText) view.findViewById(R.id.yc_money);
        fangling = (ClearWriteEditText) view.findViewById(R.id.fangling);
        ycce = (ClearWriteEditText) view.findViewById(R.id.ycce);
        total_money = (ClearWriteEditText) view.findViewById(R.id.total_money);
        ll0 = (LinearLayout) view.findViewById(R.id.ll0);
        ll1 = (LinearLayout) view.findViewById(R.id.ll1);
        ll2 = (LinearLayout) view.findViewById(R.id.ll2);
        ll3 = (LinearLayout) view.findViewById(R.id.ll3);
        sfyj = (Spinner) view.findViewById(R.id.sfyj);
        fwmj = (Spinner) view.findViewById(R.id.fwmj);
        xb = (Spinner) view.findViewById(R.id.xb);
        xz = (Spinner) view.findViewById(R.id.xz);
        year = (Spinner) view.findViewById(R.id.year);
        tudi_xz = (Spinner) view.findViewById(R.id.tudi_xz);
        gf_address = (Spinner) view.findViewById(R.id.gf_address);
        ed = (TextView) view.findViewById(R.id.ed);
        ly = (TextView) view.findViewById(R.id.ly);
        scroll = (LinearLayout) view.findViewById(R.id.scroll);
        detail_find = (Button) view.findViewById(R.id.detail_find);
        jieguo = (LinearLayout) view.findViewById(R.id.jieguo);

        yc_money.addTextChangedListener(new EditChangedListener(yc_money));
        ycce.addTextChangedListener(new EditChangedListener(ycce));
        total_money.addTextChangedListener(new EditChangedListener(total_money));

        sfyj.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 1) ll0.setVisibility(View.VISIBLE);
                else ll0.setVisibility(View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        xz.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 2) {
                    ll1.setVisibility(View.VISIBLE);
                    ll2.setVisibility(View.VISIBLE);
                    ll3.setVisibility(View.VISIBLE);
                } else {
                    ll1.setVisibility(View.GONE);
                    ll2.setVisibility(View.GONE);
                    ll3.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        detail_find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xz_str = (String) xz.getSelectedItem();
                if ("期房".equals(xz_str))
                    xz_str = "01";
                else if ("现房".equals(xz_str))
                    xz_str = "02";
                else xz_str = "03";
                String detail_find_str = detail_find.getText().toString().trim();
                if ("重新计算".equals(detail_find_str)) {
                    scroll.setVisibility(View.VISIBLE);
                    jieguo.setVisibility(View.GONE);
                    detail_find.setText("开始计算");
                    return;
                }

                String fangling_str = fangling.getText().toString().trim();
                String yc_money_str = yc_money.getText().toString().trim();
                String ycce_str = ycce.getText().toString().trim();
                String total_money_str = total_money.getText().toString().trim();

                if (TextUtils.isEmpty(yc_money_str)) {
                    yc_money.setError("不能为空！");
                    yc_money.requestFocus();
                } else if (TextUtils.isEmpty(ycce_str)) {
                    ycce.setError("不能为空！");
                    ycce.requestFocus();
                } else if (TextUtils.isEmpty(total_money_str)) {
                    total_money.setError("不能为空！");
                    total_money.requestFocus();
                } else if (TextUtils.isEmpty(fangling_str) && "03".equals(xz_str)) {
                    fangling.setError("不能为空");
                    fangling.requestFocus();
                } else if (!PhoneUtils.IsRight(yc_money_str)) {
                    yc_money.setError("请输入正确的格式！");
                    yc_money.requestFocus();
                } else if (!PhoneUtils.IsRight(ycce_str)) {
                    ycce.setError("请输入正确的格式！");
                    ycce.requestFocus();
                } else if ("0".equals(total_money_str)) {
                    total_money.setError("金额必须大于0！");
                    total_money.requestFocus();
                } else if (!PhoneUtils.IsRight(total_money_str)) {
                    total_money.setError("请输入正确的格式！");
                    total_money.requestFocus();
                } else {
                    sfyj_str = (String) sfyj.getSelectedItem();
                    if ("否".equals(sfyj_str)) sfyj_str = "0";
                    else
                        sfyj_str = "1";

                    fwmj_str = (String) fwmj.getSelectedItem();
                    if ("90平米以上".equals(fwmj_str)) fwmj_str = "01";
                    else
                        fwmj_str = "02";

                    xb_str = (String) xb.getSelectedItem();
                    if ("男".equals(fwmj_str)) xb_str = "1";
                    else
                        xb_str = "0";

                    tudi_xz_str = (String) tudi_xz.getSelectedItem();
                    if ("行政划拨".equals(fwmj_str))
                        tudi_xz_str = "01";
                    else if ("有偿出让".equals(fwmj_str))
                        tudi_xz_str = "02";
                    else tudi_xz_str = "03";


                    gf_address_str = (String) gf_address.getSelectedItem();
                    if ("中心城区".equals(fwmj_str))
                        gf_address_str = "01";
                    else if ("县区首位城镇".equals(fwmj_str))
                        gf_address_str = "02";
                    else gf_address_str = "03";

                    String year_str = (String) year.getSelectedItem();
                    str1 = year_str.substring(0, 2);
                    if (str1.contains("年"))
                        str1 = str1.substring(0, 1);

                    pro_dialog.show();


                    JSONObject obj = new JSONObject();
                    String jsonData = null;
                    try {
                        obj.put("SQRYJC", yc_money_str);
                        obj.put("POYJC", ycce_str);
                        obj.put("SFYJRC", sfyj_str);
                        obj.put("ZFZJ", total_money_str);

                        obj.put("FWMJ", fwmj_str);
                        obj.put("SQRXB", xb_str);
                        obj.put("TDXZ", tudi_xz_str);
                        obj.put("GFWZ", gf_address_str);

                        obj.put("FWXZ", xz_str);
                        obj.put("DKNX", str1);
                        obj.put("FL", fangling_str);

                        JSONObject obj3 = new JSONObject();
                        obj3.put("TrsCode", "2088");
                        obj3.put("ChanCode", "02");

//                        obj3.put("ReqTrsBank", getUserData().getOrgNo());
//                        obj3.put("ReqTrsCent", getUserData().getCenterCode());
//                        obj3.put("ReqTrsTell", getUserData().getCustCode());

                        jsonData = JsonAnalysis.getJsonData(obj, obj3);
                    } catch (JSONException ignored) {
                    }
                    RequestParams params = new RequestParams(SERVICE_REQUEST_URL);
                    params.addBodyParameter("code", "2088");// 添加到请求body体的参数, 只有POST, PUT, PATCH, DELETE请求支持.
                    params.addBodyParameter("json", jsonData);
                    x.http().post(params, new Callback.CommonCallback<String>() {
                        @Override
                        public void onSuccess(String result) {
                            pro_dialog.dismiss();
                            try {
                                String msg = JsonAnalysis.getMsg(result);
                                if (!"交易成功".equals(msg)) {
                                    ToastUtils.showLong(x.app(), msg);
                                    return;
                                }
                                String body = JsonAnalysis.getJsonBody(result);

                                String DKED = new JSONObject(body).getString("DKED");
                                String DKLL = new JSONObject(body).getString("DKLL");
                                ed.setText(DKED);
                                if (!TextUtils.isEmpty(DKLL))
                                ly.setText((Double.parseDouble(DKLL) * 100) + "%");
                                
                                scroll.setVisibility(View.GONE);
                                detail_find.setText("重新计算");
                                jieguo.setVisibility(View.VISIBLE);
                            } catch (Exception ignored) {
                            }
                        }

                        @Override
                        public void onError(Throwable ex, boolean isOnCallback) {
                            pro_dialog.dismiss();
                            ToastUtils.showShort(x.app(), NOINTENT_STR);
                        }

                        @Override
                        public void onCancelled(CancelledException cex) {
                            pro_dialog.dismiss();
                            ToastUtils.showShort(x.app(), "取消数据加载！");
                        }

                        @Override
                        public void onFinished() {
                        }
                    });
                }

            }
        });

    }
}
