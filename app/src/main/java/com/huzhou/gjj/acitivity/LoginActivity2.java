package com.huzhou.gjj.acitivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.antgroup.zmxy.openplatform.api.ZhimaApiException;
import com.antgroup.zmxy.openplatform.api.request.ZhimaCustomerCertificationCertifyRequest;
import com.antgroup.zmxy.openplatform.api.request.ZhimaCustomerCertificationInitializeRequest;
import com.huzhou.gjj.R;
import com.huzhou.gjj.face.DefaultZhimaClient2;
import com.huzhou.gjj.utils.PhoneUtils;
import com.huzhou.gjj.utils.SharedPreferencesUtil;
import com.huzhou.gjj.utils.ToastUtils;
import com.huzhou.gjj.viewUtils.ClearWriteEditText;
import com.huzhou.gjj.viewUtils.LoadingDialog;
import com.huzhou.gjj.viewUtils.MyProgressDialog;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


@ContentView(R.layout.activity_login2)
public class LoginActivity2 extends BaseAppCompatActivity {


    @ViewInject(R.id.username)
    private ClearWriteEditText username;


    @ViewInject(R.id.password)
    private ClearWriteEditText password;


    private MyProgressDialog pro_dialog;

    private SharedPreferencesUtil utils;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        utils = new SharedPreferencesUtil(this);
        pro_dialog = LoadingDialog.createLoadingDialog(this, "加载中...");
        pro_dialog.setActivity(this);


