package com.android.renly.plusclub;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.android.renly.plusclub.Activity.LoginActivity;
import com.android.renly.plusclub.Common.BaseActivity;
import com.android.renly.plusclub.Common.NetConfig;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;
import com.zhy.http.okhttp.callback.StringCallback;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Call;
import okhttp3.Response;

public class MainActivity extends BaseActivity {

    @BindView(R.id.btn_main_exit)
    Button btnMainExit;
    @BindView(R.id.tv_main_name)
    TextView tvMainName;

    private Unbinder unbinder;

    private String Cookie;
    private String id;
    private String stuName;
    private SharedPreferences sp;

    private static final int LOGOUT_SUCCESS = 1;
    private static final int LOGIN_FAIL_SYSTEMBUSY = 2;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case LOGOUT_SUCCESS:
                    afterSuccessLogout();
                    break;
                case LOGIN_FAIL_SYSTEMBUSY:
                    ToastShort("错误原因：系统正忙！");
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        unbinder = ButterKnife.bind(this);
        printLog("butterKnife bind");
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_main;
    }

    @Override
    protected void initData() {
        sp = getSharedPreferences("userInfo", MODE_PRIVATE);
        Cookie = sp.getString("Cookie", "");
        id = sp.getString("id", "");
        stuName = sp.getString("stuName","");
        printLog("stuName++ " + stuName);
    }


    @Override
    protected void initView() {
        if (!stuName.isEmpty() && stuName != null)
            tvMainName.setText("你好，" + stuName );
    }

    @OnClick(R.id.btn_main_exit)
    public void onViewClicked() {
        doLogout();
    }

    @Override
    protected void onDestroy() {
        unbinder.unbind();
        super.onDestroy();
    }

    private void doLogout() {
        printLog("退出登陆");
        OkHttpUtils.post()
                .url(NetConfig.BASE_EDU_HOST_ME + id)
                .addHeader("Cookie", Cookie)
                .addParams("__EVENTARGUMENT", "")
                .addParams("__EVENTTARGET", "likTc")
                //无__VIEWSTATE参数不能够正常post
                .addParams("__VIEWSTATE", "dDwxMjg4MjkxNjE4Ozs+AHTwpKTHnvjKgKNdCxSpAhxJyXQ=")
                .build()
                .execute(new Callback() {
                    @Override
                    public Object parseNetworkResponse(Response response, int id) throws Exception {
                        String responseHTML = new String(response.body().bytes(), "GB2312");
                        writeData("/sdcard/Test/LogoutHTML.txt", responseHTML);
                        getLogout();
                        return null;
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        printLog("doLogout onError");
                    }

                    @Override
                    public void onResponse(Object response, int id) {
                        printLog("doLogout onResponse");
                    }
                });
    }

    /**
     * 注册二次退出
     */
    private void getLogout() {
        OkHttpUtils.get()
                .url(NetConfig.GET_LOGOUT_URL)
                .addHeader("Cookie", Cookie)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        printLog("getLogout onError");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        printLog(response);
                        writeData("/sdcard/Test/LogoutGetHTML.txt", response);
                        checkLogoutSuccess(response);
                    }
                });
    }

    /**
     * 完成退出登录操作
     */
    private void afterSuccessLogout() {
        SharedPreferences.Editor editor = sp.edit();
        String Cookie = sp.getString("Cookie", "");
        editor.clear();
        editor.putString("Cookie", Cookie);
        editor.apply();
        printLog("退出登陆成功");
        gotoActivity(LoginActivity.class);
        finish();
    }

    /**
     * 检查是否退出登录成功
     */
    private void checkLogoutSuccess(String logoutresult) {
        Document doc = Jsoup.parse(logoutresult);
        Elements success = doc.select("title");
        Elements err = doc.select("p[class]");

        for (Element link : success) {
            //获取所要查询的URL,这里相应地址button的名字叫成绩查询
            if (link.text().equals("欢迎使用正方教务管理系统！请登录")) {
                handler.sendEmptyMessage(LOGOUT_SUCCESS);
                return;
            }
        }

        for (Element link : err) {
            if (link.text().equals("错误原因：系统正忙！"))
                handler.sendEmptyMessage(LOGIN_FAIL_SYSTEMBUSY);
            return;
        }
    }

}