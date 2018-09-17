package com.huzhou.gjj.acitivity;

import com.google.gson.Gson;
import com.huzhou.gjj.R;
import com.huzhou.gjj.bean.Info;
import com.huzhou.gjj.bean.Number;
import com.huzhou.gjj.utils.CodeUtils;
import com.huzhou.gjj.utils.JsonAnalysis;
import com.huzhou.gjj.utils.PhoneUtils;
import com.huzhou.gjj.utils.ToastUtils;
import com.huzhou.gjj.viewUtils.ClearWriteEditText;
import com.huzhou.gjj.viewUtils.LoadingDialog;
import com.huzhou.gjj.viewUtils.MyProgressDialog;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
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
import static com.huzhou.gjj.utils.Const.SERVICE_REQUEST_URL;
import static com.huzhou.gjj.utils.Const.SERVICE_RHIDAIN_URL;
import static com.huzhou.gjj.utils.DbUtils.getUserData;


@ContentView(R.layout.activity_phone_change)
public class PhoneChangeActivity extends BaseAppCompatActivity {
    protected static final int TIMER = 1;/* 计时 */
    @ViewInject(R.id.code_get)
    private Button code_get;

    @ViewInject(R.id.new_phone)
    private ClearWriteEditText new_phone;
    @ViewInject(R.id.code)
    private ClearWriteEditText code;
    @ViewInject(R.id.old_phone)
    private TextView old_phone;

    @ViewInject(R.id.name)
    private TextView name;
    @ViewInject(R.id.type)
    private TextView type;

    @ViewInject(R.id.num)
    private TextView num;

    @ViewInject(R.id.zy)
    private TextView zy;
    @ViewInject(R.id.ll)
    private LinearLayout ll;
    private MyProgressDialog pro_dialog;

