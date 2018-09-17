package com.huzhou.gjj.acitivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.huzhou.gjj.R;
import com.huzhou.gjj.bean.Loan;
import com.huzhou.gjj.bean.Number;
import com.huzhou.gjj.utils.Const;
import com.huzhou.gjj.utils.DbUtils;
import com.huzhou.gjj.utils.JsonAnalysis;
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


import static com.huzhou.gjj.utils.Const.NOINTENT_STR;
import static com.huzhou.gjj.utils.Const.SERVICE_REQUEST_URL;
import static com.huzhou.gjj.utils.Const.SERVICE_RHIDAIN_URL;

@ContentView(R.layout.activity_loan_message)
public class LoanMessageActivity extends BaseAppCompatActivity {

    private MyProgressDialog pro_dialog;
    @ViewInject(R.id.detail_find)
    private Button detail_find;
    @ViewInject(R.id.loan_name)
    private TextView loan_name;
    @ViewInject(R.id.loan_id)
    private TextView loan_id;
    @ViewInject(R.id.loan_date)
    private TextView loan_date;
    @ViewInject(R.id.loan_year)
    private TextView loan_year;
    @ViewInject(R.id.loan_money)
    private TextView loan_money;
    @ViewInject(R.id.loan_huan_money)
    private TextView loan_huan_money;
    @ViewInject(R.id.loan_lixi)
    private TextView loan_lixi;
    @ViewInject(R.id.loan_faxi)
    private TextView loan_faxi;
    @ViewInject(R.id.loan_style_huan)
    private TextView loan_style_huan;
    @ViewInject(R.id.loan_yue_lixi)
    private TextView loan_yue_lixi;
    @ViewInject(R.id.loan_yue)
    private TextView loan_yue;
    @ViewInject(R.id.loan_qisu)
    private TextView loan_qisu;
    @ViewInject(R.id.loan_ztai)
    private TextView loan_ztai;
    @ViewInject(R.id.loan_address)
    private TextView loan_address;
    @ViewInject(R.id.loan_benjing)
    private TextView loan_benjing;
    @ViewInject(R.id.loan_huan_lixi)
    private TextView loan_huan_lixi;
    @ViewInject(R.id.loan_total)
    private TextView loan_total;
    @ViewInject(R.id.loan_yuqi)
    private TextView loan_yuqi;
    @ViewInject(R.id.loan_count)
    private TextView loan_count;

    @ViewInject(R.id.loan_dkxz)
    private TextView loan_dkxz;

    @ViewInject(R.id.loan_yitotal)
    private TextView loan_yitotal;


    @ViewInject(R.id.scroll)
    private ScrollView scroll;
    private String jkhtbh;
    private Loan loan = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        getdata();
    }

    @Event(R.id.detail_find)
    private void DetailFind(View view) {
        Intent intent = new Intent(this, LoanHistoryActivity.class);
        intent.putExtra("jkhtbh", jkhtbh);
        startActivity(intent);
    }

    @Event(R.id.plan)
    private void Plan(View view) {
        Intent intent = new Intent(this, FuturePlanActivity.class);
        intent.putExtra("jkhtbh", jkhtbh);
        startActivity(intent);
    }

    private void initView() {
        pro_dialog = LoadingDialog.createLoadingDialog(this, "加载中...");
        pro_dialog.setActivity(this);
        pro_dialog.show();
    }

    private void getZhiDian() {
        JSONArray array = new JSONArray();
        try {
            JSONObject data1 = new JSONObject();
            data1.put("dictType", "hkfs");
            JSONObject data2 = new JSONObject();
            data2.put("dictType", "xyztdk");
            JSONObject data3 = new JSONObject();
            data3.put("dictType", "dkxz");
            array.put(data1);
            array.put(data2);
            array.put(data3);
        } catch (JSONException ignored) {
        }
        RequestParams params = new RequestParams(SERVICE_RHIDAIN_URL);
        params.addBodyParameter("typeArray", array.toString());
        params.setCancelFast(true);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                pro_dialog.dismiss();
                try {
                    final JSONArray array = new JSONArray(result);
                    if (array.length() == 0) return;
                    Number number;
                    String hkfs = null, xyztdk = null, dkxz_str = null;
                    for (int i = 0; i < array.length(); i++) {
                        number = new Gson().fromJson(array.getJSONObject(i).toString(), Number.class);
                        if (!TextUtils.isEmpty(loan.getDkhkfs())) {
                            if ("hkfs".equals(number.getDictType()) && loan.getDkhkfs().equals(number.getCode()))
                                hkfs = number.getName();
                        }
                        if (!TextUtils.isEmpty(loan.getXyzt())) {
                            if ("xyztdk".equals(number.getDictType()) && loan.getXyzt().equals(number.getCode()))
                                xyztdk = number.getName();
                        }
                        if (!TextUtils.isEmpty(loan.getDkxz())) {
                            if ("dkxz".equals(number.getDictType()) && loan.getDkxz().equals(number.getCode()))
                                dkxz_str = number.getName();
                        }
                    }
                    loan_style_huan.setText(hkfs);
                    loan_ztai.setText(xyztdk);
                    loan_dkxz.setText(dkxz_str);
                } catch (Exception ignored) {
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                pro_dialog.dismiss();
            }

            @Override
            public void onCancelled(CancelledException cex) {
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

        } catch (JSONException ignored) {
        }
        RequestParams params = new RequestParams(SERVICE_REQUEST_URL);
        params.addBodyParameter("code", "2051");// 添加到请求body体的参数, 只有POST, PUT, PATCH, DELETE请求支持.
        params.addBodyParameter("json", jsonData);
        params.setCancelFast(true);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    String body = JsonAnalysis.getJsonBody(result);
                    String msg = JsonAnalysis.getMsg(result);
                    if (!"交易成功".equals(msg)) {
                        ToastUtils.showShort(x.app(), msg);
                        pro_dialog.dismiss();
                        return;
                    }
                    loan = new Gson().fromJson(body, Loan.class);
                } catch (Exception e) {
                    ToastUtils.showLong(x.app(), NOINTENT_STR);
                    pro_dialog.dismiss();
                }
                if (TextUtils.isEmpty(loan.getJkrxm())) {
                    detail_find.setVisibility(View.GONE);
                    scroll.setVisibility(View.GONE);
                    ToastUtils.showShort(x.app(), Const.NULL_STR);
                    pro_dialog.dismiss();
                    return;
                }
                scroll.setVisibility(View.VISIBLE);
                getZhiDian();
                jkhtbh = loan.getJkhtbh();
                loan_name.setText(loan.getJkrxm());
                loan_id.setText(loan.getJkhtbh());
                loan_date.setText(loan.getSlrq());
                loan_year.setText(loan.getDkqs());
                loan_money.setText(loan.getHtdkje());
                loan_huan_money.setText(loan.getHsbjze());
                loan_lixi.setText(loan.getHslxze());
                loan_faxi.setText(loan.getFxze());
                loan_yue_lixi.setText(loan.getDkyll());
                loan_yue.setText(loan.getDkye());
                loan_qisu.setText(loan.getSyqs());
                loan_address.setText(loan.getFwzl());
                loan_benjing.setText(loan.getDqyhbj());
                loan_huan_lixi.setText(loan.getDqyhlx());
                loan_total.setText(loan.getDqyhhj());
                loan_yuqi.setText(loan.getYqje());
                loan_count.setText(loan.getLjyqqs());
                loan_yitotal.setText(loan.getDqyghje());
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                pro_dialog.dismiss();
                scroll.setVisibility(View.GONE);
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
