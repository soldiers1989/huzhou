package com.huzhou.gjj.acitivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.avast.android.dialogs.fragment.SimpleDialogFragment;
import com.avast.android.dialogs.iface.ISimpleDialogListener;
import com.google.gson.Gson;
import com.huzhou.gjj.R;
import com.huzhou.gjj.adapter.SpinnerAdapter2;
import com.huzhou.gjj.bean.Distribution;
import com.huzhou.gjj.bean.DistributionType;
import com.huzhou.gjj.utils.Const;
import com.huzhou.gjj.utils.DateUtils;
import com.huzhou.gjj.utils.DbUtils;
import com.huzhou.gjj.utils.JsonAnalysis;
import com.huzhou.gjj.utils.PhoneUtils;
import com.huzhou.gjj.utils.ToastUtils;
import com.huzhou.gjj.viewUtils.ClearWriteEditText;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.huzhou.gjj.utils.Const.NOINTENT_STR;
import static com.huzhou.gjj.utils.Const.SERVICE_REQUEST_URL;
import static com.huzhou.gjj.utils.DbUtils.getUserData;


@ContentView(R.layout.activity_dotqueue)
public class DotQueueActivity extends BaseAppCompatActivity implements ISimpleDialogListener {

    @ViewInject(R.id.yy_spinner)
    private Spinner yy_spinner;
    @ViewInject(R.id.yw_spinner)
    private Spinner yw_spinner;
    @ViewInject(R.id.start_date)
    private TextView start_date;
    @ViewInject(R.id.yw_time)
    private Spinner yw_time;
    @ViewInject(R.id.yw_card)
    private TextView yw_card;
    @ViewInject(R.id.yw_phone)
    private ClearWriteEditText yw_phone;
    @ViewInject(R.id.ll)
    private LinearLayout ll;
    @ViewInject(R.id.detail_find)
    private Button detail_find;

    private DatePickerDialog dialog_start;
    private MyProgressDialog pro_dialog;

    private List<Distribution> list_di = new ArrayList<>();
    private List<String> list = new ArrayList<>();
    private List<String> list_type = new ArrayList<>();
    private List<DistributionType> list_ditype = new ArrayList<>();

