package com.huzhou.gjj.acitivity;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.avast.android.dialogs.fragment.SimpleDialogFragment;
import com.avast.android.dialogs.iface.IListDialogListener;
import com.avast.android.dialogs.iface.ISimpleDialogListener;
import com.huzhou.gjj.AppManager;
import com.huzhou.gjj.R;
import com.huzhou.gjj.fragment.FindFragment;
import com.huzhou.gjj.fragment.HomeFragment;
import com.huzhou.gjj.fragment.SetFragment;
import com.huzhou.gjj.reciver.DownloadService;
import com.huzhou.gjj.utils.Const;
import com.huzhou.gjj.utils.DbUtils;
import com.huzhou.gjj.utils.DownloadManagerUtil;

import com.huzhou.gjj.utils.JsonAnalysis;
import com.huzhou.gjj.utils.PhoneUtils;
import com.huzhou.gjj.utils.SharedPreferencesUtil;
import com.huzhou.gjj.utils.ToastUtils;
import com.huzhou.gjj.viewUtils.LoadingDialog;
import com.huzhou.gjj.viewUtils.MyProgressDialog;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.RuntimePermissions;

import static com.huzhou.gjj.utils.Const.SERVICE_REQUEST_URL;
import static com.huzhou.gjj.utils.Const.SERVICE_VERSION_URL;


//主页面
@ContentView(R.layout.activity_main)
@RuntimePermissions
public class MainActivity extends BaseAppCompatActivity implements ISimpleDialogListener, IListDialogListener {

    @ViewInject(R.id.main_viewPager)
    private ViewPager viewPager;

    @ViewInject(R.id.main_tab)
    private TabLayout tabLayout;

    @ViewInject(R.id.main_toolbar)
    private Toolbar toolbar;

    @ViewInject(R.id.login)
    private ImageView login;

    @ViewInject(R.id.title)
    private TextView title;

    @ViewInject(R.id.news_my)
    private ImageView news_my;

    @ViewInject(R.id.ewm)
    private ImageView ewm;

    public static String current_version;
    private MyProgressDialog pro_dialog;

