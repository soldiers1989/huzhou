package com.huzhou.gjj.acitivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ScrollView;

import com.google.gson.Gson;
import com.huzhou.gjj.R;
import com.huzhou.gjj.adapter.KfsAdapter;
import com.huzhou.gjj.bean.Kfs;
import com.huzhou.gjj.utils.Const;
import com.huzhou.gjj.utils.JsonAnalysis;
import com.huzhou.gjj.utils.ToastUtils;
import com.huzhou.gjj.viewUtils.ClearWriteEditText;
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

import static com.huzhou.gjj.R.id.listview;
import static com.huzhou.gjj.utils.Const.NOINTENT_STR;
import static com.huzhou.gjj.utils.Const.SERVICE_REQUEST_URL;


@ContentView(R.layout.activity_find_company)
public class FindKfActivity extends BaseAppCompatActivity implements
        XListView.IXListViewListener {
    @ViewInject(R.id.company_id)
    private ClearWriteEditText company_id;

    @ViewInject(R.id.company_name)
    private ClearWriteEditText company_name;

    @ViewInject(R.id.company_xyh)
    private ClearWriteEditText company_xyh;

    @ViewInject(R.id.scroll)
    private ScrollView scroll;


    @ViewInject(listview)
    private XListView expandList;

    @ViewInject(R.id.find)
    private Button find;

    private MyProgressDialog pro_dialog;
    private List<Kfs> list = new ArrayList<>();

    private int pageNum = 1;
    private String totalNum = "";
    private KfsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        pro_dialog = LoadingDialog.createLoadingDialog(this, "加载中...");
        pro_dialog.setActivity(this);
        company_xyh.setVisibility(View.GONE);

        expandList.setPullRefreshEnable(true);
        expandList.setPullLoadEnable(true);
        expandList.setXListViewListener(this);
        registerForContextMenu(expandList);// 给ExpandListView添加上下文菜单

        expandList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Intent mIntent = new Intent();
                Bundle mBundle = new Bundle();
                mBundle.putSerializable("company", list.get(groupPosition));
                mIntent.putExtras(mBundle);
                FindKfActivity.this.setResult(1, mIntent);
                finish();
                return true;
            }
        });
    }

    private void initDatas(int page, final String str) {
        JSONObject obj = new JSONObject();
        String jsonData = null;
        try {
            obj.put("kfgszh", company_id_str);
            obj.put("Kfgsmc", company_name_str);
            obj.put("page", page);
            obj.put("rows", 10);
            obj.put("startRowNum", 0);
            jsonData = JsonAnalysis.putJsonBody(obj, "2059").toString();
        } catch (JSONException ignored) {
        }
        RequestParams params = new RequestParams(SERVICE_REQUEST_URL);
        params.addBodyParameter("code", "2059");// 添加到请求body体的参数, 只有POST, PUT, PATCH, DELETE请求支持.
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
                    String body = JsonAnalysis.getJsonBody(result);
                    totalNum = new JSONObject(body).getString("totalNum");
                    Kfs change;
                    JSONArray array = new JSONObject(body).getJSONArray("kfsxxList");
                    if (array.length() == 0) {
                        ToastUtils.showShort(x.app(), Const.NULL_STR);
                        return;
                    }
                    if (!"onLoadMore".equals(str)) {
                        list.clear();
                    }
                    for (int j = 0; j < array.length(); j++) {
                        change = new Gson().fromJson(array.get(j).toString(), Kfs.class);
                        list.add(change);
                    }
                    scroll.setVisibility(View.GONE);
                    expandList.setVisibility(View.VISIBLE);
                    find.setText("重新查询");

                    if ("onLoadMore".equals(str)) {
                        adapter.notifyDataSetChanged();
                    } else {
                        pageNum = 1;
                        adapter = new KfsAdapter(FindKfActivity.this, list);
                        expandList.setAdapter(adapter);
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

    String company_id_str, company_name_str, company_xyh_str;

    @Event(value = {R.id.find, R.id.reset}, type = View.OnClickListener.class)
    private void getEvent(View v) {
        company_id_str = company_id.getText().toString().trim();
        company_name_str = company_name.getText().toString().trim();
        company_xyh_str = company_xyh.getText().toString().trim();
        switch (v.getId()) {
            case R.id.find:
                String find_str = find.getText().toString().trim();
                if ("重新查询".equals(find_str)) {
                    scroll.setVisibility(View.VISIBLE);
                    find.setText("查询");
                    return;
                }
                pro_dialog.show();
                initDatas(pageNum, "getdata");
                break;
            case R.id.reset:
                company_id.setText(null);
                company_name.setText(null);
                company_xyh.setText(null);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
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
