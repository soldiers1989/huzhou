package com.huzhou.gjj.acitivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;

import com.huzhou.gjj.R;
import com.huzhou.gjj.utils.JsonAnalysis;
import com.huzhou.gjj.utils.Md5Utils;
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

import static com.huzhou.gjj.acitivity.PhoneChangeActivity.TIMER;
import static com.huzhou.gjj.utils.CodeUtils.sendCode;
import static com.huzhou.gjj.utils.Const.NOINTENT_STR;
import static com.huzhou.gjj.utils.Const.SERVICE_REQUEST_URL;


@ContentView(R.layout.activity_forget)
public class ForgetActivity extends BaseAppCompatActivity {


    @ViewInject(R.id.regis_id)
    private ClearWriteEditText regis_id;

    @ViewInject(R.id.regiscard)
    private ClearWriteEditText regiscard;

    @ViewInject(R.id.password)
    private ClearWriteEditText password;

    @ViewInject(R.id.re_pass)
    private ClearWriteEditText re_pass;

    @ViewInject(R.id.findpass)
    private ClearWriteEditText findpass;

    @ViewInject(R.id.regisphone)
    private ClearWriteEditText regisphone;

    @ViewInject(R.id.phone_code)
    private ClearWriteEditText phone_code;

    @ViewInject(R.id.detail_find)
    private Button detail_find;

    @ViewInject(R.id.code_get)
    private Button code_get;

    @ViewInject(R.id.scroll)
    private ScrollView scroll;

    String regis_id_string;
    String regiscard_string;
    String password_string;
    String findpass_string = "******";
    String regisphone_string;
    String phone_code_string;
    String re_pass_string;