    private List<Fragment> fragmentList = new ArrayList<>();
    //    private String tabTitles[] = new String[]{"账户信息", "便民工具", "业务办理"};
    private String tabTitles[] = new String[]{"我的账户", "便民服务", "业务办理"};
    private boolean isExit;
    private Handler handler;
    private SharedPreferencesUtil utils;
    private String url_str;
    private String file_path;
    private String uuid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ZXingLibrary.initDisplayOpinion(this);
        //检查是否有版本更新
        initData();
        SetView();
        //        检查权限
        MainActivityPermissionsDispatcher.VisionWithCheck(this);
    }

    //    注释请求的权限后面跟着权限获取后执行的方法  必输
    @NeedsPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    void Vision() {
        checkVision();
    }


    //    用户拒绝后执行的方法
    @OnPermissionDenied(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    void Visiondenied() {
        showPDialg("湖州公积金需要你授予存储空间权限，否则将无法正常使用.", 4, false);
    }

    //    用户点击不再询问后执行的方法
    @OnNeverAskAgain(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    void VisionnotAsk() {
        showPDialg("湖州公积金需要你授予存储空间权限，否则将无法正常使用.", 4, false);
    }

    //    注释请求的权限后面跟着权限获取后执行的方法  必输
    @NeedsPermission(Manifest.permission.CAMERA)
    void check() {
        Intent intent = new Intent(MainActivity.this, CaptureActivity.class);
        startActivityForResult(intent, 1);
//        ListDialogFragment
//                .createBuilder(this, getSupportFragmentManager())
//                .setTitle("请选择:")
//                .setItems(new String[]{"扫码", "从相册选取二维码"})
//                .setRequestCode(6)
//                .setChoiceMode(AbsListView.CHOICE_MODE_SINGLE)
//                .show();
    }

    @Override
    public void onListItemSelected(CharSequence value, int number, int requestCode) {
        if (requestCode == 6) {
            if (number == 0) {
                Intent intent = new Intent(MainActivity.this, CaptureActivity.class);
                startActivityForResult(intent, 1);
            } else {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(intent, 4);
            }
        }

    }
    //    一般用于展示用户点击取消后向用户说明原因
//    @OnShowRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)
//    void showWhy(final PermissionRequest request) {
//    }

    //    用户拒绝后执行的方法
    @OnPermissionDenied(Manifest.permission.CAMERA)
    void denied() {
        showPDialg("湖州公积金需要你授予相机权限，否则将无法正常使用.", 5, true);
    }

    //    用户点击不再询问后执行的方法
    @OnNeverAskAgain(Manifest.permission.CAMERA)
    void notAsk() {
        showPDialg("湖州公积金需要你授予相机权限，否则将无法正常使用.", 5, true);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        MainActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    private void showPDialg(String text, int code, boolean is) {
        SimpleDialogFragment.createBuilder(x.app(), getSupportFragmentManager())
                .setTitle("提示")
                .setCancelable(is)
                .setMessage(text)
                .setCancelableOnTouchOutside(false)
                .setPositiveButtonText("我知道了")
                .setRequestCode(code)
                .show();

    }

    private void checkVision() {
        JSONObject obj = new JSONObject();
        try {
            PackageManager pm = getPackageManager();
            PackageInfo pi = pm.getPackageInfo(getPackageName(), 0);//getPackageName()是你当前类的包名，0代表是获取版本信息
//            String name = pi.versionName;
            String code = pi.versionName;
            current_version = code;
            obj.put("type", 0);
            obj.put("msg", System.currentTimeMillis() + "");
            obj.put("version", code);
        } catch (Exception ignored) {
        }
        RequestParams params = new RequestParams(SERVICE_VERSION_URL);
        params.addBodyParameter("_sid", "download");// 添加到请求body体的参数, 只有POST, PUT, PATCH, DELETE请求支持.
        params.addBodyParameter("json", obj.toString());
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                String updateMsg = "";
                try {
                    String success = new JSONObject(result).getString("success");
                    if ("true".equals(success)) {
                        String version = new JSONObject(result).getJSONObject("obj").getString("version");
                        url_str = new JSONObject(result).getJSONObject("obj").getString("url");
                        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + Environment.DIRECTORY_DOWNLOADS + File.separator;
                        String name = Const.APP_NAME + version + ".apk";
                        file_path = path + name;
                        if (PhoneUtils.isFileExists(file_path)) {
                            Long download_id = utils.read_long(Const.DOWNLOAD_ID);
                            DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                            int status = DownloadManagerUtil.queryStatus(download_id, dm);
                            switch (status) {
                                case DownloadManager.STATUS_FAILED:
                                    dm.remove(download_id);
                                    if (PhoneUtils.isFileExists(file_path))
                                        PhoneUtils.deleteFile(file_path);
                                    break;
                                case DownloadManager.STATUS_SUCCESSFUL:
                                    if (result.contains("updateMsg"))
                                        updateMsg = new JSONObject(result).getJSONObject("obj").getString("updateMsg");
                                    Spanned ssbuilder = Html.fromHtml("是否更新到 " + "<font color=#E10979>" + version + "</font>" + " 版本?");
                                    SimpleDialogFragment.createBuilder(x.app(), getSupportFragmentManager())
                                            .setTitle(ssbuilder)
                                            .setMessage(updateMsg)
                                            .setCancelableOnTouchOutside(false)
                                            .setPositiveButtonText("立即更新")
                                            .setRequestCode(2)
                                            .setNegativeButtonText("以后再说").show();
                                    break;
                            }
                        } else {
                            Intent intent = new Intent(x.app(), DownloadService.class);
                            intent.putExtra("url", url_str);
//                            intent.putExtra("file_path", file_path);
                            intent.putExtra("name", name);
                            startService(intent);
                        }
                    }
                } catch (JSONException ignored) {
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
            }

            @Override
            public void onCancelled(CancelledException cex) {
            }

            @Override
            public void onFinished() {
            }
        });
    }


    @Event(value = {R.id.news_my, R.id.login, R.id.ewm}, type = View.OnClickListener.class)
    private void getEvent(View view) {
        if (!Const.IS_LOGIN) {
            ToastUtils.showShort(x.app(), "请先登录！");
            startActivity(new Intent(x.app(), LoginActivity.class));
            return;
        }
        switch (view.getId()) {
            case R.id.news_my:
                startActivity(new Intent(MainActivity.this, NewsActivity.class));
                break;
            case R.id.login:
                if ("注销".equals(islogin)) {
                    Spanned ssbuilder = Html.fromHtml("<font color=#000000>是否退出登录?</font>");
                    SimpleDialogFragment.createBuilder(this, getSupportFragmentManager())
                            .setMessage(ssbuilder)
                            .setPositiveButtonText("确定")
                            .setRequestCode(1)
                            .setNegativeButtonText("取消").show();

                } else {
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                }
                break;
            case R.id.ewm:
                MainActivityPermissionsDispatcher.checkWithCheck(this);
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            //处理扫描结果（在界面上显示）
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    uuid = bundle.getString(CodeUtils.RESULT_STRING);
                    showWebLogin();
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    Toast.makeText(MainActivity.this, "解析二维码失败,请重试！", Toast.LENGTH_LONG).show();
                }
            }
        }
