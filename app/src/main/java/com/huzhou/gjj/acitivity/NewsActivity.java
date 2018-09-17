package com.huzhou.gjj.acitivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.huzhou.gjj.R;
import com.huzhou.gjj.adapter.NewsAdapter;
import com.huzhou.gjj.bean.News;
import com.huzhou.gjj.utils.Const;
import com.huzhou.gjj.utils.DateUtils;
import com.huzhou.gjj.utils.JsonAnalysis;
import com.huzhou.gjj.utils.ToastUtils;
import com.huzhou.gjj.viewUtils.ClearWriteEditText;
import com.huzhou.gjj.viewUtils.LoadingDialog;
import com.huzhou.gjj.viewUtils.MyListView;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.huzhou.gjj.R.id.xlistview;
import static com.huzhou.gjj.utils.Const.NOINTENT_STR;
import static com.huzhou.gjj.utils.Const.SERVICE_REQUEST_URL;


@ContentView(R.layout.activity_news)
public class NewsActivity extends BaseAppCompatActivity implements
        MyListView.IXListViewListener {

    @ViewInject(R.id.gjz)
    private ClearWriteEditText gjz;

    @ViewInject(R.id.start_date_text)
    private TextView start_date;

    @ViewInject(R.id.end_date_text)
    private TextView end_date_text;

    @ViewInject(R.id.detail_find_btn)
    private Button detail_find_btn;

    @ViewInject(xlistview)
    private MyListView expandList;

    @ViewInject(R.id.ll)
    private LinearLayout ll;

    private DatePickerDialog dialog_start;
    private DatePickerDialog dialog_end;
    private MyProgressDialog pro_dialog;
    private String start = null;
    private String end = null;

    private int pageNum = 1;
    private String totalNum = "";

    private List<News> list = new ArrayList<>();
    private NewsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        expandList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.setSelectedPosition(position);
                adapter.notifyDataSetChanged();
                Intent mIntent = new Intent(NewsActivity.this, NewsSingleActivity.class);
                Bundle mBundle = new Bundle();
                mBundle.putSerializable("news_data", list.get(position - 1));
                mIntent.putExtras(mBundle);
                startActivity(mIntent);
            }
        });
        pro_dialog = LoadingDialog.createLoadingDialog(this, "加载中...");
        pro_dialog.setActivity(this);
        expandList.setPullRefreshEnable(true);
        expandList.setPullLoadEnable(true);
        expandList.setXListViewListener(this);
        registerForContextMenu(expandList);// 给ExpandListView添加上下文菜单


        Calendar c = Calendar.getInstance();
        start_date.setText("请选择开始日期");
        end_date_text.setText("请选择结束日期");

        dialog_start = new DatePickerDialog(this, AlertDialog.THEME_HOLO_LIGHT,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        String dayOfMonth_str;
                        String monthOfYear_str;
                        if (dayOfMonth < 10)
                            dayOfMonth_str = "0" + dayOfMonth;
                        else
                            dayOfMonth_str = "" + dayOfMonth;

                        monthOfYear = monthOfYear + 1;
                        if (monthOfYear < 10)
                            monthOfYear_str = "0" + monthOfYear;
                        else
                            monthOfYear_str = "" + monthOfYear;
                        start = year + monthOfYear_str
                                + dayOfMonth_str;
                        start_date.setText(year + "年" + monthOfYear + "月"
                                + dayOfMonth + "日");
                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH),
                c.get(Calendar.DAY_OF_MONTH));
        dialog_end = new DatePickerDialog(this, AlertDialog.THEME_HOLO_LIGHT,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        String dayOfMonth_str;
                        String monthOfYear_str;
                        if (dayOfMonth < 10)
                            dayOfMonth_str = "0" + dayOfMonth;
                        else
                            dayOfMonth_str = "" + dayOfMonth;

                        monthOfYear = monthOfYear + 1;
                        if (monthOfYear < 10)
                            monthOfYear_str = "0" + monthOfYear;
                        else
                            monthOfYear_str = "" + monthOfYear;
                        end = year + monthOfYear_str
                                + dayOfMonth_str;
                        end_date_text.setText(year + "年" + monthOfYear + "月"
                                + dayOfMonth + "日");
                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH),
                c.get(Calendar.DAY_OF_MONTH));
    }

    @Event(value = {R.id.detail_find_btn, R.id.start_date_text, R.id.end_date_text}, type = View.OnClickListener.class)
    private void getEvent(View v) {
        switch (v.getId()) {
            case R.id.detail_find_btn:
                Date date_start = DateUtils.strToDate(start);
                Date date_end = DateUtils.strToDate(end);
                if (!TextUtils.isEmpty(start) && date_start.after(DateUtils.getCurrentTime())) {
                    ToastUtils.showShort(x.app(), "不能超过当前日期");
                    return;
                } else if (!TextUtils.isEmpty(end) && date_end.after(DateUtils.getCurrentTime())) {
                    ToastUtils.showShort(x.app(), "不能超过当前日期");
                    return;
                } else if (!TextUtils.isEmpty(start) && !TextUtils.isEmpty(end) && date_start.after(date_end)) {
                    ToastUtils.showShort(x.app(), "起始日期不能超过结束日期");
                    return;
                } else if (!TextUtils.isEmpty(start) && !TextUtils.isEmpty(end) && DateUtils.CheckDate(date_start, date_end)) {
                    ToastUtils.showShort(x.app(), "与起始日期的间隔不能超过三年");
                    return;
                } else {
                    String str = detail_find_btn.getText().toString().trim();
                    if ("重新查询".equals(str)) {
                        ll.setVisibility(View.VISIBLE);
                        detail_find_btn.setText("查询");
                        return;
                    }
                    pro_dialog.show();
                    initDatas(pageNum, "getdata");
                }
                break;
            case R.id.start_date_text:
                dialog_start.show();
                break;
            case R.id.end_date_text:
                dialog_end.show();
                break;
        }

    }

    private void initDatas(int page, final String str) {
        JSONObject obj = new JSONObject();
        String jsonData = null;
        try {
            String gjz_str = gjz.getText().toString().trim();
            obj.put("subSystem", "0");
            obj.put("infoTitle", gjz_str);
            obj.put("ksrq", start);
            obj.put("zzrq", end);
            obj.put("page", page);
            obj.put("rows", 10);
            obj.put("startRowNum", 0);

            JSONObject obj3 = new JSONObject();
            obj3.put("TrsCode", "1014");
            obj3.put("ChanCode", "02");
            jsonData = JsonAnalysis.getJsonData(obj, obj3);

        } catch (JSONException ignored) {
        }
        RequestParams params = new RequestParams(SERVICE_REQUEST_URL);
        params.addBodyParameter("code", "1014");// 添加到请求body体的参数, 只有POST, PUT, PATCH, DELETE请求支持.
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
                        ToastUtils.showLong(x.app(), msg);
                        return;
                    }
                    String body = JsonAnalysis.getJsonBody(result);
                    totalNum = new JSONObject(body).getString("totalNum");
                    News news;
                    JSONArray array = new JSONObject(body).getJSONArray("bizNoticeList");
                    if (array.length() == 0) {
                        ToastUtils.showLong(x.app(), Const.NULL_STR);
                        return;
                    }
                    if (!"onLoadMore".equals(str)) {
                        list.clear();
                    }
                    for (int i = 0; i < array.length(); i++) {
                        news = new Gson().fromJson(array.getJSONObject(i).toString(), News.class);
                        list.add(news);
                    }
                    ll.setVisibility(View.GONE);
                    detail_find_btn.setText("重新查询");
                    if ("onLoadMore".equals(str)) {
                        adapter.notifyDataSetChanged();
                    } else {
                        pageNum = 1;
                        adapter = new NewsAdapter(NewsActivity.this, list);
                        expandList.setAdapter(adapter);
                    }
                } catch (Exception e) {
                    ToastUtils.showLong(x.app(), NOINTENT_STR);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                pro_dialog.dismiss();
                ToastUtils.showLong(x.app(), NOINTENT_STR);
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
    public void onRefresh() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        expandList.setRefreshTime(sd.format(date));
        initDatas(1, "onRefresh");
    }

    @Override
    public void onLoadMore() {
        if ((list.size() + "").equals(totalNum)) {
            expandList.stopLoadMore();
        } else {
            pro_dialog.show();
            pageNum++;
            initDatas(pageNum, "onLoadMore");
        }
    }
}