    private String netNumber = null;
    private String start = null;
    private String yw_phone_str, card;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initview();
        getImformation();
    }

    private void initview() {
        card = DbUtils.getUserData().getCertNo();
        yw_card.setText(card);
        Calendar c = Calendar.getInstance();
        dialog_start = new DatePickerDialog(this, AlertDialog.THEME_HOLO_LIGHT,
                new DatePickerDialog.OnDateSetListener() {
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
                        start_date.setText(year + "年" + monthOfYear + "月"
                                + dayOfMonth + "日");
                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH),
                c.get(Calendar.DAY_OF_MONTH));

        //初始化dialog
        pro_dialog = LoadingDialog.createLoadingDialog(this, "加载中...");
        pro_dialog.setActivity(this);
        pro_dialog.show();
        yy_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getType((String) yy_spinner.getSelectedItem());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void getImformation() {
        JSONObject obj = new JSONObject();
        String jsonData = null;
        try {
            jsonData = JsonAnalysis.putJsonBody(obj, "2006").toString();
        } catch (JSONException ignored) {
            pro_dialog.dismiss();
        }
        RequestParams params = new RequestParams(SERVICE_REQUEST_URL);
        params.addBodyParameter("code", "2006");// 添加到请求body体的参数, 只有POST, PUT, PATCH, DELETE请求支持.
        params.addBodyParameter("json", jsonData);
        params.setCancelFast(true);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    String msg = JsonAnalysis.getMsg(result);
                    if (!"交易成功".equals(msg)) {
                        ToastUtils.showShort(x.app(), msg);
                        return;
                    }
                    String body = JsonAnalysis.getJsonBody(result);
                    JSONArray array = new JSONObject(body).getJSONArray("bmxxList");
                    if (array.length() == 0) {
                        ToastUtils.showShort(x.app(), Const.NULL_STR);
                        return;
                    }
                    Distribution distribution;
                    for (int j = 0; j < array.length(); j++) {
                        distribution = new Gson().fromJson(array.get(j).toString(), Distribution.class);
                        list.add(distribution.getDepName());
                        list_di.add(distribution);
                    }
                    SpinnerAdapter2 adapter = new SpinnerAdapter2(x.app(), list);
                    yy_spinner.setAdapter(adapter);
                } catch (Exception e) {
                    ToastUtils.showShort(x.app(), NOINTENT_STR);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                ToastUtils.showShort(x.app(), NOINTENT_STR);
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

    private void getType(final String string) {
        pro_dialog.show();
        for (int i = 0; i < list_di.size(); i++) {
            if (string.equals(list_di.get(i).getDepName())) {
                netNumber = list_di.get(i).getId();
                break;
            }
        }
        JSONObject obj = new JSONObject();
        String jsonData = null;
        try {
            obj.put("NetNumber", netNumber);
            jsonData = JsonAnalysis.putJsonBody(obj, "2093").toString();
        } catch (JSONException ignored) {
            pro_dialog.dismiss();
        }
        RequestParams params = new RequestParams(SERVICE_REQUEST_URL);
        params.addBodyParameter("code", "2093");// 添加到请求body体的参数, 只有POST, PUT, PATCH, DELETE请求支持.
        params.addBodyParameter("json", jsonData);
        params.setCancelFast(true);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {

                try {
                    String msg = JsonAnalysis.getMsg(result);
                    if (!"交易成功".equals(msg)) {
                        ToastUtils.showShort(x.app(), msg);
                        yw_spinner.setAdapter(null);
                        yw_time.setAdapter(null);
                        return;
                    }
                    String body = JsonAnalysis.getJsonBody(result);

                    if (!"1".equals(new JSONObject(body).getString("IsOpen"))) {
                        detail_find.setFocusable(false);
                        return;
                    }
                    String work_time_morning = new JSONObject(body).getString("Morning");
                    String work_time_afternoon = new JSONObject(body).getString("Afternoon");
                    List<String> list_str = DateUtils.StringDuan(work_time_morning);
                    list_str.addAll(DateUtils.StringDuan(work_time_afternoon));
                    SpinnerAdapter2 adapter = new SpinnerAdapter2(x.app(), list_str);
                    yw_time.setAdapter(adapter);

                    String body_str = new JSONObject(body).getString("JobList");
                    JSONArray array = new JSONArray(body_str);
                    if (array.length() == 0) {
                        ToastUtils.showShort(x.app(), Const.NULL_STR);
                        return;
                    }
                    list_type.clear();
                    list_ditype.clear();
                    DistributionType distribution;
                    for (int j = 0; j < array.length(); j++) {
                        distribution = new Gson().fromJson(array.get(j).toString(), DistributionType.class);
                        list_type.add(distribution.getJobName());
                        list_ditype.add(distribution);
                    }
                    SpinnerAdapter2 adapter1 = new SpinnerAdapter2(x.app(), list_type);
                    yw_spinner.setAdapter(adapter1);

                } catch (Exception e) {
                    ToastUtils.showShort(x.app(), NOINTENT_STR);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                ToastUtils.showShort(x.app(), NOINTENT_STR);
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

    @Event(value = {R.id.detail_find, R.id.start_date}, type = View.OnClickListener.class)
    private void getEvent(View v) {
        switch (v.getId()) {
            case R.id.detail_find:
                Date date_start = DateUtils.strToDate(start);
                yw_phone_str = yw_phone.getText().toString().trim();
                if (TextUtils.isEmpty((String) yw_spinner.getSelectedItem())) {
                    ToastUtils.showShort(x.app(), "请重新选择预约营业厅！");
                } else if (TextUtils.isEmpty(yw_phone_str)) {
                    yw_phone.setError("不能为空！");
                    yw_phone.requestFocus();
                } else if ("请选择预约日期".equals(start_date.getText().toString().trim())) {
                    ToastUtils.showShort(x.app(), "请选择预约日期！");
                    dialog_start.show();
                } else if (date_start.before(DateUtils.getCurrentTime())) {
                    ToastUtils.showShort(x.app(), "预约日期必须超过当前日期");
                } else if (!PhoneUtils.isMobile(yw_phone_str)) {
                    yw_phone.setError("手机号格式错误！");
                    yw_phone.requestFocus();
                } else {
                    pro_dialog.show();
                    initDatas();
                }
                break;
            case R.id.start_date:
                dialog_start.show();
                break;
        }
    }

    private void initDatas() {
        JSONObject obj = new JSONObject();
        String jsonData = null;
        try {
            String jobtype = null;
            String s = (String) yw_spinner.getSelectedItem();
            for (int i = 0; i < list_ditype.size(); i++) {
                if (s.equals(list_ditype.get(i).getJobName())) {
                    jobtype = list_ditype.get(i).getJobType();
                }
            }
            obj.put("NetNumber", netNumber);
            obj.put("JobType", jobtype);
            obj.put("Ymd", start);
            obj.put("Times", yw_time.getSelectedItem());
            obj.put("CodeStr", card);
            obj.put("Phone", yw_phone_str);


            JSONObject obj3 = new JSONObject();
            obj3.put("TrsCode", "2092");
            obj3.put("ChanCode", "01");
            obj3.put("ReqTrsBank", getUserData().getOrgNo());
            obj3.put("ReqTrsCent", getUserData().getCenterCode());
            obj3.put("ReqTrsTell", getUserData().getCustCode());
            jsonData = JsonAnalysis.getJsonData(obj, obj3);
//            jsonData = JsonAnalysis.putJsonBody(obj, "2092").toString();
        } catch (JSONException ignored) {
        }
        RequestParams params = new RequestParams(SERVICE_REQUEST_URL);
        params.addBodyParameter("code", "2092");// 添加到请求body体的参数, 只有POST, PUT, PATCH, DELETE请求支持.
        params.addBodyParameter("json", jsonData);
        params.setCancelFast(true);
        x.http().post(params, new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        pro_dialog.dismiss();
                        try {
                            String msg = JsonAnalysis.getMsg(result);
                            if (!"交易成功".equals(msg)) {
                                ToastUtils.showShort(x.app(), msg);
                                return;
                            }
                            String body = JsonAnalysis.getJsonBody(result);

                            String yynumber_str = new JSONObject(body).getString("YYNumber");
                            String TransDate = new JSONObject(body).getString("TransDate");
                            String EffectTime = new JSONObject(body).getString("EffectTime");
                            String address = (String) yy_spinner.getSelectedItem();
                            SimpleDialogFragment.createBuilder(DotQueueActivity.this, getSupportFragmentManager())
                                    .setMessage("预约码为:" + yynumber_str + "\n请您于" + TransDate + "  " + EffectTime + "到" + address + "取号\n"
                                            + "请您务必于预约当天营业期间内至营业厅取票"
                                    )
                                    .setPositiveButtonText("确定")
                                    .setRequestCode(1)
                                    .show();

                        } catch (Exception e) {
                            ToastUtils.showShort(x.app(), NOINTENT_STR);
                        }

                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {
                        ToastUtils.showShort(x.app(), NOINTENT_STR);
                        pro_dialog.dismiss();

                    }

                    @Override
                    public void onCancelled(CancelledException cex) {
                    }

                    @Override
                    public void onFinished() {
                    }
                }

        );
    }

    @Override
    public void onNegativeButtonClicked(int requestCode) {

    }

    @Override
    public void onNeutralButtonClicked(int requestCode) {

    }

    @Override
    public void onPositiveButtonClicked(int requestCode) {
        if (requestCode == 1) {
            finish();
        }
    }
}