        if (!TextUtils.isEmpty(name_str) || !TextUtils.isEmpty(card_str)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity2.this);
            builder.setTitle("登陆").setMessage("是否使用人脸识别进行登陆认证？").setCancelable(false);
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    faceLogin();
                }
            });
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    name_str = null;
                    card_str = null;
                    dialog.dismiss();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }
    }


    @Override
    protected void onDestroy() {
        if (pro_dialog.isShowing())
            pro_dialog.dismiss();
        super.onDestroy();
    }

    @Event(value = {R.id.sign}, type = View.OnClickListener.class)
    private void signEvent(View view) {
        switch (view.getId()) {
            case R.id.sign:
                name_str = username.getText().toString().trim();
                card_str = password.getText().toString().trim();
                if (TextUtils.isEmpty(name_str) || TextUtils.isEmpty(card_str))
                    ToastUtils.showShort(LoginActivity2.this, "请检查输入");
                else if (!PhoneUtils.isIDCard(card_str))
                    ToastUtils.showShort(LoginActivity2.this, "身份证格式错误");
                else {
                    utils.save("name_str", name_str);
                    utils.save("card_str", card_str);
                    ToastUtils.showShort(LoginActivity2.this, "登陆成功");
                    Intent intent = new Intent(LoginActivity2.this, LoginActivity3.class);
                    startActivity(intent);
                    finish();
                }
                break;
        }
    }


    //人脸识别登陆
    private String platform = "zmop";
    private String biz_no;
    private DefaultZhimaClient2 client;
    //    姓名    //    身份证号码

    private String name_str, card_str;


    private void faceLogin() {
        initClient();//      初始化DefaultZhimaClient
        initZhima();//       认证初始化并获取返回值 biz_no
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

    private void initZhima() {
        pro_dialog.show();
        final ZhimaCustomerCertificationInitializeRequest request = new ZhimaCustomerCertificationInitializeRequest();
//        request.setChannel("apppc");
        request.setPlatform(platform);
//       需每次保持唯一
        String transaction_id = initTransactionId();
        request.setTransactionId(transaction_id);// 必要参数
        String product_code = "w1010100000000002978";
        request.setProductCode(product_code);// 必要参数
        String biz_code = "FACE";
        request.setBizCode(biz_code);// 必要参数
//        姓名+身份证号码
        String identity_param = "{\"identity_type\":\"CERT_INFO\",\"cert_type\":\"IDENTITY_CARD\",\"cert_name\":\"" + name_str
                + "\",\"cert_no\":\"" + card_str + "\"}";
        request.setIdentityParam(identity_param);// 必要参数
//        request.setMerchantConfig("{\"need_user_authorization\":\"false\"}");
        String ext_biz_param = "{}";
        request.setExtBizParam(ext_biz_param);// 必要参数
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final String response = client
                            .execute2(request);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (TextUtils.isEmpty(response)) {
                                Toast.makeText(LoginActivity2.this, "biz_no获取失败！", Toast.LENGTH_SHORT).show();
                                pro_dialog.dismiss();
                                return;
                            }
                            try {
                                JSONObject obj = new JSONObject(response);
                                boolean issuccess = obj.getBoolean("success");
                                if (!issuccess) {
                                    Toast.makeText(LoginActivity2.this, obj.getString("error_message"), Toast.LENGTH_SHORT).show();
                                    pro_dialog.dismiss();
                                } else {
                                    if (response.contains("biz_no")) {
                                        biz_no = (new JSONObject(response)).getString("biz_no");
                                        utils.save("biz_no", biz_no);
                                        // 2. 生成认证请求 URL
                                        initRUrl();
                                    }
                                }
                            } catch (JSONException e) {
                                Toast.makeText(LoginActivity2.this, "biz_no获取失败！", Toast.LENGTH_SHORT).show();
                                pro_dialog.dismiss();
                            }
                        }
                    });
                } catch (ZhimaApiException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (pro_dialog.isShowing())
                                pro_dialog.dismiss();
                            Toast.makeText(LoginActivity2.this, "biz_no获取失败！", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();
    }

    private String initTransactionId() {
//        前面几位字符是商户自定义的简称,中间可以使用一段日期,结尾可以使用一个序列
        String name = "HZGJJ";
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String time = formatter.format(curDate);
        String sj = "";
        java.util.Random random = new java.util.Random();// 定义随机类
        for (int i = 0; i < 5; i++) {
            sj += random.nextInt(5) + "";
        }
        return name + time + sj;
    }

    private void initRUrl() {
        ZhimaCustomerCertificationCertifyRequest request = new ZhimaCustomerCertificationCertifyRequest();
        request.setPlatform(platform);
        request.setBizNo(biz_no);// 必要参数
        // 设置回调地址,必填. 如果需要直接在支付宝APP里面打开回调地址使用alipay协议
        // alipay://www.taobao.com 或者 alipays://www.taobao.com,分别对应http和https请求
        String return_url = "cundong://splash";
        request.setReturnUrl(return_url);// 必要参数
        try {
            String url = client.generatePageRedirectInvokeUrl(request);
            doVerify(url);
            System.out.println("generateCertifyUrl url:" + url);
        } catch (ZhimaApiException e) {
            pro_dialog.dismiss();
        }
    }


    @Override
    protected void onStop() {
        if (pro_dialog.isShowing())
            pro_dialog.dismiss();
        super.onStop();
    }

    /**
     * 启动支付宝进行认证
     *
     * @param url 开放平台返回的URL
     */
    private void doVerify(String url) {
        if (hasApplication()) {
            Intent action = new Intent(Intent.ACTION_VIEW);
            String builder = "alipays://platformapi/startapp?appId=20000067&url=" +
                    URLEncoder.encode(url);
            // 这里使用固定appid 20000067
            action.setData(Uri.parse(builder));
            startActivity(action);
//            open.loadUrl(url);
        } else {
            // 处理没有安装支付宝的情况
            new AlertDialog.Builder(this)
                    .setMessage("是否下载并安装支付宝完成认证?")
                    .setPositiveButton("好的", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent action = new Intent(Intent.ACTION_VIEW);
                            action.setData(Uri.parse("https://m.alipay.com"));
                            startActivity(action);
                        }
                    }).setNegativeButton("算了", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).show();
        }
    }

    /**
     * 判断是否安装了支付宝
     *
     * @return true 为已经安装
     */
    private boolean hasApplication() {
        PackageManager manager = getPackageManager();
        Intent action = new Intent(Intent.ACTION_VIEW);
        action.setData(Uri.parse("alipays://"));
        List list = manager.queryIntentActivities(action, PackageManager.GET_RESOLVED_FILTER);
        return list != null && list.size() > 0;
    }

}
