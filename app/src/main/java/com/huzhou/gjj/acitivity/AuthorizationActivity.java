package com.huzhou.gjj.acitivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.huzhou.gjj.R;
import com.huzhou.gjj.adapter.EditChangedListener;
import com.huzhou.gjj.bean.Company;
import com.huzhou.gjj.bean.UserInfo;
import com.huzhou.gjj.utils.DateUtils;
import com.huzhou.gjj.utils.DbUtils;
import com.huzhou.gjj.utils.JsonAnalysis;
import com.huzhou.gjj.utils.PhoneUtils;
import com.huzhou.gjj.utils.ToastUtils;
import com.huzhou.gjj.viewUtils.ClearWriteEditText;
import com.huzhou.gjj.viewUtils.LoadingDialog;
import com.huzhou.gjj.viewUtils.MyDatePickerDialog;
import com.huzhou.gjj.viewUtils.MyProgressDialog;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.Calendar;
import java.util.Date;

import static com.huzhou.gjj.R.id.detail_find_btn;
import static com.huzhou.gjj.utils.Const.NOINTENT_STR;
import static com.huzhou.gjj.utils.Const.SERVICE_REQUEST_URL;
import static com.huzhou.gjj.utils.DbUtils.getUserData;

//委托提取授权
@ContentView(R.layout.activity_authorization)
public class AuthorizationActivity extends BaseAppCompatActivity {

    //    有效期开始年月 对应弹出框
    private MyDatePickerDialog dialog_start;
    //    有效期结束年月 对应弹出框
    private MyDatePickerDialog dialog_end;
    //    有效期开始年月
    @ViewInject(R.id.start_date_text)
    private TextView start_date_text;
    //    有效期结束年月
    @ViewInject(R.id.end_date_text)
    private TextView end_date_text;
    //    个人账号
    @ViewInject(R.id.person_id)
    private TextView person_id;
    //    个人姓名
    @ViewInject(R.id.person_name)
    private TextView person_name;
    //    个人余额
    @ViewInject(R.id.person_money)
    private TextView person_money;

    //    收款单位账号
    @ViewInject(R.id.find)
    private Button find;

    //    授权金额
    @ViewInject(R.id.sq_money)
    private ClearWriteEditText sq_money;


    //    收款单位名称
    @ViewInject(R.id.company_name)
    private TextView company_name;

    //    收款银行
    @ViewInject(R.id.company_code)
    private TextView company_code;

    //    收款银行账号
    @ViewInject(R.id.yh_id)
    private TextView yh_id;

    //    收款账户名
    @ViewInject(R.id.sq_name)
    private TextView sq_name;

    @ViewInject(R.id.ll)
    private LinearLayout ll;

    @ViewInject(R.id.linearLayout)
    private LinearLayout linearLayout;

    private MyProgressDialog pro_dialog;
    private UserInfo userinfo = null;

