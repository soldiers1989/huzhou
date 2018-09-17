package com.huzhou.gjj.acitivity;

import java.util.Calendar;

import com.huzhou.gjj.R;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

@ContentView(R.layout.activity_consult)
public class ConsultActivity extends BaseAppCompatActivity {
    private DatePickerDialog dialog;
    @ViewInject(R.id.time)
    private TextView start_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        Calendar c = Calendar.getInstance();
        start_date.setText("请选择开始日期");

        dialog = new DatePickerDialog(this,
                new OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        start_date.setText(year + "年" + (monthOfYear + 1) + "月"
                                + dayOfMonth + "日");
                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH),
                c.get(Calendar.DAY_OF_MONTH));
    }

    //等同于@Event(value={R.id.btn_get,R.id.btn_post},type=View.OnClickListener.class)
    @Event(value = {R.id.detail_find, R.id.time})
    private void getEvent(View view) {
        switch (view.getId()) {
            case R.id.detail_find:
//                startActivity(new Intent(this, demo.class));
                break;
            case R.id.time:
                dialog.show();
                break;
        }

    }
}
