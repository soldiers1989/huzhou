package com.huzhou.gjj.acitivity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Xml;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.antgroup.zmxy.openplatform.api.ZhimaApiException;
import com.antgroup.zmxy.openplatform.api.request.ZhimaCustomerCertificationCertifyRequest;
import com.antgroup.zmxy.openplatform.api.request.ZhimaCustomerCertificationInitializeRequest;
import com.antgroup.zmxy.openplatform.api.request.ZhimaCustomerCertificationQueryRequest;
import com.avast.android.dialogs.fragment.ListDialogFragment;
import com.avast.android.dialogs.iface.IListDialogListener;
import com.google.gson.Gson;
import com.huzhou.gjj.AppApplication;
import com.huzhou.gjj.R;
import com.huzhou.gjj.adapter.SpinnerAdapter;
import com.huzhou.gjj.bean.Number;
import com.huzhou.gjj.bean.User;
import com.huzhou.gjj.bean.XmlData;
import com.huzhou.gjj.face.DefaultZhimaClient2;
import com.huzhou.gjj.utils.Const;
import com.huzhou.gjj.utils.JsonAnalysis;
import com.huzhou.gjj.utils.Md5Utils;
import com.huzhou.gjj.utils.PhoneUtils;
import com.huzhou.gjj.utils.SharedPreferencesUtil;
import com.huzhou.gjj.utils.ToastUtils;
import com.huzhou.gjj.viewUtils.ClearWriteEditText;
import com.huzhou.gjj.viewUtils.LoadingDialog;
import com.huzhou.gjj.viewUtils.MyProgressDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xutils.DbManager;
import org.xutils.common.Callback;
import org.xutils.ex.DbException;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.huzhou.gjj.utils.Const.NOINTENT_STR;
import static com.huzhou.gjj.utils.Const.SERVICE_REQUEST_URL;
import static com.huzhou.gjj.utils.Const.SERVICE_RHIDAIN_URL;

@ContentView(R.layout.activity_login)
public class LoginActivity extends BaseAppCompatActivity implements IListDialogListener {

    @ViewInject(R.id.cuerrt_version)
    private TextView cuerrt_version;


    @ViewInject(R.id.username)
    private ClearWriteEditText username;

    @ViewInject(R.id.login_type)
    private Spinner login_type;

    @ViewInject(R.id.password)
    private ClearWriteEditText password;

    @ViewInject(R.id.usercard)
    private ClearWriteEditText usercard;

//    private AutoCompleteTextView usercard;

    @ViewInject(R.id.yzm)
    private ClearWriteEditText yzm;

    @ViewInject(R.id.scroll)
    private ScrollView scroll;

    @ViewInject(R.id.checkview)
    private com.huzhou.gjj.viewUtils.CheckView checkview;

    @ViewInject(R.id.relative)
    private RelativeLayout relative;

    @ViewInject(R.id.back)
    private ImageView back;

    private MyProgressDialog pro_dialog;

