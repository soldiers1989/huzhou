package com.huzhou.gjj.utils;


import android.content.Context;
import android.text.TextUtils;
import android.widget.Spinner;

import com.google.gson.Gson;
import com.huzhou.gjj.AppApplication;
import com.huzhou.gjj.bean.Number;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.DbManager;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import static com.huzhou.gjj.utils.Const.SERVICE_REQUEST_URL;
import static com.huzhou.gjj.utils.Const.SERVICE_RHIDAIN_URL;

public class JsonAnalysis {


    /**
     * 获取obj里面的boad内容
     *
     * @param
     * @return
     * @throws JSONException
     */
    public static double addToDouble(String a, String b) {
        if (TextUtils.isEmpty(a)) a = "0.00";
        if (TextUtils.isEmpty(b)) b = "0.00";
        return Double.parseDouble(a) + Double.parseDouble(b);
    }


    public static String getJsonBody(String data) throws JSONException {
        JSONObject body = new JSONObject(data).getJSONObject("ROOT").getJSONObject("BODY");
        return body.toString();
    }

    public static String getMsg(String data) throws JSONException {
        String body = new JSONObject(data).getJSONObject("ROOT").getJSONObject("HEAD").getString("ResMsg");
        return body;
    }

    public static JSONObject putJsonBody(JSONObject obj, String code) throws JSONException {
        JSONObject head = new JSONObject();
        head.put("ChanCode", "02");
        head.put("TrsCode", code);
        JSONObject obj2 = new JSONObject();
        JSONObject obj3 = new JSONObject();
        obj2.put("BODY", obj);
        obj2.put("HEAD", head);
        obj3.put("ROOT", obj2);
        return obj3;
    }


    public static String putJson2(JSONObject obj, String code) throws JSONException {
        JSONObject head = new JSONObject();
        head.put("ChanCode", "02");
        head.put("TrsCode", code);
        head.put("ReqTrsCent", DbUtils.getUserData().getCenterCode());
        JSONObject obj2 = new JSONObject();
        JSONObject obj3 = new JSONObject();
        obj2.put("BODY", obj);
        obj2.put("HEAD", head);
        obj3.put("ROOT", obj2);
        return obj3.toString();
    }


    public static String getJsonData(JSONObject body, JSONObject head) throws JSONException {
        JSONObject obj = new JSONObject();
        obj.put("HEAD", head);
        obj.put("BODY", body);
        JSONObject obj1 = new JSONObject();
        obj1.put("ROOT", obj);
        return obj1.toString();
    }

    public static void TableDate(final SharedPreferencesUtil utils, final Context context) {
        JSONObject obj = new JSONObject();
        final long tiem = System.currentTimeMillis();
        String jsonData = null;
        try {
            obj.put("tableName", "DICT_ENTRY");
            jsonData = JsonAnalysis.putJsonBody(obj, "1020").toString();
        } catch (JSONException e) {
        }
        RequestParams params = new RequestParams(SERVICE_REQUEST_URL);
        params.addBodyParameter("code", "1020");// 添加到请求body体的参数, 只有POST, PUT, PATCH, DELETE请求支持.
        params.addBodyParameter("json", jsonData);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    ToastUtils.showShort(x.app(), (System.currentTimeMillis() - tiem) / 1000 + "s");
                    String body = JsonAnalysis.getJsonBody(result);
                    String array_str = new JSONObject(body).getString("data");
                    final JSONArray array = new JSONArray(array_str);
                    if (array.length() == 0) return;
                    new Thread() {
                        @Override
                        public void run() {
                            try {
                                Number number;
                                List<Number> list = new ArrayList<Number>();
                                DbManager db = x.getDb(((AppApplication) context.getApplicationContext()).getDaoConfig());
                                for (int i = 0; i < array.length(); i++) {
                                    number = new Gson().fromJson(array.getJSONObject(i).toString(), Number.class);
                                    list.add(number);
                                }
                                db.save(list);
                            } catch (Exception e) {
                                ToastUtils.showShort(x.app(), "数据字典加载失败！");
                            }
                        }
                    }.start();

                } catch (Exception e) {
                    ToastUtils.showShort(x.app(), "数据字典加载失败！");
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                ToastUtils.showShort(x.app(), "数据加载失败!");
            }

            @Override
            public void onCancelled(CancelledException cex) {
            }

            @Override
            public void onFinished() {
            }
        });
    }


    public static void getZhiDian_spinner(String[] strings, Spinner spinner) {
        JSONObject obj = new JSONObject();
        String jsonData = null;
        try {
            JSONArray array = new JSONArray();
            JSONObject data;
            for (int i = 0; i < strings.length; i++) {
                data = new JSONObject();
                data.put("dictType", strings[i]);
                array.put(data);
            }
            obj.put("typeArray", array);
            jsonData = JsonAnalysis.putJsonBody(obj, "1020").toString();
        } catch (JSONException e) {
        }
        RequestParams params = new RequestParams(SERVICE_RHIDAIN_URL);
        params.addBodyParameter("code", "1020");// 添加到请求body体的参数, 只有POST, PUT, PATCH, DELETE请求支持.
        params.addBodyParameter("json", jsonData);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    String body = JsonAnalysis.getJsonBody(result);
                    String array_str = new JSONObject(body).getString("data");
                    final JSONArray array = new JSONArray(array_str);
                    if (array.length() == 0) return;
                    Number number;
                    List<Number> list = new ArrayList<Number>();
                    for (int i = 0; i < array.length(); i++) {
                        number = new Gson().fromJson(array.getJSONObject(i).toString(), Number.class);
                        list.add(number);
                    }
                } catch (Exception e) {
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                ToastUtils.showShort(x.app(), "数据加载失败!");
            }

            @Override
            public void onCancelled(CancelledException cex) {
            }

            @Override
            public void onFinished() {
            }
        });
    }

    public static void getZhiDian(String[] strings, Spinner spinner) {
        JSONObject obj = new JSONObject();
        String jsonData = null;
        try {
            JSONArray array = new JSONArray();
            JSONObject data;
            for (int i = 0; i < strings.length; i++) {
                data = new JSONObject();
                data.put("dictType", strings[i]);
                array.put(data);
            }
            obj.put("typeArray", array);
            jsonData = JsonAnalysis.putJsonBody(obj, "1020").toString();
        } catch (JSONException e) {
        }
        RequestParams params = new RequestParams(SERVICE_RHIDAIN_URL);
        params.addBodyParameter("code", "1020");// 添加到请求body体的参数, 只有POST, PUT, PATCH, DELETE请求支持.
        params.addBodyParameter("json", jsonData);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    String body = JsonAnalysis.getJsonBody(result);
                    String array_str = new JSONObject(body).getString("data");
                    final JSONArray array = new JSONArray(array_str);
                    if (array.length() == 0) return;
                    Number number;
                    List<Number> list = new ArrayList<Number>();
                    for (int i = 0; i < array.length(); i++) {
                        number = new Gson().fromJson(array.getJSONObject(i).toString(), Number.class);
                        list.add(number);
                    }
                } catch (Exception e) {
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                ToastUtils.showShort(x.app(), "数据加载失败!");
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

