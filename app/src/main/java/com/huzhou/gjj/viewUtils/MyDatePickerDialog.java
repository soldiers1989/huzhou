package com.huzhou.gjj.viewUtils;

import android.app.DatePickerDialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Administrator on 2016/12/23.
 */

public class MyDatePickerDialog extends DatePickerDialog {
    public MyDatePickerDialog(Context context,int theme, OnDateSetListener callBack, int year, int monthOfYear, int dayOfMonth) {
        super(context, theme,callBack, year, monthOfYear, dayOfMonth);
        ((ViewGroup) ((ViewGroup) this.getDatePicker().getChildAt(0)).getChildAt(0)).getChildAt(2).setVisibility(View.GONE);
    }
}
