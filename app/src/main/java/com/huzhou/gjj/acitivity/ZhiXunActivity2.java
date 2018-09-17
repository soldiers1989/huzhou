package com.huzhou.gjj.acitivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.huzhou.gjj.R;
import com.huzhou.gjj.adapter.SpinnerAdapter;
import com.huzhou.gjj.bean.Dkjc;
import com.huzhou.gjj.bean.Dkjc2;
import com.huzhou.gjj.bean.Dkjc3;
import com.huzhou.gjj.bean.Number;
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

import static com.huzhou.gjj.utils.Const.SERVICE_REQUEST_URL;
import static com.huzhou.gjj.utils.Const.SERVICE_RHIDAIN_URL;


@ContentView(R.layout.activity_zhixun2)
public class ZhiXunActivity2 extends BaseAppCompatActivity {


    private MyProgressDialog pro_dialog;
    private Dkjc dkjc;
    private Dkjc2 dkjc2;
    private Dkjc3 dkjc3;
    private List<Number> list_rclb = new ArrayList<>();
    private List<Number> list_hkfs = new ArrayList<>();
    private List<Number> list_fwlx = new ArrayList<>();
    private List<Number> list_address = new ArrayList<>();

    @ViewInject(R.id.address)
    private Spinner address;

    @ViewInject(R.id.ll_address)
    private LinearLayout ll_address;

    @ViewInject(R.id.ll_fangling)
    private LinearLayout ll_fangling;

    @ViewInject(R.id.fangling)
    private ClearWriteEditText fangling;

    @ViewInject(R.id.rclb)
    private Spinner rclb;

    @ViewInject(R.id.lplx)
    private Spinner lplx;

    @ViewInject(R.id.yc_money)
    private ClearWriteEditText yc_money;

    @ViewInject(R.id.ycce)
    private ClearWriteEditText ycce;

    @ViewInject(R.id.total_money)
    private ClearWriteEditText total_money;

    @ViewInject(R.id.detail_find)
    private Button detail_find;

    @ViewInject(R.id.loan_num)
    private TextView loan_num;

    @ViewInject(R.id.loan_lv)
    private TextView loan_lv;

    @ViewInject(R.id.high_ed)
    private TextView high_ed;

    @ViewInject(R.id.high_qx)
    private TextView high_qx;

    @ViewInject(R.id.hkfs)
    private Spinner hkfs;

    @ViewInject(R.id.loan_money)
    private ClearWriteEditText loan_money;

    @ViewInject(R.id.loan_qx)
    private ClearWriteEditText loan_qx;

    @ViewInject(R.id.hkjk_btn)
    private Button hkjk_btn;

    @ViewInject(R.id.edgs)
    private TextView edgs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initview();
        getZhiDian();
    }

    private void initview() {
        list_address.add(new Number("330501", "市区"));
        list_address.add(new Number("330502", "南浔区"));
        list_address.add(new Number("330521", "德清县"));
        list_address.add(new Number("330522", "长兴县"));
        list_address.add(new Number("330523", "安吉县"));
        SpinnerAdapter adapter4 = new SpinnerAdapter(x.app(),
                list_address);
        address.setAdapter(adapter4);

        lplx.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ll_address.setVisibility(View.GONE);
                ll_fangling.setVisibility(View.GONE);
                if (lplx.getSelectedItem() != null) {
                    String name = ((Number) lplx.getSelectedItem()).getName();
                    if ("二手房".equals(name)) {
                        ll_address.setVisibility(View.VISIBLE);
                        ll_fangling.setVisibility(View.VISIBLE);
                    }
                    if ("其他".equals(name)) {
                        ll_address.setVisibility(View.GONE);
                        ll_fangling.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //初始化dialog
        pro_dialog = LoadingDialog.createLoadingDialog(this, "加载中...");
        pro_dialog.setActivity(this);
        pro_dialog.show();


        loan_money.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String high_ed_str = high_ed.getText().toString().trim();
                String loan_money_str = loan_money.getText().toString().trim();
//                if (TextUtils.isEmpty(high_ed_str)) {
////                    loan_money.clearFocus();
////                    loan_money.setText("");
//                } else
                if (!TextUtils.isEmpty(loan_money_str))
                    if (Double.parseDouble(loan_money_str) > Double.parseDouble(high_ed_str)) {
                        loan_money.setText(null);
                        Toast.makeText(x.app(), "贷款金额不能大于最高可贷额度！", Toast.LENGTH_SHORT).show();
                    }
            }
        });


        loan_qx.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String high_qx_str = high_qx.getText().toString().trim();
                String loan_qx_str = loan_qx.getText().toString().trim();
