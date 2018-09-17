package com.huzhou.gjj.acitivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.airsaid.pickerviewlibrary.CityPickerView;
import com.airsaid.pickerviewlibrary.listener.OnSimpleCitySelectListener;
import com.google.gson.Gson;
import com.huzhou.gjj.R;
import com.huzhou.gjj.adapter.EditChangedListener;
import com.huzhou.gjj.bean.Kfs;
import com.huzhou.gjj.bean.UserInfo;
import com.huzhou.gjj.utils.Const;
import com.huzhou.gjj.utils.DbUtils;
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
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import static com.huzhou.gjj.utils.Const.NOINTENT_STR;
import static com.huzhou.gjj.utils.Const.SERVICE_REQUEST_URL;
import static com.huzhou.gjj.utils.DbUtils.getUserData;

@ContentView(R.layout.activity_pay)
public class PayActivity extends BaseAppCompatActivity {
    private MyProgressDialog pro_dialog;
    @ViewInject(R.id.person_id)
    private TextView person_id;
    @ViewInject(R.id.person_name)
    private TextView person_name;
    @ViewInject(R.id.person_money)
    private TextView person_money;
    //    @ViewInject(R.id.person_type)
//    private TextView person_type;
    @ViewInject(R.id.company_id)
    private TextView company_id;
    @ViewInject(R.id.kfgs_id)
    private Button kfgs_id;
    @ViewInject(R.id.kf_name)
    private TextView kf_name;

    @ViewInject(R.id.kf_address)
    private TextView kf_address;

    @ViewInject(R.id.sq_money)
    private ClearWriteEditText sq_money;
    @ViewInject(R.id.address)
    private ClearWriteEditText address;
    @ViewInject(R.id.ll)
    private LinearLayout ll;

    @ViewInject(R.id.qu)
    private android.support.v7.widget.AppCompatSpinner qu;

