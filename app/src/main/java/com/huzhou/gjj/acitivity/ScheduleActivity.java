package com.huzhou.gjj.acitivity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.huzhou.gjj.R;
import com.huzhou.gjj.bean.Number;
import com.huzhou.gjj.bean.Schedule;
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

import static com.huzhou.gjj.utils.Const.NOINTENT_STR;
import static com.huzhou.gjj.utils.Const.SERVICE_REQUEST_URL;
import static com.huzhou.gjj.utils.Const.SERVICE_RHIDAIN_URL;

@ContentView(R.layout.activity_schedule)
public class ScheduleActivity extends BaseAppCompatActivity {
    private MyProgressDialog pro_dialog;
    @ViewInject(R.id.loan_name)
    private TextView loan_name;

    @ViewInject(R.id.loan_id)
    private TextView loan_id;

    @ViewInject(R.id.cen_id)
    private TextView cen_id;

    @ViewInject(R.id.jigou_id)
    private TextView jigou_id;

    @ViewInject(R.id.loan_xz)
    private TextView loan_xz;

    @ViewInject(R.id.loan_money)
    private TextView loan_money;

    @ViewInject(R.id.hun_fs)
    private TextView hun_fs;

    @ViewInject(R.id.wt_bank)
    private TextView wt_bank;

    @ViewInject(R.id.now_zt)
    private TextView now_zt;

    @ViewInject(R.id.gf_address)
    private TextView gf_address;

    @ViewInject(R.id.fw_type)
    private TextView fw_type;

    @ViewInject(R.id.czy_id)
    private TextView czy_id;

    @ViewInject(R.id.loan_progress)
    private TextView loan_progress;

    @ViewInject(R.id.scroll)
    private ScrollView scroll;

    @ViewInject(R.id.recyview)
    private RecyclerView recyview;

