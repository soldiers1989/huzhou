package com.huzhou.gjj.acitivity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Xml;
import android.widget.TextView;
import android.widget.Toast;

import com.huzhou.gjj.AppManager;
import com.huzhou.gjj.R;
import com.huzhou.gjj.bean.XmlData;
import com.huzhou.gjj.utils.SharedPreferencesUtil;
import com.huzhou.gjj.viewUtils.LoadingDialog;
import com.huzhou.gjj.viewUtils.MyProgressDialog;

import org.xmlpull.v1.XmlPullParser;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;


@ContentView(R.layout.activity_login3)
public class LoginActivity3 extends AppCompatActivity {
    @ViewInject(R.id.khbh)
    private TextView khbh;
    @ViewInject(R.id.zxbh)
    private TextView zxbh;
    @ViewInject(R.id.grxm)
    private TextView grxm;
    @ViewInject(R.id.zjhm)
    private TextView zjhm;
    @ViewInject(R.id.csrq)
    private TextView csrq;
    @ViewInject(R.id.dwmc)
    private TextView dwmc;
    @ViewInject(R.id.dwdz)
    private TextView dwdz;

    private MyProgressDialog pro_dialog;
    private SharedPreferencesUtil utils;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //完成视图注解框架的初始化
        x.view().inject(this);
        pro_dialog = LoadingDialog.createLoadingDialog(this, "加载中...");
        pro_dialog.setActivity(this);
        utils = new SharedPreferencesUtil(this);
        getdata();
    }


    @Override
    protected void onDestroy() {
        if (pro_dialog.isShowing())
            pro_dialog.dismiss();
        AppManager.getAppManager().deleteAllActivity();
        super.onDestroy();
    }

    //    获取assets文件To String
    private String getFromAssets(String fileName) {
        try {
            InputStreamReader inputReader = new InputStreamReader(getResources().getAssets().open(fileName));
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line;
            String Result = "";
            while ((line = bufReader.readLine()) != null)
                Result += line;
            return Result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //    获取数据
    public void getdata() {
        pro_dialog.show();
        new Thread() {
            @Override
            public void run() {
                URL url;
                PrintWriter writer;
                HttpURLConnection conn;
                try {
                    //通过类装载器装载XML资源
//                    String out = getFromAssets("test.xml");
                    String out = createdata(utils.read("card_str"));
                    String path = "http://211.142.115.252:8081/pdsgjj4zfb/processAccess";
//			url = new URL("http://localhost:8081/ejx4basis01/processAccess");
                    url = new URL(path);
                    conn = (HttpURLConnection) url.openConnection();
//			conn.setRequestProperty("contentType", "GBK");
                    conn.addRequestProperty("code", "2001"); //这两个比较重要
                    conn.addRequestProperty("channel", "ZFB");
                    conn.setDoInput(true);
                    conn.setDoOutput(true);
//			conn.setRequestMethod("POST");
                    System.out.println("发送内容：\n" + out);
                    writer = new PrintWriter(new OutputStreamWriter(conn.getOutputStream(), "utf-8"));
                    writer.print(out);
                    writer.flush();
                    writer.close();
                    final XmlData data = getPeople(conn.getInputStream());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pro_dialog.dismiss();
                            if (data.getResCode().equals("000")) {
                                khbh.setText(data.getKhbh());
                                zxbh.setText(data.getZxbh());
                                grxm.setText(data.getGrxm());
                                zjhm.setText(data.getZjhm());
                                csrq.setText(data.getCsrq());
                                dwmc.setText(data.getDwmc());
                                dwdz.setText(data.getDwdz());
                            } else {
                                Toast.makeText(LoginActivity3.this, data.getResMsg(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } catch (Exception e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (pro_dialog.isShowing()) pro_dialog.dismiss();
                            Toast.makeText(LoginActivity3.this, "查询失败！", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }.start();
    }


    /**
     * 从XML文件中读取数据
     *
     * @param xml XML文件输入流
     */
    public XmlData getPeople(InputStream xml) throws Exception {
        XmlData person = null;
        // 利用ANDROID提供的API快速获得pull解析器
        XmlPullParser pullParser = Xml.newPullParser();
        // 设置需要解析的XML数据
        pullParser.setInput(xml, "UTF-8");
        // 取得事件
        int event = pullParser.getEventType();
        // 若为解析到末尾
        while (event != XmlPullParser.END_DOCUMENT) // 文档结束
        {
            String nodeName = pullParser.getName();
            switch (event) {
                case XmlPullParser.START_TAG: // 标签开始
                    if ("ROOT".equals(nodeName)) {
                        person = new XmlData();
                    } else if ("ResCode".equals(nodeName)) {
                        String name = pullParser.nextText();
                        assert person != null;
                        person.setResCode(name);
                    } else if ("ResMsg".equals(nodeName)) {
                        String name = pullParser.nextText();
                        assert person != null;
                        person.setResMsg(name);
                    } else if ("khbh".equals(nodeName)) {
                        String name = pullParser.nextText();
                        assert person != null;
                        person.setKhbh(name);
                    } else if ("zxbh".equals(nodeName)) {
                        String name = pullParser.nextText();
                        assert person != null;
                        person.setZxbh(name);
                    } else if ("zjhm".equals(nodeName)) {
                        String name = pullParser.nextText();
                        assert person != null;
                        person.setZjhm(name);
                    } else if ("grxm".equals(nodeName)) {
                        String name = pullParser.nextText();
                        assert person != null;
                        person.setGrxm(name);
                    } else if ("csrq".equals(nodeName)) {
                        String name = pullParser.nextText();
                        assert person != null;
                        person.setCsrq(name);
                    }
//                    else if ("minzu".equals(nodeName)) {
//                        String name = pullParser.nextText();
//                        assert person != null;
//                        person.setCsrq(name);
//                    }
                    else if ("dwmc".equals(nodeName)) {
                        String name = pullParser.nextText();
                        assert person != null;
                        person.setDwmc(name);
                    } else if ("dwdz".equals(nodeName)) {
                        String name = pullParser.nextText();
                        assert person != null;
                        person.setDwdz(name);
                    }
            }
            event = pullParser.next(); // 下一个标签
        }
        return person;
    }

    private String createdata(String card) {
        StringBuffer sb = new StringBuffer();
        sb.append("<?xml version=\"1.0\" encoding=\"GBK\"?>\n" +
                "<ROOT>\n" +
                "  <HEAD>\n" +
                "    <UnitCode>PDSGJJ</UnitCode>\n" +
                "    <ChanCode>ZFB</ChanCode>\n" +
                "    <ReqTrsCent>10001</ReqTrsCent>\n" +
                "    <ReqTrsBank>10001</ReqTrsBank>\n" +
                "    <ReqTrsTell>10001</ReqTrsTell>\n" +
                "    <ReqTrsTellName>10001</ReqTrsTellName>\n" +
                "    <ReqDate>20170808</ReqDate>\n" +
                "    <ReqTime>095121</ReqTime>\n" +
                "    <TrsCode>2001</TrsCode>\n" +
                "    <TrsChildCode>299</TrsChildCode>\n" +
                "    <ReqSerial>000000</ReqSerial>\n" +
                "    <Remark></Remark>\n" +
                "  </HEAD>\n" +
                "  <BODY>\n" +
                "    <khbh></khbh>\n" +
                "    <zjlx></zjlx>\n");
        sb.append("    <zjhm>").append(card).append("</zjhm>\n");
        sb.append("  </BODY>\n" +
                "</ROOT>");
        return sb.toString();
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
    }

}
