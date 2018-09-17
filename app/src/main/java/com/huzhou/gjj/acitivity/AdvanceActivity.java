package com.huzhou.gjj.acitivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.huzhou.gjj.R;
import com.huzhou.gjj.bean.Tque;
import com.huzhou.gjj.utils.DbUtils;
import com.huzhou.gjj.utils.JsonAnalysis;
import com.huzhou.gjj.utils.ToastUtils;
import com.huzhou.gjj.viewUtils.ClearWriteEditText;
import com.huzhou.gjj.viewUtils.LoadingDialog;
import com.huzhou.gjj.viewUtils.MyProgressDialog;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;


import static com.huzhou.gjj.utils.Const.SERVICE_REQUEST_URL;
import static com.huzhou.gjj.utils.DbUtils.getUserData;

@ContentView(R.layout.activity_advance)
public class AdvanceActivity extends BaseAppCompatActivity {
    @ViewInject(R.id.tiqian_loan_msg)
    private Button tiqian_loan_msg;

    @ViewInject(R.id.old_loan_msg)
    private Button old_loan_msg;

    @ViewInject(R.id.yinghuan)
    private Button yinghuan;

    @ViewInject(R.id.user_info)
    private Button user_info;
    @ViewInject(R.id.activity_advance_tiqian)
    private LinearLayout activity_advance_tiqian;
    @ViewInject(R.id.activity_advance_oldmsg)
    private LinearLayout activity_advance_oldmsg;
    @ViewInject(R.id.activity_advance_yinghuan)
    private LinearLayout activity_advance_yinghuan;
    @ViewInject(R.id.activity_advance_userinfo)
    private LinearLayout activity_advance_userinfo;

    @ViewInject(R.id.hetong_id)
    private TextView hetong_id;
    @ViewInject(R.id.jiekuan_name)
    private TextView jiekuan_name;
    @ViewInject(R.id.loan_yh)
    private TextView loan_yh;
    @ViewInject(R.id.loan_money)
    private TextView loan_money;
    @ViewInject(R.id.loan_year)
    private TextView loan_year;
    @ViewInject(R.id.qidai__start_day)
    private TextView qidai__start_day;
    @ViewInject(R.id.qidai__end_day)
    private TextView qidai__end_day;
    @ViewInject(R.id.loan_fs)
    private TextView loan_fs;
    @ViewInject(R.id.huankuan_lv)
    private TextView huankuan_lv;
    @ViewInject(R.id.yihuan_benjing)
    private TextView yihuan_benjing;
    @ViewInject(R.id.yihuan_lixi)
    private TextView yihuan_lixi;
    @ViewInject(R.id.loan_yue)
    private TextView loan_yue;
    @ViewInject(R.id.loan_qishu)
    private TextView loan_qishu;
    @ViewInject(R.id.wh_qishu)
    private TextView wh_qishu;
    @ViewInject(R.id.bq_qishu)
    private TextView bq_qishu;
    @ViewInject(R.id.kq_zh)
    private TextView kq_zh;
    @ViewInject(R.id.loan_date_yh)
    private TextView loan_date_yh;

    @ViewInject(R.id.loan_date)
    private TextView loan_date;
    @ViewInject(R.id.loan_bj)
    private TextView loan_bj;
    @ViewInject(R.id.loan_lx)
    private TextView loan_lx;
    @ViewInject(R.id.loan_total)
    private TextView loan_total;
    @ViewInject(R.id.loan_ztai)
    private TextView loan_ztai;
    @ViewInject(R.id.user_name)
    private TextView user_name;
    @ViewInject(R.id.user_money)
    private TextView user_money;
    @ViewInject(R.id.kequ_money)
    private TextView kequ_money;
    @ViewInject(R.id.hq_money)
    private TextView hq_money;
    @ViewInject(R.id.baoliu_moeny)
    private TextView baoliu_moeny;
    @ViewInject(R.id.xubu_money)
    private TextView xubu_money;
    @ViewInject(R.id.loan_most_ben)
    private TextView loan_most_ben;
    @ViewInject(R.id.pe_loan_most_ben)
    private TextView pe_loan_most_ben;
    @ViewInject(R.id.huan_total_y)
    private TextView huan_total_y;
    @ViewInject(R.id.jqr_total_money)
    private TextView jqr_total_money;
    @ViewInject(R.id.pe_jqr_total_money)
    private TextView pe_jqr_total_money;
    @ViewInject(R.id.xianji_hk)
    private TextView xianji_hk;
    @ViewInject(R.id.jkrhb)
    private ClearWriteEditText jkrhb;
    @ViewInject(R.id.xjhb)
    private TextView xjhb;
    @ViewInject(R.id.hjhke)
    private TextView hjhke;
    @ViewInject(R.id.tqhkfs)
    private TextView tqhkfs;
    @ViewInject(R.id.sydkbj)
    private TextView sydkbj;
    @ViewInject(R.id.xyhkje)
    private TextView xyhkje;
    @ViewInject(R.id.hkqx)
    private TextView hkqx;
    @ViewInject(R.id.xzdk)
    private TextView xzdk;
    @ViewInject(R.id.sdqs)
    private TextView sdqs;
    @ViewInject(R.id.syqs)
    private TextView syqs;
    private MyProgressDialog pro_dialog;

