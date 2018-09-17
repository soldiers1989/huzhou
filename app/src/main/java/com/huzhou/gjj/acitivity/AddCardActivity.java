package com.huzhou.gjj.acitivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.google.gson.Gson;
import com.huzhou.gjj.R;
import com.huzhou.gjj.adapter.SpinnerAdapter;
import com.huzhou.gjj.bean.Bank;
import com.huzhou.gjj.bean.Info;
import com.huzhou.gjj.bean.Number;
import com.huzhou.gjj.bean.UserInfo;
import com.huzhou.gjj.utils.Const;
import com.huzhou.gjj.utils.DbUtils;
import com.huzhou.gjj.utils.JsonAnalysis;
import com.huzhou.gjj.utils.ToastUtils;
import com.huzhou.gjj.viewUtils.ClearWriteEditText;
import com.huzhou.gjj.viewUtils.LoadingDialog;
import com.huzhou.gjj.viewUtils.MyProgressDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import static com.huzhou.gjj.utils.Const.NOINTENT_STR;
import static com.huzhou.gjj.utils.Const.SERVICE_REQUEST_URL;
import static com.huzhou.gjj.utils.DbUtils.getUserData;


//添加银行卡
@ContentView(R.layout.activity_add_card)
public class AddCardActivity extends BaseAppCompatActivity {

    //    开户银行
    @ViewInject(R.id.bank_name)
    private Spinner bank_name;

    //    银行卡号
    @ViewInject(R.id.bank_id)
    private ClearWriteEditText bank_id;

    @ViewInject(R.id.ll)
    private LinearLayout ll;

    private MyProgressDialog pro_dialog;

    private Info info = null;

    private UserInfo userinfo = null;

    private List<Bank> listObj;

    private List<Number> list_zd = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initview();
        getdata();
    }

    //    private void getZhiDian() {
