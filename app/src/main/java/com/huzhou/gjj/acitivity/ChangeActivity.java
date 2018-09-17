package com.huzhou.gjj.acitivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.google.gson.Gson;
import com.huzhou.gjj.R;
import com.huzhou.gjj.adapter.SpinnerAdapter;
import com.huzhou.gjj.bean.Number;
import com.huzhou.gjj.utils.DateUtils;
import com.huzhou.gjj.utils.DbUtils;
import com.huzhou.gjj.utils.ToastUtils;
import com.huzhou.gjj.viewUtils.LoadingDialog;
import com.huzhou.gjj.viewUtils.MyProgressDialog;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

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
import static com.huzhou.gjj.utils.Const.SERVICE_RHIDAIN_URL;


@ContentView(R.layout.activity_change)

public class ChangeActivity extends BaseAppCompatActivity {

    private DatePickerDialog dialog;
    private DatePickerDialog dialog_end;

    @ViewInject(R.id.time)
    private TextView time;

    @ViewInject(R.id.end_date_text)
    private TextView end_date_text;

    @ViewInject(R.id.spinner)
    private Spinner spinner;

    @ViewInject(R.id.user_id)
    private TextView user_id;

    @ViewInject(R.id.user_name)
    private TextView user_name;

    @ViewInject(R.id.linear)
    private LinearLayout linear;

    private String start = null;
    private String end = null;
    private MyProgressDialog pro_dialog;
    private List<Number> list_str = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        getZhiDian();
    }

    private void getZhiDian() {
        //初始化dialog
        pro_dialog = LoadingDialog.createLoadingDialog(this, "加载中...");
        pro_dialog.setActivity(this);
        pro_dialog.show();
        JSONArray array = new JSONArray();
        try {
            JSONObject data1 = new JSONObject();
            data1.put("dictType", "cxlx");
            array.put(data1);
        } catch (JSONException ignored) {
            pro_dialog.dismiss();
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
                    for (int i = 0; i < array.length(); i++) {
                        number = new Gson().fromJson(array.getJSONObject(i).toString(), Number.class);
                        list_str.add(number);
                    }
                    SpinnerAdapter adapter1 = new SpinnerAdapter(x.app(),
                            list_str);
                    spinner.setAdapter(adapter1);
                    linear.setVisibility(View.VISIBLE);
                } catch (Exception ignored) {
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

    private void initView() {
        time.setText("请选择开始日期");
        end_date_text.setText("请选择结束日期");
        user_id.setText(DbUtils.getUserData().getPersonAcctNo());
        user_name.setText(DbUtils.getUserData().getCustName());
        Calendar c = Calendar.getInstance();
        dialog = new DatePickerDialog(this, AlertDialog.THEME_HOLO_LIGHT,
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
                        time.setText(year + "年" + monthOfYear + "月"
                                + dayOfMonth + "日");
                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH),
                c.get(Calendar.DAY_OF_MONTH));
        dialog_end = new DatePickerDialog(this, AlertDialog.THEME_HOLO_LIGHT,
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
                        end_date_text.setText(year + "年" + monthOfYear + "月"
                                + dayOfMonth + "日");
                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH),
                c.get(Calendar.DAY_OF_MONTH));
    }

    @Event(value = {R.id.detail_find, R.id.time, R.id.end_date_text}, type = View.OnClickListener.class)
    private void getEvent(View view) {
        switch (view.getId()) {
            case R.id.detail_find:
                Date date_start = DateUtils.strToDate(start);
                Date date_end = DateUtils.strToDate(end);
                if (TextUtils.isEmpty(start)) {
                    ToastUtils.showShort(x.app(), "请选择开始日期");
                    dialog.show();
                    return;
                } else if (TextUtils.isEmpty(end)) {
                    ToastUtils.showShort(x.app(), "请选择结束日期");
                    dialog_end.show();
                    return;
                } else if (date_start.after(DateUtils.getCurrentTime()) ||
                        date_end.after(DateUtils.getCurrentTime())) {
                    ToastUtils.showShort(x.app(), "不能超过当前日期");
                    return;
                } else if (date_start.after(date_end)) {
                    ToastUtils.showShort(x.app(), "起始日期不能超过结束日期");
                    return;
                } else if (DateUtils.CheckDate(date_start, date_end)) {
                    ToastUtils.showShort(x.app(), "与起始日期的间隔不能超过三年");
                    return;
                } else {
                    Intent intent = new Intent(x.app(), ChangeMoreActivity.class);
                    Number number = (Number) spinner.getSelectedItem();
                    String code = null;
                    for (int i = 0; i < list_str.size(); i++) {
                        if (number.getName().equals(list_str.get(i).getName())) {
                            code = list_str.get(i).getCode();
                            break;
                        }
                    }
                    intent.putExtra("jzrqBegin", start);
                    intent.putExtra("jzrqEnd", end);
                    intent.putExtra("spinner", code);
                    startActivity(intent);
                }
                break;
            case R.id.time:
                dialog.show();
                break;
            case R.id.end_date_text:
                dialog_end.show();
                break;
        }
    }

}