    private String city_str;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        getdata();
    }

    private UserInfo info = null;

    private void initView() {
        pro_dialog = LoadingDialog.createLoadingDialog(this, "加载中...");
        pro_dialog.setActivity(this);
        pro_dialog.show();

        person_id.setText(DbUtils.getUserData().getPersonAcctNo());
        person_name.setText(DbUtils.getUserData().getCustName());
        sq_money.addTextChangedListener(new EditChangedListener(sq_money));
    }

    private void getdata() {
        JSONObject obj = new JSONObject();
        String jsonData = null;
        try {
            obj.put("grzh", DbUtils.getUserData().getPersonAcctNo());
            obj.put("zhlx", "01");
            jsonData = JsonAnalysis.putJsonBody(obj, "2002").toString();
        } catch (JSONException ignored) {
        }
        RequestParams params = new RequestParams(SERVICE_REQUEST_URL);
        params.addBodyParameter("code", "2002");// 添加到请求body体的参数, 只有POST, PUT, PATCH, DELETE请求支持.
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
                    info = new Gson().fromJson(body, UserInfo.class);
                    if (TextUtils.isEmpty(info.getGrzh())) {
                        ToastUtils.showLong(x.app(), Const.NULL_STR);
                        return;
                    }
                } catch (Exception ignored) {
                }
                person_money.setText(info.getGrzhye());
                company_id.setText(info.getDwzh());
                ll.setVisibility(View.VISIBLE);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                pro_dialog.dismiss();
                ToastUtils.showLong(x.app(), NOINTENT_STR);
            }

            @Override
            public void onCancelled(CancelledException cex) {
            }

            @Override
            public void onFinished() {
            }
        });
    }

    @Event(value = {R.id.kfgs_id, R.id.submit, R.id.kf_address}, type = View.OnClickListener.class)
    private void getEvent(View v) {
        switch (v.getId()) {
            case R.id.kf_address:
//判断隐藏软键盘是否弹出
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (inputMethodManager.isActive(sq_money) || inputMethodManager.isActive(address)) {
                    //因为是在fragment下，所以用了getView()获取view，也可以用findViewById（）来获取父控件
                    kf_address.requestFocus();//使其它view获取焦点.这里因为是在fragment下,所以便用了getView(),可以指定任意其它view
                    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
                CityPickerView mCityPickerView = new CityPickerView(this);
                // 设置点击外部是否消失
                mCityPickerView.setCancelable(true);
                // 设置滚轮字体大小
                mCityPickerView.setTextSize(18f);
                // 设置标题
//                mCityPickerView.setTitle("我是标题");
                // 设置取消文字
                mCityPickerView.setCancelText("取消");
                // 设置取消文字颜色
//                mCityPickerView.setCancelTextColor(Color.GRAY);
                // 设置取消文字大小
                mCityPickerView.setCancelTextSize(14f);
                // 设置确定文字
                mCityPickerView.setSubmitText("确定");
                // 设置确定文字颜色
//                mCityPickerView.setSubmitTextColor(Color.BLACK);
                // 设置确定文字大小
                mCityPickerView.setSubmitTextSize(14f);
                // 设置头部背景
//                mCityPickerView.setHeadBackgroundColor(Color.RED);
                mCityPickerView.setOnCitySelectListener(new OnSimpleCitySelectListener() {
                    @Override
                    public void onCitySelect(String prov, String city, String area) {
                        if (prov.equals(city))
                            city_str = prov + area;
                        else
                            city_str = prov + city + area;
                        kf_address.setText(city_str);
                    }

                    @Override
                    public void onCitySelect(String str) {
                    }
                });
                mCityPickerView.show();
                break;
            case R.id.kfgs_id:
                startActivityForResult(new Intent(this, FindKfActivity.class), 0);
                break;
            case R.id.submit:
                String sq_money_string = sq_money.getText().toString().trim();
                String person_money_str = person_money.getText().toString().trim();
                Double d = Double.parseDouble(person_money_str);
                String kf_id_str = kfgs_id.getText().toString().trim();
                String address_str = address.getText().toString().trim();
                if (TextUtils.isEmpty(sq_money_string)) {
                    sq_money.setError("不能为空！");
                    sq_money.requestFocus();
                } else if (TextUtils.isEmpty(address_str)) {
                    address.setError("不能为空！");
                    address.requestFocus();
                } else if ("请输入开发公司账号".equals(kf_id_str)) {
                    ToastUtils.showShort(this, "请选择开发公司！");
                    startActivityForResult(new Intent(this, FindKfActivity.class), 0);
                } else if ("0".equals(sq_money_string)) {
                    sq_money.setError("金额必须大于0！");
                    sq_money.requestFocus();
                } else if (!PhoneUtils.IsRight(sq_money_string)) {
                    sq_money.setError("请输入正确的格式！");
                    sq_money.requestFocus();
                } else if (d.compareTo(Double.parseDouble(sq_money_string)) < 0) {
                    sq_money.setError("余额不足！");
                    sq_money.requestFocus();
                } else {
                    pro_dialog.show();
                    city_str = (String) qu.getSelectedItem();
                    String new_address = "浙江省湖州市" + city_str + address_str;
//                    ToastUtils.showShort(PayActivity.this, new_address);
                    JSONObject obj = new JSONObject();
                    String jsonData = null;
                    try {
                        obj.put("grzh", DbUtils.getUserData().getPersonAcctNo());
                        obj.put("dwzh", info.getDwzh());
                        obj.put("kfgszh", company.getKfgszh());
                        obj.put("kfgsmc", company.getDwmc());
                        obj.put("zhlb", "2");
                        obj.put("zhye", info.getGrzhye());
                        obj.put("sqje", sq_money_string);
                        obj.put("skyhdm", company.getYhdm());
                        obj.put("skyhzh", company.getKhzh());
                        obj.put("skzhm", company.getKhmc());
                        obj.put("gfdz", new_address);

                        JSONObject obj3 = new JSONObject();
                        obj3.put("TrsCode", "4016");
                        obj3.put("ChanCode", "02");
                        obj3.put("ReqTrsBank", getUserData().getOrgNo());
                        obj3.put("ReqTrsCent", getUserData().getCenterCode());
                        obj3.put("ReqTrsTell", getUserData().getCustCode());
                        jsonData = JsonAnalysis.getJsonData(obj, obj3);
//                    jsonData = JsonAnalysis.putJsonBody(obj, "4016").toString();
                    } catch (JSONException ignored) {

                    }
                    RequestParams params = new RequestParams(SERVICE_REQUEST_URL);
                    params.addBodyParameter("code", "4016");// 添加到请求body体的参数, 只有POST, PUT, PATCH, DELETE请求支持.
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
                                ToastUtils.showShort(x.app(), "授权成功！");
                                finish();
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
                        }

                        @Override
                        public void onFinished() {
                        }
                    });
                }
                break;
        }
    }

    private Kfs company;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 1) {
            company = (Kfs) data.getSerializableExtra("company");
            kfgs_id.setText(company.getKfgszh());
            kf_name.setText(company.getDwmc());
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
