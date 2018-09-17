package com.huzhou.gjj.acitivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.huzhou.gjj.R;
import com.huzhou.gjj.adapter.ChangeExpandableListAdapter;
import com.huzhou.gjj.bean.Change;
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
import static com.huzhou.gjj.utils.Const.NOINTENT_STR;
import static com.huzhou.gjj.utils.Const.SERVICE_REQUEST_URL;

@ContentView(R.layout.activity_demo)
public class ChangeMoreActivity extends BaseAppCompatActivity implements
        XListView.IXListViewListener {

    @ViewInject(R.id.expandlist)
    private XListView expandList;

    @ViewInject(R.id.gjj_id)
    private TextView gjj_id;

    @ViewInject(R.id.user_name)
    private TextView user_name;

    private ChangeExpandableListAdapter myAdapter;
    private List<Change> list = new ArrayList<>();
    private MyProgressDialog pro_dialog;
    private int pageNum = 1;
    private String totalNum = "";
    private String grzh, start, end, spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initDatas(pageNum, "getdata");
    }

    /**
     * group和child子项的数据源
     */
    private void initDatas(int page, final String str) {
        JSONObject obj = new JSONObject();
        String jsonData = null;
        try {
            obj.put("ksrq", start);
            obj.put("jsrq", end);
            obj.put("grzh", grzh);

            obj.put("cxlx", spinner);
            obj.put("page", page);
            obj.put("rows", 10);
            obj.put("startRowNum", 0);
            jsonData = JsonAnalysis.putJsonBody(obj, "2004").toString();
        } catch (JSONException ignored) {
            pro_dialog.dismiss();
        }
        RequestParams params = new RequestParams(SERVICE_REQUEST_URL);
        params.addBodyParameter("code", "2004");// 添加到请求body体的参数, 只有POST, PUT, PATCH, DELETE请求支持.
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
                    Change change;
                    String body = JsonAnalysis.getJsonBody(result);
                    JSONArray array = new JSONObject(body).getJSONArray("bgxxList");
                    totalNum = new JSONObject(body).getString("totalNum");
                    if (array.length() == 0) {
                        ToastUtils.showShort(x.app(), Const.NULL_STR);
                        return;
                    }
                    if (!"onLoadMore".equals(str)) {
                        list.clear();
                    }
                    for (int j = 0; j < array.length(); j++) {
                        change = new Gson().fromJson(array.get(j).toString(), Change.class);
                        list.add(change);
                    }
                    if ("onLoadMore".equals(str)) {
                        myAdapter.notifyDataSetChanged();
                    } else {
                        pageNum = 1;
                        myAdapter = new ChangeExpandableListAdapter(x.app(), list);
                        expandList.setAdapter(myAdapter);
                    }
                    for (int i = 0; i < list.size(); i++) {
                        expandList.expandGroup(i);
                    }
                } catch (Exception e) {
                    ToastUtils.showShort(x.app(), NOINTENT_STR);
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
                if ("onRefresh".equals(str)) {
                    expandList.stopRefresh();
                } else if ("onLoadMore".equals(str)) {
                    pro_dialog.dismiss();
                    expandList.stopLoadMore();
                } else {
                    pro_dialog.dismiss();
                }
            }
        });
    }


    private void initView() {
        grzh = DbUtils.getUserData().getPersonAcctNo();
        start = getIntent().getStringExtra("jzrqBegin");
        end = getIntent().getStringExtra("jzrqEnd");
        spinner = getIntent().getStringExtra("spinner");

        pro_dialog = LoadingDialog.createLoadingDialog(this, "加载中...");
        pro_dialog.setActivity(this);
        pro_dialog.show();

        gjj_id.setText(grzh);
        user_name.setText(DbUtils.getUserData().getCustName());

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
                startActivity(new Intent(ChangeMoreActivity.this, MainActivity.class));
                finish();
                break;
        }
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
        if (!(list.size() + "").equals(totalNum)) {
            pro_dialog.show();
            pageNum++;
            initDatas(pageNum, "onLoadMore");
        }
    }
}
