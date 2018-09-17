package com.huzhou.gjj.acitivity;

import com.google.gson.Gson;
import com.huzhou.gjj.AppManager;
import com.huzhou.gjj.R;
import com.huzhou.gjj.adapter.BankAdapter;
import com.huzhou.gjj.bean.Bank;
import com.huzhou.gjj.bean.Number;
import com.huzhou.gjj.utils.Const;
import com.huzhou.gjj.utils.DbUtils;
import com.huzhou.gjj.utils.JsonAnalysis;
import com.huzhou.gjj.utils.ToastUtils;
import com.huzhou.gjj.viewUtils.LoadingDialog;
import com.huzhou.gjj.viewUtils.MyProgressDialog;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.huzhou.gjj.utils.Const.NOINTENT_STR;
import static com.huzhou.gjj.utils.Const.SERVICE_REQUEST_URL;
import static com.huzhou.gjj.utils.Const.SERVICE_RHIDAIN_URL;

//银行卡绑定
@ContentView(R.layout.activity_binding)
public class BindingActivity extends BaseAppCompatActivity {

    @ViewInject(R.id.back)
    private ImageView back;

    @ViewInject(R.id.toolbar)
    private Toolbar toolbar;

    @ViewInject(R.id.listview)
    private ListView listview;

    private AppManager mAppManager = null;
    private MyProgressDialog pro_dialog;

    private List<Bank> list;
    private BankAdapter adapter;
    private List<Number> list_zd = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        initview();
    }

    @Override
    protected void onResume() {
        getdata();
        super.onResume();
    }

    private void getZhiDian() {

        JSONArray array = new JSONArray();
        try {
            JSONObject data1 = new JSONObject();
            data1.put("dictType", "khyhlx");
            JSONObject data2 = new JSONObject();
            data2.put("dictType", "kzyt");
            JSONObject data3 = new JSONObject();
            data3.put("dictType", "yhkbdzt");
            array.put(data1);
            array.put(data2);
            array.put(data3);
        } catch (JSONException ignored) {
            pro_dialog.dismiss();
        }
        RequestParams params = new RequestParams(SERVICE_RHIDAIN_URL);
        params.addBodyParameter("typeArray", array.toString());
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    final JSONArray array = new JSONArray(result);
                    if (array.length() == 0) return;
                    list_zd.clear();
                    Number number;
                    for (int i = 0; i < array.length(); i++) {
                        number = new Gson().fromJson(array.getJSONObject(i).toString(), Number.class);
                        list_zd.add(number);
                    }
                    adapter = new BankAdapter(x.app(), list, list_zd);
                    listview.setAdapter(adapter);
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

    @Event(R.id.back)
    private void Back(View v) {
        onBackPressed();
    }

    private void initview() {
        mAppManager = AppManager.getAppManager();
        mAppManager.addActivity(this);

        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setHomeButtonEnabled(true);

        pro_dialog = LoadingDialog.createLoadingDialog(this, "加载中...");
        pro_dialog.setActivity(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        if (id == R.id.calendar) {
            Intent intent = new Intent(x.app(), AddCardActivity.class);
            intent.putExtra("data", (Serializable) list);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        mAppManager.addActivity(this);
        super.onDestroy();
    }

    //    个人所属银行查询
    private void getdata() {
        pro_dialog.show();

        JSONObject obj = new JSONObject();
        String jsonData = null;
        try {
            obj.put("grzh", DbUtils.getUserData().getPersonAcctNo());
            jsonData = JsonAnalysis.putJsonBody(obj, "2005").toString();
        } catch (JSONException ignored) {
            pro_dialog.dismiss();
        }
        RequestParams params = new RequestParams(SERVICE_REQUEST_URL);
        params.addBodyParameter("code", "2005");// 添加到请求body体的参数, 只有POST, PUT, PATCH, DELETE请求支持.
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
                    list = new ArrayList<>();
                    Bank detail;
                    JSONArray array = new JSONObject(body).getJSONArray("grssyhxxList");
                    for (int j = 0; j < array.length(); j++) {
                        detail = new Gson().fromJson(array.get(j).toString(), Bank.class);
                        list.add(detail);
                    }
                    if (list.size() == 0) {
                        pro_dialog.dismiss();
                        ToastUtils.showShort(x.app(), Const.NULL_STR);
                    } else {
                        getZhiDian();
                    }
                } catch (JSONException e) {
                    pro_dialog.dismiss();
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