//        JSONArray array = new JSONArray();
//        try {
//            JSONObject data1 = new JSONObject();
//            data1.put("dictType", "khyhlx");
//            array.put(data1);
//        } catch (JSONException ignored) {
//            pro_dialog.dismiss();
//        }
//        RequestParams params = new RequestParams(SERVICE_RHIDAIN_URL);
//        params.addBodyParameter("typeArray", array.toString());
//        x.http().post(params, new Callback.CommonCallback<String>() {
//            @Override
//            public void onSuccess(String result) {
//                try {
//                    final JSONArray array = new JSONArray(result);
//                    if (array.length() == 0) return;
//                    Number number;
//                    for (int i = 0; i < array.length(); i++) {
//                        number = new Gson().fromJson(array.getJSONObject(i).toString(), Number.class);
//                        list_zd.add(number);
//                    }
//                    SpinnerAdapter adapter = new SpinnerAdapter(x.app(),
//                            list_zd);
//                    bank_name.setAdapter(adapter);
//                    ll.setVisibility(View.VISIBLE);
//                } catch (Exception ignored) {
//                }
//            }
//
//            @Override
//            public void onError(Throwable ex, boolean isOnCallback) {
//
//            }
//
//            @Override
//            public void onCancelled(CancelledException cex) {
//            }
//
//            @Override
//            public void onFinished() {
//                if (pro_dialog.isShowing())
//                    pro_dialog.dismiss();
//            }
//        });
//    }
    private void getZhiDian() {
        JSONObject obj = new JSONObject();
        String jsonData = null;
        try {
            obj.put("zxbh", DbUtils.getUserData().getCenterCode());
            jsonData = JsonAnalysis.putJsonBody(obj, "2086").toString();
        } catch (JSONException ignored) {
            pro_dialog.dismiss();
        }
        RequestParams params = new RequestParams(SERVICE_REQUEST_URL);
        params.addBodyParameter("code", "2086");// 添加到请求body体的参数, 只有POST, PUT, PATCH, DELETE请求支持.
        params.addBodyParameter("json", jsonData);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    String body = JsonAnalysis.getJsonBody(result);
                    JSONArray array = new JSONObject(body).getJSONArray("yhxx");
                    if (array.length() == 0) return;
                    Number number;
                    String code, name;
                    JSONObject jsonObject;
                    for (int i = 0; i < array.length(); i++) {
                        jsonObject = array.getJSONObject(i);
                        if (jsonObject.toString().contains("yhmc") && jsonObject.toString().contains("yhdm")) {
                            name = jsonObject.getString("yhmc");
                            code = jsonObject.getString("yhdm");
                            number = new Number(code, name);
                            list_zd.add(number);
                        }
                    }
                    SpinnerAdapter adapter = new SpinnerAdapter(x.app(),
                            list_zd);
                    bank_name.setAdapter(adapter);
                    ll.setVisibility(View.VISIBLE);
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
                if (pro_dialog.isShowing())
                    pro_dialog.dismiss();
            }
        });
    }

    private void initview() {
        //初始化dialog
        pro_dialog = LoadingDialog.createLoadingDialog(this, "加载中...");
        pro_dialog.setActivity(this);
        //获取到已添加的银行卡信息
        listObj = (ArrayList<Bank>) getIntent().getSerializableExtra("data");
    }

    //    个人客户信息查询
    private void getdata() {
        pro_dialog.show();
        JSONObject obj = new JSONObject();
        String jsonData = null;
        try {
            obj.put("grzh", getUserData().getPersonAcctNo());//公积金账号
            obj.put("khbh", getUserData().getCustCode());  //客户编号
            obj.put("zhlx", "01");//证件类型
            jsonData = JsonAnalysis.putJsonBody(obj, "2001").toString();
        } catch (JSONException ignored) {
            pro_dialog.dismiss();
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
                        ToastUtils.showShort(x.app(), msg);
                        pro_dialog.dismiss();
                        return;
                    }
                    String body = JsonAnalysis.getJsonBody(result);
                    info = new Gson().fromJson(body, Info.class);
                    getPersonData();
                } catch (Exception e) {
                    ToastUtils.showShort(x.app(), NOINTENT_STR);
                    pro_dialog.dismiss();
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


    //    个人账户信息查询
    private void getPersonData() {
        JSONObject obj = new JSONObject();
        String jsonData = null;
        try {
            obj.put("grzh", DbUtils.getUserData().getPersonAcctNo());
            obj.put("zhlx", "01");//            默认为01公积金账户
            jsonData = JsonAnalysis.putJsonBody(obj, "2002").toString();
        } catch (JSONException ignored) {
            pro_dialog.dismiss();
        }
        RequestParams params = new RequestParams(SERVICE_REQUEST_URL);
        params.addBodyParameter("code", "2002");// 添加到请求body体的参数, 只有POST, PUT, PATCH, DELETE请求支持.
        params.addBodyParameter("json", jsonData);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    String msg = JsonAnalysis.getMsg(result);
                    if (!"交易成功".equals(msg)) {
                        ToastUtils.showShort(x.app(), msg);
                        pro_dialog.dismiss();
                        return;
                    }
                    getZhiDian();
                    String body = JsonAnalysis.getJsonBody(result);
                    userinfo = new Gson().fromJson(body, UserInfo.class);
                } catch (Exception e) {
                    ToastUtils.showShort(x.app(), NOINTENT_STR);
                    pro_dialog.dismiss();
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

    @Event(R.id.detail_find)
    private void initData(View v) {

        //获取到输入的银行卡信息
        String bank_id_str = bank_id.getText().toString().trim();
        if (TextUtils.isEmpty(bank_id_str)) {
            bank_id.setError("不能为空！");
            bank_id.requestFocus();
            return;
        }
        if (bank_id.length() < 14) {
            bank_id.setError("银行卡格式错误！！");
            bank_id.requestFocus();
            return;
        }

        pro_dialog.show();

//        个人银行卡绑定
        String jsonData = null;
        try {
            JSONObject obj = new JSONObject();
            obj.put("zjlx", info.getZjlx());
            obj.put("zjhm", info.getZjhm());
            obj.put("grzh", DbUtils.getUserData().getPersonAcctNo());
            obj.put("xingbie", info.getXingbie());
            obj.put("grxm", info.getGrxm());
            obj.put("dwzh", userinfo.getDwzh());
            obj.put("dwmc", info.getDwmc());
            obj.put("zhiye", info.getZhiye());
            obj.put("aybtqdbz", userinfo.getAybtqdbz());
            obj.put("ycxbtqdbz", userinfo.getYcxbtqdbz());
            obj.put("btgz", userinfo.getGrjcjs());
            obj.put("xhj", info.getXhj());
            obj.put("khbh", info.getKhbh());
            obj.put("grjcjs", userinfo.getGrjcjs());

            JSONArray array = new JSONArray();
            JSONObject obj1 = new JSONObject();
            String str_name = null;
            Number number;
            Number number_select = (Number) bank_name.getSelectedItem();
            for (int i = 0; i < list_zd.size(); i++) {
                number = list_zd.get(i);
                if (number.getName().equals(number_select.getName())) {
                    str_name = number.getCode();
                    break;
                }
            }
            obj1.put("KHYH", str_name);
            obj1.put("YHZH", bank_id_str);

//            卡折用途
            obj1.put("KZYT", "3");
//            银行卡绑定状态
            obj1.put("YHKBDZT", "0");
            array.put(obj1);

            for (int i = 0; i < listObj.size(); i++) {
                JSONObject obj12 = new JSONObject();
                obj12.put("KHYH", listObj.get(i).getKHYH());
                obj12.put("YHZH", listObj.get(i).getYHZH());
                obj12.put("KZYT", listObj.get(i).getKZYT());
                obj12.put("YHKBDZT", listObj.get(i).getYHKBDZT());
                obj12.put("ID", listObj.get(i).getID());
                array.put(obj12);
            }

            JSONObject obj2 = new JSONObject();
            obj2.put("new_datagridData", array);
            obj2.put("formData", obj);

            JSONObject obj3 = new JSONObject();
            obj3.put("TrsCode", "4014");
            obj3.put("ChanCode", "02");
            obj3.put("ReqTrsBank", getUserData().getOrgNo());
            obj3.put("ReqTrsCent", getUserData().getCenterCode());
            obj3.put("ReqTrsTell", getUserData().getCustCode());
            jsonData = JsonAnalysis.getJsonData(obj2, obj3);

        } catch (JSONException ignored) {
            pro_dialog.dismiss();
        }
        RequestParams params = new RequestParams(SERVICE_REQUEST_URL);
        params.addBodyParameter("code", "4014");// 添加到请求body体的参数, 只有POST, PUT, PATCH, DELETE请求支持.
        params.addBodyParameter("json", jsonData);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    String msg = JsonAnalysis.getMsg(result);
                    if (!"交易成功".equals(msg)) {
                        ToastUtils.showShort(x.app(), msg);
                        return;
                    }
                    ToastUtils.showShort(x.app(), "交易成功");
                    finish();
                } catch (Exception e) {
                    ToastUtils.showShort(x.app(), "交易失败！");
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                ToastUtils.showShort(x.app(), Const.NOINTENT_STR);
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

}