//                if (TextUtils.isEmpty(high_qx_str)) {
////                    loan_qx.clearFocus();
////                    loan_qx.setText(null);
//                } else
                if (!TextUtils.isEmpty(loan_qx_str))
                    if (Integer.parseInt(loan_qx_str) > Integer.parseInt(high_qx_str)) {
                        loan_qx.setText(null);
                        Toast.makeText(x.app(), "贷款期限不能大于最多可贷期限！", Toast.LENGTH_SHORT).show();
                    }
            }
        });
    }


    private void getZhiDian() {
        JSONArray array = new JSONArray();
        try {
            JSONObject data1 = new JSONObject();
            data1.put("dictType", "rclb");
            JSONObject data2 = new JSONObject();
            data2.put("dictType", "fwlx");
            JSONObject data3 = new JSONObject();
            data3.put("dictType", "hkfs");
//            JSONObject data4 = new JSONObject();
//            data4.put("dictType", "dywfwzl");

            array.put(data1);
            array.put(data2);
            array.put(data3);
//            array.put(data4);
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
                        switch (number.getDictType()) {
                            case "rclb":
                                list_rclb.add(number);
                                break;
                            case "fwlx":
                                list_fwlx.add(number);
                                break;
                            case "hkfs":
                                list_hkfs.add(number);
                                break;
                            case "dywfwzl":
                                list_address.add(number);
                                break;
                        }
                    }
                    SpinnerAdapter adapter1 = new SpinnerAdapter(x.app(),
                            list_rclb);
                    rclb.setAdapter(adapter1);

                    SpinnerAdapter adapter2 = new SpinnerAdapter(x.app(),
                            list_fwlx);
                    lplx.setAdapter(adapter2);

                    SpinnerAdapter adapter3 = new SpinnerAdapter(x.app(),
                            list_hkfs);
                    hkfs.setAdapter(adapter3);

//                    SpinnerAdapter adapter4 = new SpinnerAdapter(x.app(),
//                            list_address);
//                    address.setAdapter(adapter4);

                    getImformation();
                } catch (Exception ignored) {
                    pro_dialog.dismiss();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                pro_dialog.dismiss();
                ToastUtils.showShort(x.app(), "数据加载失败！");
            }

            @Override
            public void onCancelled(CancelledException cex) {
                pro_dialog.dismiss();
                ToastUtils.showShort(x.app(), "取消数据加载！");
            }

            @Override
            public void onFinished() {
            }
        });
    }

    private void getImformation() {
        JSONObject obj = new JSONObject();
        String jsonData = null;
        try {
            obj.put("flag", "fydjkr");
            JSONObject obj1 = new JSONObject();
            obj1.put("jkrgjjzh", DbUtils.getUserData().getPersonAcctNo());
            obj.put("formData", obj1);
            jsonData = JsonAnalysis.putJson2(obj, "2052");
        } catch (JSONException ignored) {
        }
        RequestParams params = new RequestParams(SERVICE_REQUEST_URL);
        params.addBodyParameter("code", "2052");// 添加到请求body体的参数, 只有POST, PUT, PATCH, DELETE请求支持.
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
                    String body = JsonAnalysis.getJsonBody(result);
                    dkjc = new Gson().fromJson(body, Dkjc.class);
                    String message = dkjc.getLxjcxx();
                    if (!TextUtils.isEmpty(message))
                        Toast.makeText(x.app(), message, Toast.LENGTH_LONG).show();
                } catch (Exception ignored) {
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                pro_dialog.dismiss();
                ToastUtils.showShort(x.app(), "数据加载失败！");
            }

            @Override
            public void onCancelled(CancelledException cex) {
                pro_dialog.dismiss();
                ToastUtils.showShort(x.app(), "取消数据加载！");
            }

            @Override
            public void onFinished() {
            }
        });
    }

    private String fangling_str, rclb_str, lplx_str, yc_money_str, ycce_str, total_money_str, hkfs_str, loan_money_str, loan_qx_str;

    @Event(value = {R.id.detail_find, R.id.hkjk_btn}, type = View.OnClickListener.class)
    private void signEvent(View view) {
        if (list_fwlx.size() == 0 || list_address.size() == 0 || list_rclb.size() == 0 || list_hkfs.size() == 0)
            return;
        rclb_str = ((Number) rclb.getSelectedItem()).getName();
        lplx_str = ((Number) lplx.getSelectedItem()).getName();
        hkfs_str = ((Number) hkfs.getSelectedItem()).getName();
        fangling_str = fangling.getText().toString().trim();
        yc_money_str = yc_money.getText().toString().trim();
        ycce_str = ycce.getText().toString().trim();
        total_money_str = total_money.getText().toString().trim();
        loan_money_str = loan_money.getText().toString().trim();
        loan_qx_str = loan_qx.getText().toString().trim();
        switch (view.getId()) {
            case R.id.detail_find:
                getFocus(detail_find);
                if (check()) {
                    EduJs();
                }
                break;
            case R.id.hkjk_btn:
                getFocus(hkjk_btn);
                if (check2()) {
                    HkJh();
                }
                break;
        }
    }

    private void getFocus(Button btn) {
        btn.setFocusable(true);
        btn.setFocusableInTouchMode(true);
        btn.requestFocus();
        btn.requestFocusFromTouch();
    }

    private boolean check() {
        String s = "不能为空!";
        if (TextUtils.isEmpty(yc_money_str)) {
            yc_money.setError(s);
            return false;
        }
        if (TextUtils.isEmpty(ycce_str)) {
            ycce.setError(s);
            return false;
        }
        if (TextUtils.isEmpty(total_money_str)) {
            total_money.setError(s);
            return false;
        }
        if ("二手房".equals(lplx_str) || "其他".equals(lplx_str)) {
            if (TextUtils.isEmpty(fangling_str)) {
                fangling.setError(s);
                return false;
            }
        }
        return true;
    }

    private boolean check2() {
        String s = "不能为空!";
        if (TextUtils.isEmpty(loan_money_str)) {
            loan_money.setError(s);
            return false;
        }
        if (TextUtils.isEmpty(loan_qx_str)) {
            loan_qx.setError(s);
            return false;
        }
        return true;
    }

    private void EduJs() {
        pro_dialog.show();
        JSONObject obj = new JSONObject();
        String jsonData = null;
        try {
            obj.put("flag", "01");
            JSONObject obj1 = new JSONObject();
            obj1.put("jkrgjjzh", DbUtils.getUserData().getPersonAcctNo());
            obj1.put("jkrzjlx", "01");
            obj1.put("jkrzjh", DbUtils.getUserData().getCertNo());
            obj1.put("csrq", dkjc.getCsrq());
            obj1.put("fcxz", "02");
            obj1.put("jcye", dkjc.getJcye());
            obj.put("jkrFormData", obj1);
            JSONObject obj2 = new JSONObject();
            String code1 = null;
            for (int i = 0; i < list_fwlx.size(); i++) {
                if (lplx_str.equals(list_fwlx.get(i).getName())) {
                    code1 = list_fwlx.get(i).getCode();
                    break;
                }
            }
            obj2.put("fwlx", code1);
            if ("二手房".equals(lplx_str)) {
                obj2.put("fangling", fangling_str);
                String address_str = ((Number) address.getSelectedItem()).getName();
                String code2 = null;
                for (int i = 0; i < list_address.size(); i++) {
                    if (address_str.equals(list_address.get(i).getName())) {
                        code2 = list_address.get(i).getCode();
                        break;
                    }
                }
                obj2.put("dywfwzl", code2);
            } else if ("其他".equals(lplx_str)) {
                obj2.put("fangling", fangling_str);
            }
            obj2.put("fwzj", ycce_str);
            obj2.put("fwjzmj", yc_money_str);

//            01-中心城区
//            02-其他城镇
//            房屋类型为二手房时，二手房坐落位置必输。  有问题？
            obj2.put("dywfwzl", DbUtils.getUserData().getCenterCode());
            obj.put("fwxxFormData", obj2);
            JSONObject obj3 = new JSONObject();
            obj3.put("dkedfd", "01");
            obj3.put("dkqs", total_money_str);
            obj3.put("llfdbl", "100");
            obj3.put("dkllfd", "01");
            obj.put("dkxxFormData", obj3);

            jsonData = JsonAnalysis.putJson2(obj, "2053");
        } catch (JSONException ignored) {
        }
        RequestParams params = new RequestParams(SERVICE_REQUEST_URL);
        params.addBodyParameter("code", "2053");// 添加到请求body体的参数, 只有POST, PUT, PATCH, DELETE请求支持.
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
                    String body = JsonAnalysis.getJsonBody(result);
                    dkjc2 = new Gson().fromJson(body, Dkjc2.class);
                    loan_num.setText(dkjc2.getYdkcs());
                    String dkyll = dkjc2.getDkyll();
                    loan_lv.setText(dkyll);
                    high_ed.setText(dkjc2.getZgkded());
                    high_qx.setText(dkjc2.getZdkdqx());
                    loan_money.setEnabled(true);
                    loan_qx.setEnabled(true);
                    edgs.setText(dkjc2.getGs());
                } catch (Exception ignored) {
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                pro_dialog.dismiss();
                ToastUtils.showShort(x.app(), "数据加载失败！");
            }

            @Override
            public void onCancelled(CancelledException cex) {
                pro_dialog.dismiss();
                ToastUtils.showShort(x.app(), "取消数据加载！");
            }

            @Override
            public void onFinished() {
            }
        });
    }


    private void HkJh() {
        pro_dialog.show();
        JSONObject obj = new JSONObject();
        String jsonData = null;
        try {

            obj.put("dkje", loan_money_str);
            obj.put("dkqx", loan_qx_str);
            obj.put("llfdbl", "100");
            String code1 = null;
            for (int i = 0; i < list_hkfs.size(); i++) {
                if (hkfs_str.equals(list_hkfs.get(i).getName())) {
                    code1 = list_hkfs.get(i).getCode();
                    break;
                }
            }
            obj.put("hkfs", code1);
            obj.put("flag", "01");

            obj.put("dkedfd", "01");
            obj.put("jyqdlx", "02");
            obj.put("zxbh", DbUtils.getUserData().getCenterCode());

            JSONObject obj1 = new JSONObject();
            obj1.put("jkrgjjzh", DbUtils.getUserData().getPersonAcctNo());
            obj.put("jkrFormData", obj1);

            JSONObject obj2 = new JSONObject();
            obj2.put("dkllfd", "01");
            obj.put("dkxxFormData", obj2);

            JSONObject obj3 = new JSONObject();
            obj3.put("jcrjcjs", dkjc.getJcye());
            obj.put("ydjkrFormData", obj3);

            jsonData = JsonAnalysis.putJson2(obj, "2054");
        } catch (JSONException ignored) {
        }
        RequestParams params = new RequestParams(SERVICE_REQUEST_URL);
        params.addBodyParameter("code", "2054");// 添加到请求body体的参数, 只有POST, PUT, PATCH, DELETE请求支持.
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
                    String body = JsonAnalysis.getJsonBody(result);
                    dkjc3 = new Gson().fromJson(body, Dkjc3.class);
                    String message = dkjc3.getRtnFlag();
                    if (!TextUtils.isEmpty(message))
                        Toast.makeText(x.app(), message, Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(ZhiXunActivity2.this, HkjhActivity.class);
                    intent.putExtra("data", dkjc3);
                    startActivity(intent);
                } catch (Exception ignored) {
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                pro_dialog.dismiss();
                ToastUtils.showShort(x.app(), "数据加载失败！");
            }

            @Override
            public void onCancelled(CancelledException cex) {
                pro_dialog.dismiss();
                ToastUtils.showShort(x.app(), "取消数据加载！");
            }

            @Override
            public void onFinished() {
            }
        });
    }
}
