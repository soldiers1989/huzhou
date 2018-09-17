package com.huzhou.gjj.acitivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.huzhou.gjj.R;
import com.huzhou.gjj.bean.Number;
import com.huzhou.gjj.bean.UserInfo;
import com.huzhou.gjj.utils.Const;
import com.huzhou.gjj.utils.DbUtils;
import com.huzhou.gjj.utils.JsonAnalysis;
import com.huzhou.gjj.utils.SharedPreferencesUtil;
import com.huzhou.gjj.utils.ToastUtils;
import com.huzhou.gjj.viewUtils.LoadingDialog;
import com.huzhou.gjj.viewUtils.MyProgressDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.text.DecimalFormat;

import static com.huzhou.gjj.utils.Const.NOINTENT_STR;
import static com.huzhou.gjj.utils.Const.SERVICE_REQUEST_URL;
import static com.huzhou.gjj.utils.Const.SERVICE_RHIDAIN_URL;
import static java.lang.Double.parseDouble;


@ContentView(R.layout.activity_user_info)
public class UserInfoActivity extends BaseAppCompatActivity {
    private MyProgressDialog pro_dialog;

    @ViewInject(R.id.username)
    private TextView username;
    @ViewInject(R.id.userid)
    private TextView userid;
    @ViewInject(R.id.type)
    private TextView type;
    @ViewInject(R.id.cardid)
    private TextView cardid;
    @ViewInject(R.id.startdate)
    private TextView startdate;
    @ViewInject(R.id.personstatus)
    private TextView personstatus;
    @ViewInject(R.id.companyid)
    private TextView companyid;
    @ViewInject(R.id.companyname)
    private TextView companyname;
    @ViewInject(R.id.companybili)
    private TextView companybili;
    @ViewInject(R.id.personbili)
    private TextView personbili;
    @ViewInject(R.id.butiebili)
    private TextView butiebili;
    @ViewInject(R.id.gzjishu)
    private TextView gzjishu;
    @ViewInject(R.id.butiejs)
    private TextView butiejs;
    @ViewInject(R.id.personyj)
    private TextView personyj;
    @ViewInject(R.id.companyyj)
    private TextView companyyj;
    @ViewInject(R.id.btyj)
    private TextView btyj;
    @ViewInject(R.id.hjyj)
    private TextView hjyj;
    @ViewInject(R.id.gjjye)
    private TextView gjjye;
    @ViewInject(R.id.zfbtye)
    private TextView zfbtye;
    @ViewInject(R.id.zfgjjbtye)
    private TextView zfgjjbtye;
    @ViewInject(R.id.qjje)
    private TextView qjje;
    @ViewInject(R.id.hjye)
    private TextView hjye;
    @ViewInject(R.id.scrrol)
    private ScrollView scrrol;

    @ViewInject(R.id.ll_zfgjjbtye)
    private LinearLayout ll_zfgjjbtye;
    @ViewInject(R.id.ll_zfbtye)
    private LinearLayout ll_zfbtye;