    private String start = null;
    private String end = null;
    private String start2 = null;
    private String end2 = null;
    private Company company;
    private String dwmc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        getPersonData();
    }

    //    个人账户信息查询
    private void getPersonData() {

        JSONObject obj = new JSONObject();
        String jsonData = null;
        try {
            obj.put("grzh", DbUtils.getUserData().getPersonAcctNo());
            obj.put("zhlx", "01");
            jsonData = JsonAnalysis.putJsonBody(obj, "2002").toString();
        } catch (JSONException e) {
            pro_dialog.dismiss();
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
                    userinfo = new Gson().fromJson(body, UserInfo.class);
                    person_money.setText(userinfo.getGrzhye());
                    ConpanyData(userinfo.getDwzh());
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

    //    单位账户信息查询
    private void ConpanyData(String dwzh) {
        JSONObject obj = new JSONObject();
        String jsonData = null;
        try {
            obj.put("dwzh", dwzh);
            jsonData = JsonAnalysis.putJsonBody(obj, "2021").toString();
        } catch (JSONException ignored) {
            pro_dialog.dismiss();
        }
        RequestParams params = new RequestParams(SERVICE_REQUEST_URL);
        params.addBodyParameter("code", "2021");// 添加到请求body体的参数, 只有POST, PUT, PATCH, DELETE请求支持.
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
                    ll.setVisibility(View.VISIBLE);
                    String body = JsonAnalysis.getJsonBody(result);
                    dwmc = new JSONObject(body).getString("dwmc");
                } catch (Exception e) {
                    ToastUtils.showLong(x.app(), NOINTENT_STR);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                ToastUtils.showLong(x.app(), NOINTENT_STR);
            }

            @Override
            public void onCancelled(CancelledException cex) {
            }

            @Override
            public void onFinished() {
                pro_dialog.dismiss();
            }
        });

    }


    private void initView() {
        sq_money.addTextChangedListener(new EditChangedListener(sq_money));
        pro_dialog = LoadingDialog.createLoadingDialog(this, "加载中...");
        pro_dialog.setActivity(this);
        pro_dialog.show();

        person_id.setText(DbUtils.getUserData().getPersonAcctNo());
        person_name.setText(DbUtils.getUserData().getCustName());

        Calendar c = Calendar.getInstance();
        start_date_text.setText("请选择开始日期");
        end_date_text.setText("请选择结束日期");
        dialog_start = new MyDatePickerDialog(this, AlertDialog.THEME_HOLO_LIGHT,
                new OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        String dayOfMonth_str;
                        String monthOfYear_str;
                        if (dayOfMonth < 10)
                            dayOfMonth_str = "0" + dayOfMonth;
                        else
                            dayOfMonth_str = "" + dayOfMonth;

                        monthOfYear = monthOfYear + 1;
                        if (monthOfYear < 10)
                            monthOfYear_str = "0" + monthOfYear;
                        else
                            monthOfYear_str = "" + monthOfYear;
                        start = year + monthOfYear_str
                                + dayOfMonth_str;
                        start2 = year + monthOfYear_str;
                        start_date_text.setText(year + "年" + monthOfYear + "月"
                        );
                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH),
                c.get(Calendar.DAY_OF_MONTH));

        dialog_end = new MyDatePickerDialog(this, AlertDialog.THEME_HOLO_LIGHT,
                new OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        String dayOfMonth_str;
                        String monthOfYear_str;
                        if (dayOfMonth < 10)
                            dayOfMonth_str = "0" + dayOfMonth;
                        else
                            dayOfMonth_str = "" + dayOfMonth;

                        monthOfYear = monthOfYear + 1;
                        if (monthOfYear < 10)
                            monthOfYear_str = "0" + monthOfYear;
                        else
                            monthOfYear_str = "" + monthOfYear;
                        end = year + monthOfYear_str
                                + dayOfMonth_str;
                        end2 = year + monthOfYear_str;
                        end_date_text.setText(year + "年" + monthOfYear + "月"
                        );
                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH),
                c.get(Calendar.DAY_OF_MONTH));
    }

    @Event(value = {R.id.detail_find_btn, R.id.start_date_text, R.id.end_date_text, R.id.clearall, R.id.find}, type = View.OnClickListener.class)
    private void getEvent(View v) {
        switch (v.getId()) {
            case detail_find_btn:
                Date date_start = DateUtils.strToDate(start);
                Date date_end = DateUtils.strToDate(end);
                if (TextUtils.isEmpty(start)) {
                    ToastUtils.showShort(x.app(), "请选择开始日期");
                    dialog_start.show();
                    return;
                } else if (TextUtils.isEmpty(end)) {
                    ToastUtils.showShort(x.app(), "请选择结束日期");
                    dialog_end.show();
                    return;
                } else if (date_start.before(DateUtils.getCurrentTime()) ||
                        date_end.before(DateUtils.getCurrentTime())) {
                    ToastUtils.showShort(x.app(), "必须超过当前日期");
                    return;
                } else if (date_start.after(date_end)) {
                    ToastUtils.showShort(x.app(), "起始日期不能超过结束日期");
                    return;
                } else {
                    String person_money_str = person_money.getText().toString().trim();
                    Double d = Double.parseDouble(person_money_str);
                    String find_str = find.getText().toString().trim();
                    String sq_money_string = sq_money.getText().toString().trim();
                    if (TextUtils.isEmpty(sq_money_string)) {
                        sq_money.setError("不能为空！");
                        sq_money.requestFocus();
                    } else if ("0".equals(sq_money_string)) {
                        sq_money.setError("金额必须大于0！");
                        sq_money.requestFocus();
                    } else if (!PhoneUtils.IsRight(sq_money_string)) {
                        sq_money.setError("请输入正确的格式！");
                        sq_money.requestFocus();
                    } else if (d.compareTo(Double.parseDouble(sq_money_string)) < 0) {
                        sq_money.setError("余额不足！");
                        sq_money.requestFocus();
                    } else if ("请选择收款单位".equals(find_str)) {
                        ToastUtils.showShort(x.app(), "请选择收款单位!");
                        startActivityForResult(new Intent(this, FindCompanyActivity.class), 0);
                    } else {
                        pro_dialog.show();
                        JSONObject obj = new JSONObject();
                        String jsonData = null;
                        try {
                            obj.put("dwzh", userinfo.getDwzh());
                            obj.put("dwmc", dwmc);
                            obj.put("grzh", getUserData().getPersonAcctNo());
                            obj.put("sqje", sq_money_string);
                            obj.put("yxqksny", start2);
                            obj.put("yxqjsny", end2);
                            obj.put("skyhdm", company.getSkyhdm());
                            obj.put("skyhzh", company.getSkyhzh());
                            obj.put("skzhm", company.getSkzhm());
                            obj.put("xyh", company.getXyh());

                            JSONObject obj3 = new JSONObject();
                            obj3.put("TrsCode", "4015");
                            obj3.put("ChanCode", "02");
                            obj3.put("ReqTrsBank", getUserData().getOrgNo());
                            obj3.put("ReqTrsCent", getUserData().getCenterCode());
                            obj3.put("ReqTrsTell", getUserData().getCustCode());
                            jsonData = JsonAnalysis.getJsonData(obj, obj3);
                        } catch (JSONException e) {
                            pro_dialog.dismiss();
                        }
                        RequestParams params = new RequestParams(SERVICE_REQUEST_URL);
                        params.addBodyParameter("code", "4015");// 添加到请求body体的参数, 只有POST, PUT, PATCH, DELETE请求支持.
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
                                    ToastUtils.showLong(x.app(), "提交成功！");
                                    finish();
                                } catch (Exception e) {
                                    ToastUtils.showLong(x.app(), NOINTENT_STR);
                                }
                            }

                            @Override
                            public void onError(Throwable ex, boolean isOnCallback) {
                                ToastUtils.showLong(x.app(), NOINTENT_STR);
                            }

                            @Override
                            public void onCancelled(CancelledException cex) {
                            }

                            @Override
                            public void onFinished() {
                                pro_dialog.dismiss();
                            }
                        });
                    }
                }
                break;
            case R.id.clearall:
                sq_money.setText("");
                company_name.setText("");
                yh_id.setText("");
                sq_name.setText("");
                break;
            case R.id.start_date_text:
                dialog_start.show();
                break;
            case R.id.end_date_text:
                dialog_end.show();
                break;
            case R.id.find:
                startActivityForResult(new Intent(this, FindCompanyActivity.class), 0);
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 1) {
            company = (Company) data.getSerializableExtra("company");
            linearLayout.setVisibility(View.VISIBLE);
            company_name.setText(company.getDwmc());
            find.setText(company.getDwzh());
            String khyhlx = data.getStringExtra("khyhlx");
            company_code.setText(khyhlx);
            yh_id.setText(company.getSkyhzh());
            sq_name.setText(company.getSkzhm());
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