    @ViewInject(R.id.yqyf)
    private TextView yqyf;

    @ViewInject(R.id.lwbz)
    private TextView lwbz;

    private Tque tque;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pro_dialog = LoadingDialog.createLoadingDialog(this, "加载中...");
        pro_dialog.setActivity(this);
        pro_dialog.show();
        getdata();
        jkrhb.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 此处为得到焦点时的处理内容
                } else {
                    // 此处为失去焦点时的处理内容
                }
                ToastUtils.showShort(AdvanceActivity.this, "变了");
            }
        });
    }

    @Event(value = {R.id.tiqian_loan_msg, R.id.old_loan_msg, R.id.yinghuan, R.id.user_info, R.id.jishuan, R.id.submit}, type = View.OnClickListener.class)
    private void getEvent(View view) {
        switch (view.getId()) {
            case R.id.tiqian_loan_msg:
                if (activity_advance_tiqian.getVisibility() == View.VISIBLE) {
                    activity_advance_tiqian.setVisibility(View.GONE);
                } else {
                    activity_advance_tiqian.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.old_loan_msg:
                if (activity_advance_oldmsg.getVisibility() == View.VISIBLE) {
                    activity_advance_oldmsg.setVisibility(View.GONE);
                } else {
                    activity_advance_oldmsg.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.yinghuan:
                if (activity_advance_yinghuan.getVisibility() == View.VISIBLE) {
                    activity_advance_yinghuan.setVisibility(View.GONE);
                } else {
                    activity_advance_yinghuan.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.user_info:
                if (activity_advance_userinfo.getVisibility() == View.VISIBLE) {
                    activity_advance_userinfo.setVisibility(View.GONE);
                } else {
                    activity_advance_userinfo.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.jishuan:
                pro_dialog.show();
                jishuan();
                break;
            case R.id.submit:
                pro_dialog.show();
                submit();
                break;
        }
    }

    private void jishuan() {
        JSONObject obj = new JSONObject();
        String jsonData = null;
        try {

            obj.put("jkhtbh", tque.getJkhtbh());
            obj.put("gjjhke", "");
            obj.put("xjhke", "1000");
            obj.put("tqhkfs", "1");
            obj.put("grzhye", tque.getGrzhye());
            obj.put("gtjkrgrzhye", "1000");
            obj.put("ktqe", tque.getKtqe());
            obj.put("gtjkrktqe", "1000");
            obj.put("hqje", tque.getHqje());
            obj.put("hklx", "1");
            obj.put("xyhje", "1000");

            JSONObject obj3 = new JSONObject();
            obj3.put("TrsCode", "2069");
            obj3.put("ChanCode", "01");
            obj3.put("ReqTrsBank", getUserData().getOrgNo());
            obj3.put("ReqTrsCent", getUserData().getCenterCode());
            obj3.put("ReqTrsTell", getUserData().getCustCode());
            jsonData = JsonAnalysis.getJsonData(obj, obj3);

        } catch (JSONException e) {
        }
        RequestParams params = new RequestParams(SERVICE_REQUEST_URL);
        params.addBodyParameter("code", "2069");// 添加到请求body体的参数, 只有POST, PUT, PATCH, DELETE请求支持.
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

                } catch (Exception e) {
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                pro_dialog.dismiss();
                ToastUtils.showShort(x.app(), "数据加载失败！");
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

    private void submit() {
        JSONObject obj = new JSONObject();
        String jsonData = null;
        try {
//            obj.put("jkhtbh", str);
            JSONObject obj3 = new JSONObject();
            obj3.put("TrsCode", "2068");
            obj3.put("ChanCode", "01");
            obj3.put("ReqTrsBank", getUserData().getOrgNo());
            obj3.put("ReqTrsCent", getUserData().getCenterCode());
            obj3.put("ReqTrsTell", getUserData().getCustCode());
            jsonData = JsonAnalysis.getJsonData(obj, obj3);
        } catch (JSONException e) {
        }
        RequestParams params = new RequestParams(SERVICE_REQUEST_URL);
        params.addBodyParameter("code", "2068");// 添加到请求body体的参数, 只有POST, PUT, PATCH, DELETE请求支持.
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

                } catch (Exception e) {
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                pro_dialog.dismiss();
                ToastUtils.showShort(x.app(), "数据加载失败！");
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

    private void gettiqian_loan_msg(String str) {
        JSONObject obj = new JSONObject();
        String jsonData = null;
        try {
            obj.put("jkhtbh", str);
            JSONObject obj3 = new JSONObject();
            obj3.put("TrsCode", "2068");
            obj3.put("ChanCode", "01");
            obj3.put("ReqTrsBank", getUserData().getOrgNo());
            obj3.put("ReqTrsCent", getUserData().getCenterCode());
            obj3.put("ReqTrsTell", getUserData().getCustCode());
            jsonData = JsonAnalysis.getJsonData(obj, obj3);
        } catch (JSONException e) {
        }
        RequestParams params = new RequestParams(SERVICE_REQUEST_URL);
        params.addBodyParameter("code", "2068");// 添加到请求body体的参数, 只有POST, PUT, PATCH, DELETE请求支持.
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
                    tque = new Gson().fromJson(body, Tque.class);


                    hetong_id.setText(tque.getJkhtbh());
                    jiekuan_name.setText(tque.getJkrxm());


                    String yhdm = DbUtils.FindNumberData(tque.getDkyhdm(), "yhdm");
                    if (TextUtils.isEmpty(yhdm)) yhdm = tque.getDkyhdm();
                    loan_yh.setText(yhdm);

                    String hkfs = DbUtils.FindNumberData(tque.getDkhkfs(), "hkfs");
                    if (TextUtils.isEmpty(hkfs)) hkfs = tque.getDkhkfs();
                    loan_fs.setText(hkfs);


                    String hkzt = DbUtils.FindNumberData(tque.getHkzt() + "", "hkzt");
                    if (TextUtils.isEmpty(hkzt)) hkzt = tque.getHkzt() + "";
                    loan_ztai.setText(hkzt);


//                    String yhdm = DbUtils.FindNumberData(tque.getDkyhdm(), "yhdm");
//                    if (TextUtils.isEmpty(yhdm)) yhdm = tque.getDkyhdm();
                    if ("1".equals(tque.getJslwbz()))
                        lwbz.setText("联网");
                    else lwbz.setText("未联网");

                    loan_money.setText(tque.getDkffe());
                    loan_year.setText(tque.getDkqs() + "");

                    qidai__start_day.setText(tque.getDkffrq());
                    qidai__end_day.setText(tque.getDqrq());


                    huankuan_lv.setText(tque.getZxdkll() + "");
                    yihuan_benjing.setText(tque.getHsbjze());
                    yihuan_lixi.setText(tque.getHslxze() + "");
                    loan_yue.setText(tque.getDkye());
                    loan_qishu.setText(tque.getYhqqs() + "");
                    wh_qishu.setText(tque.getWhqqs() + "");

                    bq_qishu.setText(tque.getDqqqs() + "");
                    kq_zh.setText(tque.getHkzh());

                    loan_date.setText(tque.getJkhtqdrq());


                    loan_bj.setText(tque.getDqjhghbj());
                    loan_lx.setText(tque.getDqjhghlx());
                    loan_total.setText(tque.getDqjhhkje());
                    loan_date_yh.setText(tque.getDqdqrq());


                    user_name.setText(tque.getJkrxm());
                    user_money.setText(tque.getGrzhye());
                    kequ_money.setText(tque.getKtqe());
                    hq_money.setText(tque.getHqje());
                    baoliu_moeny.setText(tque.getBlje());

                    yqyf.setText(tque.getWyqk());


//                    xubu_money.setText(tque.getxjj());
//                    loan_most_ben.setText(tque.getJkhtbh());
//                    pe_loan_most_ben.setText(tque.getJkhtbh());
//                    huan_total_y.setText(tque.gety());
//                    jqr_total_money.setText(tque.);
                    activity_advance_tiqian.setVisibility(View.VISIBLE);
                } catch (Exception e) {
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                pro_dialog.dismiss();
                ToastUtils.showShort(x.app(), "数据加载失败！");
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

    private void getdata() {
        JSONObject obj = new JSONObject();
        String jsonData = null;
        try {
            obj.put("grgjjzh", DbUtils.getUserData().getPersonAcctNo());
            jsonData = JsonAnalysis.putJsonBody(obj, "2051").toString();

        } catch (JSONException e) {
        }
        RequestParams params = new RequestParams(SERVICE_REQUEST_URL);
        params.addBodyParameter("code", "2051");// 添加到请求body体的参数, 只有POST, PUT, PATCH, DELETE请求支持.
        params.addBodyParameter("json", jsonData);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    String msg = JsonAnalysis.getMsg(result);
                    if (!"交易成功".equals(msg)) {
                        ToastUtils.showLong(x.app(), msg);
                        return;
                    }
                    String body = JsonAnalysis.getJsonBody(result);
                    gettiqian_loan_msg(new JSONObject(body).getString("jkhtbh"));
                } catch (Exception e) {
                    pro_dialog.dismiss();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                pro_dialog.dismiss();
                ToastUtils.showShort(x.app(), "数据加载失败！");
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