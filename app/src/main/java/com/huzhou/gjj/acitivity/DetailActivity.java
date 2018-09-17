package com.huzhou.gjj.acitivity;

import java.util.Calendar;
import java.util.Date;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import com.huzhou.gjj.R;
import com.huzhou.gjj.utils.DateUtils;
import com.huzhou.gjj.utils.DbUtils;
import com.huzhou.gjj.utils.ToastUtils;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import static com.huzhou.gjj.R.id.detail_find_btn;


@ContentView(R.layout.activity_detail)
public class DetailActivity extends BaseAppCompatActivity {
    private DatePickerDialog dialog_start;
    private DatePickerDialog dialog_end;

    @ViewInject(R.id.start_date_text)
    private TextView start_date;

    @ViewInject(R.id.user_id)
    private TextView user_id;

    @ViewInject(R.id.user_name)
    private TextView user_name;

    @ViewInject(R.id.end_date_text)
    private TextView end_date_text;

    @ViewInject(R.id.sjkd)
    private Spinner sjkd;

    private String start = null;
    private String end = null;
    private Calendar c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initSpinner();
    }

    private void initSpinner() {
        sjkd.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                int year_str = c.get(Calendar.YEAR);
                int mouth_str = c.get(Calendar.MONTH) + 1;
                int dat_str = c.get(Calendar.DAY_OF_MONTH);
                String dayOfMonth_str;
                String monthOfYear_str;
                if (dat_str < 10)
                    dayOfMonth_str = "0" + dat_str;
                else
                    dayOfMonth_str = "" + dat_str;

                if (mouth_str < 10)
                    monthOfYear_str = "0" + mouth_str;
                else
                    monthOfYear_str = "" + mouth_str;

                end = year_str + monthOfYear_str
                        + dayOfMonth_str;
                switch (position) {

                    case 0:
                        start = year_str + monthOfYear_str
                                + "01";
                        start_date.setText(year_str + "年" + mouth_str + "月"
                                + 1 + "日");
                        end_date_text.setText(year_str + "年" + mouth_str + "月"
                                + dat_str + "日");
                        break;
                    case 1:

                        Calendar cal = Calendar.getInstance();
                        cal.add(Calendar.MONTH, -3);
                        int year_str_cal = cal.get(Calendar.YEAR);
                        int mouth_str_cal = cal.get(Calendar.MONTH) + 1;
                        int dat_str_cal = cal.get(Calendar.DAY_OF_MONTH);

                        String dayOfMonth_str_cal;
                        String monthOfYear_str_cal;
                        if (dat_str_cal < 10)
                            dayOfMonth_str_cal = "0" + dat_str_cal;
                        else
                            dayOfMonth_str_cal = "" + dat_str_cal;

                        if (mouth_str_cal < 10)
                            monthOfYear_str_cal = "0" + mouth_str_cal;
                        else
                            monthOfYear_str_cal = "" + mouth_str_cal;

                        start = year_str_cal + monthOfYear_str_cal
                                + dayOfMonth_str_cal;

                        start_date.setText(year_str_cal + "年" + mouth_str_cal + "月"
                                + dat_str_cal + "日");

                        end_date_text.setText(year_str + "年" + mouth_str + "月"
                                + dat_str + "日");
                        break;
                    case 2:

                        Calendar cal2 = Calendar.getInstance();
                        cal2.add(Calendar.MONTH, -6);
                        int year_str_cal2 = cal2.get(Calendar.YEAR);
                        int mouth_str_cal2 = cal2.get(Calendar.MONTH) + 1;
                        int dat_str_cal2 = cal2.get(Calendar.DAY_OF_MONTH);
                        String dayOfMonth_str_cal2;
                        String monthOfYear_str_cal2;
                        if (dat_str_cal2 < 10)
                            dayOfMonth_str_cal2 = "0" + dat_str_cal2;
                        else
                            dayOfMonth_str_cal2 = "" + dat_str_cal2;

                        if (mouth_str_cal2 < 10)
                            monthOfYear_str_cal2 = "0" + mouth_str_cal2;
                        else
                            monthOfYear_str_cal2 = "" + mouth_str_cal2;

                        start = year_str_cal2 + monthOfYear_str_cal2
                                + dayOfMonth_str_cal2;

                        start_date.setText(year_str_cal2 + "年" + mouth_str_cal2 + "月"
                                + dat_str_cal2 + "日");
                        end_date_text.setText(year_str + "年" + mouth_str + "月"
                                + dat_str + "日");
                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void initView() {
        user_id.setText(DbUtils.getUserData().getPersonAcctNo());
        user_name.setText(DbUtils.getUserData().getCustName());
        c = Calendar.getInstance();
        start_date.setText("请选择开始日期");
        end_date_text.setText("请选择结束日期");
        dialog_start = new DatePickerDialog(this, AlertDialog.THEME_HOLO_LIGHT,
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
                        start_date.setText(year + "年" + monthOfYear + "月"
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

    @Event(value = {detail_find_btn, R.id.start_date_text, R.id.end_date_text}, type = View.OnClickListener.class)
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
                    Intent intent = new Intent(DetailActivity.this, DetailMoreActivity.class);
                    intent.putExtra("jzrqBegin", start);
                    intent.putExtra("jzrqEnd", end);
                    startActivity(intent);
                }
                break;
            case R.id.start_date_text:
                dialog_start.show();
                break;
            case R.id.end_date_text:
                dialog_end.show();
                break;
        }

    }
}