    private MyProgressDialog pro_dialog;
    private int time = 80;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case TIMER:
                    if (time == 0) {
                        code_get.setText("重新获取");
                        time = 80;
                        handler.removeCallbacks(runnable);
                        return;
                    }
                    code_get.setText("重新发送(" + time + ")");
                    time--;
                    break;
            }
            super.handleMessage(msg);
        }
    };
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            handler.sendEmptyMessage(TIMER);
            handler.postDelayed(this, 1000);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pro_dialog = LoadingDialog.createLoadingDialog(this, "加载中...");
        pro_dialog.setActivity(this);
    }

    private void initView() {
        regis_id_string = regis_id.getText().toString().trim();
        regiscard_string = regiscard.getText().toString().trim();
        password_string = password.getText().toString().trim();
        re_pass_string = re_pass.getText().toString().trim();
        if (regiscard_string.length() > 7) {
            if (regiscard_string.contains("x") || regiscard_string.contains("X"))
                findpass.setText(regiscard_string.substring(regiscard_string.length() - 6, regiscard_string.length() - 1) + "*");
            else
                findpass.setText(regiscard_string.substring(regiscard_string.length() - 6, regiscard_string.length()));
        }
        findpass_string = findpass.getText().toString().trim();
        regisphone_string = regisphone.getText().toString().trim();
        phone_code_string = phone_code.getText().toString().trim();

    }

    @Event(value = {R.id.detail_find, R.id.code_get}, type = View.OnClickListener.class)
    private void click(View view) {
        Spanned ssbuilder = Html.fromHtml("<font color=#E10979>不能为空~~！</font>");
        initView();
        if (TextUtils.isEmpty(regis_id_string)) {
            regis_id.setError(ssbuilder);
            regis_id.requestFocus();
            return;
        } else if (TextUtils.isEmpty(regiscard_string)) {
            regiscard.setError(ssbuilder);
            regiscard.requestFocus();
            return;
        } else if (TextUtils.isEmpty(password_string)) {
            password.setError(ssbuilder);
            password.requestFocus();
            return;
        } else if (TextUtils.isEmpty(re_pass_string)) {
            re_pass.setError(ssbuilder);
            re_pass.requestFocus();
            return;
        }
//        else if (TextUtils.isEmpty(findpass_string)) {
//            findpass.setError(ssbuilder);
//            findpass.requestFocus();
//            return;
//        }

        else if (TextUtils.isEmpty(regisphone_string)) {
            regisphone.setError(ssbuilder);
            regisphone.requestFocus();
            return;
        } else if (!PhoneUtils.isIDCard(regiscard_string)) {
            regiscard.setError("身份证号格式错误！");
            regiscard.requestFocus();
            return;
        } else if (!PhoneUtils.isMobile(regisphone_string)) {
            regisphone.setError("手机号格式错误！");
            regisphone.requestFocus();
            return;
        } else if (!PhoneUtils.isPassword(password_string)) {
            password.setError("密码格式错误！");
            password.requestFocus();
            return;
        } else if (!password_string.equals(re_pass_string)) {
            password.setError("密码不一致！");
            re_pass.setError("密码不一致！");
            password.requestFocus();
            re_pass.requestFocus();
            return;
        }
//        else if (findpass_string.length() != 6) {
//            findpass.setError("12329查询密码只能为6位数字！");
//            findpass.requestFocus();
//            return;
//        }
        switch (view.getId()) {
            case R.id.detail_find:
                if (TextUtils.isEmpty(phone_code_string)) {
                    phone_code.setError(ssbuilder);
                    phone_code.requestFocus();
                } else {
                    pro_dialog.show();
                    find();
                }
                break;
            case R.id.code_get:
                if (time == 80) {
                    handler.postDelayed(runnable, 1000);
                    CheckIsExists();
                }
                break;

        }
    }

    private String custCode = null;


    private void CheckIsExists() {
        JSONObject obj = new JSONObject();
        String jsonData = null;
        try {
            obj.put("personAcctNo", regis_id_string);
            obj.put("certType", "01");
            obj.put("certNo", regiscard_string);
            obj.put("phone", regisphone_string);
            jsonData = JsonAnalysis.putJsonBody(obj, "1007").toString();
        } catch (JSONException ignored) {
        }
        RequestParams params = new RequestParams(SERVICE_REQUEST_URL);
        params.addBodyParameter("code", "1007");// 添加到请求body体的参数, 只有POST, PUT, PATCH, DELETE请求支持.
        params.addBodyParameter("json", jsonData);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    String msg = JsonAnalysis.getMsg(result);
                    if (!"交易成功".equals(msg)) {
                        pro_dialog.dismiss();
                        ToastUtils.showShort(x.app(), msg);
                        return;
                    }
                    String body = JsonAnalysis.getJsonBody(result);
                    if ("true".equals(new JSONObject(body).getString("flag"))) {
                        custCode = new JSONObject(body).getString("custCode");
                        sendCode(custCode, regisphone_string);
                    } else
                        ToastUtils.showShort(x.app(), "用户不存在");
                } catch (Exception e) {
                    pro_dialog.dismiss();
                    ToastUtils.showShort(x.app(), "用户不存在");
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
            }
        });
    }

    private void find() {
        JSONObject obj = new JSONObject();
        String jsonData = null;
        try {
            obj.put("custCode", custCode);
            obj.put("newPassWord", Md5Utils.md5(password_string));
            obj.put("phone", regisphone_string);
            obj.put("queryPassWord", Md5Utils.md5(findpass_string));
            obj.put("phoneCode", phone_code_string);
            jsonData = JsonAnalysis.putJsonBody(obj, "1003").toString();
        } catch (JSONException ignored) {
        }
        RequestParams params = new RequestParams(SERVICE_REQUEST_URL);
        params.addBodyParameter("code", "1003");// 添加到请求body体的参数, 只有POST, PUT, PATCH, DELETE请求支持.
        params.addBodyParameter("json", jsonData);
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
                    ToastUtils.showShort(x.app(), msg);
                    finish();
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
        });


    }
}