    private Schedule schedule;
    List<String> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initdata();
    }

    private void initdata() {
        JSONObject obj = new JSONObject();
        String jsonData = null;
        try {
            obj.put("jkrgjjzh", DbUtils.getUserData().getPersonAcctNo());
            jsonData = JsonAnalysis.putJsonBody(obj, "2055").toString();
        } catch (JSONException ignored) {
        }
        RequestParams params = new RequestParams(SERVICE_REQUEST_URL);
        params.addBodyParameter("code", "2055");// 添加到请求body体的参数, 只有POST, PUT, PATCH, DELETE请求支持.
        params.addBodyParameter("json", jsonData);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    String msg = JsonAnalysis.getMsg(result);
                    if (!"交易成功".equals(msg)) {
                        ToastUtils.showLong(x.app(), msg);
                        pro_dialog.dismiss();
                        return;
                    }
                    String body = JsonAnalysis.getJsonBody(result);
                    schedule = new Gson().fromJson(body, Schedule.class);
                    if (TextUtils.isEmpty(schedule.getZxbh())) {
                        ToastUtils.showLong(x.app(), Const.NULL_STR);
                        pro_dialog.dismiss();
                        return;
                    }
                    getZhiDian();
                    loan_name.setText(schedule.getJkrxm());
                    loan_id.setText(schedule.getJkhtbh());
                    cen_id.setText(schedule.getZxbh());
                    jigou_id.setText(schedule.getJgbh());
                    loan_money.setText(schedule.getHtdkje());
                    gf_address.setText(schedule.getFwzl());
                    czy_id.setText(schedule.getCzybh());
                    String str = schedule.getDkjc();
                    if (str.contains(schedule.getCzybh()))
                        str = str.replace(schedule.getCzybh() + ",", "");
                    loan_progress.setText(str);

                    int code = checkJD(schedule.getDkzt());
                    if (code != 7) {
                        JdAdapter adapter = new JdAdapter(ScheduleActivity.this, list, code);
                        recyview.setAdapter(adapter);
                    }
                } catch (Exception ignored) {
                    ToastUtils.showLong(x.app(), Const.NULL_STR);
                    pro_dialog.dismiss();
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

    private void getZhiDian() {
        JSONArray array = new JSONArray();
        try {
            JSONObject data1 = new JSONObject();
            data1.put("dictType", "dkxz");
            JSONObject data2 = new JSONObject();
            data2.put("dictType", "hkfs");
            JSONObject data3 = new JSONObject();
            data3.put("dictType", "dkzt");
            JSONObject data4 = new JSONObject();
            data4.put("dictType", "fwlx");
            JSONObject data5 = new JSONObject();
            data5.put("dictType", "khyhlx");
            array.put(data1);
            array.put(data2);
            array.put(data3);
            array.put(data4);
            array.put(data5);
        } catch (JSONException ignored) {
        }
        RequestParams params = new RequestParams(SERVICE_RHIDAIN_URL);
        params.addBodyParameter("typeArray", array.toString());
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                pro_dialog.dismiss();
                try {
                    final JSONArray array = new JSONArray(result);
                    if (array.length() == 0) {
                        return;
                    }
                    Number number;
                    String dkxz = null, hkfs = null, dkzt = null, fwlx = null, khyhlx = null;
                    for (int i = 0; i < array.length(); i++) {
                        number = new Gson().fromJson(array.getJSONObject(i).toString(), Number.class);
                        if ("dkxz".equals(number.getDictType()) && schedule.getDkxz().equals(number.getCode()))
                            dkxz = number.getName();
                        else if ("hkfs".equals(number.getDictType()) && schedule.getDkhkfs().equals(number.getCode()))
                            hkfs = number.getName();
                        else if ("dkzt".equals(number.getDictType()) && schedule.getDkzt().equals(number.getCode()))
                            dkzt = number.getName();
                        else if ("fwlx".equals(number.getDictType()) && schedule.getFwlx().equals(number.getCode()))
                            fwlx = number.getName();
                        else if ("khyhlx".equals(number.getDictType()) && schedule.getSwtyhdm().equals(number.getCode()))
                            khyhlx = number.getName();
                    }
                    loan_xz.setText(dkxz);
                    hun_fs.setText(hkfs);
                    wt_bank.setText(khyhlx);
                    now_zt.setText(dkzt);
                    fw_type.setText(fwlx);
                    scroll.setVisibility(View.VISIBLE);
                } catch (Exception ignored) {
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

    private void initView() {
        list.add(0, "已受理");
        list.add(1, "已审查");
        list.add(2, "已审批");
        list.add(3, "待办证");
        list.add(4, "已办证");
        list.add(5, "指标核定");
        list.add(6, "已放款");
        //设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyview.setLayoutManager(linearLayoutManager);

        pro_dialog = LoadingDialog.createLoadingDialog(this, "加载中...");
        pro_dialog.setActivity(this);
        pro_dialog.show();
    }

    private int checkJD(String str) {
        int code = 7;
        int num;
        if (!TextUtils.isEmpty(str)) {
            if (str.startsWith("0")) {
                str = str.replace("0", "");
            }
            num = Integer.parseInt(str);
            if (num >= 1) code = 0;
            if (num >= 4) code = 1;
            if (num >= 6) code = 2;
            if (num >= 7) code = 3;
            if (num >= 8) code = 4;
            if (num >= 9) code = 5;
            if (num == 13) code = 6;
        }
        return code;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    class JdAdapter extends RecyclerView.Adapter<JdAdapter.ViewHolder> {
        private LayoutInflater mInflater;
        private List<String> mDatas;
        private int code;

        JdAdapter(Context context, List<String> mDatas, int code) {
            mInflater = LayoutInflater.from(context);
            this.mDatas = mDatas;
            this.code = code;
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            ViewHolder(View arg0) {
                super(arg0);
            }

            View xian;
            TextView status, yuan;
            RelativeLayout rl2;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = mInflater.inflate(R.layout.adapter_schedule,
                    parent, false);
            ViewHolder viewHolder = new ViewHolder(view);

            viewHolder.status = (TextView) view
                    .findViewById(R.id.status);
            viewHolder.yuan = (TextView) view
                    .findViewById(R.id.yuan);
            viewHolder.xian = view.findViewById(R.id.xian);
            viewHolder.rl2 = (RelativeLayout) view
                    .findViewById(R.id.rl2);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.status.setText(mDatas.get(position));
            holder.yuan.setText("" + (position + 1));
            if (position <= code) {
                holder.yuan.setBackgroundResource(R.drawable.jindu);
                holder.xian.setBackgroundResource(R.color.jindu);
                holder.status.setTextColor(getColor(R.color.jindu));
            } else {
                holder.yuan.setBackgroundResource(R.drawable.jindu2);
                holder.xian.setBackgroundResource(R.color.jindu2);
                holder.status.setTextColor(getColor(R.color.jindu2));
            }
            if (position == 0) {
                holder.rl2.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public int getItemCount() {
            return mDatas.size();
        }
    }
}