    @ViewInject(R.id.toolbar)
    private Toolbar toolbar;
    private SharedPreferencesUtil utils;
    private DbManager db;
    private List<Number> list_str = new ArrayList<>();
    private int REQUEST_CODE = 1;
    private String str_username;
    private String str_password;
    private String str_card;
    private String deviced_id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            initview();
            getZhiDian();
        } else {
            Toast.makeText(getApplicationContext(), "请给予app读取手机信息的权限", Toast.LENGTH_LONG).show();
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    initview();
                    getZhiDian();
                } else {
                    Toast.makeText(getApplicationContext(), "请给予app读取手机信息的权限", Toast.LENGTH_LONG).show();
                    onBackPressed();
                }
                break;
        }
    }

    private void showListDialog(List<String> list_user) {
        int[] checkedItems = {0};
        ListDialogFragment
                .createBuilder(this, getSupportFragmentManager())
                .setTitle("请选择登陆的公积金账号")
                .setCheckedItems(checkedItems)
                .setItems(list_user.toArray((new CharSequence[list_user.size()])))
                .setRequestCode(REQUEST_CODE)
                .setCancelableOnTouchOutside(false)
                .setChoiceMode(AbsListView.CHOICE_MODE_SINGLE)
                .show();
    }

    @Event(R.id.back)
    private void Back(View v) {
        onBackPressed();
    }

    private void getZhiDian() {
        //初始化dialog
        pro_dialog.show();
        JSONArray array = new JSONArray();
        try {
            JSONObject data1 = new JSONObject();
            data1.put("dictType", "zjlx");
            array.put(data1);
        } catch (JSONException ignored) {
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
                    login_type.setAdapter(adapter1);
                    relative.setVisibility(View.VISIBLE);
                } catch (Exception ignored) {
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
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

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.login, menu);
//        return true;
//    }

    @Override
    protected void onDestroy() {
//        mAppManager.addActivity(this);
        if (pro_dialog.isShowing())
            pro_dialog.dismiss();
        super.onDestroy();
    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        if (id == android.R.id.home) {
//            finish();
//            return true;
//        }
//        if (id == R.id.calendar) {
//            startActivity(new Intent(this, ForgetActivity.class));
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }

    private void initview() {
//        手机设备号
        deviced_id = PhoneUtils.getIMEI();
        cuerrt_version.setText("当前版本： V" + MainActivity.current_version);
        db = x.getDb(((AppApplication) x.app().getApplicationContext()).getDaoConfig());
        utils = new SharedPreferencesUtil(this);
//        增加
        String card = utils.read(Const.SFZ);
        if (!TextUtils.isEmpty(card)) {
            usercard.setText(card);
            usercard.clearFocus();
        }
//

        if (!TextUtils.isEmpty(utils.read(Const.GJJ_DATA))) {
            try {
                final List<User> list_user = db.findAll(User.class);
                List<String> list_number = new ArrayList<>();
                String user_str;
                for (int i = 0; i < list_user.size(); i++) {
                    user_str = list_user.get(i).getCertNo();
                    list_number.add(user_str + "     " + list_user.get(i).getCustName());
                }
                for (int i = 0; i < list_number.size(); i++)  //外循环是循环的次数
                {
                    for (int j = list_number.size() - 1; j > i; j--)  //内循环是 外循环一次比较的次数
                    {
                        if (list_number.get(i).equals(list_number.get(j))) {
                            list_number.remove(j);
                            list_user.remove(j);
                        }
                    }
                }
//                ArrayAdapter arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list_number);
//                usercard.setAdapter(arrayAdapter);
//                usercard.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
////                        username.setText(list_user.get(position).getPersonAcctNo());
//                        usercard.setText(list_user.get(position).getCertNo());
//                        password.setFocusable(true);
//                        password.setFocusableInTouchMode(true);
//                        password.requestFocus();
//                    }
//                });
            } catch (DbException ignored) {
            }
        }
        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setHomeButtonEnabled(true);
        pro_dialog = LoadingDialog.createLoadingDialog(this, "加载中...");
        pro_dialog.setActivity(this);
    }


    @Event(value = {R.id.sign, R.id.regis, R.id.checkview, R.id.forget, R.id.face_login}, type = View.OnClickListener.class)
    private void signEvent(View view) {
        switch (view.getId()) {
            case R.id.sign:
                String checkCode = checkview.getvCode();
                str_password = password.getText().toString().trim();
                str_card = usercard.getText().toString().trim();
                Number number = (Number) login_type.getSelectedItem();
                String str_yzm = yzm.getText().toString().trim();
                //                str_username = username.getText().toString().trim();
//                if (TextUtils.isEmpty(str_username)) {
//                    username.setError("公积金账号不能为空！");
//                    username.requestFocus();
//                } else
                if (TextUtils.isEmpty(str_card)) {
                    usercard.setError("用户证件号不能为空！");
                    usercard.requestFocus();
                } else if (TextUtils.isEmpty(str_password)) {
                    password.setError("密码不能为空！");
                    password.requestFocus();
                } else if (TextUtils.isEmpty(str_yzm)) {
                    yzm.setError("验证码不能为空！");
                    yzm.requestFocus();
                } else if (!PhoneUtils.isIDCard(str_card) && "二代身份证".equals(number != null ? number.getName() : null)) {
                    usercard.setError("用户证件号格式错误！");
                    usercard.requestFocus();
                } else if (!PhoneUtils.isPassword(str_password)) {
                    password.setError("密码格式错误！");
                    password.requestFocus();
                } else if (!str_yzm.equalsIgnoreCase(checkCode)) {
                    yzm.setError("验证码错误！");
                    yzm.requestFocus();
                    // 生成新的验证码
                    checkview.refreshCode();
                } else {
                    pro_dialog.show();
                    checkUserList();
                }
                break;
            case R.id.regis:
                startActivity(new Intent(x.app(), RegisterActivity.class));
                break;
            case R.id.checkview:
                //重新初始化验证码
                checkview.refreshCode();
                break;
            case R.id.forget:
                startActivity(new Intent(this, ForgetActivity.class));
                break;
            case R.id.face_login:
                if (TextUtils.isEmpty(utils.read(Const.GJJ_DATA))) {
                    ToastUtils.showShort(x.app(), "请先登陆！");
                } else
                    faceLogin();
                break;
        }

    }

    private void checkUserList() {
        JSONObject obj = new JSONObject();
        String jsonData = null;
        try {
            obj.put("zjhm", str_card);
            obj.put("zjlx", "01");
            jsonData = JsonAnalysis.putJsonBody(obj, "2007").toString();
        } catch (JSONException ignored) {
        }
        RequestParams params = new RequestParams(SERVICE_REQUEST_URL);
        params.addBodyParameter("code", "2007");// 添加到请求body体的参数, 只有POST, PUT, PATCH, DELETE请求支持.
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
                    List<String> list_user = new ArrayList<>();
                    String id_str;
                    JSONArray array = new JSONObject(body).getJSONArray("grzhlist");
                    for (int i = 0; i < array.length(); i++) {
                        id_str = array.getJSONObject(i).getString("grzh");
                        list_user.add(id_str);
                    }

                    if (list_user.size() == 0) {
                        pro_dialog.dismiss();
                        ToastUtils.showShort(x.app(), "登陆失败");
                    } else if (list_user.size() == 1) {
                        str_username = list_user.get(0);
                        Login();
                    } else {
                        showListDialog(list_user);
                    }
                } catch (Exception e) {
                    pro_dialog.dismiss();
                    ToastUtils.showShort(x.app(), "登陆失败");
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

    private void Login() {
        JSONObject obj = new JSONObject();
        String jsonData = null;
        try {
//            obj.put("personAcctNo", str_username);
            Number number = (Number) login_type.getSelectedItem();
            obj.put("personAcctNo", str_username);
            String code = null;
            for (int i = 0; i < list_str.size(); i++) {
                if (number.getName().equals(list_str.get(i).getName())) {
                    code = list_str.get(i).getCode();
                    break;
                }
            }
            obj.put("certType", code);
            obj.put("certNo", str_card);
            obj.put("password", Md5Utils.md5(str_password));

//            添加设备编号
            obj.put("equipmentID", "Android:" + deviced_id);
            jsonData = JsonAnalysis.putJsonBody(obj, "1001").toString();
        } catch (JSONException ignored) {
        }
        RequestParams params = new RequestParams(SERVICE_REQUEST_URL);
        params.addBodyParameter("code", "1001");// 添加到请求body体的参数, 只有POST, PUT, PATCH, DELETE请求支持.
        params.addBodyParameter("json", jsonData);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                pro_dialog.dismiss();
                User user;
                try {
                    String msg = JsonAnalysis.getMsg(result);
                    if (!"交易成功".equals(msg)) {
                        ToastUtils.showShort(x.app(), msg);
                        //重新初始化验证码
                        checkview.refreshCode();
                        return;
                    }
                    String body = JsonAnalysis.getJsonBody(result);
                    user = new Gson().fromJson(body, User.class);
                    Const.IS_LOGIN = true;
                    //保存数据到huzhou.db数据库
                    db.saveOrUpdate(user);
                    utils.save(Const.LOGIN, "true");
//                    增加
                    utils.save(Const.SFZ, str_card);

                    utils.save(Const.GJJ_DATA, str_username);
                    ToastUtils.showShort(x.app(), "登录成功！");
                    finish();
                } catch (Exception e) {
                    //重新初始化验证码
                    checkview.refreshCode();
                    ToastUtils.showShort(x.app(), "登录失败！");
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                pro_dialog.dismiss();
                //重新初始化验证码
                checkview.refreshCode();
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

    @Override
    public void onListItemSelected(CharSequence value, int number, int requestCode) {
        if (requestCode == REQUEST_CODE) {
            str_username = value.toString();
            Login();
        }
    }

    //人脸识别登陆
    private String platform = "zmop";
    private String biz_no;
    private DefaultZhimaClient2 client;
    //    姓名
    private String name_str = "雷力",
    //    身份证号码
    card_str = "500223199410218032";

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
                                Toast.makeText(LoginActivity.this, "biz_no获取失败！", Toast.LENGTH_SHORT).show();
                                pro_dialog.dismiss();
                                return;
                            }
                            try {
                                JSONObject obj = new JSONObject(response);
                                boolean issuccess = obj.getBoolean("success");
                                if (!issuccess) {
                                    Toast.makeText(LoginActivity.this, obj.getString("error_message"), Toast.LENGTH_SHORT).show();
                                    pro_dialog.dismiss();
                                } else {
                                    if (response.contains("biz_no")) {
                                        biz_no = (new JSONObject(response)).getString("biz_no");
                                        // 2. 生成认证请求 URL
                                        initRUrl();
                                    }
                                }
                            } catch (JSONException e) {
                                Toast.makeText(LoginActivity.this, "biz_no获取失败！", Toast.LENGTH_SHORT).show();
                                pro_dialog.dismiss();
                            }
                        }
                    });

                } catch (ZhimaApiException e) {
                    Toast.makeText(LoginActivity.this, "biz_no获取失败！", Toast.LENGTH_SHORT).show();
                    pro_dialog.dismiss();
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
//        String return_url = "alipays://www.taobao.com";
//        request.setReturnUrl(return_url);// 必要参数
        try {
            String url = client.generatePageRedirectInvokeUrl(request);
            doVerify(url);
            System.out.println("generateCertifyUrl url:" + url);
        } catch (ZhimaApiException e) {
            pro_dialog.dismiss();
        }
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
            startActivityForResult(action, 1);
//            open.loadUrl(url);
        } else {
            pro_dialog.dismiss();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1)
            // 4.认证查询
            doFind();
    }

    private void doFind() {
        final ZhimaCustomerCertificationQueryRequest req = new ZhimaCustomerCertificationQueryRequest();
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
                                Toast.makeText(LoginActivity.this, "查询失败！", Toast.LENGTH_SHORT).show();
                                pro_dialog.dismiss();
                                return;
                            }
                            try {
                                JSONObject obj = new JSONObject(response);
                                boolean success = obj.getBoolean("success");
                                if (!success) {
                                    if (pro_dialog.isShowing()) pro_dialog.dismiss();
                                    Toast.makeText(LoginActivity.this, obj.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                                } else {
                                    String passed = obj.getString("passed");
                                    if ("false".equals(passed)) {
                                        if (pro_dialog.isShowing()) pro_dialog.dismiss();
                                        Toast.makeText(LoginActivity.this, obj.getString("failed_reason"), Toast.LENGTH_SHORT).show();
                                    } else {
//                                        人脸识别成功
                                        utils.save(Const.LOGIN, "true");
                                        Const.IS_LOGIN = true;
                                        getdata();
                                    }
                                }
                            } catch (JSONException e) {
                                pro_dialog.dismiss();
                                Toast.makeText(LoginActivity.this, "查询失败！", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } catch (ZhimaApiException e) {
                    pro_dialog.dismiss();
                    Toast.makeText(LoginActivity.this, "查询失败！", Toast.LENGTH_SHORT).show();
                }
            }
        }).start();

    }


    //    获取assets文件To String
    private String getFromAssets(String fileName) {
        try {
            InputStreamReader inputReader = new InputStreamReader(getResources().getAssets().open(fileName));
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line;
            String Result = "";
            while ((line = bufReader.readLine()) != null)
                Result += line;
            return Result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //    获取数据
    public void getdata() {
        new Thread() {
            @Override
            public void run() {
                URL url;
                PrintWriter writer;
                HttpURLConnection conn;
                try {
                    //通过类装载器装载XML资源
                    String out = getFromAssets("test.xml");
                    String path = "http://211.142.115.252:8081/pdsgjj4zfb/processAccess";
//			url = new URL("http://localhost:8081/ejx4basis01/processAccess");
                    url = new URL(path);
                    conn = (HttpURLConnection) url.openConnection();
//			conn.setRequestProperty("contentType", "GBK");
                    conn.addRequestProperty("code", "2001"); //这两个比较重要
                    conn.addRequestProperty("channel", "ZFB");
                    conn.setDoInput(true);
                    conn.setDoOutput(true);
//			conn.setRequestMethod("POST");
                    System.out.println("发送内容：\n" + out);
                    writer = new PrintWriter(new OutputStreamWriter(conn.getOutputStream(), "utf-8"));
                    writer.print(out);
                    writer.flush();
                    writer.close();
                    final XmlData data = getPeople(conn.getInputStream());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pro_dialog.dismiss();
                            if (data.getResCode().equals("000")) {
                                String items[] = {
                                        "客户编号:" + data.getKhbh(),
                                        "中心编号:" + data.getZxbh(),
                                        "个人姓名:" + data.getGrxm(),
                                        "证件号码:" + data.getZjhm(),
                                        "出生日期:" + data.getCsrq(),
                                };
                                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                builder.setTitle("个人信息").setItems(items, null).setCancelable(false);
                                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        LoginActivity.this.finish();
                                    }
                                });

                                AlertDialog dialog = builder.create();
                                dialog.setCanceledOnTouchOutside(false);
                                dialog.show();
                            } else {
                                Toast.makeText(LoginActivity.this, data.getResMsg(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } catch (Exception e) {
                    pro_dialog.dismiss();
                    Toast.makeText(LoginActivity.this, "查询失败！", Toast.LENGTH_SHORT).show();
                }
            }
        }.start();
    }


    /**
     * 从XML文件中读取数据
     *
     * @param xml XML文件输入流
     */
    public XmlData getPeople(InputStream xml) throws Exception {
        XmlData person = null;
        // 利用ANDROID提供的API快速获得pull解析器
        XmlPullParser pullParser = Xml.newPullParser();
        // 设置需要解析的XML数据
        pullParser.setInput(xml, "UTF-8");
        // 取得事件
        int event = pullParser.getEventType();
        // 若为解析到末尾
        while (event != XmlPullParser.END_DOCUMENT) // 文档结束
        {
            String nodeName = pullParser.getName();
            switch (event) {
                case XmlPullParser.START_TAG: // 标签开始
                    if ("ROOT".equals(nodeName)) {
                        person = new XmlData();
                    } else if ("ResCode".equals(nodeName)) {
                        String name = pullParser.nextText();
                        assert person != null;
                        person.setResCode(name);
                    } else if ("ResMsg".equals(nodeName)) {
                        String name = pullParser.nextText();
                        assert person != null;
                        person.setResMsg(name);
                    } else if ("khbh".equals(nodeName)) {
                        String name = pullParser.nextText();
                        assert person != null;
                        person.setKhbh(name);
                    } else if ("zxbh".equals(nodeName)) {
                        String name = pullParser.nextText();
                        assert person != null;
                        person.setZxbh(name);
                    } else if ("zjhm".equals(nodeName)) {
                        String name = pullParser.nextText();
                        assert person != null;
                        person.setZjhm(name);
                    } else if ("grxm".equals(nodeName)) {
                        String name = pullParser.nextText();
                        assert person != null;
                        person.setGrxm(name);
                    } else if ("csrq".equals(nodeName)) {
                        String name = pullParser.nextText();
                        assert person != null;
                        person.setCsrq(name);
                    }
            }
            event = pullParser.next(); // 下一个标签
        }
        return person;
    }
}
