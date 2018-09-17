package com.huzhou.gjj.acitivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.widget.Toast;

import com.antgroup.zmxy.openplatform.api.ZhimaApiException;
import com.antgroup.zmxy.openplatform.api.request.ZhimaCustomerCertificationQueryRequest;
import com.huzhou.gjj.R;
import com.huzhou.gjj.face.DefaultZhimaClient2;
import com.huzhou.gjj.utils.SharedPreferencesUtil;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.ContentView;
import org.xutils.x;


@ContentView(R.layout.activity_loginjy)
public class LoginActivityjy extends Activity {

    private String biz_no;
    private DefaultZhimaClient2 client;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //完成视图注解框架的初始化
        x.view().inject(this);
        SharedPreferencesUtil utils = new SharedPreferencesUtil(this);
        biz_no = utils.read("biz_no");
        initClient();
        doFind();
    }

    private void initClient() {
//        联调阶段
        String request_url = "https://zmopenapi.zmxy.com.cn/sandbox.do";
//        上线阶段
//        String request_url = "https://zmopenapi.zmxy.com.cn/openapi.do";
//        芝麻信用分配
        String private_key = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAIjfNWUXqCNtlaBFpv4KrBTVQb4btX9Gi+UPuz7AMvwbSXmtIxV2awkz4Rg0Q3PtTkuAUpuW958u3uNxypcsCJD3o4qphgqSMVpe33bJ1bZaeL/h8WT9KNBiIxGsYQco44JbJkjrHTAI3Wt+RNhHyn+nKI9wCaeYPCY7ZipVpmL3AgMBAAECgYBn/BxWx1hIQjMQ5pnuGzGNSk9+HRMQtQoHZqI9FEwn2JtDw9QJtEOxZCa4+svcQQfguIcKCfHqj/NqHMNrglqmmJhKrut+ilUt8rqJCmvRSh8AdG7ZnqzxQYdTrrEEKgO932WqPOI98X2H+2cPzLfIOgxN7FhCDg+lVi4CBdfJ8QJBANosNf2b4f50AgAkstAKX1Hqqsb+k5L5reoF9e6eQufPXIpSrbME3cETyRMMLKX+YfhHpv66fpfg0X4JiHZIpdsCQQCgmmPNWs2nbvHY3Eo3xuYs/KJRIltgYaAd9+CCjpCTbU21afqyGkK7zibHfLADh+m5GOkOZ5IFn5Alhg1NQNgVAkBTX/PeEDVEPWcKUPv4nw4gSvKqi10wHLSGq3J5lwdweQEfZ0s0D5cDEyGTYuKpKNadwBwkWnbIacUFSnVY5phjAkAt4Ix73+F5X77kROFKl52u4if350mU+a5EgUd35AO2qXWWSgTcFZZUkaoQODULfSqtvkjs3Xcf9hm2LlnkZI6VAkEAl8+0+a6Rjz119mxJXy9JWKTbWirhja+ijyndLVEAmxTYjWTuNKbRdTYe59ekRYhmMWkiKzggP+PKjXHCHV6eRQ==";
        String zhima_public_key = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCZe80REOhZVf4aAWrp460/KsdmVe2K9oDueGYkzDt5WYEZFE8n86GnVKsWw3gcY+k+ahepFOlSf3shmm7LFHME8MXfVuLop5fP5jDUDillYjmvSGviwgA9/H11fI7MJKtHCgJFtr7u7yx27IywCgWzZRzCWTNQoJq9+HZEKOxBwQIDAQAB";

        String app_id = "1004457";
        client = new DefaultZhimaClient2(
                request_url,
                app_id,
                private_key,
                zhima_public_key);
    }

    private void doFind() {
        final ZhimaCustomerCertificationQueryRequest req = new ZhimaCustomerCertificationQueryRequest();
        String platform = "zmop";
        req.setPlatform(platform);
        req.setBizNo(biz_no);// 必要参数
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final String response = client.execute3(req);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            if (TextUtils.isEmpty(response)) {
                                Toast.makeText(LoginActivityjy.this, "查询失败！", Toast.LENGTH_SHORT).show();
                                finish();
                                return;
                            }
                            try {
                                JSONObject obj = new JSONObject(response);
                                boolean success = obj.getBoolean("success");
                                if (!success) {
                                    Toast.makeText(LoginActivityjy.this, obj.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    String passed = obj.getString("passed");
                                    if ("false".equals(passed)) {
                                        Toast.makeText(LoginActivityjy.this, obj.getString("failed_reason"), Toast.LENGTH_SHORT).show();
                                        finish();
                                    } else {
//                                        人脸识别成功
                                        Intent intent = new Intent(LoginActivityjy.this, LoginActivity3.class);
                                        startActivity(intent);
                                        Toast.makeText(LoginActivityjy.this, "登陆成功！", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                }
                            } catch (JSONException e) {
                                Toast.makeText(LoginActivityjy.this, "查询失败！", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }
                    });
                } catch (ZhimaApiException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(LoginActivityjy.this, "查询失败！", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
                }
            }
        }).start();

    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return keyCode == KeyEvent.KEYCODE_BACK;
    }
}