    private Info info = null;
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
    private String code_str;
    private String new_phone_str;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        getdata();
    }

    private void getdata() {
        JSONObject obj = new JSONObject();
        String jsonData = null;
        try {
            obj.put("grzh", getUserData().getPersonAcctNo());
            obj.put("khbh", getUserData().getCustCode());
            obj.put("zhlx ", "01");
            jsonData = JsonAnalysis.putJsonBody(obj, "2001").toString();
        } catch (JSONException ignored) {
        }
        RequestParams params = new RequestParams(SERVICE_REQUEST_URL);
        params.addBodyParameter("code", "2001");// 添加到请求body体的参数, 只有POST, PUT, PATCH, DELETE请求支持.
        params.addBodyParameter("json", jsonData);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {

                try {
                    String msg = JsonAnalysis.getMsg(result);
                    if (!"交易成功".equals(msg)) {
                        pro_dialog.dismiss();
                        ToastUtils.showLong(x.app(), msg);
                        return;
                    }
                    String body = JsonAnalysis.getJsonBody(result);
                    info = new Gson().fromJson(body, Info.class);
                    name.setText(info.getGrxm());
                    old_phone.setText(info.getSjhm());
                    num.setText(info.getZjhm());
                    getZhiDian();
                } catch (Exception ignored) {
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

    private void getZhiDian() {
        JSONArray array = new JSONArray();
        try {
            JSONObject data1 = new JSONObject();
            data1.put("dictType", "zjlx");
            JSONObject data2 = new JSONObject();
            data2.put("dictType", "zy");

            array.put(data1);
            array.put(data2);
        } catch (JSONException ignored) {
        }
        RequestParams params = new RequestParams(SERVICE_RHIDAIN_URL);
        params.addBodyParameter("typeArray", array.toString());
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    pro_dialog.dismiss();
                    final JSONArray array = new JSONArray(result);
                    if (array.length() == 0) return;
                    Number number;
                    String zjlx = null, zy_str = null;
                    for (int i = 0; i < array.length(); i++) {
                        number = new Gson().fromJson(array.getJSONObject(i).toString(), Number.class);
                        if ("zjlx".equals(number.getDictType()) && info.getZjlx().equals(number.getCode()))
                            zjlx = number.getName();
                        if (!TextUtils.isEmpty(info.getZhiye())) {
                            if ("zy".equals(number.getDictType()) && info.getZhiye().equals(number.getCode()))
                                zy_str = number.getName();
                        }
                    }
                    ll.setVisibility(View.VISIBLE);
                    if (!TextUtils.isEmpty(info.getZhiye()))
                        zy.setText(zy_str);
                    else zy.setText("无");
                    type.setText(zjlx);
                } catch (Exception ignored) {
                    if (TextUtils.isEmpty(info.getZhiye()))
                        zy.setText("无");
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


    private void initView() {
        pro_dialog = LoadingDialog.createLoadingDialog(this, "加载中...");
        pro_dialog.setActivity(this);
        pro_dialog.show();

    }

    @Event(value = {R.id.code_get, R.id.detail_find}, type = View.OnClickListener.class)
    private void getEvent(View view) {
        String old_phone_string = old_phone.getText().toString().trim();
        code_str = code.getText().toString();
        new_phone_str = new_phone.getText().toString();
        switch (view.getId()) {
            case R.id.code_get:
                if (time == 80) {
                    handler.postDelayed(runnable, 1000);
                    CodeUtils.sendCode(getUserData().getCustCode(), old_phone_string);
                }
                break;
            case R.id.detail_find:
                if (TextUtils.isEmpty(code_str)) {
                    code.setError("验证码不能为空！");
                    code.requestFocus();
                } else if (TextUtils.isEmpty(new_phone_str)) {
                    new_phone.setError("新手机号不能为空！");
                    new_phone.requestFocus();
                } else if (!PhoneUtils.isMobile(new_phone_str)) {
                    new_phone.setError("新手机号格式错误！");
                    new_phone.requestFocus();
                } else {
                    pro_dialog.show();
                    update();
                }
                break;
        }

    }


    private void update() {
        String obj7 = null;
        try {
            JSONObject obj1 = new JSONObject();
            obj1.put("zhiye", info.getZhiye());
            obj1.put("khbh", info.getKhbh());
            obj1.put("zjhm", info.getZjhm());
            obj1.put("sjhm", new_phone_str);

            JSONObject obj2 = new JSONObject();
            obj2.put("grzh", getUserData().getPersonAcctNo());
            obj2.put("ZJHM", info.getZjhm());
            obj2.put("KHBH", info.getKhbh());
            obj2.put("CSRQ", info.getCsrq());
            obj2.put("grxm", info.getGrxm());
            obj2.put("xhj", info.getXhj());
            obj2.put("ZJLX", "01");
            obj2.put("zhiye", info.getZhiye());
            obj2.put("yzm", code_str);
            obj2.put("sjhm", new_phone_str);
            obj2.put("XINGBIE", info.getXingbie());


            JSONObject obj3 = new JSONObject();
            obj3.put("old_grxx", obj1);
            obj3.put("new_grxx", obj2);


            JSONObject obj = new JSONObject();
            obj.put("TrsCode", "4005");
            obj.put("TrsChildCode", "40051");
            obj.put("ChanCode", "02");

            obj.put("ReqTrsBank", getUserData().getOrgNo());
            obj.put("ReqTrsCent", getUserData().getCenterCode());
            obj.put("ReqTrsTell", getUserData().getCustCode());

            obj7 = JsonAnalysis.getJsonData(obj3, obj);
        } catch (JSONException ignored) {
        }
        RequestParams params = new RequestParams(SERVICE_REQUEST_URL);
        params.addBodyParameter("code", "4005");// 添加到请求body体的参数, 只有POST, PUT, PATCH, DELETE请求支持.
        params.addBodyParameter("json", obj7);
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
                } catch (JSONException e) {
                    ToastUtils.showShort(x.app(), "修改手机号失败");
                }
                ToastUtils.showShort(x.app(), "修改手机号成功！");
                finish();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                pro_dialog.dismiss();
                ToastUtils.showShort(x.app(), "修改手机号失败");
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
