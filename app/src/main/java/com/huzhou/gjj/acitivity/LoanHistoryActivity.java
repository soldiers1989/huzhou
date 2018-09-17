package com.huzhou.gjj.acitivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.huzhou.gjj.R;
import com.huzhou.gjj.adapter.LoanHistoryAdapter;
import com.huzhou.gjj.bean.History;
import com.huzhou.gjj.bean.Number;
import com.huzhou.gjj.utils.Const;
import com.huzhou.gjj.utils.DbUtils;
import com.huzhou.gjj.utils.JsonAnalysis;
import com.huzhou.gjj.utils.ToastUtils;
import com.huzhou.gjj.viewUtils.LoadingDialog;
import com.huzhou.gjj.viewUtils.MyProgressDialog;
import com.huzhou.gjj.viewUtils.XListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.huzhou.gjj.R.id.fab;
import static com.huzhou.gjj.utils.Const.SERVICE_REQUEST_URL;
import static com.huzhou.gjj.utils.Const.SERVICE_RHIDAIN_URL;


@ContentView(R.layout.activity_loan_history)
public class LoanHistoryActivity extends BaseAppCompatActivity implements
        XListView.IXListViewListener {

    @ViewInject(R.id.num)
    private TextView num;

    @ViewInject(R.id.name)
    private TextView name;

    @ViewInject(R.id.expandlist)
    private XListView expandList;

    private LoanHistoryAdapter myAdapter;
    private MyProgressDialog pro_dialog;

    private List<History> list = new ArrayList<>();
    private int pageNum = 1;
    private String totalNum = "";
    private List<Number> list_zd = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        getZhiDian();
    }

    private void initView() {
        pro_dialog = LoadingDialog.createLoadingDialog(this, "加载中...");
        pro_dialog.setActivity(this);
        pro_dialog.show();
        num.setText(DbUtils.getUserData().getPersonAcctNo());
        name.setText(DbUtils.getUserData().getCustName());
        expandList.setPullRefreshEnable(true);
        expandList.setPullLoadEnable(true);
        expandList.setXListViewListener(this);
        registerForContextMenu(expandList);// 给ExpandListView添加上下文菜单

    }


    //等同于@Event(value={R.id.btn_get,R.id.btn_post},type=View.OnClickListener.class)
    @Event(value = {R.id.fab})
    private void getEvent(View view) {
        switch (view.getId()) {
            case fab:
                startActivity(new Intent(LoanHistoryActivity.this, MainActivity.class));
                finish();
                break;
        }

    }

    private void getZhiDian() {
        JSONArray array = new JSONArray();
        try {
            JSONObject data1 = new JSONObject();
            data1.put("dictType", "dkywmxlx");
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
                    if (array.length() == 0) {
                        pro_dialog.dismiss();
                        return;
                    }
                    Number number;
                    for (int i = 0; i < array.length(); i++) {
                        number = new Gson().fromJson(array.getJSONObject(i).toString(), Number.class);
                        list_zd.add(number);
                    }
                    initDatas(pageNum, "getdata");
                } catch (Exception e) {
                    pro_dialog.dismiss();
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

    private void initDatas(int page, final String str) {
        JSONObject obj = new JSONObject();
        String jsonData = null;
        try {
            String jkhtbh = getIntent().getStringExtra("jkhtbh");
            obj.put("jkhtbh", jkhtbh);
            obj.put("page", page);
            obj.put("rows", 10);
            obj.put("startRowNum", 0);
            jsonData = JsonAnalysis.putJsonBody(obj, "2056").toString();
        } catch (JSONException ignored) {
        }
        RequestParams params = new RequestParams(SERVICE_REQUEST_URL);
        params.addBodyParameter("code", "2056");// 添加到请求body体的参数, 只有POST, PUT, PATCH, DELETE请求支持.
        params.addBodyParameter("json", jsonData);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                if ("onRefresh".equals(str)) {
                    expandList.stopRefresh();
                } else if ("onLoadMore".equals(str)) {
                    pro_dialog.dismiss();
                    expandList.stopLoadMore();
                } else {
                    pro_dialog.dismiss();
                }
                try {
                    String msg = JsonAnalysis.getMsg(result);
                    if (!"交易成功".equals(msg)) {
                        ToastUtils.showShort(x.app(), msg);
                        return;
                    }
                    String body = JsonAnalysis.getJsonBody(result);
                    totalNum = new JSONObject(body).getString("totalNum");

                    History history;
                    JSONArray array = new JSONObject(body).getJSONArray("dkxxList");
                    if (array.length() == 0) {
                        ToastUtils.showShort(x.app(), Const.NULL_STR);
                        return;
                    }
                    if ("onRefresh".equals(str)) {
                        list.clear();
                    }
                    for (int j = 0; j < array.length(); j++) {
                        history = new Gson().fromJson(array.get(j).toString(), History.class);
                        list.add(history);
                    }
                    if ("onLoadMore".equals(str)) {
                        myAdapter.notifyDataSetChanged();
                    } else {
                        pageNum = 1;
                        myAdapter = new LoanHistoryAdapter(x.app(), list, list_zd);
                        expandList.setAdapter(myAdapter);
                    }
                    for (int i = 0; i < list.size(); i++) {
                        expandList.expandGroup(i);
                    }
                } catch (Exception ignored) {
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if ("onRefresh".equals(str)) {
                    expandList.stopRefresh();
                } else if ("onLoadMore".equals(str)) {
                    pro_dialog.dismiss();
                    expandList.stopLoadMore();
                } else {
                    pro_dialog.dismiss();
                }
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

    @Override
    public void onRefresh() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        expandList.setRefreshTime(sd.format(date));
        initDatas(1, "onRefresh");
    }

    @Override
    public void onLoadMore() {
        if (!(list.size() + "").equals(totalNum))
         {
            pro_dialog.show();
            pageNum++;
            initDatas(pageNum, "onLoadMore");
        }
    }
}