    private SharedPreferencesUtil utils;
    private double hjye_str;
    private String company_id;
    private UserInfo info = null;
    private DecimalFormat df;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        getdata("01");
    }

    private void initView() {
        df = new DecimalFormat("#######.00");
        utils = new SharedPreferencesUtil(this);
        pro_dialog = LoadingDialog.createLoadingDialog(this, "加载中...");
        pro_dialog.setActivity(this);
        pro_dialog.show();
    }

    @Event(R.id.exit)
    private void exit(View v) {
        utils.save(Const.LOGIN, "false");
        Const.IS_LOGIN = false;
        ToastUtils.showShort(x.app(), "注销成功！");
        finish();
    }

    private void getZhiDian() {
        JSONArray array = new JSONArray();
        try {
            JSONObject data1 = new JSONObject();
            data1.put("dictType", "zjlx");
            JSONObject data2 = new JSONObject();
            data2.put("dictType", "grzhzt");

            array.put(data1);
            array.put(data2);
        } catch (JSONException ignored) {
        }
        RequestParams params = new RequestParams(SERVICE_RHIDAIN_URL);
        params.addBodyParameter("typeArray", array.toString());
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    final JSONArray array = new JSONArray(result);
                    if (array.length() == 0) return;
                    Number number;
                    String zjlx = null, grzhzt = null;
                    for (int i = 0; i < array.length(); i++) {
                        number = new Gson().fromJson(array.getJSONObject(i).toString(), Number.class);
                        if ("zjlx".equals(number.getDictType()) && info.getZjlx().equals(number.getCode()))
                            zjlx = number.getName();
                        if ("grzhzt".equals(number.getDictType()) && info.getGrzhzt().equals(number.getCode()))
                            grzhzt = number.getName();
                    }
                    type.setText(zjlx);
                    personstatus.setText(grzhzt);
                } catch (Exception ignored) {
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
            }

            @Override
            public void onCancelled(CancelledException cex) {
            }

            @Override
            public void onFinished() {
            }
        });
    }

    private void getdata(final String str) {
        JSONObject obj = new JSONObject();
        String jsonData = null;
        try {
            obj.put("grzh", DbUtils.getUserData().getPersonAcctNo());
            obj.put("zhlx", str);
            jsonData = JsonAnalysis.putJsonBody(obj, "2002").toString();
        } catch (JSONException ignored) {
        }
        RequestParams params = new RequestParams(SERVICE_REQUEST_URL);
        params.addBodyParameter("code", "2002");// 添加到请求body体的参数, 只有POST, PUT, PATCH, DELETE请求支持.
        params.addBodyParameter("json", jsonData);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    String msg = JsonAnalysis.getMsg(result);
                    if (!"交易成功".equals(msg)) {
                        ToastUtils.showLong(x.app(), msg);
                        pro_dialog.dismiss();
                        return;
                    }
                    String body = JsonAnalysis.getJsonBody(result);
                    info = new Gson().fromJson(body, UserInfo.class);
                    if ("02".equals(str)) {
                        if (TextUtils.isEmpty(info.getGrzh())) {
                            getdata("03");
                            return;
                        } else
                            getCompany(company_id);
                        String str = hjyj.getText().toString().trim();

                        hjyj.setText(df.format(JsonAnalysis.addToDouble(str, info.getDwyjce()))
                        );
                        hjye_str += parseDouble(info.getGrzhye());
//                        补贴基数
                        butiejs.setText(info.getGrjcjs());
//                        补贴月缴
                        btyj.setText(info.getDwyjce());
//  添加
                        String grzhye = info.getGrzhye();
                        if (TextUtils.isEmpty(grzhye)) {
                            ll_zfgjjbtye.setVisibility(View.GONE);
                        } else if ("0".equals(grzhye) || "0.0".equals(grzhye) || "0.00".equals(grzhye))
                            ll_zfgjjbtye.setVisibility(View.GONE);
                        else {
                            ll_zfgjjbtye.setVisibility(View.VISIBLE);
                            zfgjjbtye.setText(info.getGrzhye());
                        }
                    }
                    if ("03".equals(str)) {
                        getCompany(company_id);
                        if (TextUtils.isEmpty(info.getGrzh()))
                            return;
                        hjye_str += parseDouble(info.getGrzhye());
//                        添加
                        String grzhye = info.getGrzhye();
                        if (TextUtils.isEmpty(grzhye)) {
                            ll_zfbtye.setVisibility(View.GONE);
                        } else if ("0".equals(grzhye) || "0.0".equals(grzhye) || "0.00".equals(grzhye))
                            ll_zfbtye.setVisibility(View.GONE);
                        else {
                            ll_zfbtye.setVisibility(View.VISIBLE);
                            zfbtye.setText(info.getGrzhye());
                        }
                    }
                    if ("01".equals(str)) {
                        if (TextUtils.isEmpty(info.getGrzh())) {
                            ToastUtils.showLong(x.app(), Const.NULL_STR);
                            return;
                        }
                        getZhiDian();
                        company_id = info.getDwzh();
                        hjye_str += parseDouble(info.getGrzhye());
                        username.setText(info.getGrxm());
                        userid.setText(info.getGrzh());
                        cardid.setText(info.getZjhm());
                        startdate.setText(info.getKhrq());
                        companyid.setText(info.getDwzh());
//                        单位比例
                        companybili.setText((int) (Double.parseDouble(info.getDwjcbl()) * 100) + "%");
//                        companybili.setText(info.getDwjcbl());
                        //                        个人比例
                        personbili.setText((int) (Double.parseDouble(info.getGrjcbl()) * 100) + "%");
//                        personbili.setText(info.getGrjcbl());
                        gzjishu.setText(info.getGrjcjs());
                        hjyj.setText(df.format(JsonAnalysis.addToDouble(info.getGryjce(), info.getDwyjce())
                        ));
                        personyj.setText(info.getGryjce());
                        companyyj.setText(info.getDwyjce());
                        qjje.setText(info.getQjje());
                        gjjye.setText(info.getGrzhye());
                        getdata("02");
                    }
                } catch (Exception e) {
                    ToastUtils.showShort(x.app(), NOINTENT_STR);
                    pro_dialog.dismiss();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                pro_dialog.dismiss();
                ToastUtils.showShort(x.app(), NOINTENT_STR);
            }

            @Override
            public void onCancelled(CancelledException cex) {
            }

            @Override
            public void onFinished() {
            }
        });
    }


    private void getCompany(String str) {
        JSONObject obj = new JSONObject();
        String jsonData = null;
        try {
            obj.put("dwzh", str);
            jsonData = JsonAnalysis.putJsonBody(obj, "2021").toString();
        } catch (JSONException ignored) {
        }
        RequestParams params = new RequestParams(SERVICE_REQUEST_URL);
        params.addBodyParameter("code", "2021");// 添加到请求body体的参数, 只有POST, PUT, PATCH, DELETE请求支持.
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
                    scrrol.setVisibility(View.VISIBLE);
                    String body = JsonAnalysis.getJsonBody(result);
                    companyname.setText(new JSONObject(body).getString("dwmc"));

                    String btbl = new JSONObject(body).getString("btbl");
//                    补贴比例
                    butiebili.setText((int) (Double.parseDouble(btbl) * 100) + "%");
//                    butiebili.setText(btbl);
                    hjye.setText(hjye_str + "");
                } catch (JSONException ignored) {
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                pro_dialog.dismiss();
                ToastUtils.showShort(x.app(), NOINTENT_STR);
            }

            @Override
            public void onCancelled(CancelledException cex) {
            }

            @Override
            public void onFinished() {
            }

        });
    }
}