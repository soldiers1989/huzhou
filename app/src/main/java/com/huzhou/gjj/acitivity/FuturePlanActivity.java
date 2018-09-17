package com.huzhou.gjj.acitivity;

import android.os.Bundle;
import android.widget.TextView;

import com.google.gson.Gson;
import com.huzhou.gjj.R;
import com.huzhou.gjj.adapter.FeturePlanAdapter;
import com.huzhou.gjj.bean.FeturePlan;
import com.huzhou.gjj.utils.Const;
import com.huzhou.gjj.utils.DbUtils;
import com.huzhou.gjj.utils.JsonAnalysis;
import com.huzhou.gjj.utils.ToastUtils;
import com.huzhou.gjj.viewUtils.LoadingDialog;
import com.huzhou.gjj.viewUtils.MyProgressDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import static com.huzhou.gjj.utils.Const.SERVICE_REQUEST_URL;


@ContentView(R.layout.activity_loan_history)
public class FuturePlanActivity extends BaseAppCompatActivity {

    @ViewInject(R.id.num)
    private TextView num;

    @ViewInject(R.id.name)
    private TextView name;

    @ViewInject(R.id.expandlist)
    private com.huzhou.gjj.viewUtils.XListView expandList;

    private FeturePlanAdapter myAdapter;
    private MyProgressDialog pro_dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        getData();
    }

    private void initView() {
        pro_dialog = LoadingDialog.createLoadingDialog(this, "加载中...");
        pro_dialog.setActivity(this);
        pro_dialog.show();
        num.setText(DbUtils.getUserData().getPersonAcctNo());
        name.setText(DbUtils.getUserData().getCustName());
        expandList.setPullRefreshEnable(false);
        expandList.setPullLoadEnable(false);
        registerForContextMenu(expandList);// 给ExpandListView添加上下文菜单
    }

    private void getData() {
        JSONObject obj = new JSONObject();
        String jsonData = null;
        try {
            String jkhtbh = getIntent().getStringExtra("jkhtbh");
            obj.put("jkhtbh", jkhtbh);
            jsonData = JsonAnalysis.putJsonBody(obj, "2095").toString();
        } catch (JSONException ignored) {
        }
        RequestParams params = new RequestParams(SERVICE_REQUEST_URL);
        params.addBodyParameter("code", "2095");// 添加到请求body体的参数, 只有POST, PUT, PATCH, DELETE请求支持.
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
                    JSONArray array = new JSONObject(body).getJSONArray("jhhkjh");
                    if (array != null) {
                        if (array.length() == 0) {
                            ToastUtils.showShort(x.app(), Const.NULL_STR);
                        } else {
                            List<FeturePlan> list = new ArrayList<>();
                            FeturePlan plan;
                            for (int j = 0; j < array.length(); j++) {
                                plan = new Gson().fromJson(array.get(j).toString(), FeturePlan.class);
                                list.add(plan);
                            }
                            myAdapter = new FeturePlanAdapter(FuturePlanActivity.this, list);
                            expandList.setAdapter(myAdapter);
                        }
                    }
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
