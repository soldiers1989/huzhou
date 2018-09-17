package com.huzhou.gjj.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.huzhou.gjj.R;
import com.huzhou.gjj.adapter.EditChangedListener;
import com.huzhou.gjj.adapter.PlanAdapter;
import com.huzhou.gjj.bean.Plan;
import com.huzhou.gjj.utils.Const;
import com.huzhou.gjj.utils.JsonAnalysis;
import com.huzhou.gjj.utils.PhoneUtils;
import com.huzhou.gjj.utils.ToastUtils;
import com.huzhou.gjj.viewUtils.ClearWriteEditText;
import com.huzhou.gjj.viewUtils.LoadingDialog;
import com.huzhou.gjj.viewUtils.MyProgressDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import static com.huzhou.gjj.utils.Const.NOINTENT_STR;
import static com.huzhou.gjj.utils.Const.SERVICE_REQUEST_URL;


public class ZhixunFragment2 extends Fragment {
    private View view;
    private MyProgressDialog pro_dialog;

    private ClearWriteEditText loan_money;
    private Spinner loan_year, is_twe, loan_fs;
    private LinearLayout linearLayout;
    private ExpandableListView listview;
    private Button detail_find;
    private TextView massage_end;
    private List<Plan> list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_zhixun2, null);
        initView();
        return view;
    }


    private void initView() {
        pro_dialog = LoadingDialog.createLoadingDialog(getActivity(), "加载中...");
        pro_dialog.setActivity(getActivity());

        loan_money = (ClearWriteEditText) view.findViewById(R.id.loan_money);
        loan_year = (Spinner) view.findViewById(R.id.loan_year);
        is_twe = (Spinner) view.findViewById(R.id.is_twe);
        loan_fs = (Spinner) view.findViewById(R.id.loan_fangshi);

        linearLayout = (LinearLayout) view.findViewById(R.id.linearLayout);
        listview = (ExpandableListView) view.findViewById(R.id.listview);
        detail_find = (Button) view.findViewById(R.id.detail_find);
        massage_end = (TextView) view.findViewById(R.id.massage_end);


        loan_money.addTextChangedListener(new EditChangedListener(loan_money));
        view.findViewById(R.id.detail_find).setOnClickListener(new View.OnClickListener() {
                                                                   @Override
                                                                   public void onClick(View v) {
                                                                       String detail_find_str = detail_find.getText().toString().trim();
                                                                       if ("重新计算".equals(detail_find_str)) {
                                                                           linearLayout.setVisibility(View.VISIBLE);
                                                                           detail_find.setText("开始计算");
                                                                           return;
                                                                       }
                                                                       String loan_money_str = loan_money.getText().toString().trim();
                                                                       if (TextUtils.isEmpty(loan_money_str)) {
                                                                           loan_money.setError("不能为空！");
                                                                           loan_fs.requestFocus();
                                                                           return;
                                                                       } else if ("0".equals(loan_money_str)) {
                                                                           loan_money.setError("金额必须大于0！");
                                                                           loan_money.requestFocus();
                                                                           return;
                                                                       } else if (!PhoneUtils.IsRight(loan_money_str)) {
                                                                           loan_money.setError("请输入正确的格式！");
                                                                           loan_money.requestFocus();
                                                                           return;
                                                                       }
                                                                       String is_twe_str = (String) is_twe.getSelectedItem();
                                                                       if ("否".equals(is_twe_str))
                                                                           is_twe_str = "0";
                                                                       else is_twe_str = "1";
                                                                       String loan_fs_str = (String) loan_fs.getSelectedItem();
                                                                       if ("等额本金".equals(loan_fs_str))
                                                                           loan_fs_str = "22";
                                                                       else loan_fs_str = "21";
                                                                       String loan_year_str = (String) loan_year.getSelectedItem();
                                                                       String str1 = loan_year_str.substring(0, 2);
                                                                       if (str1.contains("年"))
                                                                           str1 = str1.substring(0, 1);

                                                                       pro_dialog.show();


                                                                       JSONObject obj = new JSONObject();
                                                                       String jsonData = null;
                                                                       try {
                                                                           obj.put("DKED", loan_money_str);
                                                                           obj.put("DKNX", str1);
                                                                           obj.put("SFECDK", is_twe_str);
                                                                           obj.put("HKFS", loan_fs_str);

                                                                           JSONObject obj3 = new JSONObject();
                                                                           obj3.put("TrsCode", "2087");
                                                                           obj3.put("ChanCode", "02");
                                                                           jsonData = JsonAnalysis.getJsonData(obj, obj3);
                                                                       } catch (JSONException ignored) {
                                                                       }
                                                                       RequestParams params = new RequestParams(SERVICE_REQUEST_URL);
                                                                       params.addBodyParameter("code", "2087");// 添加到请求body体的参数, 只有POST, PUT, PATCH, DELETE请求支持.
                                                                       params.addBodyParameter("json", jsonData);
                                                                       x.http().post(params, new Callback.CommonCallback<String>() {
                                                                                   @Override
                                                                                   public void onSuccess(String result) {
                                                                                       pro_dialog.dismiss();
                                                                                       try {
                                                                                           String msg = JsonAnalysis.getMsg(result);
                                                                                           if (!"交易成功".equals(msg)) {
                                                                                               ToastUtils.showLong(x.app(), msg);
                                                                                               return;
                                                                                           }
                                                                                           String body = JsonAnalysis.getJsonBody(result);
                                                                                           JSONArray array = new JSONObject(body).getJSONArray("yhkxxlist");
                                                                                           if (array.length() == 0) {
                                                                                               ToastUtils.showLong(x.app(), Const.NULL_STR);
                                                                                               return;
                                                                                           }
                                                                                           list = new ArrayList<>();
                                                                                           Plan plan = null;
                                                                                           Plan change;
                                                                                           for (int j = 0; j < array.length(); j++) {
                                                                                               if (j == 0)
                                                                                               plan = new Gson().fromJson(array.get(j).toString(), Plan.class);

                                                                                               change = new Gson().fromJson(array.get(j).toString(), Plan.class);
                                                                                               list.add(change);
                                                                                           }
                                                                                           listview.setVisibility(View.VISIBLE);
                                                                                           linearLayout.setVisibility(View.GONE);
                                                                                           massage_end.setVisibility(View.VISIBLE);

                                                                                           detail_find.setText("重新计算");

                                                                                           String DKLL = null;
                                                                                           if (!TextUtils.isEmpty(plan.getDKLL()))
                                                                                               DKLL = Double.parseDouble(plan.getDKLL()) * 100 + "%";
                                                                                           massage_end.setText("温馨提醒:首期还款金额以实际放款计算为准!\n" + "首期还款额：" + plan.getYHKE()
                                                                                                   + "元，贷款年利率：" + DKLL + "\n利息总额："
                                                                                                   + plan.getLXZE() + "元，本息总额："
                                                                                                   + plan.getBXZE() + "元"
                                                                                           );
                                                                                           PlanAdapter adapter = new PlanAdapter(getActivity(), list);
                                                                                           listview.setAdapter(adapter);
                                                                                           for (int i = 0; i < list.size(); i++) {
                                                                                               listview.expandGroup(i);
                                                                                           }
                                                                                       } catch (
                                                                                               Exception ignored
                                                                                               )

                                                                                       {
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
                                                                               }

                                                                       );
                                                                   }
                                                               }

        );
    }
}
