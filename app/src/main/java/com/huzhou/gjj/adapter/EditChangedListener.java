package com.huzhou.gjj.adapter;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;


public class EditChangedListener implements TextWatcher {
    private EditText text;

    public EditChangedListener(EditText text) {
        this.text = text;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        String sb = s.toString();
        if (sb.startsWith(".")) {
            text.setError("格式错误！");
            text.requestFocus();
            text.setText("");
        } else if (sb.startsWith("0") && sb.length() > 1) {
            if (!sb.startsWith("0.")) {
                text.setError("格式错误！");
                text.requestFocus();
                text.setText("");
            }
        }

    }

    @Override
    public void afterTextChanged(Editable s) {
//        限定小数点后两位
        String temp = s.toString();
        int posDot = temp.indexOf(".");
        if (posDot <= 0) return;
        if (temp.length() - posDot - 1 > 2) {
            s.delete(posDot + 3, posDot + 4);
        }
    }
}
