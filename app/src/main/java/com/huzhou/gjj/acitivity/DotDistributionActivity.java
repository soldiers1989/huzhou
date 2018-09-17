package com.huzhou.gjj.acitivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.huzhou.gjj.R;
import com.huzhou.gjj.adapter.SpinnerAdapter2;
import com.huzhou.gjj.bean.Distribution;
import com.huzhou.gjj.bean.DistributionFind;
import com.huzhou.gjj.bean.DistributionType;
import com.huzhou.gjj.utils.Const;
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
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import static com.huzhou.gjj.utils.Const.NOINTENT_STR;
import static com.huzhou.gjj.utils.Const.SERVICE_REQUEST_URL;


@ContentView(R.layout.activity_dotdistribution)
public class DotDistributionActivity extends BaseAppCompatActivity {
    @ViewInject(R.id.yy_spinner)
    private Spinner yy_spinner;
    @ViewInject(R.id.yw_spinner)
    private Spinner yw_spinner;
    @ViewInject(R.id.detail_find)
    private Button detail_find;

    @ViewInject(R.id.ll)
    private LinearLayout ll;

    @ViewInject(R.id.yy_id)
    private TextView yy_id;
    @ViewInject(R.id.name)
    private TextView name;
    @ViewInject(R.id.address)
    private TextView address;
    @ViewInject(R.id.people_count)
    private TextView people_count;
    @ViewInject(R.id.pj_time)
    private TextView pj_time;
    @ViewInject(R.id.scroll)
    private LinearLayout scroll;


    private MyProgressDialog pro_dialog;

    private List<Distribution> list_di = new ArrayList<>();
    private List<String> list = new ArrayList<>();
    private List<String> list_type = new ArrayList<>();
    private List<DistributionType> ditype = new ArrayList<>();
    private String netNumber = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initview();
        getImformation();
    }

    private void initview() {
        //初始化dialog
        pro_dialog = LoadingDialog.createLoadingDialog(this, "加载中...");
        pro_dialog.setActivity(this);
        pro_dialog.show();
        yy_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getType((String) yy_spinner.getSelectedItem());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Event(value = {R.id.detail_find}, type = View.OnClickListener.class)
    private void getEvent(View v) {
        String find_str = detail_find.getText().toString().trim();
        if ("重新查询".equals(find_str)) {
            ll.setVisibility(View.VISIBLE);
            detail_find.setText("查询");
            return;
        }

        if (TextUtils.isEmpty((String) yw_spinner.getSelectedItem())) {
            ToastUtils.showShort(x.app(), "请重新选择预约营业厅！");
            return;
        }
        pro_dialog.show();
        initDatas();
    }

    private void initDatas() {
        JSONObject obj = new JSONObject();
        String jsonData = null;
        try {
            obj.put("NetNumber", netNumber);
            jsonData = JsonAnalysis.putJsonBody(obj, "2091").toString();
        } catch (JSONException ignored) {
        }
        RequestParams params = new RequestParams(SERVICE_REQUEST_URL);
        params.addBodyParameter("code", "2091");// 添加到请求body体的参数, 只有POST, PUT, PATCH, DELETE请求支持.
        params.addBodyParameter("json", jsonData);
        params.setCancelFast(true);
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
                            String body_str = new JSONObject(body).getString("Message");
                            JSONArray array = new JSONArray(body_str);

                            if (array.length() == 0) {
                                ToastUtils.showShort(x.app(), Const.NULL_STR);
                                return;
                            }

                            DistributionFind distribution = null;
                            String yw_spinner_str = (String) yw_spinner.getSelectedItem();
                            for (int j = 0; j < array.length(); j++) {
                                distribution = new Gson().fromJson(array.get(j).toString(), DistributionFind.class);
                                if (yw_spinner_str.equals(distribution.getJobName())) break;
                            }
                            ll.setVisibility(View.GONE);
                            detail_find.setText("重新查询");
                            scroll.setVisibility(View.VISIBLE);
                            assert distribution != null;
                            pj_time.setText("大约" + distribution.getWaitTime() + "分钟");
                            people_count.setText(distribution.getQueueNum());
                            yy_id.setText(netNumber);

                            String name_str = (String) yy_spinner.getSelectedItem();
                            name.setText(name_str);
                            String address_str = null;
                            for (int i = 0; i < list_di.size(); i++) {
                                if (name_str.equals(list_di.get(i).getDepName())) {
                                    address_str = list_di.get(i).getDepAddress();
                                    break;
                                }
                            }
                            address.setText(address_str);

                        } catch (Exception ignored) {
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
                }

        );
    }

    private void getImformation() {
        JSONObject obj = new JSONObject();
        String jsonData = null;
        try {
            jsonData = JsonAnalysis.putJsonBody(obj, "2006").toString();
        } catch (JSONException ignored) {
            pro_dialog.dismiss();
        }
        RequestParams params = new RequestParams(SERVICE_REQUEST_URL);
        params.addBodyParameter("code", "2006");// 添加到请求body体的参数, 只有POST, PUT, PATCH, DELETE请求支持.
        params.addBodyParameter("json", jsonData);
        params.setCancelFast(true);
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
                    JSONArray array = new JSONObject(body).getJSONArray("bmxxList");
                    if (array.length() == 0) {
                        ToastUtils.showShort(x.app(), Const.NULL_STR);
                        return;
                    }
                    Distribution distribution;
                    for (int j = 0; j < array.length(); j++) {
                        distribution = new Gson().fromJson(array.get(j).toString(), Distribution.class);
                        list.add(distribution.getDepName());
                        list_di.add(distribution);
                    }
                    SpinnerAdapter2 adapter = new SpinnerAdapter2(x.app(), list);
                    yy_spinner.setAdapter(adapter);

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
                pro_dialog.dismiss();
            }
        });
    }


    private void getType(String string) {
        pro_dialog.show();
        for (int i = 0; i < list_di.size(); i++) {
            if (string.equals(list_di.get(i).getDepName())) {
                netNumber = list_di.get(i).getId();
                break;
            }
        }
        JSONObject obj = new JSONObject();
        String jsonData = null;
        try {
            obj.put("NetNumber", netNumber);
            jsonData = JsonAnalysis.putJsonBody(obj, "2093").toString();
        } catch (JSONException ignored) {
        }
        RequestParams params = new RequestParams(SERVICE_REQUEST_URL);
        params.addBodyParameter("code", "2093");// 添加到请求body体的参数, 只有POST, PUT, PATCH, DELETE请求支持.
        params.addBodyParameter("json", jsonData);
        params.setCancelFast(true);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    String msg = JsonAnalysis.getMsg(result);
                    if (!"交易成功".equals(msg)) {
                        ToastUtils.showShort(x.app(), msg);
                        yw_spinner.setAdapter(null);
                        return;
                    }
                    String body = JsonAnalysis.getJsonBody(result);
                    String body_str = new JSONObject(body).getString("JobList");

                    JSONArray array = new JSONArray(body_str);
                    if (array.length() == 0) {
                        ToastUtils.showShort(x.app(), Const.NULL_STR);
                        return;
                    }
                    list_type.clear();
                    ditype.clear();
                    DistributionType distribution;
                    for (int j = 0; j < array.length(); j++) {
                        distribution = new Gson().fromJson(array.get(j).toString(), DistributionType.class);
                        list_type.add(distribution.getJobName());
                        ditype.add(distribution);
                    }

                    SpinnerAdapter2 adapter = new SpinnerAdapter2(x.app(), list_type);
                    yw_spinner.setAdapter(adapter);

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
                pro_dialog.dismiss();
            }
        });
    }


}