//        else if (requestCode == 4) {
//            if (data != null) {
//                Uri uri = data.getData();
//                try {
//                    CodeUtils.analyzeBitmap(ImageUtils.getImageAbsolutePath(this, uri), new CodeUtils.AnalyzeCallback() {
//                        @Override
//                        public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
//                            uuid = result;
//                            showWebLogin();
//                        }
//
//                        @Override
//                        public void onAnalyzeFailed() {
//                            Toast.makeText(MainActivity.this, "解析二维码失败", Toast.LENGTH_LONG).show();
//                        }
//                    });
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }//        内存
        else if (requestCode == 2) {
            MainActivityPermissionsDispatcher.VisionWithCheck(this);
        }
//        相机
        if (requestCode == 3) {
            MainActivityPermissionsDispatcher.checkWithCheck(this);
        }
    }

    String islogin = "登录";

    private void showWebLogin() {
        Spanned ssbuilder = Html.fromHtml("是否登陆网页版?");
        SimpleDialogFragment.createBuilder(x.app(), getSupportFragmentManager())
                .setTitle(ssbuilder)
                .setCancelableOnTouchOutside(false)
                .setPositiveButtonText("立即登录")
                .setRequestCode(3)
                .setNegativeButtonText("以后再说").show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Const.IS_LOGIN)
            islogin = "注销";
        else
            islogin = "登录";
    }

    @Event(R.id.main_fab)
    private void fabEvent(View view) {
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
    }

    private void initData() {
        pro_dialog = LoadingDialog.createLoadingDialog(this, "加载中...");
        pro_dialog.setActivity(this);
        utils = new SharedPreferencesUtil(MainActivity.this);
        fragmentList.add(new HomeFragment());
        fragmentList.add(new FindFragment());
        fragmentList.add(new SetFragment());
        handler = new Handler();
    }

    private void SetView() {
        setSupportActionBar(toolbar);
        MyFragmentPagerAdapter pagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        final TabLayout.Tab one = tabLayout.getTabAt(0);
        final TabLayout.Tab two = tabLayout.getTabAt(1);
        final TabLayout.Tab three = tabLayout.getTabAt(2);

        assert one != null;
        one.setIcon(getResources().getDrawable(R.mipmap.ic_account_1));
        assert two != null;
        two.setIcon(getResources().getDrawable(R.mipmap.ic_tools));
        assert three != null;
        three.setIcon(getResources().getDrawable(R.mipmap.ic_us));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.equals(tabLayout.getTabAt(0))) {
                    viewPager.setCurrentItem(0);
                    title.setText("湖州公积金");
                    login.setVisibility(View.VISIBLE);
                    news_my.setVisibility(View.VISIBLE);
                    ewm.setVisibility(View.GONE);
                    one.setIcon(getResources().getDrawable(R.mipmap.ic_account_1));
                    two.setIcon(getResources().getDrawable(R.mipmap.ic_tools));
                    three.setIcon(getResources().getDrawable(R.mipmap.ic_us));
                } else if (tab.equals(tabLayout.getTabAt(1))) {
                    viewPager.setCurrentItem(1);
                    title.setText("便民服务");
                    login.setVisibility(View.GONE);
                    news_my.setVisibility(View.GONE);
                    ewm.setVisibility(View.GONE);
                    one.setIcon(getResources().getDrawable(R.mipmap.ic_account));
                    two.setIcon(getResources().getDrawable(R.mipmap.ic_tools_1));
                    three.setIcon(getResources().getDrawable(R.mipmap.ic_us));
                } else if (tab.equals(tabLayout.getTabAt(2))) {
                    viewPager.setCurrentItem(2);
                    title.setText("业务办理");
                    login.setVisibility(View.GONE);
                    news_my.setVisibility(View.GONE);
                    ewm.setVisibility(View.GONE);
                    one.setIcon(getResources().getDrawable(R.mipmap.ic_account));
                    two.setIcon(getResources().getDrawable(R.mipmap.ic_tools));
                    three.setIcon(getResources().getDrawable(R.mipmap.ic_us_1));
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }


    @Override
    public void onBackPressed() {
        if (!isExit) {
            ToastUtils.showShort(this, "再点击一次则退出");
            isExit = true;
            // 发送消息
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    isExit = false;
                }
            }, 3000);// 延时3秒操作
        } else {
            AppManager.getAppManager().deleteAllActivity();
            Runtime.getRuntime().gc();
        }
    }

    @Override
    public void onNegativeButtonClicked(int requestCode) {

    }

    @Override
    public void onNeutralButtonClicked(int requestCode) {

    }

    @Override
    public void onPositiveButtonClicked(int requestCode) {
        if (requestCode == 1) {
            utils.save(Const.LOGIN, "false");
            Const.IS_LOGIN = false;
            ToastUtils.showShort(x.app(), "注销成功！");
//            login.setText("登录");
            islogin = "登录";
        }
//安装更新
        if (requestCode == 2) {
//            Intent it = new Intent(Intent.ACTION_VIEW, Uri.parse(url_str));
//            it.setClassName("com.android.browser", "com.android.browser.BrowserActivity");
//            startActivity(it);
            PhoneUtils.installApp(file_path);
        }
        if (requestCode == 3) {
            LoginWeb();
        }
        if (requestCode == 4 || requestCode == 5) {
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", MainActivity.this.getPackageName(), null);
            intent.setData(uri);
            if (requestCode == 4)
                startActivityForResult(intent, 2);
            else startActivityForResult(intent, 3);
        }
    }

    private void LoginWeb() {
        pro_dialog.show();
        JSONObject obj = new JSONObject();
        String jsonData = null;
        try {
            obj.put("uuid", uuid);
            obj.put("identification", DbUtils.getUserData().getPersonAcctNo());
            jsonData = JsonAnalysis.putJsonBody(obj, "1045").toString();
        } catch (JSONException ignored) {
            pro_dialog.dismiss();
        }
        RequestParams params = new RequestParams(SERVICE_REQUEST_URL);
        params.addBodyParameter("code", "1045");// 添加到请求body体的参数, 只有POST, PUT, PATCH, DELETE请求支持.
        params.addBodyParameter("json", jsonData);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                if (result.contains("交易成功"))
                    ToastUtils.showShort(x.app(), "网页版登陆成功！");
                else
                    ToastUtils.showShort(x.app(), "网页版登陆失败！");
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                ToastUtils.showShort(x.app(), "网页版登陆失败！");
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


    private class MyFragmentPagerAdapter extends FragmentPagerAdapter {


        MyFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }
    }
}
