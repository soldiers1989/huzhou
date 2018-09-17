package com.huzhou.gjj.utils;


import com.google.gson.Gson;
import com.huzhou.gjj.bean.UserInfo;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import static com.huzhou.gjj.utils.Const.NOINTENT_STR;
import static com.huzhou.gjj.utils.Const.SERVICE_REQUEST_URL;

/**
 * Toast统一管理类
 */


public class CodeUtils {


    public static void sendCode(String khbh, String regisphone) {
        JSONObject obj = new JSONObject();
        String jsonData = null;
        try {
            obj.put("custCode", khbh);
            obj.put("phone", regisphone);
            jsonData = JsonAnalysis.putJsonBody(obj, "1004").toString();
        } catch (JSONException ignored) {
        }
        RequestParams params = new RequestParams(SERVICE_REQUEST_URL);
        params.addBodyParameter("code", "1004");// 添加到请求body体的参数, 只有POST, PUT, PATCH, DELETE请求支持.
        params.addBodyParameter("json", jsonData);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    String body = JsonAnalysis.getJsonBody(result);
//                    ToastUtils.showLong(x.app(), new JSONObject(body).getString("smscode"));
                } catch (Exception ignored) {
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
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

    public static void CheckIsExists(final String regiscard_string, final String regisphone) {
        JSONObject obj = new JSONObject();
        String jsonData = null;
        try {
            obj.put("grzh", regiscard_string);
            obj.put("zhlx", "01");
            jsonData = JsonAnalysis.putJsonBody(obj, "2002").toString();
        } catch (JSONException ignored) {
        }
        RequestParams params = new RequestParams(SERVICE_REQUEST_URL);
        params.addBodyParameter("code", "2002");// 添加到请求body体的参数, 只有POST, PUT, PATCH, DELETE请求支持.
        params.addBodyParameter("json", jsonData);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                UserInfo info ;
                try {
                    String msg = JsonAnalysis.getMsg(result);
                    if (!"交易成功".equals(msg)) {
                        ToastUtils.showLong(x.app(), msg);
                        return;
                    }
                    String body = JsonAnalysis.getJsonBody(result);
                    info = new Gson().fromJson(body, UserInfo.class);
                    if (info.getKhbh() != null) {
                        sendCode(info.getKhbh(), regisphone);
                    } else
                        ToastUtils.showShort(x.app(), "客户编号不存在");
                } catch (Exception e) {
                    ToastUtils.showShort(x.app(), "客户编号不存在");
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
            }
        });
    }

}
